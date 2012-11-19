package antcolony;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;


/**
 * The Heap class represents an heap of items as is defined in the AntClass paper.
 * The class stores all the items but also some metrics of the heap, namely:
 * <p>
 * <ul>
 * <li> Most dissimilar item
 * <li> The heap center
 * <li> The maximum dissimilarity between items
 * </ul>
 * 
 * @author      António Fonseca
 * @version     1.6
 * @since       1.0
 * */
public class Heap {
	private UUID id;							// id of the heap
	private int x,y;						// coordinates of the heap
	private Configuration conf;
	private double max_distance;			// the maximum distance between two objects
	private double[]  center_of_mass;			// the object corresponding to the center of mass of this heap
	private UUID most_dissimilar; 			// the most dissimilar object
	private double max_dissimilar;
	private double mean_distance;			// mean distance between all the objects and the center of mass
	private LinkedList<Item> items; 		// data carried by the item
	private int pheromone;
	private boolean picked;

/*********** Constructor ****************************************************************************/


	/** Constructor given a grid position and two initial items
	 * @param c current configuration of the simulation
	 * @param x_i coordinate to build this heap
	 * @param y_i coordinate to build this heap
	 * @param it1 first item of the heap
	 * @param it2 second item of the heap
	 * */
	public Heap(Configuration c, int x_i, int y_i, Item it1, Item it2) {
		this.conf = c;
		this.id = java.util.UUID.randomUUID();
		this.x = x_i;
		this.y = y_i;
		this.items = new LinkedList<Item>();
		it1.setXY(x_i, y_i);
		this.items.add(it1);
		this.center_of_mass = new double[conf.getnkeys()];
		this.putItem(it2);
		this.pheromone = 500;
		this.picked = false;
	}
	
	/** Constructor given a grid position and just one initial items
	 * @param c current configuration of the simulation
	 * @param x_i coordinate to build this heap
	 * @param y_i coordinate to build this heap
	 * @param it1 first item of the heap
	 * */
	public Heap(Configuration c, int x_i, int y_i, Item it1) {
		this.conf = c;
		this.id = java.util.UUID.randomUUID();
		this.x = x_i;
		this.y = y_i;
		this.items = new LinkedList<Item>();
		it1.setXY(x_i, y_i);
		this.items.add(it1);
		this.center_of_mass = new double[conf.getnkeys()];
		this.pheromone = 500;
		this.picked = false;
	}
			
/*********** Access & Modification Functions ****************************************************************************/


/****** item data *********/
	
	/** get the maximum distance between items in thsi heap
	 * @return measure of maximum distance
	 */
	public double getMaxDistance() {
		return this.max_distance;
	}
	
	/** get the distance between center and most dissimilar
	 * @return the measure of distance most dissimilar
	 */
	public double getMaxDissimilar() {
		return this.max_dissimilar;
	}
	
	/** get the center of mass
	 * @return the center of mass
	 */
	public double[] getCenterMass() {
		return this.center_of_mass;
	}
	
	/** get the most dissimilar item
	 * @return the most dissimilar item UUID
	 */
	public UUID getMostDissimilar() {
		return this.most_dissimilar;
	}
	
	/** Get id of the heap
	 * @return the UUID of the heap
	 */
	public UUID getID() {
	return this.id;
	}
	
	/** get the mean distance between items
	 * @return the mean distance
	 */
	public double getMeanDistance() {
		return this.mean_distance;
	}
	
	/** Get the items on this heap
	 * @return a linked list with the ietms
	*/
	public LinkedList<Item> getItems() {
		return this.items;
	}
	
	/** Get the size this heap
	 * @return the int size of this heap
	*/
	public int getSize() {
		return this.items.size();
	}
	
	/** Get the pheromone of this heap
	 * @return the pheromone level
	*/
	public int getPheromone() {
		return this.pheromone;
	}
	
	/** Is this heap picked
	 * @return the picked status of this heap
	*/
	public boolean isPicked() {
		return this.picked;
	}

	
	
	/****** heap position *********/
	
	/** Get heap  horizontal position
	* @return the current document x position on the grid
	*/
	public int getX() {
		return this.x;
	}
	
	/** Get heap vertical position
	* @return the current document y position on the grid
	*/
	public int getY() {
		return this.y;
	}

	/** Set the heap position
	* @param x the position coordinate
	* @param y the position coordinate 
	*/
	public void setXY(int x, int y){
		this.x = x;
		this.y = y;
	}

/*********** Calculate values ****************************************************************************/

	
/** Compute the center of mass of this heap and store it on the heap
*/
public void computeCenterMass(){
	for (int i=0; i<conf.getnkeys(); i++) this.center_of_mass[i]=0;
	Iterator<Item> it = this.items.iterator();
	while (it.hasNext()){
		Item i = it.next();
		Iterator<Double> it1 = i.getData().iterator();
		int j=0;
		while (it1.hasNext()){
			this.center_of_mass[j] += it1.next();
			j++;
		}
	}
	for (int i=0; i<conf.getnkeys(); i++) center_of_mass[i]=center_of_mass[i]/(double)this.items.size();
}

/** Compute the distance from this item to the center of mass of this heap
 * @param i the item
 * @return the distance
*/
public double computeDistanceCenterMass(Item i){
		Iterator<Double> it = i.getData().iterator();
		int j=0;
		double sum = 0.0;
		while (it.hasNext()){
			sum += Math.pow(this.center_of_mass[j] - (Double)it.next(),2);
			j++;
		}
		return Math.sqrt(sum);
}

/** Compute the distance from a vector to the center of mass of this heap
 * @param v the vector
 * @return the distance
*/
public double computeDistanceCenterMassVector(double[] v){
	double sum = 0.0;
	for (int i=0; i<v.length; i++) sum += Math.pow(this.center_of_mass[i] - v[i],2);
	return Math.sqrt(sum);
}

/** Compute the most dissimilar item and store his reference on the heap
*/
public void computeMostDissimilar(){
	Iterator<Item> it = this.items.iterator();
	this.max_dissimilar = 0;
	while (it.hasNext()){
		Item i = it.next();
		double dist = this.computeDistanceCenterMass(i);
		if (dist >= this.max_dissimilar){
			this.max_dissimilar = dist;
			this.most_dissimilar = i.getID();
		}
	}	
}

/** Compute the mean distance between items and store it on the heap
*/
public void computeMeanDistance(){
	Iterator<Item> it = this.items.iterator();
	double mean_d = 0;
	while (it.hasNext())mean_d += this.computeDistanceCenterMass(it.next());
	this.mean_distance = mean_d / (double)this.items.size();
}

/** Put an item on the heap and update all the heap metrics
 * @param i item to put
*/
public void putItem(Item i){
	Iterator<Item> it = this.items.iterator();
	while (it.hasNext()){
		Item j = it.next();
		double dist = i.distance(j, 2);
		if (dist > this.max_distance)this.max_distance = dist;
		}
	i.setXY(this.x, this.y);
	this.items.add(i);
	this.computeCenterMass();
	this.computeMeanDistance();
	this.computeMostDissimilar();
}

/** Put a list of items on the heap and update all the heap metrics
 * @param items list of items to put
*/
public void putItems(LinkedList<Item> items){
	Iterator<Item> it = items.iterator();
	while (it.hasNext()) this.putItem(it.next());	
}


/** Get an item from this heap by its UUID and remove it
 * @param id of the item
 * @return item
*/
public Item getItem(UUID id){
	Item r = null;
	Iterator<Item> it = this.items.iterator();
	done: while (it.hasNext()) {
		r = it.next();
		if (r.getID()==id) {
			it.remove();
			break done ;
		}
	}
	LinkedList<Item> list = new LinkedList<Item>(this.items);
	this.items= new LinkedList<Item>();
	it = list.iterator();
	while(it.hasNext()) this.putItem(it.next());
	return r;
}

/** Set the picked status of this heap
 * @param f boolean status
*/
public void setPicked(boolean f){
	this.picked = f;
}

/** Set the pheromone level of this heap
 * @param p the pheromone level
*/
public void setPheromone(int p){
	this.pheromone = p;
}

/** Decrement the pheromone level of this heap
*/
public void decPheromone(){
	this.pheromone--;
	if (this.pheromone<=0) this.pheromone=0;
}

}