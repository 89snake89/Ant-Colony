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
	
	Super Class for the Representation of Document Collections
*****************************************************************/

package antcolony;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

import antcolony.Configuration.Datasets;


/**
* Super Class for the Representation of Document Collections
*/
public class Data implements Serializable {
	
	private Configuration conf;
	private Item [] items;          // document collection
	private String [] keys;
	

	/** 
	* Create a new empty document dollection */
	public Data(){
		this.items = null;
		this.keys = null;
	}

    /** 
    * Create new document collection from provided data */
    
	public Data(Configuration c) {
		this.conf = c;
		generate_keys();
		generate_items();
	}

    /** get the dimensionality of the document space */
	public int getnkeys() {
		return this.keys.length;
	}
	/** get the number of documents */
	public int getnitems() {
		return this.items.length;
	}
	/** retrieve all documents */
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
							int x = centers[type][0]+(int)((generator.nextDouble()- 0.5)* this.conf.getxsize()/6);
							int y = centers[type][1]+(int)((generator.nextDouble()- 0.5)* this.conf.getysize()/6);
							items[i]= new Item(i,this.conf,x,y,type,generate_map(keys,generator.nextInt(this.conf.getMaxitemsize())));
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
						int x = centers[type][0]+(int)(generator.nextGaussian()* this.conf.getxsize()/12);
						int y = centers[type][1]+(int)(generator.nextGaussian()* this.conf.getysize()/12);
						items[i]= new Item(i,this.conf,x,y,type,generate_map(keys,generator.nextInt(this.conf.getMaxitemsize())));
						}
						break;
		}
}

private HashMap<Integer,String> generate_map(String [] k, int l){
	HashMap<Integer,String> m = new HashMap();
	Random generator = new Random();
	for (int i=0; i<l; i++) m.put(i, k[generator.nextInt(k.length)]);
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

}
