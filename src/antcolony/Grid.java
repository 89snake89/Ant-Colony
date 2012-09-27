/*  
    Copyright (C) 2012 Antonio Fonseca
    Email: Julia.Handl@gmx.de

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

	File: Grid.java
	Package: antcolony

	Description: 

	* Represents the grid underlying a topic map, provides functions
	  for access and manipulation
	* Stores all essential information (ant and document positions)
	=> Manages access in both directions:
		- position -> document
		- document -> position

	* Generates:
		- initial random distribution for ants
	

	* Provides functions used by ants:
		- density function
		- next free position
		- execution of a step

*****************************************************************/


package antcolony;

import java.util.*;
import java.io.*;
import javax.swing.JPanel;

/**  Represents the grid underlying a topic map, provides functions
	*	  for access and manipulation
	*/
public class Grid extends JPanel implements Serializable{


	private static final long serialVersionUID = -6894027807371164326L;
	
	private Configuration conf;			// Current configuration
	private Item [] items;				// Current document collection
	private int [][] cells;				// Cell matrix
	private DistanceMatrix distance;	// Precomputed distance matrix
	private boolean antMode = true;		// Switch Ant-Mode / MDS-Mode
	private double scaleFactor = 0;     // scale factor
	private Vector labels = null;

	
	/**** Constructor and Initialisation **************************************************************/

	/** Constructor
	* @param conf the current parameter settings
	* @param documents the current document data
	* @param antMode switch between the four different mapping modes
	*/
	public Grid(Configuration conf, Item [] items) {

		// store provided information
		this.items = items;
		this.conf = conf;
		this.cells = new int[this.conf.getysize()][this.conf.getxsize()];
				
		// initialize base matrix (item id "-1" signifies "not occupied")
		for (int i=0; i < conf.getysize(); i++)
			for (int j=0; j< conf.getxsize(); j++)
				this.cells[i][j] = -1;

		this.distance = new DistanceMatrix(items, conf);
 		
		// generate starting distribution
		scatterDocuments();
	}



	
	/**
	* Advise random position on the grid to all document in ant-mode
	* Or advise MDS positions in MDS mode
	*/
	public void scatterDocuments() {
	
		int x, y;
		
		for (int i = 0; i < this.conf.getndocs(); i++) {
			
			posIndex[i] = new Position();

			/// advise random and distinct positions if operating with ants
			if ((antMode == true)  && (conf.getMethod() != 2)) {
				while (true) {
					x = (int)Math.floor(this.conf.getxsize() * Math.random());
					y = (int)Math.floor(this.conf.getysize() * Math.random());
					pos = new Position(y, x);
					
					// one document only per grid cell	
					if ( free(pos) ) {
					
						// store information in grid
						set(pos, i);
						
						// advose position to document
						documents[i].setPosition(pos);
						
						// update position index
						posIndex[i].set(pos);
						
						// go on with next document
						break;
					}
				}
			}
			// otherwise advise positions computed by MDS
			else {
				// retrieve information
				pos = documents[i].getPosition();
				
				if (free(pos) != true) nextFree(pos);
				
				
				// store information in grid
				set(pos, i);
				
								
				// update position index
				posIndex[i].set(pos);
				
				
			}
		}
	}
	

	/**** simple access functions *********************************************************/
	 


	/**
	* Get the document position for a given document number
	* @param i the provided document number
	* @param pos storage space for the corresponding position
	*/
	public void getPos(int i, Position pos) {
		pos.set(this.posIndex[i]);
		return;
	}


	/** Get the document number for a given grid position
	* @param pos the provided grid position
	* @return the document number or -1 if empty
	*/
	public int at(Position pos) {
		return occupied[pos.getY()][pos.getX()];
	}

	/** Get the document number for a given grid position
	* @param x the provided grid x-coordinate
	* @param y the provided grid y-coordinate
	* @return the document number or -1 if empty
	*/
	public int at(int y, int x) {
		return occupied[y][x];
	}

	/** Get the document colour for a given grid position
	* @param x the provided grid x-coordinate
	* @param y the provided grid y-coordinate
	* @return the document number or 0 if empty
	*/
	public int getColor(int y, int x) {
		int doc = occupied[y][x];
		if  ( doc!= -1) {
			return documents[doc].getColor();
		}
		else return 0;
	}

	/** Check whether a position on the grid occupied
	* @param pos the provided grid position
	* @return the true if occupied, false otherwise
	*/
	public boolean free(Position pos) {
		return (occupied[pos.getY()][pos.getX()] == -1);
	}
  
    /** Check whether a position on the grid occupied
	* @param x th provided grid x-coordinate
	* @param y the provided grid y-coordinate
	* @return the true if occupied, false otherwise
	*/
	public boolean free(int y, int x) {
		return (occupied[y][x] == -1);
	}
	
	/** Return the mean dissimilarity computed for the current document data
	* @return the mean dissimilarity of the document collection
	*/
	public double getScaleFactor() {
		return conf.getdistscale();
	}
	
	/**
	* Access to precomputed matrix if inter-document distances
	* @param i a first document
	* @param j a second document
	* @return the distance betweeen these documents in document space
	*/
	public double distance(int i, int j) {
		return distance.get(i,j);
	}


	

	/**** simple manipulation *******************************************************/
	
	
	/** Update position Index (store the new position for document i)
	* @param i the document number
	* @param pos the new position of this document
	*/
	public void setPos(Position pos, int i) {
		this.posIndex[i].set(pos);
	}
	
	/** Place a document (number provided) at a given position on the grid
	* @param ant the document number
	* @param pos the document position on the grid
	*/
	public void set(Position pos, int ant) {
		occupied[pos.getY()][pos.getX()] = ant;
	}
	
	/** Remove document at a given position from the grid
	* @param pos the document position on the grid
	*/
	public void remove(Position pos) {
		occupied[pos.getY()][pos.getX()] = -1;
	}
	
	



	/**** functions for ants *********************************************************************/

	
	/** Move an ants one step in dependence of its speed and memory
	* @param pos the ant's grid position
	* @param speed the ant's speed
	* @param a pointer to the ant's memory
	*/
	public void move(Position pos, int speed, Memory memory) {

		Position target = new Position();
		int xsize = this.conf.getxsize();
		int ysize = this.conf.getysize();
	

		// check whether the memory is activated, keep using memory non-deterministic
		
		// walk with use of memory
		if ( ( memory.isOriented() )  && (Math.random() < 0.6) ) {
		
			// get the best matching memory position
			memory.targetPos(target);
			
			// compute the resulting direction			
			double y = target.getY() - pos.getY();
			double x = target.getX() - pos.getX();
			double dist = (int)Math.sqrt(x*x+y*y);

			// take shortest way: ants are walking on a torus!
			if (y > conf.getysize()/2) y = -(conf.getysize()-y);
			if (x > conf.getxsize()/2) x = -(conf.getxsize()-x);

			// go one step (stepsize defined by speed) or
			// (if it closer than one step) directly to the memory position
			if (dist > speed) {
				if (dist != 0) x /= dist;
				if (dist != 0) y /= dist;
		
				y = pos.getY() + (double)speed*y;
				x = pos.getX() + (double)speed*x;
			}
			else {
				y = pos.getY() + y;
				x = pos.getX() + x;
				memory.setOriented(false);
			}
			
			// keep ant on the torus
			if (x < 0) x = xsize + x%xsize;
			if (x >= xsize) x = x%xsize;
			if (y < 0) y = ysize + y%ysize;
			if (y >= ysize) y = y%ysize;
			
			// advise new position		
			pos.set((int) y, (int) x);
			return;
		}
		
		// walk without use of memory
		else {
			// compute random x and y direction
			int xpart = (int)Math.round(speed * Math.random());
			int ypart = speed - xpart;
		
			// take shortest way: ants are walking on a torus!
			if ( Math.random() < 0.5) xpart = -xpart;
			if (Math.random() < 0.5) ypart = -ypart;

			// compute new position
			int x = pos.getX() + xpart;
			int y = pos.getY() + ypart;

			// keep on torus
			if (x < 0) x = xsize + x%xsize;
			if (x >= xsize) x = x%xsize;
			if (y < 0) y = ysize + y%ysize;
			if (y >= ysize) y = y%ysize;
					
			// advise new position
			pos.set((int) y, (int) x);
                        return;
		}
	}

	/** Informs ant about the density and similarity  f of documents (relative to the considered document) at a given position
	* @param docnbr the considered document
	* @param pos the considered position
	* @param speed the speed of the ant judging
	* @return the similarity and density f
	*/
    public double densityAt(int docnbr, Position pos, int speed) {
	
             	int xsize = this.conf.getxsize();
		int ysize = this.conf.getysize();
	
		int xhigh = pos.getX()+this.conf.getsigma();
		int yhigh = pos.getY()+this.conf.getsigma();
		int xlow = pos.getX()-this.conf.getsigma();
		int ylow = pos.getY()-this.conf.getsigma();
		
		int ih, jh;
		double sum = 0;

                // look at local neigbourhood (size defined by sigma)
		for (int i = ylow; i <= yhigh; i++) {
			for (int j = xlow; j <= xhigh; j++) {
				ih = i;
				jh = j;
				
				// at the borders: implement torus
				if (jh < 0) jh = xsize + jh%xsize;
				if (jh >= xsize) jh = jh%xsize;
				if (ih < 0) ih = ysize + ih%ysize;
				if (ih >= ysize) ih = ih%ysize;
		
				// consider all other documents yu find in that area
				if ((occupied[ih][jh] != -1) && (occupied[ih][jh] != docnbr)) {
				    	
				    // make selectivity speed-dependent
					double div = this.conf.getdistscale()+this.conf.getdistscale()*((speed-1)/conf.getspeed());
					
					// distance scale factor
					//double div =  this.conf.getdistscale();
					
					// compute the density-simlarity measure
					// for each occupied grid cell reward is in [-1, 1]
					sum += (1 - distance.get(docnbr,occupied[ih][jh])/div);
				}
			}
		}	
		
		// divide by area size
		int size = this.conf.getsigma()*2+1;
		size *= size;
		size -= 1;
			
		return Math.max(0, sum/size);
	}
	
	/** Moves an ant from its current (already occupied) position to the next free one
	* @param pos the current ant position, the new position is also stored in here
	*/
	public void nextFree(Position pos) {	
		int xlow, ylow, xhigh, yhigh, i, j;
		int xsize = this.conf.getxsize();
		int ysize = this.conf.getysize();
		int step = 1;
	
		// try until you find a free position (increase searching radius over time)
		while (true) {

			// try five times with each radius
			for (i = 0; i< 5; i++) {
				
				// make a step with current radius
				int xpart = (int)Math.round(step * Math.random());
				int ypart = step - xpart;
		
				if ( Math.random() < 0.5) xpart = -xpart;
				if (Math.random() < 0.5) ypart = -ypart;

				int x = pos.getX() + xpart;
				int y = pos.getY() + ypart;

				if (x < 0) x = xsize + x%xsize;
				if (x >= xsize) x = x%xsize;
				if (y < 0) y = ysize + y%ysize;
				if (y >= ysize) y = y%ysize;

				pos.set(y, x);
				
				// finish, if the position is free, otherwise try further
				if ( free(pos) ) return;
				
			}
                step++;
		}
	}
	


	/**
	* Find the document closest to a provided position
	* @param x the provided x coordinate
	* @param y the provided y coordinate
	*/
	public int nextOccupied(int y, int x) {
		int xsize = this.conf.getxsize();
		int ysize = this.conf.getysize();
		int step = 0;
	
		// try until you find an occupied position (increase searching radius over time)
		while (true) {
			for (int i=Math.max(0, y-step); i<=Math.min(ysize-1,y+step); i++) {
				for (int j=Math.max(0, x-step); j<=Math.min(xsize-1,x+step); j++) {
					if ((occupied[i][j] != -1)) {
						return occupied[i][j];
					}
				}
			}
			step++;
		}
	}
		
	
	


	/**
	* Compute the eucclidean distance between two map positions A and B
	* @param i1 y coordinate for position A
	* @param j1 x coordinate for position A
	* @param i2 y coordinate for position B
	* @param j2 x coordinate for position B
	* @return return the Euclidean distance
	*/
	private double euclidean(int i1, int j1, int i2, int j2) {
		double temp1 = (i1-i2);
		temp1 *= temp1;
		double temp2 = (j1-j2);
		temp2 *= temp2;
		return Math.sqrt(temp1+temp2);
	}
		
}


