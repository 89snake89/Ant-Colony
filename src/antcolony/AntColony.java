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
	
	
/************** Constructor ************************************************************/	

	/** Constructor
	* @param conf the current parameter setting
	* @param grid the colony's environment
	*/
	public AntColony(Configuration conf, Grid grid) {
	
		this.conf = conf;
		this.model = conf.getModel();
		this.grid = grid;
		this.ants = new Ant[this.conf.getnants()];
		for (int i = 0; i < this.conf.getnants(); i++) ants[i] = new Ant(grid,conf);
		System.out.println("mais isto");
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
	
	public void sort() {
		switch (model){
		
		case LUMERFAIETA : for (int a = 0; a< conf.getnants(); a++) {
								if ( ants[a].getLoad() != null) {
									if (ants[a].drop() == true) ants[a].nextOccupied(true, this.model);
									}
								else {
									boolean f = false;
									while (!f){
										f = ants[a].pick();
										ants[a].nextOccupied(true, this.model);
									}
									ants[a].nextOccupied(false, this.model);
									}
								}
								break;
								
		case LUMERFAIETA_S : for (int a = 0; a< conf.getnants(); a++) {
								if ( ants[a].getLoad() != null){
									if (ants[a].drop() == true) ants[a].nextOccupied(true, this.model);
									}
								else {
									boolean f = false;
									while (!f){
										f = ants[a].pick();
										ants[a].nextOccupied(true, this.model);
									}
									ants[a].nextOccupied(false, this.model);
								}
								}
								break;
		}	
	}

}
