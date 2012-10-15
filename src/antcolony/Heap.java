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

	File: Heap.java
	Package: antcolony

	Description:

	* Represents an individual item
	* Stores all essential information e.g. grid position
                                                                                                                        	
*****************************************************************/

package antcolony;

import java.util.Iterator;
import java.util.List;


/** Stores the data for an individual document
	using compressed storage format for sparse document vectors
	 */

public class Heap {
	
	public int id;
	private int x,y;						// position on the grid
	private int x_init,y_init;				// starting position
	private int xsize, ysize;				// size of grid
	private int color;						// the color of the item to display
	private List<Item> items; 				// data carried by the item
	private boolean isPicked = false;		// flag whether document is picked

/*********** Constructor ****************************************************************************/

	/** Default Constructor */
	public Heap(int i, Configuration c, String t, int cl, List<Double> a) {
		this.id = i;
		this.xsize = c.getxsize();
		this.ysize = c.getysize();
		this.x_init = (int)(Math.random()* xsize);
		this.y_init = (int)(Math.random()* ysize);
		this.x = this.x_init;
		this.y = this.y_init;
		this.color = cl;
	}

	/** Constructor given a grid position and initial data*/
	public Heap(int i, Configuration c, int x_i, int y_i, String t,int cl, List<Double> a) {
		this.id = i;
		this.xsize = c.getxsize();
		this.ysize = c.getysize();
		this.x_init = x_i;
		this.y_init = y_i;
		this.x = x_i;
		this.y = y_i;
		this.color = cl;
	}
		
	

	
			
/*********** Access & Modification Functions ****************************************************************************/


/****** item data *********/
	
	/** Get type of the item
	 * @return the associated document vector
	 */
	public int getColor() {
	return this.color;
	}
	
	
    	
	

/****** item position *********/
		
	/** Get item position
	* @return the current document x position on the grid
	*/
	public int getX() {
		return this.x;
	}
	
	/** Get item position
	* @return the current document y position on the grid
	*/
	public int getY() {
		return this.y;
	}
	
	/** Get initial item position
	* @return the starting x position of the document on the grid
	*/
	public int getinitX() {
		return this.x_init;
	}
	
	/** Get initial item position
	* @return the starting x position of the document on the grid
	*/
	public int getinitY() {
		return this.y_init;
	}
	
	
	/** Set item position
	* @param x the x coordinate of the current position of the document on the grid
	* @param y the y coordinate of the current position of the document on the grid
	*/
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

/******** misc **********************/

	/** Is this item currently being transported by an ant?
	* @return truth value signalising whether item is being transported or not
	*/
	public boolean isPicked() {
		return isPicked;
	}

	/** Set picked flag
	* @param value the new truth value
	*/
	public void setPicked(boolean value) {
		this.isPicked = value;
	}


}

