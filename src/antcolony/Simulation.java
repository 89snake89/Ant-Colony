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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import java.util.Iterator;


/** Wrapping class for the item and ant data. Also contains the basic grid
*/

public class Simulation extends JPanel implements Runnable  {

	private Configuration conf;	
	private AntColony antColony;
	private Clustering clustering;
	private Grid grid;
	private String [] symbols;
	private int[] colors;
	private boolean  original = true;
	private double scale;
	private boolean stop;
	private boolean interrupted;
	private int tick;
	private String[] measures;
	private double[][] record;
	private PrintWriter out;
	private boolean rec;

		
/********************** Constructor **************************************************************/

	
	/** Constructor
	* @param conf the current parameter settings
	* @param data is the items
	* @param clt the current underlying main application
	*/
	public Simulation(Configuration conf, Data data, Clustering clt) {
		
		this.conf = conf;
		this.grid = new Grid(conf,data);
		this.symbols = conf.getSymbols();
		this.colors = conf.getColors();
		this.scale = 1;
		this.original = true;
		this.antColony = new AntColony(conf, grid);
		this.clustering = clt;
		this.measures = new String[]{"Pearson","Entropy"};
		this.record = new double[2][10000];
		this.rec = false;
	
	}
	
	/** Update the current simulation with new data and new parameters
	* @param conf the current parameter settings
	* @param data is the items
	*/
	public void update(Configuration conf, Data data) {
		
		this.conf = conf;
		this.grid = new Grid(conf,data);
		this.scale = 1.0;
		this.antColony = new AntColony(conf, grid);	
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
		
	   	stop = false;
	   	tick = 0;
		long start = System.currentTimeMillis();
		double pearson;
		double entropy;
        while (!stop) {
            try {            	
            	this.antColony.sort();
            	this.repaint();
            	pearson = computePearson(false);
            	entropy = computeEntropy();
            	this.clustering.setPearsons(pearson);
            	this.clustering.setEntropy(entropy);
            	this.clustering.setTick(tick);
            	if (this.rec && tick < 10000){
            		this.record[0][tick]=pearson;
            		this.record[1][tick]=entropy;
            	}
            	tick++;
            	if (interrupted) {
            		synchronized(this) {
            			while (interrupted)
            				wait();
            		}
            	}
            } 
            catch (InterruptedException e){
            }
        }

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
	      if (this.conf.getModel() == Configuration.Models.LUMERFAIETA ||
	    	  this.conf.getModel() == Configuration.Models.LUMERFAIETA_S){
	    	  
	    	  Item[] it = this.grid.getItems();
	    	  if (it != null)
	    		  for (int i=0; i<it.length; i++){
	    			  if (this.original)
//	    			  	g.drawString(symbols[it[i].getType()],(int)(it[i].getinitX()*this.scale), (int)(it[i].getinitY()*this.scale));
	    				  g.fillOval((int)(it[i].getinitX()*this.scale), (int)(it[i].getinitY()*this.scale),5,5);
	    			  else
//    		  		  	g.drawString(symbols[it[i].getType()], (int)(it[i].getX()*this.scale), (int)(it[i].getY()*this.scale));
	    				  g.fillOval((int)(it[i].getX()*this.scale), (int)(it[i].getY()*this.scale),5,5);
	    			  g.setColor(new Color(colors[it[i].getColor()]));
	    	  }
	      }
	      else {
	    	  Heap[] ht = this.grid.getHeaps();
	    	  if (ht != null)
	    		  for (int i=0; i<ht.length; i++){
	    			  ArrayList<Item> items = ht[i].getItems();
	    			  Iterator<Item> it = items.iterator();
	    			  int s = 0;
	    			  while(it.hasNext()){			  
	    			  if (this.original)
	    				 g.drawString(symbols[it.next().getColor()],(int)((ht[i].getinitX()+s)*this.scale), (int)(ht[i].getinitY()*this.scale));
	    			  else
	    				  g.drawString(symbols[it.next().getColor()], (int)((ht[i].getX()+s)*this.scale), (int)(ht[i].getY()*this.scale));
	    			  s++;
	    			  }
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
				  for(String s: this.measures)
					  out.print(s + '\t');
				  out.println();
				  for (int i=0; i< this.tick;i++){
					  out.print(Integer.toString(i) +'\t');
					  for (int j=0; j< this.measures.length; j++)
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
		Item[] items =this.grid.getItems();
		DistanceMatrix d = this.grid.getDistanceMatrix();
		
		
		for (int i=0; i < conf.getnitems(); i++) {
			if (ignore && items[i].isPicked()) continue;
			for (int j=0; j < i; j++) {
				if (ignore && items[j].isPicked()) continue;
				N++;
				double x = Math.abs(d.get(i, j));
				double y = Math.abs(items[i].distance(items[j], 1));
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
	public double computeEntropy() {
		int xdim = (int)Math.floor(conf.getxsize()/10);
		int ydim = (int)Math.floor(conf.getysize()/10);
		double[][] bins = new double[xdim][ydim];
		Item[] items =this.grid.getItems();
		double count = 0;
		for (int i=0; i<items.length;i++) {
			if(items[i].isPicked()) continue;
			bins[(int)Math.floor(items[i].getX()/10)][(int)Math.floor(items[i].getY()/10)]++;
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

}





