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
import java.util.UUID;

import antcolony.Configuration.Datasets;


/**
 * The Data class is class to build the datasets used by the application.
 * This class needs the following files in the same directory of the running applications:
 * <ul>
 * <li> <b>Iris.csv</b> a file with the Iris dataset with all features scaled between in the interval [0,1],
 * the last column should be a string with the class for each instance. 4 attributes, 3 classes.
 * <li> <b>Wine.csv</b> a file with the Iris dataset with all features scaled between in the interval [0,1],
 * the last column should be a string with the class for each instance. 13 attributes, 3 classes.
 * <li> <b>Glass.csv</b> a file with the Iris dataset with all features scaled between in the interval [0,1],
 * the last column should be a string with the class for each instance. 9 attributes, 6 classes.
 * <li> <b>Breast.csv</b> a file with the Iris dataset with all features scaled between in the interval [0,1],
 * the last column should be a string with the class for each instance. 9 attributes, 2 classes.
 * </ul>
 * <p>
 * The fist two datasets <b>UNIFORM9</b> and <b>NORMAL4</b> are built by this class, all the others are loaded from the files.
 * 
 * @author      António Fonseca
 * @version     1.2
 * @since       1.0
 * */
public class Data {
	
	private Configuration conf;
	private HashMap<UUID,Item> items;          // document collection
	private List<List<String>> csvData;
	

	
    /** 
    * Constructor using one configuration
    * @param c the configuration
    * */
	public Data(Configuration c) {
		this.conf = c;
		generate_items();
	}

	/** Get the number of items
	 *  in the current data.
	 *  @return number of items
	 * */
	public int getnitems() {
		return this.items.size();
	}
	
	/** Retrieve all items
	 * @return HashMap with items IDs and the items
	 * */
	public HashMap<UUID,Item> getItems() {
		return this.items;
	}


/*********** hardcoded generation of artificial data *************************************/

/** Generate the hardcoded data for the test distributions
*/

private void generate_items() {

		int n = this.conf.getnitems();
		Datasets d = this.conf.getDataset();
		Random generator = new Random();
		int [][] centers = new int[9][2];
	
		switch (d) {
	
		// Hard-coded test distribution (Uniform Distribution)
	
		case UNIFORM9 :	items = new HashMap<UUID,Item>();
						this.conf.setnkeys(2);
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
							UUID key = UUID.randomUUID();
							items.put(key,new Item(key,this.conf,x,y,t,type,generate_map(x,y)));
							}
						conf.setTypes(new String[]{"0","1","2","3","4","5","6","7","8"});
						conf.setntypes(9);
						break;
									
		// Hard-coded test distribution (Normal Distribution)
									
		case NORMAL4 :	items = new HashMap<UUID,Item>();
						conf.setnkeys(2);
						centers[0][0] = this.conf.getxsize()/4; centers[0][1] = this.conf.getysize()/4;
						centers[1][0] = this.conf.getxsize()/4 * 3; centers[1][1] = this.conf.getysize()/4;
						centers[2][0] = this.conf.getxsize()/4; centers[2][1] = this.conf.getysize()/4 * 3;
						centers[3][0] = this.conf.getxsize()/4 * 3; centers[3][1] = this.conf.getysize()/ 4 * 3;
						for (int i=0; i<n; i++){
						int type = (i+1)%4;
						String t = Integer.toString(type);
						int x = centers[type][0]+(int)(generator.nextGaussian()* this.conf.getxsize()/12);
						int y = centers[type][1]+(int)(generator.nextGaussian()* this.conf.getysize()/12);
						UUID key = UUID.randomUUID();
						items.put(key,new Item(key,this.conf,x,y,t,type,generate_map(x,y)));
						}
						conf.setTypes(new String[]{"0","1","2","3"});
						conf.setntypes(4);
						break;
						
		// Hard-coded IRIS dataset from http://archive.ics.uci.edu/ml/datasets.html
						
		case IRIS :		try {
						n = this.readCsvFile("Iris.csv");
						conf.setTypes(new String[]{"Iris-setosa","Iris-versicolor","Iris-virginica"});
						conf.setnitems(n);
						conf.setntypes(3);
						conf.setnkeys(4);
						items = new HashMap<UUID,Item>();
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
							String type = list.get(list.size()-1);
							if (type.equals("Iris-setosa")) cl=1;
							if (type.equals("Iris-versicolor")) cl=2;
							if (type.equals("Iris-virginica")) cl=3;
							list=list.subList(0, list.size()-1);
							List<Double> l_out = new ArrayList<Double>();
							Iterator<String> it = list.iterator();
							while (it.hasNext()) l_out.add(Double.parseDouble(it.next()));
							double x = (Double.parseDouble(list.get(0))+Double.parseDouble(list.get(1))-1);
							double y = (Double.parseDouble(list.get(2))+Double.parseDouble(list.get(3))-1);
							x = x * this.conf.getxsize()/4 + mapx.get(type);
							y = y * this.conf.getxsize()/4 + mapy.get(type);
							UUID key = UUID.randomUUID();
							items.put(key,new Item(key,this.conf,(int)x,(int)y,type,cl,l_out));
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
						conf.setTypes(new String[]{"1","2","3"});
						conf.setntypes(3);
						conf.setnitems(n);
						conf.setnkeys(13);
						items = new HashMap<UUID,Item>();
						HashMap<String,Integer> mapx = new HashMap<String,Integer>();
						HashMap<String,Integer> mapy = new HashMap<String,Integer>();
						mapx.put("1", this.conf.getxsize()/5);
						mapy.put("1", this.conf.getysize()/5);
						mapx.put("2", this.conf.getxsize()/5 * 3); 
						mapy.put("2", this.conf.getysize()/5);
						mapx.put("3", this.conf.getxsize()/5 * 2);
						mapy.put("3", this.conf.getysize()/5 * 2);
						for (int i=0; i<n; i++){
							List<String> list = this.csvData.get(i);
							String type = list.get(list.size()-1);
							list=list.subList(0, list.size()-1);
							List<Double> l_out = new ArrayList<Double>();
							Iterator<String> it = list.iterator();
							while (it.hasNext()) l_out.add(Double.parseDouble(it.next()));
							double x = 0;
							double y = 0;
							int j=0;
							while (j<12){
								x += Double.parseDouble(list.get(j));
								j++;
								y += Double.parseDouble(list.get(j));
								j++;
								}
							x = (x-1.5) * this.conf.getxsize()/6 + mapx.get(type);
							y = (y-1.5) * this.conf.getxsize()/6 + mapy.get(type);
							UUID key = UUID.randomUUID();
							items.put(key,new Item(key,this.conf,(int)x,(int)y,type,Integer.parseInt(type),l_out));
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
						conf.setTypes(new String[]{"1","2","3","5","6","7"});
						conf.setntypes(6);
						conf.setnitems(n);
						conf.setnkeys(9);
						items = new HashMap<UUID,Item>();
						HashMap<String,Integer> mapx = new HashMap<String,Integer>();
						HashMap<String,Integer> mapy = new HashMap<String,Integer>();
						mapx.put("1", this.conf.getxsize()/6);
						mapy.put("1", this.conf.getysize()/6);
						mapx.put("2", this.conf.getxsize()/6); 
						mapy.put("2", this.conf.getysize()/6 * 4);
						mapx.put("3", this.conf.getxsize()/6 );
						mapy.put("3", this.conf.getysize()/6 * 4);
						mapx.put("5", this.conf.getxsize()/6 * 4);
						mapy.put("5", this.conf.getysize()/6);
						mapx.put("6", this.conf.getxsize()/6 * 5); 
						mapy.put("6", this.conf.getysize()/6 * 2);
						mapx.put("7", this.conf.getxsize()/6 * 4);
						mapy.put("7", this.conf.getysize()/6 * 4);
						for (int i=0; i<n; i++){
							List<String> list = this.csvData.get(i);
							String type = list.get(list.size()-1);
							list=list.subList(0, list.size()-1);
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
							x = (x-1.5) * this.conf.getxsize()/6 + mapx.get(type);
							y = y * this.conf.getysize()/6 + mapy.get(type);
							UUID key = UUID.randomUUID();
							items.put(key,new Item(key,this.conf,(int)x,(int)y,type,Integer.parseInt(type),l_out));
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
						conf.setTypes(new String[]{"2","4"});
						conf.setntypes(2);
						conf.setnitems(n);
						conf.setnkeys(9);
						items = new HashMap<UUID,Item>();
						HashMap<String,Integer> mapx = new HashMap<String,Integer>();
						HashMap<String,Integer> mapy = new HashMap<String,Integer>();
						mapx.put("2", this.conf.getxsize()/ 2);
						mapy.put("2", this.conf.getysize()/ 2);
						mapx.put("4", this.conf.getxsize()/ 4 * 3); 
						mapy.put("4", this.conf.getysize()/ 2);
						for (int i=0; i<n; i++){
							List<String> list = this.csvData.get(i);
							String type = list.get(list.size()-1);
							list=list.subList(0, list.size()-1);
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
							x = (x/4-2) * this.conf.getxsize()/ 6 + mapx.get(type);
							y = (y/4-2) * this.conf.getysize()/ 6 + mapy.get(type);
							UUID key = UUID.randomUUID();
							items.put(key,new Item(key,this.conf,(int)x,(int)y,type,Integer.parseInt(type),l_out));
						}
						}		
						catch (Exception e) {
							System.out.println("Could not build dataset BREAST");
							e.printStackTrace();
						}
						break;
						
		// Hard-coded Glass Identification dataset from http://archive.ics.uci.edu/ml/datasets.html					
		case YEAST :	try {
							n = this.readCsvFile("Yeast.csv");
							conf.setTypes(new String[]{"CYT","NUC","MIT","ME3","ME2","ME1","EXC","VAC","POX","ERL" });
							conf.setntypes(10);
							conf.setnitems(n);
							conf.setnkeys(8);
							items = new HashMap<UUID,Item>();
							HashMap<String,Integer> mapx = new HashMap<String,Integer>();
							HashMap<String,Integer> mapy = new HashMap<String,Integer>();
							mapx.put("CYT", this.conf.getxsize()/6);
							mapy.put("CYT", this.conf.getysize()/6);
							mapx.put("NUC", this.conf.getxsize()/6); 
							mapy.put("NUC", this.conf.getysize()/6 * 3);
							mapx.put("MIT", this.conf.getxsize()/6);
							mapy.put("MIT", this.conf.getysize()/6 * 5);
							mapx.put("ME3", this.conf.getxsize()/6 * 3);
							mapy.put("ME3", this.conf.getysize()/6);
							mapx.put("ME2", this.conf.getxsize()/6 * 3); 
							mapy.put("ME2", this.conf.getysize()/6 * 3);
							mapx.put("ME1", this.conf.getxsize()/6 * 3);
							mapy.put("ME1", this.conf.getysize()/6 * 5);
							mapx.put("EXC", this.conf.getxsize()/6 * 5); 
							mapy.put("EXC", this.conf.getysize()/6);
							mapx.put("VAC", this.conf.getxsize()/6 * 5);
							mapy.put("VAC", this.conf.getysize()/6 * 3);
							mapx.put("POX", this.conf.getxsize()/6 * 5); 
							mapy.put("POX", this.conf.getysize()/6 * 5);
							mapx.put("ERL", this.conf.getxsize()/6 * 7);
							mapy.put("ERL", this.conf.getysize()/6);
							int cl=0;
							for (int i=0; i<n; i++){
								List<String> list = this.csvData.get(i);
								String type = list.get(list.size()-1);
								if (type.equals("CYT")) cl=1;
								if (type.equals("NUC")) cl=2;
								if (type.equals("MIT")) cl=3;
								if (type.equals("ME3")) cl=4;
								if (type.equals("ME2")) cl=5;
								if (type.equals("ME1")) cl=6;
								if (type.equals("EXC")) cl=7;
								if (type.equals("VAC")) cl=8;
								if (type.equals("POX")) cl=9;
								if (type.equals("ERL")) cl=10;
								list=list.subList(0, list.size()-1);
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
								x = (x-2) * this.conf.getxsize()/6 + mapx.get(type);
								y = (y) * this.conf.getysize()/6 + mapy.get(type);
								UUID key = UUID.randomUUID();
								items.put(key,new Item(key,this.conf,(int)x,(int)y,type,cl,l_out));
							}
							}
							catch (Exception e) {
								System.out.println("Could not build dataset YEAST");
								e.printStackTrace();
							}
							break;
		case CMC :		try {
								n = this.readCsvFile("CMC.csv");
								conf.setTypes(new String[]{"1","2","3"});
								conf.setntypes(3);
								conf.setnitems(n);
								conf.setnkeys(21);
								items = new HashMap<UUID,Item>();
								HashMap<String,Integer> mapx = new HashMap<String,Integer>();
								HashMap<String,Integer> mapy = new HashMap<String,Integer>();
								mapx.put("1", this.conf.getxsize()/5);
								mapy.put("1", this.conf.getysize()/5);
								mapx.put("2", this.conf.getxsize()/5 * 3); 
								mapy.put("2", this.conf.getysize()/5);
								mapx.put("3", this.conf.getxsize()/5 * 2);
								mapy.put("3", this.conf.getysize()/5 * 2);
								for (int i=0; i<n; i++){
									List<String> list = this.csvData.get(i);
									String type = list.get(list.size()-1);
									list=list.subList(0, list.size()-1);
									List<Double> l_out = new ArrayList<Double>();
									Iterator<String> it = list.iterator();
									while (it.hasNext()) l_out.add(Double.parseDouble(it.next()));
									double x = 0;
									double y = 0;
									int j=0;
									while (j<20){
										x += Double.parseDouble(list.get(j));
										j++;
										y += Double.parseDouble(list.get(j));
										j++;
									}
									x = (x-1.5) * this.conf.getxsize()/6 + mapx.get(type);
									y = (y-1.5) * this.conf.getxsize()/6 + mapy.get(type);
									UUID key = UUID.randomUUID();
									items.put(key,new Item(key,this.conf,(int)x,(int)y,type,Integer.parseInt(type),l_out));
								}
								}
								catch (Exception e) {
									System.out.println("Could not build dataset CMC");
									e.printStackTrace();
								}
								break;
		case IONOSPHERE :	try {
								n = this.readCsvFile("Ionosphere.csv");
								conf.setTypes(new String[]{"g","b"});
								conf.setntypes(2);
								conf.setnitems(n);
								conf.setnkeys(33);
								items = new HashMap<UUID,Item>();
								HashMap<String,Integer> mapx = new HashMap<String,Integer>();
								HashMap<String,Integer> mapy = new HashMap<String,Integer>();
								mapx.put("g", this.conf.getxsize()/ 2);
								mapy.put("g", this.conf.getysize()/ 2);
								mapx.put("b", this.conf.getxsize()/ 4 * 3); 
								mapy.put("b", this.conf.getysize()/ 2);
								int cl=0;
								for (int i=0; i<n; i++){
									List<String> list = this.csvData.get(i);
									String type = list.get(list.size()-1);
									if (type.equals("g")) cl=1;
									if (type.equals("b")) cl=2;
									list=list.subList(0, list.size()-1);
									List<Double> l_out = new ArrayList<Double>();
									Iterator<String> it = list.iterator();
									while (it.hasNext()) l_out.add(Double.parseDouble(it.next()));
									double x = 0;
									double y = 0;
									int j=0;
									while (j<30){
										x += Double.parseDouble(list.get(j));
										j++;
										y += Double.parseDouble(list.get(j));
										j++;
									}
									x = (x/4-2) * this.conf.getxsize()/ 6 + mapx.get(type);
									y = (y/4-2) * this.conf.getysize()/ 6 + mapy.get(type);
									UUID key = UUID.randomUUID();
									items.put(key,new Item(key,this.conf,(int)x,(int)y,type,cl,l_out));
								}
								}
								catch (Exception e) {
									System.out.println("Could not build dataset IONOSPHERE");
									e.printStackTrace();
								}
								break;
		case ZOO:	try {
			n = this.readCsvFile("ZOO.csv");
								conf.setTypes(new String[]{"1","2","3","4","5","6","7"});
								conf.setntypes(7);
								conf.setnitems(n);
								conf.setnkeys(16);
								items = new HashMap<UUID,Item>();
								HashMap<String,Integer> mapx = new HashMap<String,Integer>();
								HashMap<String,Integer> mapy = new HashMap<String,Integer>();
								mapx.put("1", this.conf.getxsize()/6);
								mapy.put("1", this.conf.getysize()/6);
								mapx.put("2", this.conf.getxsize()/6); 
								mapy.put("2", this.conf.getysize()/6 * 3);
								mapx.put("3", this.conf.getxsize()/6 );
								mapy.put("3", this.conf.getysize()/6 * 5);
								mapx.put("4", this.conf.getxsize()/6 * 3);
								mapy.put("4", this.conf.getysize()/6 );
								mapx.put("5", this.conf.getxsize()/6 * 3);
								mapy.put("5", this.conf.getysize()/6 * 3);
								mapx.put("6", this.conf.getxsize()/6 * 3); 
								mapy.put("6", this.conf.getysize()/6 * 5);
								mapx.put("7", this.conf.getxsize()/6 * 5);
								mapy.put("7", this.conf.getysize()/6 );
								for (int i=0; i<n; i++){
									List<String> list = this.csvData.get(i);
									String type = list.get(list.size()-1);
									list=list.subList(0, list.size()-1);
									List<Double> l_out = new ArrayList<Double>();
									Iterator<String> it = list.iterator();
									while (it.hasNext()) l_out.add(Double.parseDouble(it.next()));
									double x = 0;
									double y = 0;
									int j=0;
									while (j<14){
										x += Double.parseDouble(list.get(j));
										j++;
										y += Double.parseDouble(list.get(j));
										j++;
									}
									x = (x-1) * this.conf.getxsize()/8 + mapx.get(type);
									y = y * this.conf.getysize()/8 + mapy.get(type);
									UUID key = UUID.randomUUID();
									items.put(key,new Item(key,this.conf,(int)x,(int)y,type,Integer.parseInt(type),l_out));
								}
								}
								catch (Exception e) {
									System.out.println("Could not build dataset ZOO");
									e.printStackTrace();
								}
								break;
		case HEART :	try {
			n = this.readCsvFile("Heart.csv");
								conf.setTypes(new String[]{"0","1","2","3","4"});
								conf.setntypes(5);
								conf.setnitems(n);
								conf.setnkeys(20);
								items = new HashMap<UUID,Item>();
								HashMap<String,Integer> mapx = new HashMap<String,Integer>();
								HashMap<String,Integer> mapy = new HashMap<String,Integer>();
								mapx.put("0", this.conf.getxsize()/5);
								mapy.put("0", this.conf.getysize()/5);
								mapx.put("1", this.conf.getxsize()/5);
								mapy.put("1", this.conf.getysize()/5 * 2);
								mapx.put("2", this.conf.getxsize()/5 * 2); 
								mapy.put("2", this.conf.getysize()/5 );
								mapx.put("3", this.conf.getxsize()/5 * 2 );
								mapy.put("3", this.conf.getysize()/5 * 2);
								mapx.put("4", this.conf.getxsize()/5 * 4);
								mapy.put("4", this.conf.getysize()/5);
								for (int i=0; i<n; i++){
									List<String> list = this.csvData.get(i);
									String type = list.get(list.size()-1);
									list=list.subList(0, list.size()-1);
									List<Double> l_out = new ArrayList<Double>();
									Iterator<String> it = list.iterator();
									while (it.hasNext()) l_out.add(Double.parseDouble(it.next()));
									double x = 0;
									double y = 0;
									int j=0;
									while (j<18){
										x += Double.parseDouble(list.get(j));
										j++;
										y += Double.parseDouble(list.get(j));
										j++;
									}
									x = (x-2) * this.conf.getxsize()/6 + mapx.get(type);
									y = (y-2) * this.conf.getysize()/6 + mapy.get(type);
									UUID key = UUID.randomUUID();
									items.put(key,new Item(key,this.conf,(int)x,(int)y,type,Integer.parseInt(type),l_out));
								}
								}
								catch (Exception e) {
									System.out.println("Could not build dataset HEART");
									e.printStackTrace();
								}
								break;
		}
}


/** Generate a map of the synthetic item to include in the data
*/
private List<Double> generate_map(int x, int y){
	List<Double> m = new ArrayList<Double>();
	double a = (double)x/(double)conf.getxsize();
	double b = (double)y/(double)conf.getysize();
	m.add(a);
	m.add(b);
	return m;
}

/** Read a csv file with the data for the UCI datasets
*/
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
