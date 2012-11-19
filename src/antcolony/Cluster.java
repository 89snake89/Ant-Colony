package antcolony;


import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/** This class represents a cluster of items.
 * <p>
 * Each cluster as a type which is corresponds to the
 * most frequent type of the items belonging to the cluster.
 * 
 * @author      António Fonseca
 * @version     1.0
 * @since       1.0
 * */
public class Cluster {
	
	private LinkedList<Item> items; 	// The list of items belonging to this cluster
	private String type;
	private double[] center;
	
	

/*********** Constructor ****************************************************************************/

	/** Constructor given a collection of items a center and a number for the cluster
	 * @param items collection of items
	 * @param center array with the dimension of an item representing the center
	 * @param c id of the cluster
	 * */
	public Cluster(Item[] items, double[] center, int c){
		this.items = new LinkedList<Item>();
		for (int i=0; i<items.length; i++) {
			items[i].setCluster(c);
			this.items.add(items[i]);
		}
		this.center = center;
		computeType();
	}
	
	/** Constructor given a collection of items 
	 * @param items collection of items
	 * */
	public Cluster(Item[] items){
		this.items = new LinkedList<Item>();
		for (int i=0; i<items.length; i++) this.items.add(items[i]);
		computeType();
	}
	
	/** Constructor given a single item
	 * @param it the item to initiate the cluster
	 * */
	public Cluster(Item it){
		this.items = new LinkedList<Item>();
		this.items.add(it);
		computeType();
	}

	
/*********** Access & Modification Functions **********************************************************/
	
	
	/** Which type are the majority of items in this cluster?
	 * @return Type of the cluster
	 * */
	public String getType(){
		return this.type;
	}
	
	
	/** Compute the type of the cluster by counting the most frequent type
	 * 
	 * */
	public void computeType(){
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		Iterator<Item> it = this.items.iterator();
		while (it.hasNext()) {
			String k = it.next().getType();
			if (map.containsKey(k)) map.put(k, map.get(k)+1);
			else map.put(k, 1);
		}
		Map.Entry<String, Integer> maxEntry = null;

		for (Map.Entry<String, Integer> entry : map.entrySet())
		{
		    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
		    {
		        maxEntry = entry;
		    }
		}
		
		this.type = maxEntry.getKey();
	}

	/** Get the items belonging to this cluster
	 * @return List of the items
	 * */	
	public LinkedList<Item> getItems() {
		return this.items;
	}

	/** Add the list of items to the cluster
	 * @param items list of the items
	 * */
	public void addElements(LinkedList<Item> items){
		this.items.addAll(items);
		this.computeType();
	}
	
	/** Add a single item to the cluster
	 * @param it the item to add
	 * */
	public void addSingleton(Item it){
		this.items.add(it);
		this.computeType();
	}

	
	/** Get an array of the dimension of the item representing
	 * the center of the cluster
	 * @return center of the cluster in a array of doubles 
	 * */
    public double[] getCenter() {
        return this.center;
    }
    
}

