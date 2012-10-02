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

public class Configuration {


	// Datasets parameters
	
	public enum Datasets {UNIFORM9, NORMAL4};
	private Datasets dataset = Datasets.UNIFORM9;
	private int nitems = 600;
	private int dmeasure = 0;
	
	// Item Parameters
	private int nkeys = 50;
	private int maxitemsize = 10;
	
	// Simulation parameters
	private int xsize = 600;
	private int ysize = 600;
	private String[] symbols = new String[]{"X","O","*","+","&","$","#","%","="};
	
	// Model parameters
	public enum Models {LUMERFAIETA, MODEL2};
	private Models model = Models.LUMERFAIETA;
	
	// Ant parameters
	private int nants = 20;
	private int speed = 50;
	private int memsize = 20;
	private int sigma = 2;
	private double kd = 0.01;
	private double kp = 0.01;
	private double alpha = 1.0;
	

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
	public void setXsize(int value) {
		this.xsize = value;
	}
	public void setYsize(int value) {
		this.ysize = value;
	}
	public void setModel(Models value) {
		this.model = value;
	}
	public void setAlpha(int value) {
		this.alpha = value;
	}
	public void setKd(int value) {
		this.kd = value;
	}
	public void setKp(int value) {
		this.kp = value;
	}
    public void setnitems(int value) {
    	this.nitems = value;
	}
	public void setnkeys(int value) {
		this.nkeys = value;
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
	public void setSpeed(int value) {
		this.speed = value;
	}
	public void setMemsize(int value) {
		this.memsize = value;
	}
	
/********** simple access functions ********************************************************************/	
	

	public Datasets getDataset() {
		return this.dataset;
	}	
	public int getDMeasure(){
		return this.dmeasure;
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
	public String[] getSymbols() {
		return this.symbols;
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
	public int getMaxitemsize(){
		return this.maxitemsize;
	}
	public int getsigma() {
		return this.sigma;
	}
	public int getspeed() {
		return this.speed;
	}
	public int getmemsize() {
		return this.memsize;
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



	
