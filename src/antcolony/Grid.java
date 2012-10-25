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

import java.util.Iterator;
import java.util.LinkedList;



/**  Represents the grid underlying a simulation, provides functions
	*	  for access and manipulation
	*/


public class Grid {

	private Configuration conf;			// Current configuration
	private Item[] items;				// Current document collection
	private Cluster[] partition;		// partitions
	private int num_clusters;
	private double clust_apart;
	private LinkedList<Heap> heaps;
	private int[][] cells;				// Cell matrix
	private int[][] hcells;
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
		this.num_clusters = this.items.length;
		this.partition = new Cluster[this.num_clusters];
		this.clust_apart = conf.getxsize()/3;
		for (int i=0; i<this.items.length; i++) this.partition[i]=new Cluster(new Item[]{items[i]});
		this.heaps = new LinkedList<Heap>();
		this.cells = new int[this.conf.getxsize()][this.conf.getysize()];
		this.hcells = new int[this.conf.getxsize()][this.conf.getysize()];			
		// initialize base matrix (item id "-1" signifies "not occupied")
		for (int i=0; i < conf.getxsize(); i++)
			for (int j=0; j< conf.getysize(); j++){
				this.cells[i][j] = -1;
				this.hcells[i][j] = -1;
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
			for (int i=0; i<items.length;i++) {
				while(true){
					x = (int)Math.floor(this.conf.getxsize() * Math.random());
					y = (int)Math.floor(this.conf.getysize() * Math.random());
					if ( this.cells[x][y]== -1) {
						this.cells[x][y] = items[i].getID();
						items[i].setXY(x, y);
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
				this.cells[i][j]=-1;
				}
	}
	
	/**
	* Clear the grid
	*/
	public void clear_heaps() {
		for (int i=0; i<conf.getxsize(); i++)
			for (int j=0; j<conf.getysize(); j++){
				this.hcells[i][j]=-1;
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
		return this.items[cells[x][y]];
	}
	
	/** Get the Heap for a given grid position
	* @param x the provided grid x-coordinate
	* @param y the provided grid y-coordinate
	* @return the document null if empty
	*/
	public Heap getHeapAt(int x, int y) {
		int id = hcells[x][y];
		Heap r = null;
		Iterator<Heap> it = this.heaps.iterator();
		while (it.hasNext()) {
			Heap h = it.next();
			if (h.getID() == id) {
				r = h;
				break;
			}
		}
		return r;
	}

	/** Check if a cell is occupied by a item
	* @param x the provided grid x-coordinate
	* @param y the provided grid y-coordinate
	* @return the state of occupation
	*/

	public boolean occupied_item(int x, int y) {
		return (this.cells[x][y] != -1);
	}
	
	/** Check if a cell is occupied by an heap
	* @param x the provided grid x-coordinate
	* @param y the provided grid y-coordinate
	* @return the state of occupation
	*/

	public boolean occupied_heap(int x, int y) {
		return (this.hcells[x][y] != -1);
	}
	
	/** Check if a cell is occupied by an heap
	* @param x the provided grid x-coordinate
	* @param y the provided grid y-coordinate
	* @return the state of occupation
	*/

	public boolean occupied(int x, int y) {
		return (this.hcells[x][y] != -1 || this.cells[x][y]!= -1);
	}

	/** Get the item number by id
	* @param x the provided grid x-coordinate
	* @param y the provided grid y-coordinate
	* @return the Item object
	*/

	public Item getById(int id) {
		return this.items[id];
	}

	/** Get the colection of items
	*/
	public Item[] getItems() {
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
	
	
	/** print short statistics about the grid
	*/
	public String printStats(){
		int sum1=0;
		int sum2=0;
		int sum3=0;
		int sum4=0;
		int sum5=0;
		this.num_clusters=0;
		int maxh=0;
		for (int i=0; i< conf.getxsize();i++)
			for (int j=0; j< conf.getxsize();j++){
				if (occupied_item(i,j)) sum1++;
				if (occupied_heap(i,j)) sum2++;
				}
		for (int i=0; i<items.length;i++)if (items[i].isPicked()) sum3++;
		Iterator<Heap> it = this.heaps.iterator();
		while (it.hasNext()) {
			Heap h = it.next();
			if (h!=null) {
				sum4++;
				if (h.getSize()>maxh) maxh=h.getSize();
				sum5+= h.getSize();
			}
		}
		for (int i=0; i<partition.length; i++) if (partition[i]!=null) this.num_clusters++;
		if (sum2 < sum4) System.out.println("sum wrong");
		return "Cells occupied by items: "+ sum1+"\nCells occupied by heaps: "+
				sum2+"   \nItems picked: "+sum3+"\nNumber of heaps: "+ sum4+
				"\nAverage heap size: "+ sum5/(sum4+1)+"\nMaximum heap size: "+ maxh+
				"\nNumber of clusters: "+ num_clusters +"\n";
	}

	
	public void calculateClusters(){
		if (conf.getModel()== Configuration.Models.ANTCLASS){
			for (int i=0; i<this.items.length; i++) this.partition[i]=null;
			Iterator<Heap> it=this.heaps.iterator();
			int j=0;
			while (it.hasNext()){
				LinkedList<Item> list= it.next().getItems();
				partition[j]= new Cluster(list.toArray(new Item[list.size()]));
				j++;
			}
		}
		else {
		 for (int i=0; i<this.items.length; i++) this.partition[i]=new Cluster(new Item[]{items[i]});
		 boolean flag=true;
		 while (flag){
			int c1=0;
			int c2=0;
			double c3=0;
			double c4=0;
			double min_dist= Double.MAX_VALUE;
			for (int i=0; i<this.partition.length-1;i++){
				for (int j=i+1; j<this.partition.length;j++){
					if (partition[i]!=null && partition[j]!=null){
					double[] d = partition[i].computeWeightedDistance(partition[j]);
					if (d[0] < min_dist){
						min_dist = d[0];
						c1 = i;
						c2 = j;
						c3 = d[1];
						c4 = d[2];
					}}
				}
				}
			if (c1!=c2) {
				partition[c1].addElements(partition[c2].getItems());
				partition[c2]=null;
				}
			int count=0;
			for (int i=0; i<this.items.length; i++) if (this.partition[i]!=null) count++;
			flag = (count>this.conf.getntypes());
		}
		}
	}
	
	
	public void kmeans(){
		if (this.heaps.size()>1){
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
		for (int j=0; j< this.items.length; j++){
			double dist_min = Double.MAX_VALUE;
			int min = 0;
			for (int l=0; l<i; l++){
				double dist = this.items[j].distance_vector(centers[l]);
				if (dist <dist_min) {
					dist_min = dist;
					min = l;
				}
				}
			if (heaps_temp[min]== null)
				heaps_temp[min]= new Heap(min, this.conf,centers_xy[min][0],centers_xy[min][1], this.items[j]);
			else
				heaps_temp[min].putItem(this.items[j]);
		}
		clear_cells();
		this.heaps = new LinkedList<Heap>();
		clear_heaps();
		for (int l=0; l<i; l++) 
			if (heaps_temp[l]!= null)
				set_heap(heaps_temp[l].getX(),heaps_temp[l].getY(),heaps_temp[l]);
		}
	}
	
	/**** simple manipulation *******************************************************/
	
	
	/** Place a item at a given position on the grid
	* @param x, y the position of the item
	* @param item the Item
	*/
	
	public void set_item(int x, int y, Item item) {
		item.setPicked(false);
		int i = item.getID();
		this.cells[x][y] = i;
		items[i].setXY(x, y);
	}
	
	/** Place a item at a given position on the grid
	* @param x, y the position of the item
	* @param item the Item
	*/
	
	public void set_heap(int x, int y, Heap heap) {
		this.hcells[x][y]= heap.getID();
		this.heaps.add(heap);
	}
	
	/** Remove item at a given position from the grid
	* @param x, y the document position on the grid
	*/
	
	public void remove_item(int x, int y) {
		//this.items[this.cells[x][y]].setXY(0, 0);
		this.cells[x][y] = -1;

	}
	
	/** Remove item at a given position from the grid
	* @param x, y the document position on the grid
	*/
	
	public void remove_heap(int x, int y) {
		int id = hcells[x][y];
		Iterator<Heap> it = this.heaps.iterator();
		while (it.hasNext()) 
			if (it.next().getID() == id) {
				it.remove();
				this.hcells[x][y]=-1;
			}
		int sum=0;
		for (int i=0; i< conf.getxsize();i++)
			for (int j=0; j< conf.getxsize();j++)
				if (occupied_heap(i,j)) sum++;

		if (this.heaps.size() > sum) System.exit(-1);
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


