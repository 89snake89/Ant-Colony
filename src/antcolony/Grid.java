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

	* Represents the grid underlying a simulation, provides functions
	  for access and manipulation
	* Stores all essential information (ant and item positions)

	* Generates:
		- initial random distribution for items
	* Provides functions used by ants:
		- density function


*****************************************************************/


package antcolony;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;



/**  Represents the grid underlying a simulation, provides functions
	*	  for access and manipulation
	*/


public class Grid {

	private Configuration conf;			// Current configuration
	private HashMap<UUID,Item> items;				// Current document collection
	private Cluster[] partition;		// partitions
	private int num_clusters;
	private LinkedList<Heap> heaps;
	private UUID[][] cells;				// Cell matrix
	private UUID[][] hcells;
	private DistanceMatrix distance;	// Precomputed distance matrix

	
	/**** Constructor and Initialisation **************************************************************/


	
	/** Constructor
	* @param conf the current parameter settings
	* @param items in the current document data
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
		for (int i=0; i < conf.getxsize(); i++)
			for (int j=0; j< conf.getysize(); j++){
				this.cells[i][j] = null;
				this.hcells[i][j] = null;
				}
		this.distance = new DistanceMatrix(data, conf);
		scatterItems();
	}

	
	/**
	* Advise random position on the grid to all items
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
	* Clear the grid
	*/
	public void clear_cells() {
		for (int i=0; i<conf.getxsize(); i++)
			for (int j=0; j<conf.getysize(); j++){
				this.cells[i][j]=null;
				}
	}
	
	/**
	* Clear the grid
	*/
	public void clear_heaps() {
		for (int i=0; i<conf.getxsize(); i++)
			for (int j=0; j<conf.getysize(); j++){
				this.hcells[i][j]=null;
				}
	}

	/**** simple access functions *********************************************************/
	 

	/**
	* Get the distance matrix
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
		return this.items.get(cells[x][y]);
	}
	
	/** Get the Heap for a given grid position
	* @param x the provided grid x-coordinate
	* @param y the provided grid y-coordinate
	* @return the document null if empty
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
	* @return the state of occupation
	*/

	public boolean occupied_item(int x, int y) {
		return (this.cells[x][y] != null);
	}
	
	/** Check if a cell is occupied by an heap
	* @param x the provided grid x-coordinate
	* @param y the provided grid y-coordinate
	* @return the state of occupation
	*/

	public boolean occupied_heap(int x, int y) {
		return (this.hcells[x][y] != null);
	}
	
	/** Check if a cell is occupied by an heap
	* @param x the provided grid x-coordinate
	* @param y the provided grid y-coordinate
	* @return the state of occupation
	*/

	public boolean occupied(int x, int y) {
		return (this.hcells[x][y] != null || this.cells[x][y]!= null);
	}

	/** Get the item number by id
	* @param x the provided grid x-coordinate
	* @param y the provided grid y-coordinate
	* @return the Item object
	*/

	public Item getById(int id) {
		return this.items.get(id);
	}

	/** Get the colection of items
	*/
	public HashMap<UUID,Item> getItems() {
		return this.items;
	}
	
	/** Get the partition of clusters
	*/
	public Cluster[] getClusters() {
		return this.partition;
	}
	
	/** Get the colection of heaps
	*/
	public LinkedList<Heap> getHeaps() {
		return this.heaps;
	}
	
	
	public void decPheromone(){
		Iterator<Heap> it = this.heaps.iterator();
		while (it.hasNext()) it.next().decPheromone();
	}
	
	/** print short statistics about the grid
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
		for (int i=0; i< conf.getxsize();i++)
			for (int j=0; j< conf.getxsize();j++){
				if (occupied_item(i,j)) sum1++;
				if (occupied_heap(i,j)) sum2++;
				}
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
	
	public int calculateClusters(){
		int r = 0;
		if (conf.getModel()== Configuration.Models.ANTCLASS){
			for (int i=0; i<this.items.size(); i++) this.partition[i]=null;
			Iterator<Heap> it=this.heaps.iterator();
			int j=0;
			while (it.hasNext()){
				LinkedList<Item> list= it.next().getItems();
				partition[j]= new Cluster(list.toArray(new Item[0]));
				j++;
			}
			r = j;
		}
		else {
		 int c=0;
		 for (UUID key : this.items.keySet()){
			 this.partition[c]=new Cluster(new Item[]{items.get(key)});
			 c++;
		 }
		 Collections.shuffle(Arrays.asList(partition));
		 boolean flag=true;
		 while (flag){
			int c1=0;
			int c2=0;
			double min_dist= Double.MAX_VALUE;
			for (int i=0; i<this.partition.length-1;i++){
				for (int j=i+1; j<this.partition.length;j++){
					if (partition[i]!=null && partition[j]!=null){
					double d = partition[i].computeWeightedDistance(partition[j]);
					if (d < min_dist){
						min_dist = d;
						c1 = i;
						c2 = j;
					}}
				}
				}
			if (c1!=c2) {
				partition[c1].addElements(partition[c2].getItems());
				partition[c2]=null;
				}
			for (int i=0; i<this.items.size(); i++) if (this.partition[i]!=null) r++;
			for (int i=0; i<this.items.size(); i++)
				if (this.partition[i]!=null)
					if (this.partition[i].getItems().size() > this.items.size()/this.conf.getntypes()) {
						flag=false;
						break;
			}		
		}
		}
		return r;
	}

	
	
	public void kmeans(){
		if (this.heaps.size()>0){
		double[][] centers = new double[this.heaps.size()][conf.getnkeys()];
		int[][] centers_xy = new int[this.heaps.size()][2];
		Iterator<Heap> it = this.heaps.iterator();
		int i=0;
		while (it.hasNext()){
			Heap h = it.next();
			centers[i] = h.getCenterMass();
			centers_xy[i][0]= h.getX();
			centers_xy[i][1]= h.getY();
			i++;
		}
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
				heaps_temp[min]= new Heap(min, this.conf,centers_xy[min][0],centers_xy[min][1], this.items.get(key));
			else
				heaps_temp[min].putItem(this.items.get(key));
		}
		clear_cells();
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
						}}
			}		
		}
	}
	
	/**** simple manipulation *******************************************************/
	
	
	/** Place a item at a given position on the grid
	* @param x, y the position of the item
	* @param item the Item
	*/
	
	public void put_item(int x, int y, Item item) {
		if (this.cells[x][y]!=null) System.out.println("Alarm tried to stack items");
		item.setPicked(false);
		UUID key = item.getID();
		this.cells[x][y] = key;
		this.items.get(key).setXY(x, y);
	}
	
	/** Place a item at a given position on the grid
	* @param x, y the position of the item
	* @param item the Item
	*/
	
	public void put_heap(int x, int y, Heap heap) {
		if (this.hcells[x][y]!=null) {
			System.out.println("Alarm tried to stack heaps");
		}
		this.hcells[x][y]= heap.getID();
		this.heaps.add(heap);
	}
	
	/** Remove item at a given position from the grid
	* @param x, y the document position on the grid
	*/
	
	public void remove_item(int x, int y) {
		this.cells[x][y] = null;

	}
	
	/** Remove item at a given position from the grid
	* @param x, y the document position on the grid
	*/
	
	public void remove_heap(int x, int y) {
		UUID id = hcells[x][y];
		Iterator<Heap> it = this.heaps.iterator();
		boolean  f = false;
		done: while (it.hasNext()) 
			 	if (it.next().getID() == id) {
			 		it.remove();
			 		this.hcells[x][y]=null;
			 		f=true;
			 		break done;
			 	}
		if (!f) System.out.println("Error, did not find heap to remove!");
	}
	
	/**** measures on the grid *******************************************************/
	
	/** Informs ant about the density and similarity  of items in a neighborhood of cells
	* @param x, y the cell coordinates
	* @param the item to compare to
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

}


