/*  
    Copyright (C) 2004 Julia Handl
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
	Julia Handl - 18004385
	Monash University, 1.7.2001

	File: Configuration.java
	Package: JavaAnts

	Description: Wrapping class for all essential parameter settings

*****************************************************************/


package antcolony;

import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;


/**
 * Wrapping class for all essential parameter settings.<br>
 * Value- and String-based access to the following parameters is provided:
 * <p>
 * <p>
 * <b>General parameters</b>
 * <ul>
 * <li><i><b>boolean test: </b></i> Enable detailed analysis of sorting progress</li>
 * <li><i><b>boolean testData: </b></i> Enable further analysis (possible only with test data A1)</li>
 * <li><i><b>boolean blocked: </b></i> Internal use: Ensures that only one topic map at a time can be viewed</li>
 * <li><i><b>int method: </b></i> Toggle between old and new ant-algorithm, Ant-Q and the Hybrid</li>
 * <li><i><b>String browser: </b></i> Configured browser path</li>
 * </ul>
 * <p>
 * <b>Map parameters</b>
 * <ul>
 * <li><i><b>int xsize: </b></i> Map width</li>
 * <li><i><b>int ysize: </b></i> Map height</li>
 * <li><i><b>int runs: </b></i> Upper limit on number of runs for the ant-based methods</li>
 * <li><i><b>int iterations: </b></i> Number of iterations per run</li>
 * </ul>
 * <p>
 * <ul>
 * <b>Ant parameters</b>
 * <li><i><b>int nants: </b></i> Size of ant colony</li>
 * <li><i><b>int speed: </b></i> Ant speed limit</li>
 * <li><i><b>boolean homogenous: </b></i> Toggle between homogenous and inhomogenous population</li>
 * <li><i><b>int memsize: </b></i> Ant memory size</li>
 * <li><i><b>int sigma: </b></i> Size of perceived neighbourhood</li>
  * <li><i><b>boolean adaptK: </b></i> Toggle between adaptive and non-adaptive mode for kd and kp</li>
 * <li><i><b>double kd: </b></i> Regulation of dropping probability</li>
 * <li><i><b>double kd_start: </b></i> Lower limit for kd in adaptive mode</li>
 * <li><i><b>double kd_end: </b></i> Upper limit for kd in adaptive mode</li>
 * <li><i><b>double kd_interval: </b></i> Stepwidth for kd in adaptive mode</li>
 * <li><i><b>double kp: </b></i> Regulation of picking probability</li>
 * <li><i><b>double kp_start: </b></i> Lower limit for kp in adaptive mode</li>
 * <li><i><b>double kp_end: </b></i> Upper limit for kp in adaptive mode</li>
 * <li><i><b>double kp_interval: </b></i> Stepwidth for kp in adaptive mode</li>
 * <li><i><b>boolean adaptAlpha: </b></i> Toggle between adaptive and non-adaptive mode for alpha</li>
 * <li><i><b>double alpha: </b></i> Scaling of dissimilarities</li>
 * <li><i><b>double alpha_start: </b></i> Lower limit for alpha in adaptive mode</li>
 * <li><i><b>double alpha_end: </b></i> Upper limit for alpha in adaptive mode</li>
 * <li><i><b>double alpha_interval: </b></i> Stepwidth for alpha in adaptive mode</li>
 * </ul>
 * <p>
 * <ul>
 * <b>Document parameters</b>
 * <li><i><b>int nkeys: </b></i> Dimension of document space</li>
 * <li><i><b>int ndocs: </b></i> Number of documents</li>
 * <li><i><b>double distscale: </b></i> Scaling paramter for dissimilarities</li>
 * <li><i><b>int simMeasure: </b></i> Select similarity measure (Euclidean / Cosine / Overlap)</li>
 * <li><i><b>int textMode: </b></i> Select retrieval mode (Snippets / Full-Text / META-Data)</li>
 * <li><i><b>boolean useDefaults: </b></i> Use default preprocessing?</li>
 * <li><i><b>int lowercutoff: </b></i> Lower bound cutoff frequency</li>
 * <li><i><b>int uppercutoff: </b></i> Upper bound cutoff frequency</li>
 * <li><i><b>int dim: </b></i> LSI Target dimension</li>
 * <li><i><b>int choice: </b></i> Select test distribution</li>
 * </ul>
 * <p>
  */
public class Configuration implements Serializable {

	// General program switches

	private boolean test = false;				
	private boolean testData = false;			
	private boolean blocked = false;			
	private int method = 0;						
	private String browser = "netscape";


	// Map parameters
	private int xsize = 100;
	private int ysize = 100;
	private int iterations = 10000;
	private int runs = 50;
	
		
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



	// Document Parameters
	private double distscale;
	private int nkeys = 2;
	private int simMeasure = 0;					/** Select similarity measure (Euclidean / Cosine / Overlap) */
	private int textMode = 0;					/** Select retrieval mode (Snippets / Full-Text / META-Data) */
		
	private boolean usedefaults = true;
	private int lowercutoff = 2;
	private int uppercutoff = 1000;
	private int dim = 50;
	
	private int ndocs = 600;
	private int choice = 0;


/********** Constructor ***************************************************************************/
	

	public Configuration() {
		
	}
	
	
/********** simple manipulation functions *********************************************************/	
	
	// string based
	public void setBrowser(String value) {
		this.browser = value;
	}
	public void setLowerCutoff(String value) {
		this.lowercutoff = new Integer(value).intValue();
	}
	public void setUpperCutoff(String value) {
		this.uppercutoff = new Integer(value).intValue();
	}
	public void setdimension(String value) {
		this.dim = new Integer(value).intValue();
	}
	public void setAlphaStart(String value) {
		this.alpha_start = new Double(value).doubleValue();
	}
	public void setAlphaEnd(String value) {
		this.alpha_end = new Double(value).doubleValue();
	}
	public void setAlphaInterval(String value) {
		this.alpha_interval = new Double(value).doubleValue();
	}
	public void setKdStart(String value) {
		this.kd_start = new Double(value).doubleValue();
	}
	public void setKdEnd(String value) {
		this.kd_end = new Double(value).doubleValue();
	}
	public void setKdInterval(String value) {
		this.kd_interval = new Double(value).doubleValue();
	}
	public void setKpStart(String value) {
		this.kp_start = new Double(value).doubleValue();
	}
	public void setKpEnd(String value) {
		this.kp_end = new Double(value).doubleValue();
	}
	public void setKpInterval(String value) {
		this.kp_interval = new Double(value).doubleValue();
	}
	public void setxsize(String value) {
	    this.xsize = new Integer(value).intValue();
	}
	public void setysize(String value) {
		this.ysize = new Integer(value).intValue();
	}
	public void setruns(String value) {
		this.runs = new Integer(value).intValue();
	}
	public void setnants(String value) {
		this.nants = new Integer(value).intValue();
	}
	public void setndocs(String value) {
		this.ndocs = new Integer(value).intValue();
	}
	public void setnkeys(String value) {
		this.nkeys = new Integer(value).intValue();
	}
	public void setspeed(String value) {
		this.speed =  new Integer(value).intValue();
	}
	public void setmemsize(String value) {
		this.memsize = new Integer(value).intValue();
	}
	public void setkd(String value) {
		this.kd = new Double(value).doubleValue();
	}
	public void setkp(String value) {
		this.kp = new Double(value).doubleValue();
	}
	public void setalpha(String value) {
		this.alpha = new Double(value).doubleValue();
	}
	public void setsigma(String value) {
		this.sigma = new Integer(value).intValue();
	}
	
	// value based
	public void setLowerCutoff(int value) {
		this.lowercutoff = value;
	}
	public void setUpperCutoff(int value) {
		this.uppercutoff = value;
	}
	public void setdimension(int value) {
		this.dim = value;
	}
	public void setUseDefaults(boolean value) {
		this.usedefaults = value;
	}
	public void setMethod(int value) {
		this.method = value;
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
	public void setTest(boolean value) {
		this.test = value;
	}
	public void setTestData(boolean value) {
		this.testData = value;
	}
	public void setAdaptAlpha(boolean value) {
		this.adaptAlpha = value;
	}
	public void setAdaptK(boolean value) {
		this.adaptK = value;
	}
	public void setTextMode(int value) {
		this.textMode = value;
	}
	public void setSimMeasure(int value) {
		this.simMeasure = value;
	}	
	public void sethomogenous(boolean value) {
		this.homogenous = value;
	}
	public void setchoice(int choice) {
		this.choice = choice;
	}
    public void setiterations(int its) {
		this.iterations = its;
	}
    public void setndocs(int value) {
		this.ndocs = value;
	}
	public void setnkeys(int value) {
		this.nkeys = value;
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
	public void setBlocked(boolean value) {
		this.blocked = value;
	}
	
	
/********** simple access functions ********************************************************************/	
	
	// value based
	public String getbrowser() {
		return this.browser;
	}

	public int getLowerCutoff() {
		return this.lowercutoff;
	}
	public int getUpperCutoff() {
		return this.uppercutoff;
	}
	public int getDimension() {
		return this.dim;
	}
	public boolean getUseDefaults() {
		return this.usedefaults;
	}
	public boolean getBlocked() {
		return this.blocked;
	}
	public int getMethod() {
		return this.method;
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

	public boolean getTest() {
		return this.test;
	}
	public boolean getTestData() {
		return this.testData;
	}
	public boolean getadaptalpha() {
		//if (method == 2) return false;
		return this.adaptAlpha;
	}
	public boolean getadaptk() {
		return this.adaptK;
	}
	public int getSimMeasure() {
		return this.simMeasure;
	}
	public int getTextMode() {
		return this.textMode;
	}	
	public int getxsize() {
		return this.xsize;
	}
	public int getysize() {
		return this.ysize;
	}
	public int getiterations() {
		return this.iterations;
	}
	public int getruns() {
		return this.runs;
	}
	public int getndocs() {
		return this.ndocs;
	}
	public int getnants() {
		return this.nants;
	}
	public int getnkeys() {
		return this.nkeys;
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
	public int getchoice() {
		return this.choice;
	}


    // String based
    	public String getsLowerCutoff() {
		return (new Integer(this.lowercutoff)).toString();
	}
	public String getsUpperCutoff() {
		return (new Integer(this.uppercutoff)).toString();
	}
	public String getsDimension() {
		return (new Integer(this.dim)).toString();
	}
	public String getsUseDefaults() {
		return (new Boolean(this.usedefaults)).toString();
	}
	public String getsalphastart() {
		return (new Double(this.alpha_start)).toString();
	}
	public String getsalphaend() {
		return (new Double(this.alpha_end)).toString();
	}
	public String getsalphainterval() {
		return (new Double(this.alpha_interval)).toString();
	}
	public String getskpstart() {
		return (new Double(this.kp_start)).toString();
	}
	public String getskpend() {
		return (new Double(this.kp_end)).toString();
	}
	public String getskpinterval() {
		return (new Double(this.kp_interval)).toString();
	}
	public String getskdstart() {
		return (new Double(this.kd_start)).toString();
	}
	public String getskdend() {
		return (new Double(this.kd_end)).toString();
	}
	public String getskdinterval() {
		return (new Double(this.kd_interval)).toString();
	}
	public String getsxsize() {
		return (new Integer(this.xsize)).toString();
	}
	public String getsysize() {
		return (new Integer(this.ysize)).toString();
	}
	public String getsruns() {
		return (new Integer(this.runs)).toString();
	}
	public String getsndocs() {
		return (new Integer(this.ndocs)).toString();
	}
	public String getsnants() {
		return (new Integer(this.nants)).toString();
	}
	public String getsnkeys() {
		return (new Integer(this.nkeys)).toString();
	}
	public String getskd() {
		return (new Double(this.kd)).toString();
	}
	public String getskp() {
		return (new Double(this.kp)).toString();
	}
	public String getssigma() {
		return (new Integer(this.sigma)).toString();
	}
	public String getsalpha() {
		return (new Double(this.alpha)).toString();
	}
	public String getsmemsize() {
		return (new Integer(this.memsize)).toString();
	}
	public String getsspeed() {
		return (new Integer(this.speed)).toString();
	}
	
		
/********** Miscellaneous ********************************************************************/
	

	
	// bring up an interface to save current configuration (plus resulting map as gif)
	public void write(String filename) throws IOException {

			JDialog dialog = new JDialog();
			dialog.setTitle("Description");
			dialog.setSize(300, 300);
			dialog.getContentPane().setLayout(new BorderLayout());
			JLabel label = new JLabel("Please enter file description");
			dialog.getContentPane().add(label, BorderLayout.NORTH);
			JTextArea description = new JTextArea();
			description.setFont(new Font("Serif", Font.ITALIC, 16));
			description.setLineWrap(true);
			description.setWrapStyleWord(true);

			JScrollPane areaScrollPane = new JScrollPane(description);
			areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			areaScrollPane.setPreferredSize(new Dimension(250, 250));
		
			dialog.getContentPane().add(areaScrollPane, BorderLayout.CENTER);
			JButton button = new JButton("OK");
			
			// an new SaveConf Object handles the storage process
			button.addActionListener(new SaveConf(dialog, description, this, filename));
			
			Panel buttonbox = new Panel();
			buttonbox.setLayout(new BorderLayout());
			buttonbox.add(button, BorderLayout.EAST);
			dialog.getContentPane().add(buttonbox, BorderLayout.SOUTH);
			dialog.show();
	}

	public void save() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("System.conf"));
			out.writeObject(this);
			out.flush();
			out.close();
		} catch (Exception e) {
		}
	}

	public Configuration read() {
		Configuration newconf = null;
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new FileInputStream("System.conf"));
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



	
