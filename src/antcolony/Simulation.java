
/*****************************************************************
	Antonio Fonseca
	antoniofilipefonseca@gmail.com

	File: Simulation.java
	Package: antcolony

	Description:

	* Simulation Visualization
	* Uses underlying grid
	* Provides several measurement functions
	
                                                                                                                     	
*****************************************************************/

package antcolony;

import java.awt.Color;
import java.awt.Graphics;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.swing.JPanel;
import java.util.Iterator;


/** Wrapping class for the main functions in the application. This class contains the main components
 * for running the following functions:
 * <ul>
 * <li> Run a simulation.
 * <li> Paint the canvas of the grid.
 * <li> Measure the results.
 * <li> Write the result file.
 * </ul>
 * <p>
 * @author      António Fonseca
 * @version     1.8
 * @since       1.0
*/
public class Simulation extends JPanel implements Runnable  {

	private Configuration conf;	
	private AntColony antColony;
	private Clustering clustering;
	private Data data;
	private Grid grid;
	private int[] colors;
	private boolean  original = true;
	private boolean  clusters = false;
	private double scale;
	private boolean interrupted;
	private int tick;
	private double[][] record;
	private PrintWriter out;
	private boolean rec;
	private boolean optimize;

		
/********************** Constructor **************************************************************/

	
	/** Constructor
	* @param conf the current parameter settings
	* @param clt the current underlying main application
	*/
	public Simulation(Configuration conf, Clustering clt) {
		
		this.conf = conf;
		this.data = new Data(conf);
		this.grid = new Grid(conf,this.data);
		this.colors = conf.getColors();
		this.scale = 4.0;
		this.original = true;
		this.antColony = new AntColony(conf, grid);
		this.clustering = clt;
		this.record = new double[5][10000];
		this.rec = false;
		this.optimize = false;
	
	}
	
	/** Update the current simulation with new data and new parameters
	* @param conf the current parameter settings
	*/
	public void update(Configuration conf) {	
		this.conf = conf;
		this.data = new Data(conf);
		this.grid = new Grid(conf,this.data);
		this.antColony = new AntColony(conf, grid);
//		this.clustering.setText(this.grid.printStats());
	}

/******************* access functions *************************************************************/
	/**
	* Get the original display flag
	* @return the original flag
	*/
	public boolean getOriginal() {
		return this.original;
	}

	/** Get the underlying grid
	* @return the current grid
	*/
	public Grid getGrid() {
		return this.grid;
	}

	/** Get the Ant Colony
	* @return the ant colony
	*/
	public AntColony getColony() {
		return this.antColony;
	}

	/** Update the original display flag
	* @param f the original display flag
	*/
	public void setOriginal(boolean f) {
		this.original = f;
	}
	
	/** Update the clusters display flag
	* @param f the clusters display flag
	*/
	public void setClusters(boolean f) {
		this.clusters = f;
	}
	
	/** Update the interrupted flag
	* @param f the flag
	*/
	public void setInterrupted(boolean f) {
		this.interrupted = f;
	}
	
	/** Set record flag
	* @param f the flag
	*/
	public void setRec(boolean f) {
		this.rec = f;
	}
	
	/** Return the status of the record flag
	* @return the status of current record flag
	*/
	public boolean getRec() {
		return this.rec;
	}
	
	/** Set optimize flag
	* @param f the flag
	*/
	public void setOpt(boolean f) {
		this.optimize = f;
	}
	
	/** Return the status of the optimize flag
	* @return the status of current record flag
	*/
	public boolean getOpt() {
		return this.optimize;
	}


/************ action ..... *********************************************************************/
	


	/** Start ant-based clustering process
	*/
	public void run() {
		if (this.optimize) optimize();
		else {
	   	tick = 0;
		double pearson;
		double entropy;
		double F_m;
		double rand;
		double InnVar;
        do {
            try {            	
            	this.antColony.sort(tick);
            	this.repaint();
            	if (this.tick%100==0 && this.tick > 0) {
            		this.antColony.drop();
                	pearson = computePearson(true);
                	this.clustering.setPearsons(pearson);
                	entropy = computeEntropy(true);
                	this.clustering.setEntropy(entropy);
            		this.grid.calculateClusters();
                	this.clustering.setText(this.grid.printStats());
            		F_m = computeFMeasure();
            		this.clustering.setF(F_m);
            		rand = computeRand();
            		this.clustering.setRand(rand);
            		InnVar = computeInnerClusterVariance();
            		this.clustering.setInnerCV(InnVar);
                	if (this.rec && tick < 10000){
                		this.record[0][tick]=pearson;
                		this.record[1][tick]=entropy;
                		this.record[2][tick]=F_m;
                		this.record[3][tick]=rand;
                		this.record[4][tick]=InnVar;
                	}
                	if (F_m > conf.getMinF()) this.clustering.stop("\nReached minimum F");
            	}

            	this.clustering.setTick(tick);
            	tick++;
            	if (tick > conf.getCicle1()+conf.getCicle2() || (tick>conf.getCicle1() && conf.getModel()!= Configuration.Models.ANTCLASS1) ) this.clustering.stop("\nEnd of Simulation");
            	if (interrupted) {
            		synchronized(this) {
            			while (interrupted)
            				wait();
           		}
          }
            } 
            catch (InterruptedException e){
            	e.printStackTrace();
            }
        } while (!interrupted);
        interrupted = false;
		}

	}


	/** Optimization routine
	 * Uses genetic algorithm to optimize configuration parameters
	 * 
	 */
	public void optimize() {
		
        do {
		
		Random generator = new Random();

		String[] names = new String[]{"N of ants","Max Carry","Speed","P Load","P Drop","P Destroy","P Direction",
				"T Create","T Remove","Memory Size"};
		
		// Size of the population of genes-individuals
		int N = 20;
		
		// Get the parameters-genes seed and variations
		HashMap<String,Double> parameters = conf.getParameters();
		Double[] gene_seed = new Double[names.length];
		Double[] gene_var = new Double[names.length];
		
		gene_seed[0]= parameters.get(names[0]);
		gene_var[0] = 5.0; //N of ants
		for (int i=1; i<(names.length-1);i++) {
			gene_seed[i]= parameters.get(names[i]+" low");
			gene_var[i]= parameters.get(names[i]+" range")/N;
		}
		gene_seed[names.length-1]= parameters.get(names[names.length-1]);
		gene_var[names.length-1] = 1.0; // memory
		
		//generate a starting population
		LinkedList<HashMap<String,Double>> population = new LinkedList<HashMap<String,Double>>();
		for (int i=0; i<N;i++){
			HashMap<String,Double> indv = new HashMap<String,Double>();
			for (int j=0; j<names.length;j++)
				indv.put(names[j], gene_seed[j]+ generator.nextDouble()* N * gene_var[j]);
			population.add(indv);
		}
		
		double fitness = 0;
		double f_threshold = conf.getMinF() * N; //average fitness given by F_measure should be equal to Minimum F
		int run = 0;
		while (fitness < f_threshold && !interrupted) {
			run++;
			Double[] scores = new Double[N];

			//Configure running from population
			int n = 0;
			for (HashMap<String,Double> indiv : population) {
				conf.setParameters("N of ants", indiv.get("N of ants"));
				conf.setParameters("Memory Size", indiv.get("Memory Size"));
				for (int i=1; i<names.length-1;i++) {
					this.conf.setParameters(names[i]+" low",indiv.get(names[i]));
					this.conf.setParameters(names[i]+" range",0.0);
				}
				tick=0;
				this.update(conf);
				this.clustering.updateTable();
				while (tick <= conf.getCicle1()){
					this.antColony.sort(tick);
					tick++;
				}
				this.antColony.drop();
				this.grid.calculateClusters();
				scores[n] = computeFMeasure();
				n++;
			}
			fitness=0;
			for (int i=0; i<N; i++) fitness+= scores[i];
			this.clustering.setText("Run : " + run +"\nAverage Fitness : "+ fitness / N);
			
			// generate new population
			LinkedList<HashMap<String,Double>> population_new = new LinkedList<HashMap<String,Double>>();
			for (int i=0; i<N ; i+=2){
				
				// Crossover
				
				// get one individual
				double p_crossover = generator.nextDouble()*fitness;
				double sum=scores[0];
				int idx=0;
				for (int j=1; sum<p_crossover; j++) {
					sum+=scores[j];
					idx=j;
				}
				HashMap<String,Double> indv1 = population.get(idx);
				
				// get another
				p_crossover = generator.nextDouble()*fitness;
				sum=scores[0];
				idx=0;
				for (int j=1; sum<p_crossover; j++) {
					sum+=scores[j];
					idx=j;
				}
				HashMap<String,Double> indv2 = population.get(idx);
				
				
				Double[] genes1 = getGenes(indv1,names);
				Double[] genes2 = getGenes(indv2,names);
				
				//crossover
				
				double crossover_rate =0.001;
				
				if (generator.nextDouble()< crossover_rate) {
					
					int cross_point = generator.nextInt(names.length);
					for (int j=0; j<cross_point; j++) {
						Double temp = 0.0;
						temp = genes2[j];
						genes2[j] = genes1[j];
						genes1[j] = temp;
					}
				}
				
				// Mutation
				
				double mutation_rate=0.001;
				
				for (int j=0; j<names.length; j++){
					if (generator.nextDouble()< mutation_rate)
						genes1[j]=gene_seed[j]+ generator.nextDouble()* N * gene_var[j];
					if (generator.nextDouble()< mutation_rate)
						genes2[j]=gene_seed[j]+ generator.nextDouble()* N * gene_var[j];
					}	
				
				indv1 = putGenes(genes1,names);
				indv2 = putGenes(genes2,names);
				
				// add to the population
				population_new.add(indv1);
				population_new.add(indv2);
			}
			
			population = population_new;

		}
        } while (!interrupted);
        this.clustering.stopOpt("\n\nEnd of Optimization");
        optimize = false;
	}

	
	/**
	* Get put genes into an individual
	* @param genes The genes
	* @param names the names of the genes
	* @return an individual
	*/
	private HashMap<String,Double> putGenes(Double[] genes, String[] names) {
		HashMap<String,Double> indiv = new HashMap<String,Double>();
		for (int j=0; j<names.length;j++)
			indiv.put(names[j], genes[j]);
		return indiv;	
	}
	
	/**
	* Get the genes from a individual
	* @param indiv The individual
	* @param names the names of the genes
	* @return a gene as Double[]
	*/
	private Double[] getGenes(HashMap<String,Double> indiv, String[] names){
		Double[] gene = new Double[names.length];
		for (int j=0; j<names.length;j++)
			gene[j]= indiv.get(names[j]);
		return gene;
	}
	
	/**
	* Change drawing scale
	* @param f zoom in or out
	*/
	  public void zoom(boolean f){
		  if (f & this.scale < 10) this.scale+=0.2 ;
		  if (!f & this.scale > 1) this.scale-=0.2 ;
	  }
	
		
	/**
	* Paint the Grid
	* @param g the Swing variable
	*/
	  public void paint(Graphics g){
	      super.paint(g);
	          HashMap<UUID,Item> items = this.grid.getItems();
	          for (Item it : items.values()){
	    			  if (this.original){
	    				  g.setColor(new Color(colors[it.getColor()]));
	    				  g.fillRect((int)(it.getinitX()*this.scale), (int)(it.getinitY()*this.scale),5,5);
	    				  }
	    			  else {
	    				  if (this.clusters)g.setColor(new Color(colors[it.getCluster()]));
	    				  else g.setColor(new Color(colors[it.getColor()]));
	    				  g.fillRect((int)(it.getX()*this.scale), (int)(it.getY()*this.scale),5,5);
	    			  }
	    	  }
	          if (this.clusters){
	        	  g.setColor(Color.BLACK);
	        	  LinkedList<double[]> centers = this.grid.getCenters();
	        	  if (centers!=null){
	        	  for (double[] c : centers){
	        		  g.drawOval((int)(c[0]*this.scale), (int)(c[1]*this.scale),10,10);
	        		  g.fillOval((int)(c[0]*this.scale)+2, (int)(c[1]*this.scale)+2,6,6);
	        	  }
	        	  }}
	  }

	  
	  
	  /**
	   * Record the running
	   * @param filename the full path name of the file
	   */
	  
	  public void writeRecord(String filename){
		  try {
			  if (out == null) {
				  FileWriter outFile = new FileWriter(filename);
				  out = new PrintWriter(outFile);
				  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				  Date date = new Date();
				  out.println("********************************************************\n");
				  out.println("Date : " + dateFormat.format(date)+"\n");
				  out.println("Parameters:\n");
				  HashMap<String,Double> map = this.conf.getParameters();
				  for (Map.Entry<String,Double> entry : map.entrySet())
					  out.println(entry.getKey()+" : "+entry.getValue().toString());
				  out.println("Data:\n");
				  out.println("********************************************************\n");
				  out.print("Tick "+'\t');
				  for(String s: new String[]{"Entropy","Pearson", "F Measure","Rand Index", "Inner Variance"})
					  out.print(s + '\t');
				  out.println();
				  for (int i=0; i< this.tick;i++){
					  out.print(Integer.toString(i) +'\t');
					  for (int j=0; j<5; j++)
						  out.print(Double.toString(this.record[j][i])+'\t');
					  out.println();
					  }
				  out.close();
				  out = null;
			  }
			  } catch (IOException e){
				  e.printStackTrace();
			  }
		  }

	  
/************ Measurements ******************************************************/


	/** Compute Pearson correlation
	* @return Pearson correlation computed for the entire grid
	* @param ignore the positions of items that are currently picked up
	*/
	public double computePearson(boolean ignore) {
		double xsum = 0;
		double ysum = 0;
		double xysum = 0;
		double xsquaresum = 0;
		double ysquaresum = 0;
		double N = 0;
		HashMap<UUID,Item> items = this.grid.getItems();
		DistanceMatrix d = this.grid.getDistanceMatrix();	
		for (UUID key : items.keySet()) {
			if (ignore && items.get(key).isPicked()) continue;
			for (UUID key1 : items.keySet())  {
				if (ignore && items.get(key1).isPicked()) continue;
				if (key.equals(key1)) continue;
				N++;
				double x = Math.abs(d.get(key, key1));
				double y = Math.abs(items.get(key).distance(items.get(key1), 1));
				xsum += x;
				xsquaresum += (x*x);
				ysum += y;
				ysquaresum += (y*y);
				xysum += (x*y);
			}
		}
		double num = xysum - xsum*ysum/N;
		double denom = Math.sqrt((xsquaresum - xsum*xsum/N)*(ysquaresum-ysum*ysum/N));
		if (denom == 0) return 0;
		return num / denom ;
	}
	
	/** Compute Entropy
	* @param ignore the positions of items that are currently picked up
	* @return Entropy computed for the entire grid
	*/
	public double computeEntropy(boolean ignore) {
		int xdim = (int)Math.floor(conf.getxsize()/10);
		int ydim = (int)Math.floor(conf.getysize()/10);
		double[][] bins = new double[xdim][ydim];
		HashMap<UUID,Item> items = this.grid.getItems();
		double count = 0;
		for (UUID key : items.keySet()) {
			if (ignore && items.get(key).isPicked()) continue;
			bins[(int)Math.floor(items.get(key).getX()/10)][(int)Math.floor(items.get(key).getY()/10)]++;
			count++;
		}
		double sum = 0;
		double log2 = Math.log(2);
		for (int i=0; i < xdim; i++) {
			for (int j=0; j < ydim; j++) {
				double p = bins[i][j]/count;
				if (p > 0) sum += p * Math.log(p)/log2;
			}}
		return -sum;
	}

	/** Compute F Measure
	* @return F measure computed for the entire grid
	*/
	public double computeFMeasure(){
		Cluster[] p = this.grid.getClusters();
		HashMap<UUID,Item> items = this.grid.getItems();
		String[] types = this.conf.getTypes();
		double[] count_i = new double[types.length];
		for (UUID key : items.keySet()) 
			for (int i=0; i<types.length;i++)
				if (items.get(key).getType().equals(types[i])) count_i[i]++;
		double[][] prec = new double[types.length][p.length];
		double[][] recl = new double[types.length][p.length];
		double[][] F = new double[types.length][p.length];
		double[] F_max = new double[types.length];
		for (int j=0; j< p.length;j++){
			if (p[j]!= null){
				double[] count_j = new double[types.length];
				LinkedList<Item> list = p[j].getItems();
				Iterator<Item> it = list.iterator();
				double sum = 0;
				while(it.hasNext()){
					Item itm = it.next();
					for(int i=0; i<types.length;i++) if (itm.getType().equals(types[i])) count_j[i]++;
					sum++;
					}
				for (int i=0;i<types.length;i++){
					prec[i][j]= count_j[i] / sum;
					recl[i][j]= count_j[i] / count_i[i];
				}
			}
			}
		for (int j=0; j< p.length;j++){
			for (int i=0;i<types.length;i++)
				if (p[j]!= null) 
					F[i][j]= 2* prec[i][j] * recl[i][j] / (prec[i][j] + recl[i][j]);
		}
		for (int i=0;i<types.length;i++){
			F_max[i]=0;
			for (int j=0; j< p.length;j++)if (F[i][j] > F_max[i]) F_max[i]=F[i][j];
		}
		double F_measure = 0;
			for (int i=0;i<types.length;i++)
				F_measure += count_i[i] / items.size() * F_max[i];	
		return F_measure;
	}
	
	/** Compute Rand Index
	* @return Rand index computed for the entire grid
	*/
	public double computeRand() {
		Cluster[] p = this.grid.getClusters();
		HashMap<UUID,Integer> part = new HashMap<UUID,Integer>();
		HashMap<UUID,Integer> clust = new HashMap<UUID,Integer>();
		for (int i=0; i< p.length; i++)
			if (p[i]!= null){
				LinkedList<Item> items = p[i].getItems();
				Iterator<Item> it = items.iterator();
				while(it.hasNext()) part.put(it.next().getID(),i+1);
			}

		HashMap<UUID,Item> items = this.grid.getItems();
		String[] types = this.conf.getTypes();
		for (UUID key : items.keySet())
			for (int i=0; i< this.conf.getTypes().length; i++)
				if (items.get(key).getType().equals(types[i])) clust.put(key,i+1);
		
		int a=0;
		int b=0;
		int c=0;
		int d=0;
		for (UUID key : items.keySet()){
			for (UUID key1 : items.keySet()){
					if (key == key1) continue;
					if (clust.get(key)==clust.get(key1) && part.get(key)==part.get(key1)) a++;
					if (clust.get(key)==clust.get(key1) && part.get(key)!=part.get(key1)) b++;
					if (clust.get(key)!=clust.get(key1) && part.get(key)==part.get(key1)) c++;
					if (clust.get(key)!=clust.get(key1) && part.get(key)!=part.get(key1)) d++;
				}}
		return ((double)(a+d))/((double) (a+b+c+d));
	}
	
	/** Compute Inner Cluster Variance
	* @return ICV computed for the entire grid
	*/
	public double computeInnerClusterVariance() {
		Cluster[] p = this.grid.getClusters();
		Double[][] centers = new Double[p.length][conf.getnkeys()];
		double[][] centers_xy = new double[p.length][2];
		Item[] centroids = new Item[p.length];
		for (int i=0; i< p.length; i++)
			if (p[i]!= null){
				LinkedList<Item> items = p[i].getItems();
				Iterator<Item> it = items.iterator();
				while (it.hasNext()){
					Item itm = it.next();
					List<Double> list = itm.getData();
					Iterator<Double> it1 = list.iterator();
					centers_xy[i][0]+=(double)itm.getX();
					centers_xy[i][1]+=(double)itm.getY();
					for (int j=0; j<conf.getnkeys(); j++)centers[i][j]=0.0;
					int j=0;
					while (it1.hasNext()){
						centers[i][j] += it1.next();
						j++;
					}
				}
				for (int j=0; j<conf.getnkeys();j++) centers[i][j]= centers[i][j]/(double)items.size();
				centers_xy[i][0]= centers_xy[i][0]/(double)items.size();
				centers_xy[i][1]= centers_xy[i][1]/(double)items.size();
			}
		for (int i=0; i< p.length; i++) 
			if (p[i]!= null) {
				Double[] c = centers[i];
				centroids[i]= new Item(UUID.randomUUID(), this.conf,(int)centers_xy[i][0],(int)centers_xy[i][1],"",0, Arrays.asList(c));
			}
		double sum = 0;
		double inc = 0.0;
		for (int i=0; i< p.length; i++)
			if (p[i]!= null){
				LinkedList<Item> items = p[i].getItems();
				Iterator<Item> it = items.iterator();
				while (it.hasNext()){
					sum+= it.next().distance(centroids[i], 2);
					inc++;
				}
			}
		return sum/inc;
	}

}



