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

	File: Item.java
	Package: antcolony

	Description:

	* Represents an individual item
	* Stores all essential information e.g. grid position
                                                                                                                        	
*****************************************************************/

package antcolony;

import java.util.List;

/** Stores the data for an individual document
	using compressed storage format for sparse document vectors
	 */

public class Item {
	
	public int id;
	private int x,y;						// position on the grid
	private int x_init,y_init;				// starting position
	private int xsize, ysize;				// size of grid
	private String type;					// the type of the item
	private int color;						// the color of the item to display
	private List<?> data; 	// data carried by the item
	private boolean isPicked = false;		// flag whether document is picked

/*********** Constructor ****************************************************************************/

	/** Default Constructor */
	public Item(int i, Configuration c, String t, int cl, List<?> a) {
		this.id = i;
		this.xsize = c.getxsize();
		this.ysize = c.getysize();
		this.x_init = (int)(Math.random()* xsize);
		this.y_init = (int)(Math.random()* ysize);
		this.x = this.x_init;
		this.y = this.y_init;
		this.type = t;
		this.color = cl;
		this.data = a;
	}

	/** Constructor given a grid position and initial data*/
	public Item(int i, Configuration c, int x_i, int y_i, String t,int cl, List<?> a) {
		this.id = i;
		this.xsize = c.getxsize();
		this.ysize = c.getysize();
		this.x_init = x_i;
		this.y_init = y_i;
		this.x = x_i;
		this.y = y_i;
		this.type = t;
		this.color = cl;
		this.data = a;
	}
		
	
/********** Distance functions********************************************************************************************/

	/* The distance between this item and other
	 * @param i other item
	 * @parame type of measure, 0 - between initial positions, 1 - current position
	 */

	public double distance(Item i, int measure) {

		double d = 0;
		
		switch (measure){

		case 0 : 		double xdiff = Math.abs(i.getinitX() - this.x_init);
						xdiff = Math.min(xdiff, xsize-xdiff);
						double ydiff = Math.abs(i.getinitY() - this.y_init);
						ydiff = Math.min(ydiff, ysize-ydiff);
						d = Math.sqrt(xdiff*xdiff + ydiff*ydiff);
						break;
						
		case 1 : 		xdiff = Math.abs(i.getX() - this.x);
						xdiff = Math.min(xdiff, xsize-xdiff);
						ydiff = Math.abs(i.getY() - this.y);
						ydiff = Math.min(ydiff, ysize-ydiff);
						d = Math.sqrt(xdiff*xdiff + ydiff*ydiff);
						break;
		
		}
		
		return d;

	}
	
			
/*********** Access & Modification Functions ****************************************************************************/


/****** item data *********/
	
	/** Get type of the item
	 * @return the associated document vector
	 */
	public String getType() {
	return this.type;
	}
	
	/** Get type of the item
	 * @return the associated document vector
	 */
	public int getColor() {
	return this.color;
	}
	
	/** Set type of the item
	* @param t - the type of the item
	*/
	public void setType(String t) {
		this.type = t;
	}
    	
	/** Get data of the item
	 * @return the associated document vector
	 */
	public List<?> getData() {
	return this.data;
	}
	
	/** Set data of the item
	 * @param a - the HashMap representing the data carried by the tem
	*/
	public void setData(List<?> a) {
		this.data = a;
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
