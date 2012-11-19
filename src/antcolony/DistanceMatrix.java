package antcolony;

import java.util.HashMap;
import java.util.UUID;

/**
 * This class is an auxiliary class to store all the original distances between items
 * before the running of the clustering.
 * The values are scale by a factor equal to the area of the matrix minus one
 * 
 * @author      António Fonseca
 * @version     1.0
 * @since       1.0
 * */
public class DistanceMatrix {

	private double scaleFactor;
	private HashMap<UUID,Item> items;
	private HashMap<UUID,Integer> dictionary;
	private double [][] matrix;

	/**
	 * This is the constructor, once built the class stores all the values.
	 * @param data the dataset to be used
	 * @param conf the configuration of the simulation
	 * */
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

	/**
	 * Get the scale factor used in the calculations
	 * @return scale factor used in the computation of the matrix
	 * */
	public double getScaleFactor(){
		return scaleFactor;
	}
	
	/**
	 * Return one value of the Matrix
	 * @return value between two items
	 * @param i the UUID of one of the items
	 * @param j the UUID of the other item
	 * */
	public double get(UUID i, UUID j) {
		if (i==j) return 0;
		else return matrix[dictionary.get(i)][dictionary.get(j)];
	}

}
