package antcolony;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


/**
 * The Item class represents an item of the dataset.
 * The class stores all the data relevant for each instance, namely:
 * <p>
 * <ul>
 * <li> The item intrinsic data for each of the attributes of the current dataset.
 * <li> Initial and current coordinates of the item on the grid.
 * <li> If it is picked
 * <li> The cluster in which the irem belongs
 * <li> The maximum dissimilarity between items
 * </ul>
 * 
 * @author      António Fonseca
 * @version     1.8
 * @since       1.0
 * */
public class Item {
	
	private UUID id;
	private int x,y;						// position on the grid
	private int x_init,y_init;				// starting position
	private int xsize, ysize;				// size of grid
	private String type;					// the type of the item
	private int color;						// the color of the item to display
	private List<Double> data; 				// data carried by the item
	private boolean isPicked = false;		// flag whether item is picked
	private int cluster;					// number of the cluster to which belong			//

/*********** Constructor ****************************************************************************/

	/** Constructor to build an Item at random in the grid
	 * @param key The UUID of the item
	 * @param c current configuration of the simulation
	 * @param t the type of the item
	 * @param cl the color of the item
	 * @param a the list of data of this item
	 * */
	public Item(UUID key, Configuration c, String t, int cl, List<Double> a) {
		this.id = key;
		this.xsize = c.getxsize();
		this.ysize = c.getysize();
		this.x_init = (int)(Math.random()* xsize);
		this.y_init = (int)(Math.random()* ysize);
		this.x = this.x_init;
		this.y = this.y_init;
		this.type = t;
		this.color = cl;
		this.cluster = 0;
		this.data = a;
		this.isPicked = false;
	}

	/** Constructor to build an Item at a given position in the grid
	 * @param key The UUID of the item
	 * @param c current configuration of the simulation
	 * @param x_i initial position x coordinate
	 * @param y_i initial position y coordinate
	 * @param t the type of the item
	 * @param cl the color of the item
	 * @param a the list of data of this item
	 * */
	public Item(UUID key, Configuration c, int x_i, int y_i, String t,int cl, List<Double> a) {
		this.id = key;
		this.xsize = c.getxsize();
		this.ysize = c.getysize();
		this.x_init = x_i;
		this.y_init = y_i;
		this.x = x_i;
		this.y = y_i;
		this.type = t;
		this.color = cl;
		this.cluster = 0;
		this.data = a;
		this.isPicked = false;
	}
		
	
/********** Distance functions********************************************************************************************/

	/** The distance between this item and other item
	 * @param i other item
	 * @param measure 0 - between initial positions, 1 - current position, 2 - between data
	 * @return the value of the distance
	 */
	public double distance(Item i, int measure) {

		double d=0;
		
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
						
		case 2 :		List<Double> other = i.getData();
						Iterator<Double> it = this.data.iterator();
						Iterator<Double> it1 = other.iterator();
						double sum = 0;
						while (it.hasNext() && it1.hasNext()) sum += Math.pow((Double)it.next()-(Double)it1.next(),2);
						d = Math.sqrt(sum);
						break;
		case 3 :		if (this.type.equals(i.getType())) d=0; else d=1;
						break;
						
		}
		
		return d;

	}
	
	
	/** The distance between this item and a vector of item keys
	 * @param v - vector
	 * @return the distance
	 */
	public double distance_vector(double[] v) {
		Iterator<Double> it = this.data.iterator();
		double sum = 0;
		int i=0;
		while (it.hasNext()){
			sum += Math.pow((Double)it.next()-v[i],2);
			i++;
		}
		return Math.sqrt(sum);
	}
			
/*********** Access & Modification Functions ****************************************************************************/


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
	* @param t the type of the item
	*/
	public void setType(String t) {
		this.type = t;
	}
    	
	/** Get data of the item
	 * @return the associated document vector
	 */
	public List<Double> getData() {
	return this.data;
	}
	
	/** Set data of the item
	 * @param a - the List representing the data carried by the item
	*/
	public void setData(LinkedList<Double> a) {
		this.data = a;
	}
	
	/** Get the id  of the item
	 * @return UUID - the item id
	*/
	public UUID getID() {
		return this.id;
	}

	/** Set the cluster id  of the item
	 * @param c the cluster
	*/
	public void setCluster(int c){
		this.cluster = c;
	}
	
	/** Get the cluster  to which belong this item
	 * @return the cluster id
	*/
	public int getCluster(){
		return this.cluster;
	}

		
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


}
