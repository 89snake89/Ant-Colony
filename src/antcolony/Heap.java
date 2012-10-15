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
import java.util.ArrayList;
import java.util.List;


/** Stores the data for an individual document
	using compressed storage format for sparse document vectors
	 */

public class Heap {
	
	public int id;
	private int x,y;						// position on the grid
	private int xsize, ysize;				// size of grid
	private int dim;						// item dimension
	private double max_distance;			// the maximum distance between two objects
	private double[]  center_of_mass;			// the object corresponding to the center of mass of this heap
	private int most_dissimilar; 			// the most dissimilar object
	private double mean_distance;			// mean distance between all the objects and the center of mass
	private ArrayList<Item> items; 				// data carried by the item
	private boolean isPicked = false;		// flag whether document is picked

/*********** Constructor ****************************************************************************/


	/** Constructor given a grid position and initial data*/
	public Heap(int i, Configuration c, int x_i, int y_i, Item item) {
		this.id = i;
		this.xsize = c.getxsize();
		this.ysize = c.getysize();
		this.x = x_i;
		this.y = y_i;
		this.items = new ArrayList<Item>();
		this.items.add(item);
		this.dim = item.getData().size();
		this.center_of_mass = new double[this.dim];
		this.most_dissimilar = item.getID();
		this.max_distance = 0;
		this.mean_distance = 0;
		this.isPicked = true;
	}
			
/*********** Access & Modification Functions ****************************************************************************/


/****** item data *********/
	
	/** get the maximum distance
	 * @return the measure
	 */
	public double getMaxDistance() {
		return this.max_distance;
	}
	
	/** get the center of mass
	 * @return the measure
	 */
	public double[] getCenterMass() {
		return this.center_of_mass;
	}
	
	/** get the most dissimilar item
	 * @return the measure
	 */
	public int getMostDissimilar() {
		return this.most_dissimilar;
	}
	
	/** get the maximum distance
	 * @return the measure
	 */
	public double getMeanDistance() {
		return this.mean_distance;
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
	* @return truth value signaling whether item is being transported or not
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


/*********** Calculate values ****************************************************************************/

public void computeCenterMass(){
	Iterator<Item> it = items.iterator();
	for (int i=0; i<dim; i++) center_of_mass[i]=0;
	while (it.hasNext()){
		Item i = it.next();
		Iterator<Double> it1 = i.getData().iterator();
		int j=0;
		while (it1.hasNext()){
			center_of_mass[j] += it1.next();
			j++;
		}
	}
	for (int i=0; i<dim; i++) center_of_mass[i]=center_of_mass[i]/dim;
}

public void computeMostDissimilarMaxDistance(){
	
}

public void updateValues(){
	
}

public void putItem(Item i){
	i.setHeap(this.id);
	this.items.add(i);
}

}