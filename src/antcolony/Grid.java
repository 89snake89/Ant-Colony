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


/**  Represents the grid underlying a topic map, provides functions
	*	  for access and manipulation
	*/


public class Grid {

	private Configuration conf;			// Current configuration
	private Item[] items;				// Current document collection
	private Item[][] cells;				// Cell matrix
	private DistanceMatrix distance;	// Precomputed distance matrix

	
	/**** Constructor and Initialisation **************************************************************/

	/** Constructor
	* @param conf the current parameter settings
	* @param documents the current document data
	*/
	public Grid(Configuration conf, Data data) {

		// store provided information
		this.conf = conf;
		this.items = data.getItems();
		this.cells = new Item[this.conf.getysize()][this.conf.getxsize()];
				
		// initialize base matrix (item id "-1" signifies "not occupied")
		for (int i=0; i < conf.getxsize(); i++)
			for (int j=0; j< conf.getysize(); j++)
				this.cells[i][j] = null;

		this.distance = new DistanceMatrix(data, conf);
 		
		// generate starting distribution
		scatterItems();
	}

	
	/**
	* Advise random position on the grid to all items
	*/
	public void scatterItems() {
		int x, y;
		clear();
		for (int i = 0; i < this.items.length; i++) {
			while (true) {
				x = (int)Math.floor(this.conf.getxsize() * Math.random());
				y = (int)Math.floor(this.conf.getysize() * Math.random());
					if ( this.cells[x][y]==null) {
						this.cells[x][y] = this.items[i];
						items[i].setXY(x, y);
						break;
					}
				}
		}
	}
	
	
	
	/**
	* Clear the grid
	*/
	public void clear() {
		for (int i=0; i<conf.getxsize(); i++)
			for (int j=0; j<conf.getxsize(); j++)
				this.cells[i][j]=null;
	}

	/**** simple access functions *********************************************************/
	 

	/**
	* Get the distance matrix
	* @param i the provided document number
	* @param pos storage space for the corresponding position
	*/
	
	public DistanceMatrix getDistanceMatrix() {
		return this.distance;
	}

	/** Get the Item for a given grid position
	* @param x the provided grid x-coordinate
	* @param y the provided grid y-coordinate
	* @return the document null if empty
	*/
	public Item getItemAt(int x, int y) {
		Item i = null;
		if (occupied(x,y)) i=this.cells[x][y];
		return i;
	}

	/** Check if a cell is occupied
	* @param x the provided grid x-coordinate
	* @param y the provided grid y-coordinate
	* @return the document number or -1 if empty
	*/

	public boolean occupied(int y, int x) {
		return (this.cells[y][x] != null);
	}

	/** Get the item number by id
	* @param x the provided grid x-coordinate
	* @param y the provided grid y-coordinate
	* @return the document number or -1 if empty
	*/

	public Item getById(int id) {
		Item r = null;
		for (int i=0; i< this.items.length; i++)
			if (this.items[i].id == id) r = this.items[i];
		return r;
	}

	/** Get the document colour for a given grid position
	* @param x the provided grid x-coordinate
	* @param y the provided grid y-coordinate
	* @return the document number or 0 if empty
	*/
	public Item[] getItems() {
		return this.items;
	}
	

	/**** simple manipulation *******************************************************/
	
	
	/** Place a item at a given position on the grid
	* @param x, y the position of the item
	* @param item the Item
	*/
	
	public void set(int x, int y, Item item) {
		cells[x][y] = item;
	}
	
	/** Remove item at a given position from the grid
	* @param x, y the document position on the grid
	*/
	
	public void remove(int x, int y) {
		this.cells[x][y] = null;
	}

	
	/**** measures on the grid *******************************************************/
	
	/** Informs ant about the density and similarity  of items in a neighborhood of cells
	* @param x, y the cell coordinates
	* @return the density 
	* */

    public double densityAt(int x, int y) {
	
        Item it = this.getItemAt(x, y);
    	int xsize = this.conf.getxsize();
		int ysize = this.conf.getysize();
		int sigma = this.conf.getsigma();
		int xhigh = x + sigma;
		int yhigh = y + sigma;
		int xlow = x - sigma;
		int ylow = y - sigma;
		
		int ih, jh;
		double sum = 0;

		for (int i = ylow; i <= yhigh; i++) {
			for (int j = xlow; j <= xhigh; j++) {
				ih = i;
				jh = j;
				
				if (jh < 0) jh = xsize + jh%xsize;
				if (jh >= xsize) jh = jh%xsize;
				if (ih < 0) ih = ysize + ih%ysize;
				if (ih >= ysize) ih = ih%ysize;
		
				double div =  this.distance.getScaleFactor();
				
				if ( occupied(jh,ih) && (jh != x || ih != y) ){
					Item it1 = this.getItemAt(jh, ih);
					sum += (1 - distance.get(it.id,it1.id)/div);
				}
					
			}
		}	
		
		// divide by area size
		int size = this.conf.getsigma()*2+1;
		size *= size;
		size -= 1;
			
		return Math.max(0, sum/size);
	}	

}


