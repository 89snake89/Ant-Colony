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

	File: AntColony.java
	Package: antcolony

	Description:

	* Represents the entire ant colony
	* Contains an array of ants
	* Stores pointer to the environment
	* Environment provides all necessary information (e.g. density)
	* Ant provides their own actions
		
                                                                                                                        	
*****************************************************************/

package antcolony;


public class AntColony {

	private Ant [] ants;					
		
	// Pointers to the environment
	private Configuration conf;   			                    
	private Configuration.Models model;
	private Grid grid;
	private int phase;
	private int cicle;
	private boolean tg;

	
	
/************** Constructor ************************************************************/	

	/** Constructor
	* @param conf the current parameter setting
	* @param grid the colony's environment
	*/
	public AntColony(Configuration conf, Grid grid) {
	
		this.conf = conf;
		this.model = conf.getModel();
		this.grid = grid;
		this.phase = 0;
		this.cicle = conf.getCicle();
		this.tg = true;
		this.ants = new Ant[this.conf.getnants()];
		for (int i = 0; i < this.conf.getnants(); i++) ants[i] = new Ant(grid,conf);
	}


/************* small access function *****************************************************************/	
	
	/** Tell me all the members of this colony
	* @return array of all ants
	*/
	public Ant [] getAnts() {
		return this.ants;
	}


/************* Sorting functions *****************************************************************/		

	/** The ant-algorithm 
	 */
	
	public void sort(int tick) {
		switch (model){
		
		case LUMERFAIETA_S : for (int a = 0; a< conf.getnants(); a++) {
								if ( ants[a].hasLoad()) {
									if (ants[a].drop_lumer_faieta() == true) 
										ants[a].move_lumer_faieta(true,Configuration.Models.LUMERFAIETA_S);
									}
								else {
									boolean f = false;
									while (!f){
										f = ants[a].pick_lumer_faieta();
										ants[a].move_random(true);
									}
									ants[a].move_lumer_faieta(false,Configuration.Models.LUMERFAIETA_S);
									}
								}
								break;
								
		case LUMERFAIETA_M : for (int a = 0; a< conf.getnants(); a++) {
								if ( ants[a].hasLoad()) {
									if (ants[a].drop_lumer_faieta() == true) 
										ants[a].move_lumer_faieta(false,Configuration.Models.LUMERFAIETA_M);
								}
								else {
									boolean f = false;
									while (!f){
										f = ants[a].pick_lumer_faieta();
										ants[a].move_random(true);
									}
									ants[a].move_lumer_faieta(false,Configuration.Models.LUMERFAIETA_M);
									}
								}
								break;
								
		case LUMERFAIETA_R : for (int a = 0; a< conf.getnants(); a++) {
								if ( ants[a].hasLoad()){
									if (ants[a].drop_lumer_faieta() == true) 
										ants[a].move_random(false);
									}
								else {
									boolean f = false;
									while (!f){
										f = ants[a].pick_lumer_faieta();
										ants[a].move_random(true);
									}
									ants[a].move_random(false);
								}
								}
								break;
								
		case ANTCLASS1 		: 	if (phase==1 && tg) {phase = 2; tg=false;}
								if (phase==1 && !tg) {phase = 0; tg=true;}
								if (tick%this.cicle==0 && tick >0) phase=1;
								for (int a = 0; a< conf.getnants(); a++) {
			
								switch (phase){
									case 0: ants[a].move_ant_class();
											if ( ants[a].hasLoad())
												ants[a].drop_ant_class();
											else
												ants[a].pick_ant_class();
											break;
											
									case 1: this.grid.kmeans_heaps();
											break;
										
									case 2: ants[a].move_ant_class();
									  		this.grid.decPheromone();
											if ( ants[a].hasHeap())
												ants[a].drop_ant_class_heap();
											else
												ants[a].pick_ant_class_heap();
											break;
								}
							  }
							  this.cleanMemories();
							  break;
		case ANTCLASS2 		: 	phase = 0;
								if (tick%this.cicle==0 && tick >0) phase=1;
								for (int a = 0; a< conf.getnants(); a++) {

									switch (phase){
									case 0: ants[a].move_ant_class();
											if ( ants[a].hasLoad())
												ants[a].drop_ant_class();
											else
												ants[a].pick_ant_class();
											break;
					
									case 1: this.grid.kmeans_heaps();
											this.grid.cluster_heaps();
											break;

		}
	  }
	  this.cleanMemories();
	  break;
		}	
	}
	
	public void drop(){
		for (int a = 0; a< conf.getnants(); a++) ants[a].drop();
	}
	
	public void cleanMemories(){
		for (int a = 0; a< conf.getnants(); a++) ants[a].cleanMemory();
	}

}
