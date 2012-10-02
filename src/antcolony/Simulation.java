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

	* Topic Map Visualization
	* Uses underlying grid
	* Provides several measurement functions
	
                                                                                                                     	
*****************************************************************/

package antcolony;

import java.awt.Graphics;
import javax.swing.JPanel;


/** Wrapping class for the item and ant data. Also contains the basic grid
*/

public class Simulation extends JPanel implements Runnable  {

	private Configuration conf;	
	private Data data;
	private AntColony antColony;
	private Grid grid;
	private String [] symbols;
	private boolean  original = true;
	private double scale;
	private boolean stop;
	private boolean interrupted;

		
/********************** Constructor **************************************************************/

	public Simulation() {
	
	}
	
	/** Constructor
	* @param conf the current parameter settings
	* @param keywords the keywords spanning document space
	* @param documents the current documents
	* @param grid the current underlying grid
	* @param colony the current antColony
	*/
	public Simulation(Configuration conf, Data data) {
		
		this.conf = conf;
		this.grid = new Grid(conf,data);
		this.symbols = conf.getSymbols();
		this.scale = 1;
		this.original = true;
		this.antColony = new AntColony(conf, this.grid);
	
	}
	
	public void update(Configuration conf, Data data) {
		
		this.conf = conf;
		this.grid = new Grid(conf,data);
		this.antColony = new AntColony(conf, this.grid);
	
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
		long start = System.currentTimeMillis();
        while (!stop) {
            try {            	
            	Thread.currentThread().sleep(100);
            	this.antColony.sort();
            	this.repaint();
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
//		this.antColony.finish();	
		long end = System.currentTimeMillis();
		System.out.println("Evaluation took " + (end - start) + " milliseconds");
	
//		System.out.println("Pearson correlation: " + computePearson(false));
		
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
	      Item[] it = this.grid.getItems();
	      if (it != null)
	    	  for (int i=0; i<it.length; i++){
	    		  if (this.original)
	    			  g.drawString(symbols[it[i].getType()],(int)(it[i].getinitX()*this.scale), (int)(it[i].getinitY()*this.scale));
	    		  else
    		  		  g.drawString(symbols[it[i].getType()], (int)(it[i].getX()*this.scale), (int)(it[i].getY()*this.scale));
	    	  }
	  }

	  
/************ Measurements & Plots ******************************************************/


	/** Compute Pearson correlation
	* @return Pearson correlation computed for the entire grid
	* @param ignore the positions of documents that are currently picked up
	*/
	public double computePearson(boolean ignore) {
		double xsum = 0;
		double ysum = 0;
		double xysum = 0;
		double diff = 0;
		double xsquaresum = 0;
		double ysquaresum = 0;
		double N = 0;
		Item[] items =this.grid.getItems();
		
		
		for (int i=0; i < conf.getnitems(); i++) {
			if (ignore == true) {
				if (items[i].isPicked() == true) continue;
			}
			for (int j=0; j < i; j++) {
				if (ignore == true) {
					if (items[j].isPicked() == true) continue;
				}
				N++;
/*				double x = Math.abs(grid.distance(i,j));
				double y = Math.abs(items[i].currentDistance(items[j], conf.getysize(), conf.getxsize()));
				xsum += x;
				xsquaresum += (x*x);
				ysum += y;
				ysquaresum += (y*y);
				xysum += (x*y);
*/			}
		}
		double num = xysum - xsum*ysum/N;
		double denom = Math.sqrt((xsquaresum - xsum*xsum/N)*(ysquaresum-ysum*ysum/N));
		if (denom == 0) return 0;
		return num / denom ;
	}

}





