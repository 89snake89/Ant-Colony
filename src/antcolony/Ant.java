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

import java.util.Iterator;
import java.util.LinkedList;


/** Ant Model - Represents an individual agent */
public class Ant {


private Item load;
private Heap load_heap;
private int x,y;
private int xsize, ysize;
private int speed;
private int max_speed;
private int direction;
private LinkedList<Heap> memory_h;
private LinkedList<Item> memory_i;
private double p_direction;
private double p_load;
private double p_drop;
private double p_destroy;
private double kd;
private double kp;
private double t_remove;
private double t_create;
private double d_max;
private int max_carry;
private int fail;
private int has_load;
private int has_load_heap;
private Grid grid;
private Configuration conf;


/******** Constructor **********************************************************************************************/
	
/** Constructor
* @param grid pointer to the ant's environment
* @param simulation configuration
*/
public Ant(Grid grid, Configuration conf) {

	this.grid = grid;
	this.conf = conf;
	this.x = (int)Math.random()*conf.getxsize();
	this.y = (int)Math.random()*conf.getysize();
	this.xsize = this.conf.getxsize();
	this.ysize = this.conf.getysize();
	this.scatter();
	this.load = grid.getItemAt(this.x,this.y);
	this.load.setPicked(true);
	this.has_load = 1;
	grid.remove_item(this.x,this.y);
	this.d_max = Math.sqrt((double)this.load.getData().size());
	this.fail = 0;
	this.max_carry = (int)(conf.getMaxCarryLow() * Math.random()* conf.getMaxCarryRange());
	this.speed = (int)(conf.getSpeedLow()+ Math.random() * conf.getSpeedRange());
	this.max_speed = conf.getMaxSpeed();
	this.p_load = conf.getPLoadLow()+ Math.random() * conf.getPLoadRange();
	this.p_drop = conf.getPDropLow()+ Math.random() * conf.getPDropRange();
	this.p_destroy = conf.getPDestroyLow() + Math.random() * conf.getPDestroyRange();
	this.p_direction = conf.getPDirectionLow() + Math.random() * conf.getPDirectionRange();
	this.t_create = conf.getTCreateLow() + Math.random() * conf.getTCreateRange();
	this.t_remove = conf.getTRemoveLow() + Math.random() * conf.getTRemoveRange();
	this.kd = conf.getKd();
	this.kp = conf.getKp();
	this.direction = (int)Math.random()*8;
	this.memory_h = new LinkedList<Heap>();
	this.memory_i = new LinkedList<Item>();
}

/******** access functions ************************************************************************************/		



/** What do I currently carry?
* @return document number of currently carried document or -1
*/
public Item getLoad() {
	return this.load;
}

/** What do I currently carry?
* @return document number of currently carried document or -1
*/
public boolean hasLoad() {
	return this.load != null;
}

/** What do I currently carry?
* @return document number of currently carried document or -1
*/
public boolean hasHeap() {
	return this.load_heap != null;
}

/** Set the coordinates of this ant
* @param x, y coordinates
*/
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
	return Math.pow((kp / (kp + f)) , 2);
}


/** Compute drop-probability given the density value
* @param f the local density and similarity f
* @return the resulting probability
*/
private double pdrop(double f) {
	return Math.pow((f / (kd + f)) , 2);
}


/** Try to pick up a item
* @return report on the success of pickup operation
* */

public boolean pick_lumer_faieta() {
	if (grid.occupied_item(this.x,this.y)) {	
    double f = grid.densityAt(this.x,this.y, grid.getItemAt(this.x, this.y));
	if (Math.random() < ppick(f)) {
			this.load = grid.getItemAt(this.x,this.y);
			this.load.setPicked(true);
			updateMemoryItem(this.load);
			grid.remove_item(this.x,this.y);
			return true;
		}
	}
	return false;
}


/** Drop a item according to a probability after 100 tentatives drop
* @return report on the success of drop operation
*/

public boolean drop_lumer_faieta() {
	double f = grid.densityAt(this.x,this.y,this.load);	
	if ((fail == 100) || (Math.random() < pdrop(f))) {
		if (!grid.occupied_item(this.x,this.y)) {
			this.load.setPicked(false);
			grid.put_item(this.x, this.y, this.load);
			this.load = null;
			fail = 0;
			return true;
		}
	}
	fail++;
	return false;
}


/** Moves an ant from its current (already occupied) position to the next position
* which can be free or occupied according to the boolean flag f the model parameter
* m determines the method of searching the new position
* @param f flag for occupied or free position, m model of moving
*/
    
public void move_lumer_faieta(boolean f, Configuration.Models model) {	
	
	switch (model) {
	
	case LUMERFAIETA_M : 	if (this.memory_i.size() > 0){
								Item i = null;
								if (this.hasLoad())i = this.getMostSimilar(this.load);
								else i = memory_i.removeFirst();
								this.x = i.getX();
								this.y = i.getY();
							}
		
	case LUMERFAIETA_S : 	int step = 2;
							boolean loop = true;
							while (loop) {
								for (int i = 0; i< this.max_speed; i++) {
									int xpart = (int)Math.round(step * Math.random());
									int ypart = step - xpart;
												
									if (Math.random() < 0.5) xpart = -xpart;
									if (Math.random() < 0.5) ypart = -ypart;
												
									int x_coor = this.x + xpart;
									int	y_coor = this.y + ypart;
									if (x_coor < 0) x_coor = xsize + x_coor%xsize;
									if (x_coor >= xsize) x_coor = x_coor%xsize;
									if (y_coor < 0) y_coor = ysize + y_coor%ysize;
									if (y_coor >= ysize) y_coor = y_coor%ysize;

									if (grid.occupied_item(x_coor,y_coor) == f) {
										this.x = x_coor;
										this.y = y_coor;
										loop = false;
									}
				
								}
								step++;
							}
							break;
	}}


public void move_random_item(boolean f, Item it){
	boolean loop = true;
	while (loop) {
		int x_coor = (int)(Math.random()*conf.getxsize());
		int	y_coor = (int)(Math.random()*conf.getysize());
		if (grid.occupied_item(x_coor,y_coor) == f) {
			this.x = x_coor;
			this.y = y_coor;
			loop = false;
		}
	}
}

public void move_random(boolean f){
	boolean loop = true;
	while (loop) {
		int x_coor = (int)(Math.random()*conf.getxsize());
		int	y_coor = (int)(Math.random()*conf.getysize());
		if (grid.occupied_item(x_coor,y_coor) == f) {
			this.x = x_coor;
			this.y = y_coor;
			loop = false;
		}
	}
}
						
public void scatter() {
	
	done: while(true) {
			int x_coor = (int)(Math.random()*conf.getxsize());
			int	y_coor = (int)(Math.random()*conf.getysize());
			if (grid.occupied_item(x_coor,y_coor) == true) {
				this.x = x_coor;
				this.y = y_coor;
				break done;
			}
	}
}


public void move_ant_class(){

	if (this.has_load > 0 && this.memory_h.size() > 0){
		Heap h = this.memory_h.removeFirst();
		this.x = h.getX() + 1;
		this.y = h.getY() + 1;
	}
	else{
		if (Math.random()< p_direction) this.direction = (int)(Math.random()*8);
		switch (this.direction){
			case 0 : this.y -= this.speed; break;
			case 1 : this.x += this.speed; this.y -= this.speed; break;
			case 2 : this.x += this.speed; break;
			case 3 : this.x += this.speed; this.y += this.speed; break;
			case 4 : this.y += this.speed; break;
			case 5 : this.x -= this.speed; this.y += this.speed; break;
			case 6 : this.x -= this.speed; break;
			case 7 : this.x -= this.speed; this.y -= this.speed; break;
		}
		if (this.x < 0) this.x = xsize + this.x%xsize;
		if (this.x >= xsize) this.x = this.x%xsize;
		if (this.y < 0) this.y = ysize + this.y%ysize;
		if (this.y >= ysize) this.y = this.y%ysize;
	}
	this.has_load++;
}

public void pick_ant_class(){
	done: for (int i= -1 ; i<=1; i++)
			for (int j= -1; j<=1; j++){
				int x_coor = this.x + i;
				int	y_coor = this.y + j;
				if (x_coor < 0) x_coor = xsize + x_coor%xsize;
				if (x_coor >= xsize) x_coor = x_coor%xsize;
				if (y_coor < 0) y_coor = ysize + y_coor%ysize;
				if (y_coor >= ysize) y_coor = y_coor%ysize;
				if (grid.occupied_item(x_coor, y_coor) && Math.random()< this.p_load ){
					this.load = grid.getItemAt(x_coor, y_coor);
					this.load.setPicked(true);
					this.has_load=1;
					grid.remove_item(x_coor,y_coor);
					break done;
				}
				if (grid.occupied_heap(x_coor, y_coor)){
					Heap h = grid.heapAt(x_coor, y_coor);
					this.updateMemoryHeap(h);
					if (h.getSize()==2) {
						if(Math.random() < this.p_destroy) {
							LinkedList<Item> l = h.getItems();
							this.load = l.removeLast();
							this.load.setPicked(true);
							this.has_load = 1;
							Item it2 = l.removeLast();
							it2.setPicked(false);
							grid.put_item(x_coor,y_coor,it2);
							grid.remove_heap(x_coor, y_coor);
							break done;
						}}
					else if ((h.getMaxDissimilar()/h.getMeanDistance()) > this.t_remove){
							this.load = h.getItem(h.getMostDissimilar());
							this.load.setPicked(true);
							this.has_load=1;
							break done;
						}
					}
							
			}
	}


public void pick_ant_class_heap(){
	done: for (int i= -1 ; i<=1; i++)
			for (int j= -1; j<=1; j++){
				int x_coor = this.x + i;
				int	y_coor = this.y + j;
				if (x_coor < 0) x_coor = xsize + x_coor%xsize;
				if (x_coor >= xsize) x_coor = x_coor%xsize;
				if (y_coor < 0) y_coor = ysize + y_coor%ysize;
				if (y_coor >= ysize) y_coor = y_coor%ysize;
				if (grid.occupied_heap(x_coor, y_coor)){
					Heap h = grid.heapAt(x_coor, y_coor);
					if (h.getPheromone() == 0 && this.load_heap==null && Math.random()< this.p_load) {
						this.load_heap = h;
						this.load_heap.setPicked(true);
						this.has_load_heap=1;
						grid.remove_heap(x_coor, y_coor);
						break done;
					}
				}
			}

}

public void drop_ant_class(){
	done: for (int i= -1 ; i<=1; i++)
			for (int j= -1; j<=1; j++){
				int x_coor = this.x + i;
				int	y_coor = this.y + j;
				if (x_coor < 0) x_coor = xsize + x_coor%xsize;
				if (x_coor >= xsize) x_coor = x_coor%xsize;
				if (y_coor < 0) y_coor = ysize + y_coor%ysize;
				if (y_coor >= ysize) y_coor = y_coor%ysize;
				
				if (!grid.occupied(x_coor, y_coor)) {
					if ((Math.random()< this.p_drop || this.has_load > this.max_carry)){
						this.load.setPicked(false);
						grid.put_item(x_coor, y_coor, this.load);
						this.load = null;
						this.has_load = 0;
						break done;
						}
				}
				if (grid.occupied_item(x_coor, y_coor) && !grid.occupied_heap(x_coor, y_coor)){
					Item it = grid.getItemAt(x_coor, y_coor);
					if (it.distance(this.load, 2)/this.d_max < this.t_create){
						grid.remove_item(x_coor, y_coor);
						this.load.setPicked(false);
						it.setPicked(false);
						grid.put_heap(x_coor, y_coor, new Heap(conf,x_coor,y_coor,it,this.load));
						this.load=null;
						this.has_load = 0;
						break done;
					}
				}
				if (grid.occupied_heap(x_coor, y_coor) ){
					Heap h = grid.heapAt(x_coor, y_coor);
					this.updateMemoryHeap(h);
					if (h.computeDistanceCenterMass(this.load) < h.getMaxDissimilar()){
						this.load.setPicked(false);
						h.putItem(this.load);
						this.load=null;
						this.has_load = 0;
						break done;
					}
				}
			}
}

public void drop_ant_class_heap(){
	done: for (int i= -1 ; i<=1; i++)
			for (int j= -1; j<=1; j++){
				int x_coor = this.x + i;
				int	y_coor = this.y + j;
				if (x_coor < 0) x_coor = xsize + x_coor%xsize;
				if (x_coor >= xsize) x_coor = x_coor%xsize;
				if (y_coor < 0) y_coor = ysize + y_coor%ysize;
				if (y_coor >= ysize) y_coor = y_coor%ysize;
				
				if (grid.occupied_heap(x_coor, y_coor) ){
					Heap h = grid.heapAt(x_coor, y_coor);
					if (h.computeDistanceCenterMassVector(this.load_heap.getCenterMass())/this.d_max <= this.t_create){
						grid.remove_heap(x_coor, y_coor);
						h.putItems(this.load_heap.getItems());
						grid.put_heap(x_coor, y_coor, h);
						this.load_heap=null;
						this.has_load_heap = 0;
						break done;
					}
				}
				
				else if (Math.random()<this.p_drop && !grid.occupied_heap(x_coor, y_coor) && !grid.occupied_item(x_coor, y_coor)){
					this.load_heap.setPicked(false);
					this.load_heap.setPheromone(500);
					grid.put_heap(x_coor, y_coor, this.load_heap);
					this.load_heap=null;
					this.has_load_heap = 0;
					break done;
				}

			}
}

public void drop(){
	if (this.load != null)
	{
		int step = 2;
		done: while (true) {
			for (int i = 0; i< 10; i++) {
				int xpart = (int)Math.round(step * Math.random());
				int ypart = step - xpart;
							
				if (Math.random() < 0.5) xpart = -xpart;
				if (Math.random() < 0.5) ypart = -ypart;
							
				int x_coor = this.x + xpart;
				int	y_coor = this.y + ypart;
				if (x_coor < 0) x_coor = xsize + x_coor%xsize;
				if (x_coor >= xsize) x_coor = x_coor%xsize;
				if (y_coor < 0) y_coor = ysize + y_coor%ysize;
				if (y_coor >= ysize) y_coor = y_coor%ysize;

				if (grid.occupied_item(x_coor,y_coor) == false) {
					this.x = x_coor;
					this.y = y_coor;
					this.load.setPicked(false);
					this.grid.put_item(x_coor, y_coor, this.load);
					this.load = null;
					this.has_load = 0;
					break done;
				}

			}
			step++;
		}
	}
}

private void updateMemoryHeap(Heap hp){
	this.memory_h.addLast(hp);
	if (this.memory_h.size() > conf.getmemsize()) this.memory_h.removeFirst();
}

private void updateMemoryItem(Item i){
	this.memory_i.addLast(i);
	if (this.memory_i.size() > conf.getmemsize()) this.memory_i.removeFirst();
}

public void cleanMemory(){
	this.memory_h = new LinkedList<Heap>();
	this.memory_i = new LinkedList<Item>();
}

private Item getMostSimilar(Item it){
	double min_dist = Double.MAX_VALUE;
	Item min = null;
	System.out.println(memory_i.size());
	for (Item i: this.memory_i){
		double d = i.distance(it, 2);
		if (d < min_dist) {min_dist=d; min=i;}
	}
	return this.memory_i.get(this.memory_i.indexOf(min));
}

}



