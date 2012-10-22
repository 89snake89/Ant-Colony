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

import java.util.Iterator;
import java.util.LinkedList;


public class Cluster {
	
	private LinkedList<Item> items; 	// The list of items belonging to this clutser
	
	

/*********** Constructor ****************************************************************************/


	/** Constructor given a grid position and initial data*/
	
	public Cluster(Item[] items){
		this.items = new LinkedList<Item>();
		for (int i=0; i<items.length; i++) this.items.add(items[i]);
	}

	
/*********** Access & Modification Functions **********************************************************/
	
/****** item data *********/
	

	public LinkedList<Item> getItems() {
		return this.items;
	}

	
	public void addElements(LinkedList<Item> items){
		this.items.addAll(items);
	}

/*********** Calculate values ****************************************************************************/


	public double[] computeWeightedDistance(Cluster c){
		Iterator<Item> it1 = this.items.iterator();
		Iterator<Item> it2 = c.getItems().iterator();
		double min_dist=Double.MAX_VALUE;
		Item item1,item2;
		double id1=0;
		double id2=0;
		while (it1.hasNext()) {
			item1 = it1.next();
			while(it2.hasNext()){
			item2 = it2.next();
			if (item1.getID()!= item2.getID()){
				double dist= item1.distance(item2,1);
				//if (dist==0) System.out.println("item1:"+item1.getID()+" x: "+item1.getX()+" y: "+item1.getY()+
				//		"item2:"+item2.getID()+" x: "+item2.getX()+" y: "+item2.getY()+"\n");
				if (dist < min_dist) {
					id1 = item1.getID();
					id2 = item2.getID();
					min_dist = dist;
				}
				}
			}
		}
		min_dist = min_dist*(1.0 + Math.log10(1.0 + 9.0 * (double)this.items.size()/(double)c.getItems().size()));
		return new double[]{min_dist,id1,id2};
	}
}
