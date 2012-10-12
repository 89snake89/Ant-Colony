/*  
    Copyright (C) 2012 Antonio Fonseca
    Email: Julia.Handl@gmx.de

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
	"Data.java"
	Package: JavaAnts
	
	Super Class for the Representation of Item Collections
*****************************************************************/

package antcolony;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import antcolony.Configuration.Datasets;


/**
* Super Class for the Representation of Document Collections
*/
public class Data {
	
	private Configuration conf;
	private Item [] items;          // document collection
	private String [] keys;
	private List<List<String>> csvData;
	

	
    /** 
    * Create new document collection from provided data */
    
	public Data(Configuration c) {
		this.conf = c;
		generate_keys();
		generate_items();
	}

    /** get the dimensionality of the itemspace */
	public int getnkeys() {
		return this.keys.length;
	}
	
	/** get the number of items */
	public int getnitems() {
		return this.items.length;
	}
	
	/** retrieve all itemss */
	public Item [] getItems() {
		return this.items;
	}
	
	/** retrieve the keyword assigned to each dimension */
	public String [] getKeys() {
		return this.keys;
	}

/*********** hardcoded generation of artificial data *************************************/

/** Generate the hardcoded data for the test distributions
* @param dist switch between the three possible distributions (0, 1 or 2)
* @return the generated test data
*/

private void generate_items() {

		int n = this.conf.getnitems();
		Datasets d = this.conf.getDataset();
		Random generator = new Random();
		int [][] centers = new int[9][2];
	
		switch (d) {
	
		// Hard-coded test distribution (Uniform Distribution)
	
		case UNIFORM9 :	items = new Item[n];											
						centers[0][0] = this.conf.getxsize()/6; centers[0][1] = this.conf.getysize()/6;
						centers[1][0] = this.conf.getxsize()/2; centers[1][1] = this.conf.getysize()/6;
						centers[2][0] = this.conf.getxsize()/6 * 5; centers[2][1] = this.conf.getysize()/6;
						centers[3][0] = this.conf.getxsize()/6; centers[3][1] = this.conf.getysize()/2;
						centers[4][0] = this.conf.getxsize()/2; centers[4][1] = this.conf.getysize()/2;
						centers[5][0] = this.conf.getxsize()/6 * 5; centers[5][1] = this.conf.getysize()/2;
						centers[6][0] = this.conf.getxsize()/6; centers[6][1] = this.conf.getysize()/6 * 5;
						centers[7][0] = this.conf.getxsize()/2; centers[7][1] = this.conf.getysize()/6 * 5;
						centers[8][0] = this.conf.getxsize()/6 * 5; centers[8][1] = this.conf.getysize()/6 * 5;
						for (int i=0; i<n; i++){
							int type = (i+1)%9;
							String t = Integer.toString(type);
							int x = centers[type][0]+(int)((generator.nextDouble()- 0.5)* this.conf.getxsize()/6);
							int y = centers[type][1]+(int)((generator.nextDouble()- 0.5)* this.conf.getysize()/6);
							items[i]= new Item(i,this.conf,x,y,t,type,generate_map(keys,generator.nextInt(this.conf.getMaxitemsize())));
						}
						break;
									
		// Hard-coded test distribution (Normal Distribution)
									
		case NORMAL4 :	items = new Item[n];
						centers[0][0] = this.conf.getxsize()/4; centers[0][1] = this.conf.getysize()/4;
						centers[1][0] = this.conf.getxsize()/4 * 3; centers[1][1] = this.conf.getysize()/4;
						centers[2][0] = this.conf.getxsize()/4; centers[2][1] = this.conf.getysize()/4 * 3;
						centers[3][0] = this.conf.getxsize()/4 * 3; centers[3][1] = this.conf.getysize()/ 4 * 3;
						for (int i=0; i<n; i++){
						int type = (i+1)%4;
						String t = Integer.toString(type);
						int x = centers[type][0]+(int)(generator.nextGaussian()* this.conf.getxsize()/12);
						int y = centers[type][1]+(int)(generator.nextGaussian()* this.conf.getysize()/12);
						items[i]= new Item(i,this.conf,x,y,t,type,generate_map(keys,generator.nextInt(this.conf.getMaxitemsize())));
						}
						break;
						
		// Hard-coded IRIS dataset from http://archive.ics.uci.edu/ml/datasets.html
						
		case IRIS :		try {
						n = this.readCsvFile("Iris.csv");
						conf.setnitems(n);
						items = new Item[n];
						int cl = 0;
						HashMap<String,Integer> mapx = new HashMap<String,Integer>();
						HashMap<String,Integer> mapy = new HashMap<String,Integer>();
						mapx.put("Iris-setosa", this.conf.getxsize()/4);
						mapy.put("Iris-setosa", this.conf.getysize()/4);
						mapx.put("Iris-versicolor", this.conf.getxsize()/4 * 3); 
						mapy.put("Iris-versicolor", this.conf.getysize()/4);
						mapx.put("Iris-virginica", this.conf.getxsize()/4 * 2);
						mapy.put("Iris-virginica", this.conf.getysize()/4 * 2);
						for (int i=0; i<n; i++){
							List<String> list = this.csvData.get(i);
							String type = list.get(4);
							if (type == "Iris-setosa") cl=1;
							if (type == "Iris-versicolor") cl=2;
							if (type == "Iris-virginica") cl=3;
							list.remove(4);
							List<Double> l_out = new ArrayList<Double>();
							Iterator<String> it = list.iterator();
							while (it.hasNext()) l_out.add(Double.parseDouble(it.next()));
							double x = (Double.parseDouble(list.get(0))+Double.parseDouble(list.get(1))-1)/2;
							double y = (Double.parseDouble(list.get(2))+Double.parseDouble(list.get(3))-1)/2;
							x = x * this.conf.getxsize()/12 + mapx.get(type);
							y = y * this.conf.getxsize()/12 + mapy.get(type);
							items[i]= new Item(i,this.conf,(int)x,(int)y,type,cl,l_out);
						}
						}
						catch (Exception e) {
							System.out.println("Could not build dataset IRIS");
							e.printStackTrace();
						}
						break;
						
		// Hard-coded Wine dataset from http://archive.ics.uci.edu/ml/datasets.html
						
		case WINE :		try {
						n = this.readCsvFile("Wine.csv");
						conf.setnitems(n);
						items = new Item[n];
						HashMap<String,Integer> mapx = new HashMap<String,Integer>();
						HashMap<String,Integer> mapy = new HashMap<String,Integer>();
						mapx.put("1", this.conf.getxsize()/4);
						mapy.put("1", this.conf.getysize()/4);
						mapx.put("2", this.conf.getxsize()/4 * 3); 
						mapy.put("2", this.conf.getysize()/4);
						mapx.put("3", this.conf.getxsize()/4 * 2);
						mapy.put("3", this.conf.getysize()/4 * 2);
						for (int i=0; i<n; i++){
							List<String> list = this.csvData.get(i);
							String type = list.get(13);
							list.remove(13);
							List<Double> l_out = new ArrayList<Double>();
							Iterator<String> it = list.iterator();
							while (it.hasNext()) l_out.add(Double.parseDouble(it.next()));
							double x = 0;
							double y = 0;
							int j=0;
							while (j<13){
								x += Double.parseDouble(list.get(j));
								j++;
								y += Double.parseDouble(list.get(j));
								j++;
								}
							x = (x-3)/2;
							y = (y-3)/2;
							x = x * this.conf.getxsize()/12 + mapx.get(type);
							y = y * this.conf.getxsize()/12 + mapy.get(type);
							items[i]= new Item(i,this.conf,(int)x,(int)y,type,Integer.parseInt(type),l_out);
						}
						}
						catch (Exception e) {
							System.out.println("Could not build dataset WINE");
							e.printStackTrace();
						}
						break;
						
		// Hard-coded Glass Identification dataset from http://archive.ics.uci.edu/ml/datasets.html
						
		case GLASS :	try {
						n = this.readCsvFile("Glass.csv");
						conf.setnitems(n);
						items = new Item[n];
						HashMap<String,Integer> mapx = new HashMap<String,Integer>();
						HashMap<String,Integer> mapy = new HashMap<String,Integer>();
						mapx.put("1", this.conf.getxsize()/6);
						mapy.put("1", this.conf.getysize()/6);
						mapx.put("2", this.conf.getxsize()/6 * 3); 
						mapy.put("2", this.conf.getysize()/6);
						mapx.put("3", this.conf.getxsize()/6 * 5);
						mapy.put("3", this.conf.getysize()/6 );
						mapx.put("5", this.conf.getxsize()/6 );
						mapy.put("5", this.conf.getysize()/6 * 5);
						mapx.put("6", this.conf.getxsize()/6 * 3); 
						mapy.put("6", this.conf.getysize()/6 * 5);
						mapx.put("7", this.conf.getxsize()/4 * 5);
						mapy.put("7", this.conf.getysize()/4 * 5);
						for (int i=0; i<n; i++){
							List<String> list = this.csvData.get(i);
							String type = list.get(7);
							list.remove(7);
							List<Double> l_out = new ArrayList<Double>();
							Iterator<String> it = list.iterator();
							while (it.hasNext()) l_out.add(Double.parseDouble(it.next()));
							double x = 0;
							double y = 0;
							int j=0;
							while (j<7){
								x += Double.parseDouble(list.get(j));
								j++;
								y += Double.parseDouble(list.get(j));
								j++;
							}
							x = (x-2)/2;
							y = (y-2)/2;
							x = x * this.conf.getxsize()/12 + mapx.get(type);
							y = y * this.conf.getxsize()/12 + mapy.get(type);
							items[i]= new Item(i,this.conf,(int)x,(int)y,type,Integer.parseInt(type),l_out);
						}
						}
						catch (Exception e) {
							System.out.println("Could not build dataset GLASS");
							e.printStackTrace();
						}
						break;
						
		// Hard-coded Breast Cancer dataset from http://archive.ics.uci.edu/ml/datasets.html
						
		case BREAST :	try {
						n = this.readCsvFile("Breast.csv");
						conf.setnitems(n);
						items = new Item[n];
						HashMap<String,Integer> mapx = new HashMap<String,Integer>();
						HashMap<String,Integer> mapy = new HashMap<String,Integer>();
						mapx.put("2", this.conf.getxsize()/4);
						mapy.put("2", this.conf.getysize()/ 2);
						mapx.put("4", this.conf.getxsize()/ 4 * 3); 
						mapy.put("4", this.conf.getysize()/2);
						for (int i=0; i<n; i++){
							List<String> list = this.csvData.get(i);
							String type = list.get(9);
							list.remove(9);
							List<Double> l_out = new ArrayList<Double>();
							Iterator<String> it = list.iterator();
							while (it.hasNext()) l_out.add(Double.parseDouble(it.next()));
							double x = 0;
							double y = 0;
							int j=0;
							while (j<9){
								x += Double.parseDouble(list.get(j));
								j++;
								y += Double.parseDouble(list.get(j));
								j++;
							}
							x = (x-3)/2;
							y = (y-3)/2;
							x = x * this.conf.getxsize()/12 + mapx.get(type);
							y = y * this.conf.getxsize()/12 + mapy.get(type);
							items[i]= new Item(i,this.conf,(int)x,(int)y,type,Integer.parseInt(type),l_out);
						}
						}		
						catch (Exception e) {
							System.out.println("Could not build dataset BREAST");
							e.printStackTrace();
						}
						break;
		}
}

private List<String> generate_map(String [] k, int l){
	List<String> m = new ArrayList<String>();
	Random generator = new Random();
	for (int i=0; i<l; i++) m.add(i, k[generator.nextInt(k.length)]);
	return m;
}


private void generate_keys(){
	this.keys = new String[this.conf.getnkeys()];
	Random generator = new Random();
	String[] dictionary = new String[]{"Lorem","ipsum","dolor","sit","amet","consectetur","adipiscing",
			"elit","Integer","in","ipsum","eu","libero","rhoncus","dapibus","Praesent","cursus","mi",
			"nec","quam","adipiscing","pharetra","Etiam","volutpat","tortor","dui","ac","euismod","turpis",
			"Vestibulum","rutrum","posuere","tellus","vel","suscipit","sem","placerat","ut","Duis",
			"ultrices","ligula","id","enim","pellentesque","eget","gravida","tellus","dictum","Mauris",
			"scelerisque","pharetra","enim","interdum","placerat","nunc","pulvinar","a","Phasellus",
			"lorem","erat","malesuada","id","ullamcorper","id","vulputate","in","dolor","Morbi",
			"ullamcorper","dui","et","nulla","tristique","a","malesuada","eros","pretium","Nullam",
			"lacinia","porta","dictum","Donec","rhoncus","nisl","nec","dui","commodo","pharetra",
			"Aliquam","eu","justo","eget","odio","fringilla","tincidunt","vel","in","est","Aliquam",
			"adipiscing","enim","nec","diam","fringilla","venenatis","Quisque","eu","pulvinar","turpis",
			"Pellentesque","placerat","congue","eleifend","Sed","lacinia","urna","suscipit","metus",
			"semper","et","sodales","ante","porttitor","Phasellus","molestie","venenatis","nulla",
			"at","posuere","orci","feugiat","at","Nunc","ac","nibh","quis","lacus","lobortis",
			"commodo","Curabitur","pharetra","tortor","in","sem","eleifend","a","mollis","sem",
			"sagittis","Sed","ut","turpis","tellus","Aenean","id","tortor","in","eros","fringilla",
			"fermentum","Ut","interdum","vestibulum","metus","et","tempus","enim","tempor","vitae",
			"Cras","at","interdum","diam","Nullam","et","sagittis","lacus","Fusce","eu","lacus",
			"erat","sed","ullamcorper","ligula","Aenean","eu","lacus","turpis","ut","porttitor",
			"lacus","Pellentesque","mollis","enim","id","erat","dignissim","congue","Nunc","enim",
			"libero","vestibulum","nec","iaculis","non","interdum","at","urna","Praesent","quis",
			"metus","at","quam","convallis","ultrices","a","nec","nibh","Praesent","at","sapien",
			"eget","magna","hendrerit","scelerisque","Aenean","rhoncus","nibh","sit","amet","augue",
			"sagittis","suscipit","Nulla","cursus","sodales","elit","in","posuere","Donec","aliquet",
			"interdum","ante","vitae","fermentum","neque","semper","non","Nunc","euismod","urna","in",
			"metus","tristique","faucibus","Maecenas","ac","risus","quis","justo","vulputate","volutpat",
			"ut","et","eros","Suspendisse","elementum","sollicitudin","orci","non","mattis","odio","egestas",
			"eget","Proin","ante","leo","adipiscing","a","ultrices","ac","venenatis","convallis","velit",
			"Sed","ac","libero","eros","Nullam","quis","risus","et","nibh","tempus","rhoncus","vel","sit",
			"amet","tortor","Aenean","fermentum","libero","congue","placerat","pharetra","urna","orci",
			"pulvinar","ligula","vitae","placerat","purus","sapien","a","neque","Aliquam","erat",
			"volutpat","Aenean","ut","ligula","turpis","id","porttitor","ante","Suspendisse",
			"accumsan","sem","eu","neque","hendrerit","mollis","Phasellus","lobortis","pulvinar",
			"enim","vitae","hendrerit","Nullam","commodo","vehicula","interdum","Quisque","vel",
			"mauris","ut","nulla","varius","pharetra","et","in","enim","Nunc","tincidunt","neque",
			"et","blandit","molestie","quam","velit","viverra","lorem","in","porttitor","augue",
			"tellus","ut","turpis","Cras","in","porttitor","enim","Aliquam","urna","lectus",
			"tristique","vitae","faucibus","vitae","iaculis","quis","lacus","Proin","feugiat",
			"malesuada","magna","luctus","consectetur","dolor","venenatis","non","Duis","feugiat",
			"tortor","a","iaculis","placerat","felis","felis","posuere","enim","at","commodo","metus",
			"quam","varius","tortor","In","sapien","nunc","pharetra","at","molestie","et","scelerisque",
			"sit","amet","mauris","Morbi","quis","dignissim","purus","Proin","in","sem","ipsum","Sed",
			"ultrices","feugiat","ante","eu","vehicula","quam","rhoncus","ut","Nulla","dolor","risus",
			"feugiat","eget","elementum","eu","egestas","et","enim","Cras","dolor","nisl","lacinia",
			"nec","feugiat","in","eleifend","quis","ligula","Duis","metus","orci","porttitor","vitae",
			"pharetra","vitae","hendrerit","at","dui","Aenean","non","mauris","sapien","Mauris","lectus",
			"nibh","volutpat","sed","lacinia","sit","amet","ornare","a","magna","Nulla","rhoncus","cursus",
			"luctus","Pellentesque","euismod","dolor","ac","tincidunt","vehicula","tellus","lorem","consectetur",
			"dolor","suscipit","adipiscing","est","mauris","eu","diam"};
	for (int i=0; i<keys.length; i++) this.keys[i]=dictionary[generator.nextInt(keys.length %dictionary.length)];
}

private int readCsvFile(String csvFileName) throws IOException {

    String line = null;
    BufferedReader stream = null;
    this.csvData = new ArrayList<List<String>>();
    int count = 0;

    try {
        stream = new BufferedReader(new FileReader(csvFileName));
        while ((line = stream.readLine()) != null){
            this.csvData.add(Arrays.asList(line.split(",")));
            count++;
        }
    } finally {
        if (stream != null)
            stream.close();
    }
    return count;
}

}
