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

package antcolony;


public class DistanceMatrix implements java.io.Serializable {


	private static final long serialVersionUID = 9154777272953272544L;
	private double [][] matrix;

	public DistanceMatrix(Item [] items, Configuration conf) {
		matrix = new double[items.length][items.length];

		for (int i=0; i<items.length; i++) matrix[i] = new double[i+1];
		double scaleFactor = 0;
		for (int i=1; i<=items.length; i++) {
			for (int j=0; j<i; j++) {
				matrix[i-1][j] = (double)items[i-1].distance(items[j],conf.getDMeasure());
				scaleFactor += matrix[i-1][j];
			}
		}
		// compute the scale factor (average over all inter-item distances)
		scaleFactor /= 0.5*(double)(items.length*(items.length - 1));
		conf.setdistscale(scaleFactor);
	}

	public double get(int i, int j) {
		if (i==j) return 0;
		if (i < j) return matrix[j-1][i];
		else return matrix[i-1][j];
	}

	public void set(int i, int j, double value) {
		if (i==j) return;
		if (i < j) matrix[j-1][i]=(float)value;
		else matrix[i-1][j]=(float)value;
	}
	public void mult(int i, int j, double value) {
		if (i==j) return;
		if (i < j) matrix[j-1][i]*=(float)value;
		else matrix[i-1][j]*=(float)value;
	}
	public void add(int i, int j, double value) {
		if (i==j) return;
		if (i < j) matrix[j-1][i]+=(float)value;
		else matrix[i-1][j]+=(float)value;
	}



}
