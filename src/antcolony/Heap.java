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
import java.util.LinkedList;


/** Stores the data for an individual document
	using compressed storage format for sparse document vectors
	 */

public class Heap {
	private int id;
	private int dim;						// item dimension
	private double max_distance;			// the maximum distance between two objects
	private double[]  center_of_mass;			// the object corresponding to the center of mass of this heap
	private int most_dissimilar; 			// the most dissimilar object
	private double max_dissimilar;
	private double mean_distance;			// mean distance between all the objects and the center of mass
	private LinkedList<Item> items; 		// data carried by the item

/*********** Constructor ****************************************************************************/


	/** Constructor given a grid position and initial data*/
	public Heap(Configuration c, int x_i, int y_i, Item it1, Item it2) {
		this.id = it1.getID();
		this.items = new LinkedList<Item>();
		this.items.addLast(it2);
		this.items.addLast(it1);
		this.dim = it1.getData().size();
		this.center_of_mass = new double[this.dim];
		this.computeCenterMass();
		this.computeMostDissimilar();
		this.computeMaxDistance(it2);
		this.computeMeanDistance();
	}
			
/*********** Access & Modification Functions ****************************************************************************/


/****** item data *********/
	
	/** get the maximum distance
	 * @return the measure
	 */
	public double getMaxDistance() {
		return this.max_distance;
	}
	
	/** get the maximum dissimilar distance
	 * @return the measure
	 */
	public double getMaxDissimilar() {
		return this.max_dissimilar;
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
	
	/** Get id of the heap
	 * @return the associated document vector
	 */
	public int getID() {
	return this.id;
	}
	
	/** get the maximum distance
	 * @return the measure
	 */
	public double getMeanDistance() {
		return this.mean_distance;
	}
	
	/** Get the items on this heap
	*/
	public LinkedList<Item> getItems() {
		return this.items;
	}
	
	/** Get the size this heap
	*/
	public int getSize() {
		return this.items.size();
	}


/*********** Calculate values ****************************************************************************/

public void computeCenterMass(){
	Iterator<Item> it = this.items.iterator();
	for (int i=0; i<dim; i++) this.center_of_mass[i]=0;
	while (it.hasNext()){
		Item i = it.next();
		Iterator<Double> it1 = i.getData().iterator();
		int j=0;
		while (it1.hasNext()){
			this.center_of_mass[j] += it1.next();
			j++;
		}
	}
	for (int i=0; i<dim; i++) center_of_mass[i]=center_of_mass[i]/dim;
}

public double computeDistanceCenterMass(Item i){
		Iterator<Double> it = i.getData().iterator();
		int j=0;
		double sum = 0;
		while (it.hasNext()){
			sum += Math.pow(this.center_of_mass[j] - (Double)it.next(),2);
			j++;
		}
		return Math.sqrt(sum);
}

public void computeMostDissimilar(){
	Iterator<Item> it = this.items.iterator();
	this.max_dissimilar = 0;
	while (it.hasNext()){
		Item i = it.next();
		Iterator<Double> it1 = i.getData().iterator();
		double sum = 0;
		int j=0;
		while (it1.hasNext()) {
			sum += Math.pow(center_of_mass[j]- it1.next(),2);
			j++;
		}
		sum = Math.sqrt(sum);
		if (sum > this.max_dissimilar){
			this.max_dissimilar = sum;
			this.most_dissimilar = i.getID();
		}
	}	
}

public void computeMaxDistance(Item j){
	Iterator<Item> it = this.items.iterator();
	this.max_distance = 0;
	while (it.hasNext()){
		Item i = it.next();
		Iterator<Double> it1 = i.getData().iterator();
		Iterator<Double> it2 = j.getData().iterator();
		double sum = 0;
		while (it1.hasNext()) sum += Math.pow(it2.next()- it1.next(),2);
		sum = Math.sqrt(sum);
		if (sum > this.max_distance)this.max_distance = sum;
	}
	
}

public void computeMeanDistance(){
	Iterator<Item> it = this.items.iterator();
	double mean_d = 0;
	while (it.hasNext()){
		Item i = it.next();
		Iterator<Double> it1 = i.getData().iterator();
		int j=0;
		while (it1.hasNext()) {
			mean_d += Math.abs(Math.pow(center_of_mass[j]- it1.next(),2));
			j++;
		}
	}	
	this.mean_distance = Math.sqrt(mean_d) / (double)this.items.size();
}

public void putItem(Item i){
	this.computeMaxDistance(i);
	this.items.addLast(i);
	this.computeCenterMass();
	this.computeMeanDistance();
	this.computeMostDissimilar();
}

public Item removeItem(int id){
	Item r = null;
	Iterator<Item> it = this.items.iterator();
	done: while (it.hasNext()) {
		r = it.next();
		if (r.getID()==id) {
			it.remove();
			break done ;
		}
	}
	return r;
}

public Item removeLastItem(){
	return this.items.removeLast();
}

}