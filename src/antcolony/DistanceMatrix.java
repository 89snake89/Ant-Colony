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

	File: DistanceMatrix.java
	Package: antcolony

	Description:

	* A matrix to store the original distance between all the items
		
                                                                                                                        	
*****************************************************************/

package antcolony;

import java.util.HashMap;
import java.util.UUID;

public class DistanceMatrix {

	private double scaleFactor;
	private HashMap<UUID,Item> items;
	private HashMap<UUID,Integer> dictionary;
	private double [][] matrix;

	public DistanceMatrix(Data data, Configuration conf) {
		items = data.getItems();
		dictionary = new HashMap<UUID,Integer>();
		int i=0;
		for (UUID key: items.keySet()) {
			dictionary.put(key, i);
			i++;
		}
		matrix = new double[items.size()][items.size()];

		for (UUID key: items.keySet())  matrix[dictionary.get(key)] = new double[items.size()];
		scaleFactor = 0;
		for (UUID key: items.keySet())  {
			for (UUID key1: items.keySet()) {
				double d = (double)items.get(key).distance(items.get(key1),2);
				matrix[dictionary.get(key)][dictionary.get(key1)] = d;
				scaleFactor += d;
			}
		}
		scaleFactor /= (double)(items.size()*(items.size() - 1));
	}

	public double getScaleFactor(){
		return scaleFactor;
	}
	
	public double get(UUID i, UUID j) {
		if (i==j) return 0;
		else return matrix[dictionary.get(i)][dictionary.get(j)];
	}

}
