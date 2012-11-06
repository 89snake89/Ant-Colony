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

	File: Configuration.java
	Package: JavaAnts

	Description: Wrapping class for all essential parameter settings

*****************************************************************/


package antcolony;

import java.io.*;
import java.util.HashMap;

public class Configuration {


	// Datasets parameters
	
	public enum Datasets {UNIFORM9, NORMAL4,IRIS,WINE,GLASS,BREAST};
	private Datasets dataset = Datasets.UNIFORM9;
	private int nitems = 200;
	private int dmeasure = 2;
	private String[] types = new String[]{"0","1","2","3","4","5","6","7","8"};
	private int ntypes = 9;
	
	// Item Parameters
	private int nkeys = 5;
	private int maxitemsize = 10;
	
	// Simulation parameters
	private int xsize = 100;
	private int ysize = 100;
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
	private int cicle = 3000;
	private double min_f = 0.85;
	private double min_d = 0.1;
	
	// Model parameters
	public enum Models {ANTCLASS,LUMERFAIETA_S, LUMERFAIETA_M, LUMERFAIETA_R};
	private Models model = Models.ANTCLASS;
	
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

	//file parameters
	private String filename = "record_ant_clustering.txt";
	

/********** Constructor ***************************************************************************/
	

	public Configuration() {	
	}
	
	
/********** simple manipulation functions *********************************************************/	

	public void setDataset(Datasets value) {
		this.dataset = value;
	}
	public void setDMeasure(int value) {
		this.dmeasure = value;
	}
	public void setTypes(String[] value){
		this.types = value;
	}
	public void setxsize(int value) {
		this.xsize = value;
	}
	public void setysize(int value) {
		this.ysize = value;
	}
	public void setModel(Models value) {
		this.model = value;
	}
	public void setAlpha(double value) {
		this.alpha = value;
	}
	public void setKd(double value) {
		this.kd = value;
	}
	public void setKp(double value) {
		this.kp = value;
	}
    public void setnitems(int value) {
    	this.nitems = Math.max(value,nants);
	}
	public void setnkeys(int value) {
		this.nkeys = value;
	}
	public void setntypes(int value) {
		this.ntypes = value;
	}
	public void setMaxitemsize(int value) {
		this.maxitemsize = value;
	}
	public void setnants(int value) {
		this.nants = value;
	}
	public void setSigma(int value) {
		this.sigma = value;
	}
	public void setMaxSpeed(int value) {
		this.max_speed = value;
	}
	
	public void setMaxCarryLow(double value){
		this.max_carry_low = value;
	}
	public void setMaxCarryRange(double value){
		this.max_carry_range = value;
	}
	
	public void setSpeedLow(double value){
		this.speed_low = value;
	}
	
	public void setSpeedRange(double value){
		this.speed_range = value;
	}
	
	public void setPLoadLow(double value){
		this.p_load_low = value;
	}
	
	public void setPLoadRange(double value){
		this.p_load_range = value;
	}
	
	public void setPDropLow(double value){
		this.p_drop_low = value;
	}
	
	public void setPDropRange(double value){
		this.p_drop_range = value;
	}
	
	public void setPDestroyLow(double value){
		this.p_destroy_low = value;
	}
	
	public void setPDestroyRange(double value){
		this.destroy_range = value;
	}
	
	public void setPDirectionLow(double value){
		this.p_direction_low = value;
	}
	
	public void setPDirectionRange(double value){
		this.p_direction_range = value;
	}
	
	public void setTCreateLow(double value){
		this.t_create_low = value;
	}
	
	public void setTCreateRange(double value){
		this.t_create_range = value;
	}
	
	public void setTRemoveLow(double value){
		this.t_remove_low = value;
	}
	
	public void setTRemoveRange(double value){
		this.t_remove_range = value;
	}
	
	public void setMemsize(int value) {
		this.memsize = value;
	}
	public void setFilename(String value) {
		this.filename = value;
	}
	
	public void setCicle(int value){
		this.cicle = value;
	}
	
	public void setMinF(double value){
		this.min_f = value;
	}
	
	public void setMinD(double value){
		this.min_d = value;
	}
	
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
		if (k == "Cicle") this.setCicle((int)v);
		if (k == "Minimum F")this.setMinF(v);
		if (k == "Minimum Density")this.setMinD(v);
	}
	
/********** simple access functions ********************************************************************/	
	

	public Datasets getDataset() {
		return this.dataset;
	}	
	public int getDMeasure(){
		return this.dmeasure;
	}
	public String[] getTypes(){
		return this.types;
	}
	public Models getModel() {
		return this.model;
	}
	public double getAlpha() {
		return this.alpha;
	}
	public double getKd() {
		return this.kd;
	}
	public double getKp() {
		return this.kp;
	}
	public int getxsize() {
		return this.xsize;
	}
	public int getysize() {
		return this.ysize;
	}
	public int[] getColors() {
		return this.colors;
	}
	public int getnitems() {
		return this.nitems;
	}
	public int getnants() {
		return this.nants;
	}
	public int getnkeys() {
		return this.nkeys;
	}
	public int getntypes() {
		return this.ntypes;
	}
	public int getMaxitemsize(){
		return this.maxitemsize;
	}
	public int getSigma() {
		return this.sigma;
	}
	public int getMaxSpeed() {
		return this.max_speed;
	}
	public double getMaxCarryLow(){
		return this.max_carry_low;
	}
	public double getMaxCarryRange(){
		return this.max_carry_range;
	}
	public double getSpeedLow(){
		return this.speed_low;
	}
	public double getSpeedRange(){
		return this.speed_range;
	}	
	public double getPLoadLow(){
		return this.p_load_low;
	}
	public double getPLoadRange(){
		return this.p_load_range;
	}
	public double getPDropLow(){
		return this.p_drop_low;
	}
	public double getPDropRange(){
		return this.p_drop_range;
	}
	public double getPDestroyLow(){
		return this.p_destroy_low;
	}
	public double getPDestroyRange(){
		return this.destroy_range;
	}
	public double getPDirectionLow(){
		return this.p_direction_low;
	}
	public double getPDirectionRange(){
		return this.p_direction_range;
	}
	public double getTCreateLow(){
		return this.t_create_low;
	}
	public double getTCreateRange(){
		return this.t_create_range;
	}
	public double getTRemoveLow(){
		return this.t_remove_low;
	}
	public double getTRemoveRange(){
		return this.t_remove_range;
	}
	public int getmemsize() {
		return this.memsize;
	}
	public String getFilename() {
		return this.filename;
	}
	public int getCicle(){
		return this.cicle;
	}
	public double getMinF(){
		return this.min_f;
	}
	
	public double getMinD(){
		return this.min_d;
	}
	
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
//		   					 r.put("Minimum Density", new Double(this.getMinD()));
		   					 break;
		case LUMERFAIETA_R : 	r.put("X size", new Double(this.getxsize()));
		   						r.put("Y size", new Double(this.getysize()));
		   						r.put("N of ants", new Double(this.getnants()));
		   						r.put("Sigma", new Double(this.getSigma()));
		   						r.put("Kd", this.getKd());
		   						r.put("Kp", this.getKp());
		   						r.put("Minimum F", new Double(this.getMinF()));
//		   						r.put("Minimum Density", new Double(this.getMinD()));
		   					break;
		case ANTCLASS : 	r.put("X size", new Double(this.getxsize()));
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
							r.put("Cicle", new Double(this.getCicle()));
		   					r.put("Minimum F", new Double(this.getMinF()));	   					
							break;
		}
		return r;
	}
		
/********** Miscellaneous ********************************************************************/


	/** Record configuration on file
	* @param filename
	*/
	public void save(String file) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(this);
			out.flush();
			out.close();
		} catch (Exception e) {
		}
	}

	
	/** Recover configuration from file
	* @param filename
	*/
	public Configuration read(String file) {
		Configuration newconf = null;
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new FileInputStream(file));
		} catch (Exception e) {
			return null;
		}
		try {
			newconf  = (Configuration)in.readObject();
			in.close();
		} catch (Exception e) {
		}
		return newconf;
	}



}



	
