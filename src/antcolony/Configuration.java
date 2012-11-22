package antcolony;

import java.util.HashMap;

/**
 * The Configuration class is an auxiliary class to store all the parameters
 * of the application.
 * <p>
 * This class is used by all the other classes as a mean for configuration
 * 
 * @author      António Fonseca
 * @version     1.9
 * @since       1.0
 * */
public class Configuration {


	/**
	 * The Datasets class determines the dataset used in the simulations
	 * @author      António Fonseca
	 * @version     1.0
	 * @since       1.0
	 * */
	public enum Datasets {
		/**
		 * This dataset is constituted by 9 different item types distributed
		 * uniformly around 9 centers in the grid. The centers are determined
		 * by the grid size located at 9 points corresponding to a square vertices
		 * mid-points and center.
		 * 
		 * @since       1.0
		 * */
		UNIFORM9,
		/**
		 * This dataset is constituted by 4 different items types distributed
		 * according to a Normal distribution of zero mean and unitary stantard deviation
		 * dimensioned to the grid defined size.
		 * @since       1.0
		 * */
		NORMAL4,
		/**
		 * This is perhaps the best known database to be found in the pattern recognition
		 * literature.The data set contains 3 classes of 50 instances each, where each class
		 * refers to a type of iris plant. One class is linearly separable from the other 2;
		 * the latter are NOT linearly separable from each other.
		 * <p>
		 * @see <a href="http://archive.ics.uci.edu/ml/datasets/Iris">Iris Data Set - UCI Machine Learning Repository</a>
		 * 
		 * @since       1.0
		 * */
		IRIS,
		/**
		 * These data are the results of a chemical analysis of wines grown in the same region in Italy
		 * but derived from three different cultivars. The analysis determined the quantities of 13
		 * constituents found in each of the three types of wines. I think that the initial data set
		 * had around 30 variables, but for some reason I only have the 13 dimensional version.
		 * I had a list of what the 30 or so variables were, but a.) I lost it, and b.), I would not
		 * know which 13 variables are included in the set.
		 * <p>
		 * @see <a href="http://archive.ics.uci.edu/ml/datasets/Wine">Wine Data Set - UCI Machine Learning Repository</a>
		 * 
		 * @since       1.0
		 * */
		WINE,
		/**
		 * This dataset includes the features from 5 different types of glass.
		 * The study of classification of types of glass was motivated by criminological investigation.
		 * At the scene of the crime,
		 * the glass left can be used as evidence...if it is correctly identified!
		 * <p>
		 * @see <a href="http://archive.ics.uci.edu/ml/datasets/Glass+Identification">Glass Data Set - UCI Machine Learning Repository</a>
		 *  
		 * @since       1.0
		 * */
		GLASS,
		/**
		 * This is one of three domains provided by the Oncology Institute that has repeatedly appeared in the machine learning literature.
		 * This data set includes 201 instances of one class and 85 instances of another class. The instances are described by 9 attributes,
		 * some of which are linear and some are nominal.
		 * <p>
		 * @see <a href="http://archive.ics.uci.edu/ml/datasets/Breast+Cancer">Breast Cancer Data Set - UCI Machine Learning Repository</a>
		 *  
		 * @since       1.0
		 * */
		BREAST
		};
	
	private Datasets dataset = Datasets.UNIFORM9;
	private int nitems = 200;
	private int dmeasure = 2;
	private String[] types = new String[]{"0","1","2","3","4","5","6","7","8"};
	private int ntypes = 9;
	
	// Item Parameters
	private int nkeys = 5;
	
	// Simulation parameters
	private int xsize = 100;
	private int ysize = 100;
	
	// Several decimal coded colors to color different item types
	private int[] colors = new int[]{16763904,13382502,10066329,6723942,3381606,10053375,13421823,
									6697830,6684723,6684927,13311,65280,3381708,13395456,65382,10066431,
									3394611,52428,6737100,16724991,3368703,3381555,16711935,10066176,
									13421619,6710835,13408512,6750003,16724889,16737996,6724095,13434828,
									16737843,10079334,10079283,10092339,10053222,16737894,6697881,3381657,
									13209,52275,39423,10079385,39270,10066380,16711833,16764108,16777011,
									26367,6697728,3368499,10040217,16763955,10053324,13382655,3394713,
									16711884,6736998,6697779,6737049,255,16724787,13434624,10053273,
									6684825,16724940,6710937,3342540,3355596,13369344,10040115,6684672,
									10040064,13395507,6711039,6723993,3407718,10092441,3368550,13369599,
									16750950,26316,16750848,3407820,10027008,10053120,26265,6710886,16737945,
									13382553,10066227,16777215,10066278,26112,3407871,3381504,10027110,
									16777062,6750207,10027263,3342489,16737792,6684876,13382451,13369548,
									10079436,3394764,16764159,13434879,3407667,3394815,13056,13408767,
									6723840,65331,10040268,13421670,3407769,13382604,65535,16750899,
									153,16724736,102,10027059,10040319,10079487,16711731,3355392,13395609,
									39321,13408563,3342336,10040166,65433,3368601,51,13421721,6724044,
									10027212,52326,6736947,26163,13369497,3368448,52377,52224,16764006,
									10053171,10079232,16776960,13434675,39219,13395711,13382400,26214,
									16777164,3407616,6749952,10092390,13369446,13434726,6750105,39168,6710988,
									6697932,6710784,10092492,13260,10092543,16751103,16764057,3355545,
									13408614,204,10027161,3394662,13421568,10092288,3342387,16711680,
									13434777,3381759,52479,3355647,16724838,16777113,6750054,16738047,
									6684774,13395558,3342438,6750156,13408716,6697983,16751052,6723891,
									13158,3342591,13395660,3394560,6736896,16751001,13107,13421772,39372,
									6737151,13369395,3355443,65484,16711782,3368652,3355494,13408665};
	private int cicle1 = 2000;
	private int cicle2 = 5000;
	private double min_f = 0.85;
	
	/**
	 * This Models class determines the algorithm model used in the simulations.
	 * 
	 * @version     1.0
	 * @since       1.0
	 * */
	public enum Models {
		/**
		 * This model is the implementation of the algorithm proposed by
		 * Nicolas Monmarché , Mohamed Slimane and Gilles Venturini
		 * "AntClass: discovery of clusters in numeric data by an hybridization
		 * of an ant colony with the kmeans algorithm".
		 * <p>
		 * This algorithm is developed in 4 steps:
		 * <ul>
		 * <li> In the first step, during <b>Cicle 1</b> iterations, a colony of ants clusters items on
		 * a grid build heaps of items in accordance to the rules explained in the paper.
		 * <li> At a second step, using the heaps centers as seed centers for a clustering, k-means
		 * is implemented to cluster all the items in the datasets.
		 * <li> As a third step, during <b>Cicle 2</b> iterations, the ants now try to clusters heaps
		 * according to the rules explained in the paper.
		 * <li> Finally, as a fourth step, a second k-means clustering is implemented this time to tighly 
		 * cluster the resulting heaps.
		 * </ul>
		 * @since       1.0
		 * */
		ANTCLASS1,
		/**
		 * This model is modification of the previous AntClass algorithm using a modified
		 * version of steps 3 and 4 on the clustering of heaps.
		 * <ul>
		 * <li> The fist step of AntClass1 is preserved
		 * <li> The second step of AntClass1 is preserved
		 * <li> In the third step all the heaps are clustered according to the following rule:
		 * If the distance between centers of heaps is less than the distance between the most dissimilar item
		 * in each heap and the center of the heaps, the heaps are clustered. This process begin examining first
		 * the least distinct heaps.
		 * </ul>
		 * 
		 * @since       1.0
		 * */
		ANTCLASS2 ,
		/**
		 * This is the classic Lumer and Faieta model as explained in the original paper.
		 * "E. Lumer and B. Faieta. Diversity and adaptation in populations of clustering ants.
		 * Proceedings of the Third International Conference on Simulation of Adaptive Behavior: From Animals to Animats 3,
		 * MIT Press, Cambridge, CA, pp. 501-508, 1994." 
		 * 
		 * @since       1.0
		 * */
		LUMERFAIETA_S,
		/**
		 * This a modified model from the original Lumer and Faieta in which the ants
		 * move according to its memory of items visited or jump randomly but don't
		 * perform any steps.
		 * 
		 * @since       1.0
		 * */
		LUMERFAIETA_M,
		/**
		 * This a modified model from the original Lumer and Faieta in which the ants
		 * move by random jumps in the grid, the ants tend to drop items only at places
		 * where the density of similar items is high.
		 * 
		 * @since       1.0
		 * */
		LUMERFAIETA_R};
	private Models model = Models.ANTCLASS1;
	
	// Ant parameters
	private int nants = 30;
	private int max_speed = 20;
	private int memsize = 8;
	private int sigma = 5;
	private double kd = 0.001;
	private double kp = 0.001;
	private double alpha = 1.0;
	private double max_carry_low = 20;
	private double max_carry_range = 180;
	private double speed_low = 1;
	private double speed_range = 9;
	private double p_load_low = 0.4;
	private double p_load_range = 0.4;
	private double p_drop_low = 0.4;
	private double p_drop_range = 0.4;
	private double p_destroy_low = 0;
	private double destroy_range = 0.6;
	private double p_direction_low = 0.5;
	private double p_direction_range = 0.5;
	private double t_create_low = 0.05;
	private double t_create_range = 0.15;
	private double t_remove_low = 0.1;
	private double t_remove_range = 0.1;
	
	// File parameters
	private String filename = "record_ant_clustering.txt";
	

/********** Constructor ***************************************************************************/
	
	/**
	 * Constructor
	 * */
	public Configuration() {	
	}
	
	
/********** simple manipulation functions *********************************************************/	

	/**
	 * Set the dataset to be used in the simulation
	 * @param value the dataset
	 * */
	public void setDataset(Datasets value) {
		this.dataset = value;
	}
	
	/**
	 * Set the distance measure to be used in the simulation
	 * @param value the distance measure
	 * */
	public void setDMeasure(int value) {
		this.dmeasure = value;
	}
	
	/**
	 * Set the types of the items to be used in the simulation
	 * @param value array of types
	 * */
	public void setTypes(String[] value){
		this.types = value;
	}
	
	/**
	 * Set the size of the x axis of the grid to be used in the simulation
	 * @param value the x size of the grid
	 * */
	public void setxsize(int value) {
		this.xsize = value;
	}
	
	/**
	 * Set the size of the y axis of the grid to be used in the simulation
	 * @param value the y size of the grid
	 * */
	public void setysize(int value) {
		this.ysize = value;
	}
	
	/**
	 * Set the model to be used in the simulation
	 * @param value the model
	 * */
	public void setModel(Models value) {
		this.model = value;
	}
	
	/**
	 * Set the value of alpha to be used in the simulation
	 * @param value the alpha
	 * */
	public void setAlpha(double value) {
		this.alpha = value;
	}
	
	/**
	 * Set the value of Kd to be used in the simulation
	 * @param value of kd
	 * */
	public void setKd(double value) {
		this.kd = value;
	}
	
	/**
	 * Set the value of Kp to be used in the simulation
	 * @param value of Kp
	 * */
	public void setKp(double value) {
		this.kp = value;
	}
	
	/**
	 * Set the number of items to be used in the simulation
	 * @param value the number of items
	 * */
    public void setnitems(int value) {
    	this.nitems = Math.max(value,nants);
	}
    
	/**
	 * Set the number of keys to be used by each item, its dimension
	 * @param value the number of key
	 * */
	public void setnkeys(int value) {
		this.nkeys = value;
	}
	
	/**
	 * Set the number of types to be used by each item
	 * @param value the number of types
	 * */
	public void setntypes(int value) {
		this.ntypes = value;
	}
	
	/**
	 * Set the number of ants to be used in the simulation
	 * @param value the number of ants
	 * */
	public void setnants(int value) {
		this.nants = value;
	}
	
	/**
	 * Set the sigma value to be used in the simulation
	 * @param value the sigma
	 * */
	public void setSigma(int value) {
		this.sigma = value;
	}
	
	/**
	 * Set the maximum speed to be used in the simulation
	 * @param value maximum speed
	 * */
	public void setMaxSpeed(int value) {
		this.max_speed = value;
	}
	
	/**
	 * Set the maximum carry low value to be used in the simulation
	 * @param value maximum carry low
	 * */
	public void setMaxCarryLow(double value){
		this.max_carry_low = value;
	}
	
	/**
	 * Set the maximum carry range to be used in the simulation
	 * @param value maximum carry range
	 * */
	public void setMaxCarryRange(double value){
		this.max_carry_range = value;
	}
	
	/**
	 * Set the maximum speed low value to be used in the simulation
	 * @param value speed low
	 * */
	public void setSpeedLow(double value){
		this.speed_low = value;
	}
	
	/**
	 * Set the maximum speed range to be used in the simulation
	 * @param value speed range
	 * */
	public void setSpeedRange(double value){
		this.speed_range = value;
	}
	
	/**
	 * Set the probability of load low value to be used in the simulation
	 * @param value probability of load low
	 * */
	public void setPLoadLow(double value){
		this.p_load_low = value;
	}
	
	/**
	 * Set the probability of load range value to be used in the simulation
	 * @param value probability of load range
	 * */
	public void setPLoadRange(double value){
		this.p_load_range = value;
	}
	
	/**
	 * Set the probability of drop low value to be used in the simulation
	 * @param value probability of drop low
	 * */
	public void setPDropLow(double value){
		this.p_drop_low = value;
	}
	
	/**
	 * Set the probability of drop range value to be used in the simulation
	 * @param value probability of drop range
	 **/
	public void setPDropRange(double value){
		this.p_drop_range = value;
	}
	
	/**
	 * Set the probability of destroy low value to be used in the simulation
	 * @param value probability of destroy low
	 * */
	public void setPDestroyLow(double value){
		this.p_destroy_low = value;
	}
	
	
	/**
	 * Set the probability of destroy range value to be used in the simulation
	 * @param value probability of destroy range
	 * */
	public void setPDestroyRange(double value){
		this.destroy_range = value;
	}
	
	/**
	 * Set the probability of changing direction low value to be used in the simulation
	 * @param value probability of changing direction low
	 * */	
	public void setPDirectionLow(double value){
		this.p_direction_low = value;
	}
	
	/**
	 * Set the probability of changing direction range value to be used in the simulation
	 * @param value probability of changing direction range
	 * */
	public void setPDirectionRange(double value){
		this.p_direction_range = value;
	}
	
	/**
	 * Set the probability of creation low value to be used in the simulation
	 * @param value probability of creation low
	 * */
	public void setTCreateLow(double value){
		this.t_create_low = value;
	}
	
	/**
	 * Set the probability of creation range value to be used in the simulation
	 * @param value probability of creation range
	 * */
	public void setTCreateRange(double value){
		this.t_create_range = value;
	}
	
	/**
	 * Set the probability of removing low value to be used in the simulation
	 * @param value probability of removing low
	 * */	
	public void setTRemoveLow(double value){
		this.t_remove_low = value;
	}
	
	/**
	 * Set the probability of removing range value to be used in the simulation
	 * @param value probability of removing range
	 * */
	public void setTRemoveRange(double value){
		this.t_remove_range = value;
	}
	
	/**
	 * Set the memory size to be used by the ants
	 * @param value memory size
	 * */
	public void setMemsize(int value) {
		this.memsize = value;
	}
	
	/**
	 * Set the filename to record measures
	 * @param value filename
	 * */
	public void setFilename(String value) {
		this.filename = value;
	}
	
	/**
	 * Set the number of iterations to be use in the first cycle of the simulation
	 * @param value number of iterations
	 * */
	public void setCicle1(int value){
		this.cicle1 = value;
	}
	
	/**
	 * Set the number of iterations to be use in the seconde cycle of the simulation
	 * @param value number of iterations
	 * */
	public void setCicle2(int value){
		this.cicle2 = value;
	}
	
	/**
	 * Set the minimum F to accept the results of the simulation
	 * @param value minimum F
	 * */
	public void setMinF(double value){
		this.min_f = value;
	}
	
	/**
	 * Set one parameter at a time according to the  designation
	 * @param k the parameter designation
	 * @param v the value
	 * */
	public void setParameters(String k, double v){
		if (k == "X size") 		this.setxsize((int)v); 
		if (k == "Y size") 		this.setysize((int)v);
		if (k == "N of ants") 	this.setnants((int)v);
		if (k == "N of items")	this.setnitems((int)v);
		if (k == "N of keys")	this.setnkeys((int)v);
		if (k == "Sigma")		this.setSigma((int)v);
		if (k == "Alpha")		this.setAlpha(v);
		if (k == "Kd")			this.setKd(v);
		if (k == "Kp")			this.setKp(v);
		if (k == "Max Speed")	this.setMaxSpeed((int)v);
		if (k == "Max Carry low") this.setMaxCarryLow(v);
		if (k == "Max Carry range") this.setMaxCarryRange(v);
		if (k == "Speed low")	this.setSpeedLow(v);
		if (k == "Speed range") this.setSpeedRange(v);
		if (k == "P Load low") this.setPLoadLow(v);
		if (k == "P Load range") this.setPLoadRange(v);
		if (k == "P Drop low") this.setPDropLow(v);
		if (k == "P Drop range") this.setPDropRange(v);
		if (k == "P Destroy low")this.setPDestroyLow(v);
		if (k == "P Destroy range") this.setPDestroyRange(v);
		if (k == "P Direction low") this.setPDirectionLow(v);
		if (k == "P Direction range") this.setPDirectionRange(v);
		if (k == "T Create low") this.setTCreateLow(v);
		if (k == "T Create range") this.setTCreateRange(v);
		if (k == "T Remove low") this.setTRemoveLow(v);
		if (k == "T Remove range") this.setTRemoveRange(v);
		if (k == "Memory Size") this.setMemsize((int)v);
		if (k == "Cicle 1") this.setCicle1((int)v);
		if (k == "Cicle 2") this.setCicle2((int)v);
		if (k == "Minimum F")this.setMinF(v);
	}
	
/********** simple access functions ********************************************************************/	
	
	/**
	 * Get the dataset to be used in the simulation
	 * @return value of the dataset
	 * */
	public Datasets getDataset() {
		return this.dataset;
	}
	
	/**
	 * get the distance measure to be used in the simulation
	 * @return value of the distance measure
	 * */
	public int getDMeasure(){
		return this.dmeasure;
	}
	
	/**
	 * Get the types of the items to be used in the simulation
	 * @return array of the types
	 * */
	public String[] getTypes(){
		return this.types;
	}
	
	/**
	 * Get the model to be used in the simulation
	 * @return the model designation
	 * */
	public Models getModel() {
		return this.model;
	}
	
	/**
	 * Get the value of alpha to be used in the simulation
	 * @return value of the alpha
	 * */
	public double getAlpha() {
		return this.alpha;
	}
	
	/**
	 * Get the value of Kd to be used in the simulation
	 * @return value of the kd
	 * */
	public double getKd() {
		return this.kd;
	}
	
	/**
	 * Set the value of Kp to be used in the simulation
	 * @return value of the Kp
	 * */
	public double getKp() {
		return this.kp;
	}
	
	/**
	 * Get the size of the x axis of the grid to be used in the simulation
	 * @return value of the x size of the grid
	 * */
	public int getxsize() {
		return this.xsize;
	}
	
	/**
	 * Get the size of the y axis of the grid to be used in the simulation
	 * @return value of the y size of the grid
	 * */
	public int getysize() {
		return this.ysize;
	}
	
	/**
	 * Get the array of decimal code of colors to be used in the simulation
	 * @return array of decimal code of colors
	 * */
	public int[] getColors() {
		return this.colors;
	}
	
	/**
	 * Get the number of items to be used in the simulation
	 * @return the number of items
	 * */
	public int getnitems() {
		return this.nitems;
	}
	
	/**
	 * Get the number of ants to be used in the simulation
	 * @return the number of ants
	 * */
	public int getnants() {
		return this.nants;
	}
	
	/**
	 * Get the number of keys to be used by each item, its dimension
	 * @return value of the number of key
	 * */
	public int getnkeys() {
		return this.nkeys;
	}
	
	/**
	 * Get the number of types of the items to be used in the simulation
	 * @return the number of types
	 * */
	public int getntypes() {
		return this.ntypes;
	}
	
	/**
	 * Get the value of sigma to be used in the simulation
	 * @return value of sigma
	 * */
	public int getSigma() {
		return this.sigma;
	}
	
	/**
	 * Get the maximum speed of the ants
	 * @return maximum speed
	 * */
	public int getMaxSpeed() {
		return this.max_speed;
	}
	
	/**
	 * Get the maximum carry low value to be used in the simulation
	 * @return maximum carry low value
	 * */
	public double getMaxCarryLow(){
		return this.max_carry_low;
	}
	
	/**
	 * Get the maximum carry range value to be used in the simulation
	 * @return maximum carry range value
	 * */
	public double getMaxCarryRange(){
		return this.max_carry_range;
	}
	
	/**
	 * Get the speed low value to be used in the simulation
	 * @return speed low value
	 * */
	public double getSpeedLow(){
		return this.speed_low;
	}
	
	/**
	 * Get the speed range value to be used in the simulation
	 * @return speed range value
	 * */
	public double getSpeedRange(){
		return this.speed_range;
	}
	
	/**
	 * Get the probability of loading low value to be used in the simulation
	 * @return probability of loading low value
	 * */
	public double getPLoadLow(){
		return this.p_load_low;
	}
	
	/**
	 * Get the probability of loading range value to be used in the simulation
	 * @return probability of loading range value
	 * */
	public double getPLoadRange(){
		return this.p_load_range;
	}
	
	/**
	 * Get the probability of dropping low value to be used in the simulation
	 * @return probability of dropping low value
	 * */
	public double getPDropLow(){
		return this.p_drop_low;
	}
	
	/**
	 * Get the probability of dropping range value to be used in the simulation
	 * @return probability of dropping range value
	 * */
	public double getPDropRange(){
		return this.p_drop_range;
	}
	
	/**
	 * Get the probability of destroying low value to be used in the simulation
	 * @return probability of destroying low value
	 * */
	public double getPDestroyLow(){
		return this.p_destroy_low;
	}
	
	/**
	 * Get the probability of destroying range value to be used in the simulation
	 * @return probability of destroying range value
	 * */
	public double getPDestroyRange(){
		return this.destroy_range;
	}
	
	/**
	 * Get the probability of changing direction low value to be used in the simulation
	 * @return probability of changing direction low  value
	 * */
	public double getPDirectionLow(){
		return this.p_direction_low;
	}
	
	/**
	 * Get the probability of changing direction range value to be used in the simulation
	 * @return probability of changing direction range value
	 * */
	public double getPDirectionRange(){
		return this.p_direction_range;
	}
	
	/**
	 * Get the probability of creating heaps low value to be used in the simulation
	 * @return probability of creating heaps low value
	 * */
	public double getTCreateLow(){
		return this.t_create_low;
	}
	
	/**
	 * Get the probability of creating heaps range value to be used in the simulation
	 * @return probability of creating heaps range value
	 * */
	public double getTCreateRange(){
		return this.t_create_range;
	}
	
	/**
	 * Get the probability of removing items or heaps low value to be used in the simulation
	 * @return probability of removing items or heaps low value
	 * */
	public double getTRemoveLow(){
		return this.t_remove_low;
	}
	
	/**
	 * Get the probability of removing items or heaps range value to be used in the simulation
	 * @return probability of removing items or heaps  range value
	 * */
	public double getTRemoveRange(){
		return this.t_remove_range;
	}
	
	/**
	 * Get the memory size of the ants
	 * @return memory size
	 * */
	public int getmemsize() {
		return this.memsize;
	}
	
	/**
	 * Get the filename to record results
	 * @return filename
	 * */
	public String getFilename() {
		return this.filename;
	}
	
	/**
	 * Get the number of iterations in the first cycle
	 * @return number of iterations
	 * */
	public int getCicle1(){
		return this.cicle1;
	}
	
	/**
	 * Get the number of iterations in the second cycle
	 * @return number of iterations
	 * */
	public int getCicle2(){
		return this.cicle2;
	}
	
	/**
	 * Get the minimum value of the F measure to accept a good result
	 * @return value of the F measure
	 * */
	public double getMinF(){
		return this.min_f;
	}

	
	/**
	 * Get the parameters to fill the panel table according to the model
	 * @return HashMap with the parameters
	 * */
	public HashMap<String,Double> getParameters(){
		HashMap<String,Double> r = new HashMap<String,Double>();
		switch(this.model){
		case LUMERFAIETA_S : r.put("X size", new Double(this.getxsize()));
						   	 r.put("Y size", new Double(this.getysize()));
						   	 r.put("N of ants", new Double(this.getnants()));
						   	 r.put("Sigma", new Double(this.getSigma()));
						   	 r.put("Kd", this.getKd());
						   	 r.put("Kp", this.getKp());
						   	 r.put("Max Speed", new Double(this.getMaxSpeed()));
						   	 r.put("Minimum F", new Double(this.getMinF()));
//						   	 r.put("Minimum Density", new Double(this.getMinD()));
						   	 break;
		case LUMERFAIETA_M : r.put("X size", new Double(this.getxsize()));
		   					 r.put("Y size", new Double(this.getysize()));
		   					 r.put("N of ants", new Double(this.getnants()));
		   					 r.put("Sigma", new Double(this.getSigma()));
		   					 r.put("Kd", this.getKd());
		   					 r.put("Kp", this.getKp());
		   					 r.put("Max Speed", new Double(this.getMaxSpeed()));
		   					 r.put("Memory Size", new Double(this.getmemsize()));
		   					 r.put("Minimum F", new Double(this.getMinF()));

		   					 break;
		case LUMERFAIETA_R : 	r.put("X size", new Double(this.getxsize()));
		   						r.put("Y size", new Double(this.getysize()));
		   						r.put("N of ants", new Double(this.getnants()));
		   						r.put("Sigma", new Double(this.getSigma()));
		   						r.put("Kd", this.getKd());
		   						r.put("Kp", this.getKp());
		   						r.put("Minimum F", new Double(this.getMinF()));

		   					break;
		case ANTCLASS1 : 	r.put("X size", new Double(this.getxsize()));
							r.put("Y size", new Double(this.getysize()));
							r.put("N of ants", new Double(this.getnants()));
							r.put("N of items", new Double(this.getnitems()));
							r.put("N of keys", new Double(this.getnkeys()));
							r.put("Max Carry low", new Double(this.getMaxCarryLow()));
							r.put("Max Carry range", new Double(this.getMaxCarryRange()));
							r.put("Speed low", new Double(this.getSpeedLow()));
							r.put("Speed range", new Double(this.getSpeedRange()));
							r.put("P Load low", new Double(this.getPLoadLow()));
							r.put("P Load range", new Double(this.getPLoadRange()));
							r.put("P Drop low", new Double(this.getPDropLow()));
							r.put("P Drop range", new Double(this.getPDropRange()));
							r.put("P Destroy low", new Double(this.getPDestroyLow()));
							r.put("P Destroy range", new Double(this.getPDestroyRange()));
							r.put("P Direction low", new Double(this.getPDirectionLow()));
							r.put("P Direction range", new Double(this.getPDirectionRange()));
							r.put("T Create low", new Double(this.getTCreateLow()));
							r.put("T Create range", new Double(this.getTCreateRange()));
							r.put("T Remove low", new Double(this.getTRemoveLow()));
							r.put("T Remove range", new Double(this.getTRemoveRange()));
		   					r.put("Memory Size", new Double(this.getmemsize()));
							r.put("Cicle 1", new Double(this.getCicle1()));
							r.put("Cicle 2", new Double(this.getCicle2()));
		   					r.put("Minimum F", new Double(this.getMinF()));	   					
							break;
		case ANTCLASS2 : 	r.put("X size", new Double(this.getxsize()));
							r.put("Y size", new Double(this.getysize()));
							r.put("N of ants", new Double(this.getnants()));
							r.put("N of items", new Double(this.getnitems()));
							r.put("N of keys", new Double(this.getnkeys()));
							r.put("Max Carry low", new Double(this.getMaxCarryLow()));
							r.put("Max Carry range", new Double(this.getMaxCarryRange()));
							r.put("Speed low", new Double(this.getSpeedLow()));
							r.put("Speed range", new Double(this.getSpeedRange()));
							r.put("P Load low", new Double(this.getPLoadLow()));
							r.put("P Load range", new Double(this.getPLoadRange()));
							r.put("P Drop low", new Double(this.getPDropLow()));
							r.put("P Drop range", new Double(this.getPDropRange()));
							r.put("P Destroy low", new Double(this.getPDestroyLow()));
							r.put("P Destroy range", new Double(this.getPDestroyRange()));
							r.put("P Direction low", new Double(this.getPDirectionLow()));
							r.put("P Direction range", new Double(this.getPDirectionRange()));
							r.put("T Create low", new Double(this.getTCreateLow()));
							r.put("T Create range", new Double(this.getTCreateRange()));
							r.put("T Remove low", new Double(this.getTRemoveLow()));
							r.put("T Remove range", new Double(this.getTRemoveRange()));
							r.put("Memory Size", new Double(this.getmemsize()));
							r.put("Cicle 1", new Double(this.getCicle1()));
//							r.put("Cicle 2", new Double(this.getCicle2()));
							r.put("Minimum F", new Double(this.getMinF()));	   					
							break;
		}
		return r;
	}
		
}



	
