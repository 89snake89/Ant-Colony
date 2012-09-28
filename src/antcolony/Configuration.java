/*  
    Copyright (C) 2012 António Fonseca
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
	António Fonseca
	antoniofilipefonseca@gmail.com

	File: Configuration.java
	Package: JavaAnts

	Description: Wrapping class for all essential parameter settings

*****************************************************************/


package antcolony;

import java.io.*;

public class Configuration implements Serializable {


	private static final long serialVersionUID = 4843124430119352057L;
	
	// Datasets parameters
	
	public enum Datasets {UNIFORM9, NORMAL4};
	private Datasets dataset = Datasets.UNIFORM9;
	private int nitems = 600;
	private int dmeasure = 0;
	
	// Item Parameters
	private double distscale;
	private int nkeys = 50;
	private int maxitemsize = 10;
	
	
	// Map parameters
	private int xsize = 600;
	private int ysize = 600;
	private int[] colors = new int[]{0,255,65280,16711680,16776960,16711935,65535,16753920,16729344};
	private int iterations = 10000;
	private int runs = 50;
	
	// Model parameters
	
	public enum Models {LUMERFAIETA, MODEL2};
	private Models model = Models.LUMERFAIETA;
	
	// Ant parameters
	private int nants = 20;
	private int speed = 50;
	private boolean homogenous = false;
	private int memsize = 20;
	private int sigma = 2;
	private double kd = 0.01;
	private double kp = 0.01;
	private double kd_end = 0.01;
	private double kd_start = 0.1;
	private double kd_interval = 0.001;
	private double kp_end = 0.01;
	private double kp_start = 0.1;
	private double kp_interval = 0.001;
	private boolean adaptK = false;				/** Toggle between adaptive and non-adaptive mode*/
	private boolean adaptAlpha = true;			/** Toggle between adaptive and non-adaptive mode*/
	private double alpha_end = 1.0;
	private double alpha_start = 0.1;
	private double alpha_interval = 0.01;
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
	public void setModel(Models value) {
		this.model = value;
	}
	public void setAlphaStart(int value) {
		this.alpha_start = value;
	}
	public void setAlphaEnd(int value) {
		this.alpha_end = value;
	}
	public void setAlphaInterval(int value) {
		this.alpha_interval = value;
	}
	public void setKdStart(int value) {
		this.kd_start = value;
	}
	public void setKdEnd(int value) {
		this.kd_end = value;
	}
	public void setKdInterval(int value) {
		this.kd_interval = value;
	}
	public void setKpStart(int value) {
		this.kp_start = value;
	}
	public void setKpEnd(int value) {
		this.kp_end = value;
	}
	public void setKpInterval(int value) {
		this.kp_interval = value;
	}
	public void setAdaptAlpha(boolean value) {
		this.adaptAlpha = value;
	}
	public void setAdaptK(boolean value) {
		this.adaptK = value;
	}
	public void sethomogenous(boolean value) {
		this.homogenous = value;
	}
    public void setiterations(int its) {
		this.iterations = its;
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
	public void setalpha(double value) {
		this.alpha = value;
	}
	public void setnants(int value) {
		this.nants = value;
	}
 	public void setdistscale(double distscale) {
		this.distscale = distscale;
	}
	public void setkd(double value) {
		this.kd = value;
	}
	public void setkp(double value) {
		this.kp = value;
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
	public double getalphastart() {
		return this.alpha_start;
	}
	public double getalphaend() {
		return this.alpha_end;
	}
	public double getalphainterval() {
		return this.alpha_interval;
	}
	public double getkdstart() {
		return this.kd_start;
	}
	public double getkdend() {
		return this.kd_end;
	}
	public double getkdinterval() {
		return this.kd_interval;
	}
	public double getkpstart() {
		return this.kp_start;
	}
	public double getkpend() {
		return this.kp_end;
	}
	public double getkpinterval() {
		return this.kp_interval;
	}
	public boolean getadaptalpha() {
		//if (method == 2) return false;
		return this.adaptAlpha;
	}
	public boolean getadaptk() {
		return this.adaptK;
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
	public int getiterations() {
		return this.iterations;
	}
	public int getruns() {
		return this.runs;
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
	public double getkd() {
		return this.kd;
	}
	public double getkp() {
		return this.kp;
	}
	public int getsigma() {
		return this.sigma;
	}
	public int getspeed() {
		return this.speed;
	}
	public double getalpha() {
		//if (method == 2) return 1.0;
		return this.alpha;
	}
	public int getmemsize() {
		return this.memsize;
	}
	public boolean gethomogenous() {
		return this.homogenous;
	}
	public double getdistscale() {
		return this.distscale * this.alpha;
	}

	
		
/********** Miscellaneous ********************************************************************/

	public void save(String file) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(this);
			out.flush();
			out.close();
		} catch (Exception e) {
		}
	}

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



	
