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

	* Represents a cluster of items
	* Stores all essential information e.g. grid position
                                                                                                                        	
*****************************************************************/


package antcolony;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;


public class Cluster {
	
	private LinkedList<Item> items; 	// The list of items belonging to this cluster
	private String type;
	private double[] center;
	
	

/*********** Constructor ****************************************************************************/

	/** Constructor given a grid position and initial data*/
	
	public Cluster(Item[] items, double[] center, int c){
		this.items = new LinkedList<Item>();
		for (int i=0; i<items.length; i++) {
			items[i].setCluster(c);
			this.items.add(items[i]);
		}
		this.center = center;
		computeType();
	}
	
	/** Constructor given a grid position and initial data*/
	
	public Cluster(Item[] items){
		this.items = new LinkedList<Item>();
		for (int i=0; i<items.length; i++) this.items.add(items[i]);
		computeType();
	}
	
	/** Constructor for a singleton*/
	
	public Cluster(Item it){
		this.items = new LinkedList<Item>();
		this.items.add(it);
		computeType();
	}

	
/*********** Access & Modification Functions **********************************************************/
	
	public String getType(){
		return this.type;
	}
	
	
	/****** item data *********/
	
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

	public LinkedList<Item> getItems() {
		return this.items;
	}

	
	public void addElements(LinkedList<Item> items){
		this.items.addAll(items);
		this.computeType();
	}
	
	public void addSingleton(Item it){
		this.items.add(it);
		this.computeType();
	}

    public double[] getCenter() {
        return this.center;
    }

/*********** Calculate values ****************************************************************************/


	public double computeWeightedDistance(Cluster c){
		Iterator<Item> it1 = this.items.iterator();
		Iterator<Item> it2 = c.getItems().iterator();
		double min_dist=Double.MAX_VALUE;
		Item item1,item2;
		while (it1.hasNext()) {
			item1 = it1.next();
			while(it2.hasNext()){
			item2 = it2.next();
			if (item1.getID()!= item2.getID()){
				double dist= item1.distance(item2,1);
				if (dist < min_dist) {
					min_dist = dist;
				}
				}
			}
		}
		min_dist = min_dist*(1.0 + Math.log10(1.0 + 9.0 * (double)this.items.size()/(double)c.getItems().size()));
		return min_dist;
	}
}

