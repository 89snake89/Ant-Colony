/*  
Copyright (C) 2012 Antonio Fonseca
Email: antoniofilipefonseca@gmail.com

*/

/*****************************************************************
António Fonseca
antoniofilipefonseca@gmail.com

File: Ant.java
Package: antclust

Description:

* Represents an individual agent
* Stores all essential information
	- Current Position
	- Current Load
	- Memory
	- Speed
* Stores pointer to the environment
* Environment provides all necessary information (e.g. density)
	
                                                                                                                    	
*****************************************************************/

package antcolony;

import javaants.Document;
import javaants.Position;
import javaants.Configuration;

import java.lang.*;
import java.io.*;

/** Ant Model - Represents an individual agent */
public class Ant implements Serializable {

// individual information
private int load;				// number of the carried document
private Position pos;			// current grid position of ant
private int speed;       		// speed of this particuar ant
private Memory memory;          // memory of this partocular ant
	
// Extension to prevent the algorithm from stagnation
private int fail;			// count number of failed dropping attempts (in a row)
private boolean reset;                  // set true if failure reaches 100 => ant drops document and tries a new one
				

// pointer to the environment
private Grid grid;                    	// grid
private Configuration conf;             // configuration
private Item [] documents;          // available data elements



/******** Constructor **********************************************************************************************/
	
/** Constructor
* @param grid pointer to the ant's environment
* @param speed the speed value associated with this ant
* @param pos the position of this ant
* @param documents a vector to the document data
* @param conf the current paramter settings
*/
public Ant(Grid grid, int speed, Position pos, int load, Item [] documents, Configuration conf) {

	this.load = load;
	this.speed = speed;
	this.pos = new Position();
	this.pos.set(pos);
	this.memory = new Memory(conf.getmemsize(), documents, grid, conf);
	
	this.fail = 0;
	this.reset = false;

	this.grid = grid;
	this.documents = documents;
	this.conf = conf;
}

/******** small access functions ************************************************************************************/		

/** Tell whether ant should give up with its current data element (i.e. drop it) and reset
* @return truth value corresponding to the state of the reset flag
*/
public boolean getReset() {
	if (reset == true) {
		reset = false;
			return true;
		}
	return false;
}

/** How fast am I allowed to crawl?
* @return current ant speed
*/
public int getSpeed() {
	return this.speed;
}

/** Where am I?
* @param pos the current position will be stored in this paramter
*/
public void getPosition(Position pos) {
	pos.set(this.pos);
	return;
}

/** What do I currently carry?
* @return document number of currently carried document or -1
*/
public int getLoad() {
	return this.load;
}




/******** small modification functions ******************************************************************************/	

/** Store my maximum speed
* @param speed speed associated with this ant
*/
public void setSpeed(int speed) {
	this.speed = speed;
}
/** store my new position
* @param pos new position of this ant
*/
public void setPosition(Position pos) {
	this.pos.set(pos);
}


/******** pick, drop & step: the three actions ********************************************************************************/

/** Compute pickup-probability given the density value
* @param f the local density and similarity f
* @return the resulting probability
*/
private double ppick(double f) {
	double p = (this.conf.getkp() / (this.conf.getkp() + f));
	return p*p;
}
/** Compute drop-probability given the density value
* @param f the local density and similarity f
* @return the resulting probability
*/
private double pdrop(double f) {
	double p = (f / (this.conf.getkd() + f));
	return p*p;
}

/** Try to pick up a document
* @return report on the success of pickup operation
*/
public boolean pick() {

	if ( !grid.free(this.pos) ) {											// Check whether document found
        	double f = grid.densityAt(grid.at(pos), pos, this.speed);		// Compute local density/similarity
		if (Math.random() < ppick(f)) {										// Compute probability -> Decision
			this.load = grid.at(this.pos);									// Pick up
			documents[this.load].setPicked(true);
			grid.remove(this.pos);											// Mark grid position as free
			this.memory.orient(this.load);          		                // Try to remember the last similiar document + position
			return true;													// report success
		}
	}
	return false;
}

/** Drop a document
* @return report on the success of drop operation
*/
public boolean drop() {
	double f = grid.densityAt(this.load, this.pos, this.speed);   		// Compute local density/similarity
	f = pdrop(f);                                                           // Compute probability
	
	if ((fail == 100) || (Math.random() < f)) {       					// Decision (always drop after 100 failures)
		if (fail == 100) reset = true;
		fail = 0;                                                       // reset failure counter after each successful dropping
		if ( grid.free(this.pos) ) {       								// Check whether the current grid cell is free
			grid.set(this.pos, this.load);                          	// manipulate environment
			documents[this.load].setPicked(false);
			this.memory.store(this.load, this.pos);                 	// update memory
			this.load = -1;                                         	// update myself
			return true;
		}
		else {                                                           // I have to look for another spot...
			grid.nextFree(this.pos);                                 	// ...that is still free
			grid.set(this.pos, this.load);                           	//...
			documents[this.load].setPicked(false);
			this.memory.store(this.load, this.pos);
			this.load = -1;
			return true;
		}
	}
	fail++;
	return false;
}

/** Drop document at best remembered position when the sorting process is halted
*/
public void finish() {
	if (this.load == -1) return;
	this.memory.getBestMatchPosition(this.load, this.pos);
	this.grid.nextFree(this.pos);
	this.grid.set(this.pos, this.load);
	this.documents[this.load].setPosition(this.pos);
}

/** Pick your document again if the sorting process is resumed
*/
public void resume() {
	this.grid.remove(this.pos);
}


/** Do one random step on the grid */
public void step() {
	grid.move(this.pos, this.speed, this.memory);   			// let the environment do the work for you...
}
}