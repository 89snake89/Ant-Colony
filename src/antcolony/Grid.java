package antcolony;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;


/**
 * This is a complex class that not also represents the grid underlying a simulation,
 * but also provides functions for access and manipulation but also other algorithms
 * of the application.
 * <p>
 * This class is used by all the other classes as a medium for running the simulations
 * 
 * @author      António Fonseca
 * @version     2.1
 * @since       1.0
 * */
public class Grid {

	private Configuration conf;			// Current configuration
	private HashMap<UUID,Item> items;	// Current document collection
	private LinkedList<double[]> centers;
	private Cluster[] partition;		// partitions
	private int num_clusters;
	private LinkedList<Heap> heaps;
	private UUID[][] cells;				// Cell matrix
	private UUID[][] hcells;
	private DistanceMatrix distance;	// Precomputed distance matrix

	
	/**** Constructor and Initialisation **************************************************************/

	/** Constructor
	* @param conf the current parameter settings
	* @param data in the current dataset
	*/
	public Grid(Configuration conf, Data data) {

		// store provided information
		this.conf = conf;
		this.items = data.getItems();
		this.num_clusters = this.items.size();
		this.partition = new Cluster[this.num_clusters];
		for (int i=0; i<this.items.size(); i++) this.partition[i]=new Cluster(this.items.values().toArray(new Item[0]));
		this.heaps = new LinkedList<Heap>();
		this.cells = new UUID[this.conf.getxsize()][this.conf.getysize()];
		this.hcells = new UUID[this.conf.getxsize()][this.conf.getysize()];
		for (int i=0; i < conf.getxsize(); i++){
			for (int j=0; j< conf.getysize(); j++){
				this.cells[i][j] = null;
				this.hcells[i][j] = null;
				}}
		this.distance = new DistanceMatrix(data, conf);
		scatterItems();
	}

	
	/**
	* Scatters the items over the grid
	*/
	public void scatterItems() {
		int x, y;
		clear_cells();
		clear_heaps();
			for (UUID key : this.items.keySet()) {
				while(true){
					x = (int)Math.floor(this.conf.getxsize() * Math.random());
					y = (int)Math.floor(this.conf.getysize() * Math.random());
					if ( this.cells[x][y]== null) {
						this.cells[x][y] = key;
						items.get(key).setXY(x, y);
						break;
					}
				}
		}
	}
	
	
	/**
	* Clear items in the grid
	*/
	public void clear_cells() {
		for (int i=0; i<conf.getxsize(); i++){
			for (int j=0; j<conf.getysize(); j++){
				this.cells[i][j]=null;
				}
	}}
	
	/**
	* Clear the heaps in the grid
	*/
	public void clear_heaps() {
		for (int i=0; i<conf.getxsize(); i++){
			for (int j=0; j<conf.getysize(); j++){
				this.hcells[i][j]=null;
				}
	}}

	/**** simple access functions *********************************************************/
	 

	/**
	* Get the distance matrix stored in this grid
	* @return distance matrix
	*/
	public DistanceMatrix getDistanceMatrix() {
		return this.distance;
	}

	/** Get the Item for a given grid position
	* @param x the provided grid x-coordinate
	* @param y the provided grid y-coordinate
	* @return the item null if empty
	*/
	public Item getItemAt(int x, int y) {
		return this.items.get(cells[x][y]);
	}
	
	/** Get the Heap for a given grid position
	* @param x the provided grid x-coordinate
	* @param y the provided grid y-coordinate
	* @return the heap null if empty
	*/
	public Heap heapAt(int x, int y) {
		UUID id = hcells[x][y];
		Heap r = null;
		Iterator<Heap> it = this.heaps.iterator();
		while (it.hasNext()) {
			Heap h = it.next();
			if (h.getID() == id) {
				r = h;
				break;
			}
		}
		if (r == null) System.out.println("Lost heap");
		return r;
	}

	/** Check if a cell is occupied by a item
	* @param x the provided grid x-coordinate
	* @param y the provided grid y-coordinate
	* @return the boolean state of occupation
	*/
	public boolean occupied_item(int x, int y) {
		return (this.cells[x][y] != null);
	}
	
	/** Check if a cell is occupied by an heap
	* @param x the provided grid x-coordinate
	* @param y the provided grid y-coordinate
	* @return the boolean state of occupation
	*/
	public boolean occupied_heap(int x, int y) {
		return (this.hcells[x][y] != null);
	}
	
	/** Check if a cell is occupied by an heap
	* @param x the provided grid x-coordinate
	* @param y the provided grid y-coordinate
	* @return the boolean state of occupation
	*/
	public boolean occupied(int x, int y) {
		return (this.hcells[x][y] != null || this.cells[x][y]!= null);
	}

	/** Get the collection of items
	 * @return the collection of items
	*/
	public HashMap<UUID,Item> getItems() {
		return this.items;
	}
	
	/** Get the collection of heaps
	 * @return the collection of heaps
	*/
	public LinkedList<Heap> getHeaps() {
		return this.heaps;
	}
	
	/** Get the partition of clusters
	 * @return the existing clusters in this grid
	*/
	public Cluster[] getClusters() {
		return this.partition;
	}

	/** Decrement the pheromone in all the heaps in this grid.
	*/
	public void decPheromone(){
		Iterator<Heap> it = this.heaps.iterator();
		while (it.hasNext()) it.next().decPheromone();
	}
	
	/** print short statistics about the grid
	 * to be shown in the panel
	 * @return a string with the satistics
	*/
	public String printStats(){
		int sum1=0;
		int sum2=0;
		int sum3=0;
		int sum4=0;
		int sum5=0;
		int sum6=0;
		this.num_clusters=0;
		int maxh=0;
		for (int i=0; i< conf.getxsize();i++){
			for (int j=0; j< conf.getxsize();j++){
				if (occupied_item(i,j)) sum1++;
				if (occupied_heap(i,j)) sum2++;
				}}
		for (UUID key : this.items.keySet()) if (items.get(key).isPicked()) sum3++;
		Iterator<Heap> it = this.heaps.iterator();
		while (it.hasNext()) {
			Heap h = it.next();
			if (h!=null) {
				sum4++;
				if (h.getSize()>maxh) maxh=h.getSize();
				if (h.isPicked()) sum6++;
				sum5+= h.getSize();
			}
		}
		for (int i=0; i<partition.length; i++) if (partition[i]!=null) this.num_clusters++;

		return "Number of items: "+this.items.size()+"\nCells occupied by items: "+ sum1+
				"\nCells occupied by heaps: "+ sum2+"   \nItems picked: "+sum3+
				"\nHeaps picked: "+sum6+ "\nNumber of heaps: "+ sum4+
				"\nAverage heap size: "+ sum5/(sum4+1)+"\nMaximum heap size: "+ maxh+
				"\nNumber of clusters: "+ num_clusters +"\n";
	}
	
	/** Sort by values a Tree Map
	 * @param map the TreeMap to be sorted
	 * @return the sorted map
	*/
	public static <K, V extends Comparable<V>> TreeMap<K, V> sortByValues(final TreeMap<K, V> map) {
	    Comparator<K> valueComparator =  new Comparator<K>() {
	        public int compare(K k1, K k2) {
	            int compare = map.get(k2).compareTo(map.get(k1));
	            if (compare == 0) return 1;
	            else return compare;
	        }
	    };
	    TreeMap<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
	    sortedByValues.putAll(map);
	    return sortedByValues;
	}
	
	
	/** Compute the clusters that exist in this grid for measurement purposes,
	 *  in case of ANTCLASS models make them equal to the already existing heaps
	 *  in the case LUMERFAIETA apply K-Means to group items by clusters.
	 */
	public void calculateClusters(){
		if (conf.getModel()== Configuration.Models.ANTCLASS1 ||
			conf.getModel()== Configuration.Models.ANTCLASS2 ||
			conf.getModel()== Configuration.Models.ANTCLASS3 ){
			this.partition = new Cluster[heaps.size()];
			int j=0;
			for (Heap h : this.heaps){
				LinkedList<Item> list = h.getItems();
				partition[j]= new Cluster(list.toArray(new Item[0]));
				j++;
			}
		}
		else {
			TreeMap<UUID,Double> map = new TreeMap<UUID,Double>();
			for (Item it : items.values()) map.put(it.getID(), this.densityRawAt(it.getX(), it.getinitY()));
			map = sortByValues(map);
			LinkedList<UUID> list = new LinkedList<UUID>();
			int i=0;
			for (UUID k : map.keySet()) {
				list.add(k);
				i++;
				if (i==conf.getntypes()) break;
			}
			KMeans km = new KMeans(this.conf,this.items,0, list,10,2500,2);
			km.compute();
			this.centers = km.getCenters();
			this.partition = km.getClusters();
			}
	}

	/** Perform K-Means using heaps centers as seed for K-means clustering, 
	 * remove all the object from the heaps and cluster in new heaps.
	*/
	public void kmeans_heaps(){
		if (this.heaps.size()>0){
		int count=0;
		double[][] centers = new double[this.heaps.size()][conf.getnkeys()];
		double[][] centers_test = new double[this.heaps.size()][conf.getnkeys()];
		int[][] centers_xy = new int[this.heaps.size()][2];
		done: while (count < 100){
				for (int j=0; j<this.heaps.size();j++){
					for (int k=0; k<conf.getnkeys(); k++)
						centers_test[j][k]=centers[j][k];
					}
				int i=0;
				for (Heap h : this.heaps){
					centers[i] = h.getCenterMass();
					centers_xy[i][0]= h.getX();
					centers_xy[i][1]= h.getY();
					i++;
				}
				boolean f=true;
				for (int j=0; j<this.heaps.size();j++){
					for (int k=0; k<conf.getnkeys(); k++)
						if (centers[j][k]!=centers_test[j][k]) f=false;
					}
				if (f) break done;
				Heap[] heaps_temp = new Heap[i];
				for (UUID key : this.items.keySet()){
					double dist_min = Double.MAX_VALUE;
					int min = 0;
					for (int l=0; l<i; l++){
						double dist = this.items.get(key).distance_vector(centers[l]);
						if (dist <dist_min) {
							dist_min = dist;
							min = l;
						}
					}
					if (heaps_temp[min]== null)
						heaps_temp[min]= new Heap(this.conf,centers_xy[min][0],centers_xy[min][1], this.items.get(key));
					else
						heaps_temp[min].putItem(this.items.get(key));
				}
				clear_heaps();
				this.heaps = new LinkedList<Heap>();
				for (int l=0; l<i; l++) 
					if (heaps_temp[l]!= null){
						int x, y;
						while(true){
							x = (int)Math.floor(this.conf.getxsize() * Math.random());
							y = (int)Math.floor(this.conf.getysize() * Math.random());
							if ( this.hcells[x][y]== null) {
								put_heap(x,y,heaps_temp[l]);
								break;
							}
						}
					}
				count++;
			}
		clear_cells();
		}
	}
	
	/** Cluster the heaps for ANTCLASS2, try to merge heaps that have close centers.
	*/
	public void cluster_heaps(){
		int l = this.heaps.size();
		if (l>2){
		TreeMap<Double,int[]> map = new TreeMap<Double,int[]>();
		for (int i=0; i<l-1; i++){
			for (int j=i+1; j<l;j++)
				map.put(this.heaps.get(i).computeDistanceCenterMassVector(this.heaps.get(j).getCenterMass()),new int[]{i,j});
			}
		LinkedList<Integer> done = new LinkedList<Integer>();
		for (Map.Entry<Double,int[]> entry: map.entrySet()){
			if (!done.contains(entry.getValue()[0]) && !done.contains(entry.getValue()[1]) && done.size() < l-3)
			if (entry.getKey() < this.heaps.get(entry.getValue()[1]).getMaxDissimilar()){
				this.heaps.get(entry.getValue()[0]).putItems(this.heaps.get(entry.getValue()[1]).getItems());
				done.add(entry.getValue()[1]);
				this.heaps.set(entry.getValue()[1], null);
			}
		}
		this.heaps.removeAll(Collections.singletonList(null));
		clear_heaps();
		for (Heap h : this.heaps) this.hcells[h.getX()][h.getY()]= h.getID();
		}

		}
	
	/** Cluster the heaps for ANTCLASS3, try to eliminate clusters
	*/
	public void cluster_3_heaps(){
			System.out.println("started cluster_3_heaps");
			if (this.heaps.size()> 2){
				double sum = 0.0;
				do{
					double[] sc = this.getSilhouettes(this.heaps);
					double min = Double.MAX_VALUE;
					int idx=0;
					sum = 0.0;
					for (int i=0; i< sc.length;i++)
						if (sc[i] < min){
							min = sc[i];
							idx=i;
							sum += sc[i];
						}
					sum = sum / (double)sc.length;
					this.heaps.remove(idx);
					this.kmeans_heaps();				
				}while (sum != 1.0 && this.heaps.size()>2);
				
			}

		}
	
	/**
	 * Calculate the silhouette index for a given list of heaps
	 * @param heaps a list of heaps
	 * @return a HashMap of item ids and corresponding silhouette index
	 */
	public double[] getSilhouettes(LinkedList<Heap> heaps) {
		HashMap<UUID,Double> silhouettes = new HashMap<UUID,Double>();
		double[] scores = new double[heaps.size()];
		for (Map.Entry<UUID, Item> entry: items.entrySet()){
			double a=0.0;
			double b=Double.MAX_VALUE;
			for (int c=0; c<heaps.size();c++){
				Heap h = heaps.get(c);
				LinkedList<Item> list = h.getItems();
				boolean f = false;
				double sum = 0.0;
				double count = 0.0;
				for (Item i : list) {
					if (i.getID().equals(entry.getKey())) {
						f=true;
						i.setCluster(c);
					}
					sum += entry.getValue().distance(i, 2);
					count++;
					}
				if (f) a = sum /count; 
				else if ((sum / count) < b) b = sum / count;	
			}
		silhouettes.put(entry.getKey(), (b - a)/Math.max(a, b));
		}
		for (int c=0; c<heaps.size();c++){
			scores[c]=0.0;
			Heap h = heaps.get(c);
			LinkedList<Item> list = h.getItems();
			for (Item i : list) scores[c] += silhouettes.get(i.getID());
			scores[c]= scores[c]/(double)list.size();
		}
		return scores;
	}
	
	/**** simple manipulation *******************************************************/
	
	
	/** Place a item at a given position on the grid
	* @param x the x position of the item
	* @param y the y position of the item
	* @param item the Item
	*/
	public void put_item(int x, int y, Item item) {
		if (this.cells[x][y]!=null) System.out.println("Alarm tried to stack items");
		item.setPicked(false);
		UUID key = item.getID();
		this.cells[x][y] = key;
		this.items.get(key).setXY(x, y);
	}
	
	/** Place a heap at a given position on the grid
	* @param x the x position of the item
	* @param y the y position of the item
	* @param heap the Heap
	*/
	public void put_heap(int x, int y, Heap heap) {
		if (this.hcells[x][y]!=null) {
			System.out.println("Alarm tried to stack heaps");
		}
		heap.setXY(x, y);
		this.hcells[x][y]= heap.getID();
		this.heaps.add(heap);
	}
	
	/** Remove an item at a given position on the grid
	* @param x the x position of the item
	* @param y the y position of the item
	*/
	public void remove_item(int x, int y) {
		this.cells[x][y] = null;

	}
	
	/** Remove an heap at a given position on the grid
	* @param x the x position of the heap
	* @param y the y position of the heap
	*/
	public void remove_heap(int x, int y) {
		UUID id = hcells[x][y];
		Iterator<Heap> it = this.heaps.iterator();
		done: while (it.hasNext()) 
			 	if (it.next().getID() == id) {
			 		it.remove();
			 		this.hcells[x][y]=null;
			 		break done;
			 	}
	}
	
	/** Get the centers of this Grid
	* @return centers of the grid
	*/
	public LinkedList<double[]> getCenters() {
		return this.centers;

	}
	

	/**** measures on the grid *******************************************************/
	
	/** Informs ant about the density and similarity  of items in a neighborhood of cells
	 * computes the density of items of a given type
	* @param x the cell x coordinates
	* @param y the cell y coordinates
	* @param it the item to compare to
	* @return the density 
	* */
    public double densityAt(int x, int y , Item it) {
	
    	int xsize = this.conf.getxsize();
		int ysize = this.conf.getysize();
		int sigma = this.conf.getSigma();
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
				
				if ( occupied_item(jh,ih) && (jh != x || ih != y) ){
				Item it1 = this.getItemAt(jh, ih);
				sum += (1 - distance.get(it.getID(),it1.getID())/div);
				}
					
			}
		}	
		double size = sigma*2+1;
		size *= size;
		size -= 1;
		return Math.max(0.0, (sum/size));

	}

	/** Compute the density of items at a given position in the grid
	 * independently of item type
	* @param x the cell x coordinates
	* @param y the cell y coordinates
	* @return the density 
	* */
    public double densityRawAt(int x, int y ) {
    	
    	int xsize = this.conf.getxsize();
		int ysize = this.conf.getysize();
		int sigma = xsize/5;
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
				
				if ( occupied_item(jh,ih) && (jh != x || ih != y) ) sum++;
					
			}
		}	
		double size = sigma*2+1;
		size *= size;
		size -= 1;
		return Math.max(0.0, (sum/size));

	}	
}


