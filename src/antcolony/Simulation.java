/*  
    Copyright (C) 2012 Antonio Fonseca
    Email: antoniofilipefonseca@gmail.com

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

*/

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.JPanel;
import java.util.Iterator;


/** Wrapping class for the item and ant data. Also contains the basic grid
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
	private boolean stop;
	private boolean interrupted;
	private int tick;
	private double[][] record;
	private PrintWriter out;
	private boolean rec;

		
/********************** Constructor **************************************************************/

	
	/** Constructor
	* @param conf the current parameter settings
	* @param data is the items
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
		this.record = new double[2][10000];
		this.rec = false;
	
	}
	
	/** Update the current simulation with new data and new parameters
	* @param conf the current parameter settings
	* @param data is the items
	*/
	public void update(Configuration conf) {	
		this.conf = conf;
		this.data = new Data(conf);
		this.grid = new Grid(conf,this.data);
		this.antColony = new AntColony(conf, grid);
		this.clustering.setText(this.grid.printStats());
	}

/******************* access functions *************************************************************/
	/**
	* Get the original display flag
	*/
	
	public boolean getOriginal() {
		return this.original;
	}

	/** Return the underlying grid
	* @return the current grid
	*/
	public Grid getGrid() {
		return this.grid;
	}

	/** Return the parameter settings
	* @return the current configuration
	*/
	public AntColony getColony() {
		return this.antColony;
	}

	
	/** Update the original display flag
	* @param the flag
	*/

	public void setOriginal(boolean f) {
		this.original = f;
	}
	
	/** Update the original display flag
	* @param the flag
	*/

	public void setClusters(boolean f) {
		this.clusters = f;
	}
	
	/** Update the interrupted flag
	* @param the flag
	*/

	public void setInterrupted(boolean f) {
		this.interrupted = f;
	}
	
	/** Reset stopflag
	* @param the flag
	*/

	public void reset() {
		this.stop = false;
	}
	
	/** Set record flag
	* @param the flag
	*/

	public void setRec(boolean f) {
		this.rec = f;
	}
	
	/** Return the underlying grid
	* @return the current record flag
	*/
	public boolean getRec() {
		return this.rec;
	}


/************ action ..... *********************************************************************/
	


	/** Interrupt ant-based sorting process
	*/
	public void stop() {
		  Thread thisThread = Thread.currentThread();
		  thisThread = null;
		  this.stop = true;
	}

	/** Start ant-based sorting process
	*/
	public void run() {
		
	   	tick = 0;
		double pearson;
		double entropy;
		double F_m;
		double rand;
		double InnVar;
        while (!stop) {
            try {            	
            	this.antColony.sort(tick);
            	this.repaint();
            	if (this.tick%100==0 && this.tick > 0) {
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
                	}
                	if (F_m > conf.getMinF()) this.clustering.stop();	
            	}

            	this.clustering.setTick(tick);
            	tick++;           	
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
        }
        stop = false;
		interrupted = true;
	}


	/**
	* Change drawing scale
	*/
	  public void zoom(boolean f){
		  if (f & this.scale < 10) this.scale+=0.2 ;
		  if (!f & this.scale > 1) this.scale-=0.2 ;
	  }
	
		
	/**
	* Paint the Grid
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
	  }

	  
	  
	  /**
	   * Record the running
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
				  for(String s: new String[]{"Entropy","Pearson"})
					  out.print(s + '\t');
				  out.println();
				  for (int i=0; i< this.tick;i++){
					  out.print(Integer.toString(i) +'\t');
					  for (int j=0; j< 2; j++)
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

	  
/************ Measurements & Plots ******************************************************/


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
		for (int i=0; i < xdim; i++) 
			for (int j=0; j < ydim; j++) {
				double p = bins[i][j]/count;
				if (p > 0) sum += p * Math.log(p)/log2;
			}
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
		for (int j=0; j< p.length;j++)
			for (int i=0;i<types.length;i++)
				if (p[j]!= null) 
					F[i][j]= 2* prec[i][j] * recl[i][j] / (prec[i][j] + recl[i][j]);
		
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
		for (UUID key : items.keySet())
			for (UUID key1 : items.keySet()){
					if (key == key1) continue;
					if (clust.get(key)==clust.get(key1) && part.get(key)==part.get(key1)) a++;
					if (clust.get(key)==clust.get(key1) && part.get(key)!=part.get(key1)) b++;
					if (clust.get(key)!=clust.get(key1) && part.get(key)==part.get(key1)) c++;
					if (clust.get(key)!=clust.get(key1) && part.get(key)!=part.get(key1)) d++;
				}
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



