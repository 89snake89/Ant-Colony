/*  
Copyright (C) 2012 Antonio Fonseca
Email: antoniofilipefonseca@gmail.com

*/

/*****************************************************************
Antonio Fonseca
antoniofilipefonseca@gmail.com

File: Ant.java
Package: antcolony

Description:

* Represents an individual agent
* Stores all essential information
	- Current Position
	- Current Load
* Stores pointer to the environment
* Environment provides all necessary information (e.g. density)
	
                                                                                                                    	
*****************************************************************/

package antcolony;


/** Ant Model - Represents an individual agent */
public class Ant {


private Item load;
private int x,y;
private int xsize, ysize;
private int speed;
private double kd;
private double kp;
private int fail;
private Grid grid;
private Configuration conf;
private Configuration.Models model;


/******** Constructor **********************************************************************************************/
	
/** Constructor
* @param grid pointer to the ant's environment
* @param speed the speed value associated with this ant
* @param pos the position of this ant
* @param documents a vector to the document data
* @param conf the current parameter settings
*/
public Ant(Grid grid, Configuration conf, int s) {

	this.grid = grid;
	this.conf = conf;
	this.speed = s;
	this.model = conf.getModel();
	this.x = (int)Math.random()*conf.getxsize();
	this.y = (int)Math.random()*conf.getysize();
	this.xsize = this.conf.getxsize();
	this.ysize = this.conf.getysize();
	this.nextOccupied(true, model);
	this.load = grid.getItemAt(this.x,this.y);
	grid.remove(this.x,this.y);
	this.fail = 0;
	this.kd = conf.getKd();
	this.kp = conf.getKp();
}

/******** access functions ************************************************************************************/		



/** What do I currently carry?
* @return document number of currently carried document or -1
*/
public Item getLoad() {
	return this.load;
}

public void setXY(int x, int y) {
	this.x = x;
	this.y = y;
}


/******** pick, drop & step: the three actions ********************************************************************************/

/** Compute pickup-probability given the density value
* @param f the local density and similarity f
* @return the resulting probability
*/
private double ppick(double f) {
//	return f;
	return Math.pow((kp / (kp + f)) , 2);
}
/** Compute drop-probability given the density value
* @param f the local density and similarity f
* @return the resulting probability
*/
private double pdrop(double f) {
//	return -f;
	return Math.pow((f / (kd + f)) , 2);
}

/** Try to pick up a item
* @return report on the success of pickup operation
* */

public boolean pick() {
	//this.grid.scatterItems();
	if (grid.occupied(this.x,this.y)) {										
		
        double f = grid.densityAt(this.x,this.y, grid.getItemAt(this.x, this.y));

		if (Math.random() < ppick(f)) {
			this.load = grid.getItemAt(this.x,this.y);
			this.load.setPicked(true);
			grid.remove(this.x,this.y);
			return true;
		}
	}
	return false;
}


/** Drop a item
* @return report on the success of drop operation
*/

public boolean drop() {
	double f = grid.densityAt(this.x,this.y,this.load);
	
	if ((fail == 100) || (Math.random() < pdrop(f))) {
//		if (fail == 100) System.out.println("alarm");
		fail = 0;
		if (!grid.occupied(this.x,this.y)) {
			this.load.setPicked(false);
			grid.set(this.x, this.y, this.load);
			this.load = null;
			return true;
		}
		else { 
			this.nextOccupied(false , this.model);
			this.load.setPicked(false);
			grid.set(this.x, this.y, this.load);
			this.load = null;
			return true;
		}
	}
	fail++;
	return false;
}

/** Drop a item
* @return report on the success of drop operation
*/

public boolean fdrop() {
	if (!grid.occupied(x,y)) this.nextOccupied(false , this.model);
	this.load.setPicked(false);
	grid.set(this.x, this.y, this.load);
	this.load = null;
	return true;
}

/** Moves an ant from its current (already occupied) position to the next free one
* @param x, y the current ant position, the new position is also stored in here
*/
    
public void nextOccupied(boolean f, Configuration.Models m) {	
	
	boolean loop = true;
	
	switch (m) {
		
	case LUMERFAIETA : 	int step = 1;
					while (loop) {
						for (int i = 0; i< 5; i++) {
							int xpart = (int)Math.round(step * Math.random());
							int ypart = step - xpart;
												
							if ( Math.random() < 0.5) xpart = -xpart;
							if (Math.random() < 0.5) ypart = -ypart;
												
							int x_coor = this.x + xpart;
							int	y_coor = this.y + ypart;
							if (x_coor < 0) x_coor = xsize + x_coor%xsize;
							if (x_coor >= xsize) x_coor = x_coor%xsize;
							if (y_coor < 0) y_coor = ysize + y_coor%ysize;
							if (y_coor >= ysize) y_coor = y_coor%ysize;

							if (grid.occupied(x_coor,y_coor) == f) {
								this.x = x_coor;
								this.y = y_coor;
								loop = false;
							}
				
						}
						step++;
					}
					break;
					
	case LUMERFAIETA_S : 	while (loop) {
							int x_coor = (int)(Math.random()*conf.getxsize());
							int	y_coor = (int)(Math.random()*conf.getysize());
							if (grid.occupied(x_coor,y_coor) == f) {
								this.x = x_coor;
								this.y = y_coor;
								loop = false;
							}
						}
					break;
	}

}

}


