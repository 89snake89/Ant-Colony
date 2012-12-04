package antcolony;

/** Ant Colony - Represents a colony of agents
 * 
 * Each Ant Colony has a corresponding running configuration and
 * a corresponding running model.
 * 
 * @author      António Fonseca
 * @version     1.0
 * @since       1.0
 * */
public class AntColony {

	private Ant [] ants;					
		
	// Pointers to the environment
	private Configuration conf;   			                    
	private Configuration.Models model;
	private Grid grid;
	private int phase;

	
	
/************** Constructor ************************************************************/	

	/** Constructor
	* @param conf the current parameter setting
	* @param grid the colony's grid environment
	*/
	public AntColony(Configuration conf, Grid grid) {
	
		this.conf = conf;
		this.model = conf.getModel();
		this.grid = grid;
		this.phase = 0;
		this.ants = new Ant[this.conf.getnants()];
		for (int i = 0; i < this.conf.getnants(); i++) ants[i] = new Ant(grid,conf);
	}


/************* small access function *****************************************************************/	
	
	/** Get me all the members of this colony
	* @return array of all ants
	*/
	public Ant[] getAnts() {
		return this.ants;
	}


/************* Sorting functions *****************************************************************/		

	/** The ant-algorithm used by this colony
	 * @param tick - the tick number of the simulation in order to implement clustering
	 * strategy change in the AntClass model
	 */
	public void sort(int tick) {
		switch (model){
		
		case LUMERFAIETA_S : for (int a = 0; a< conf.getnants(); a++) {
								if ( ants[a].hasLoad()) {
										ants[a].drop_lumer_faieta(); 
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
									ants[a].drop_lumer_faieta(); 
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
									 ants[a].drop_lumer_faieta(); 
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
								
		case ANTCLASS1 		: 	phase = 0;
								if (tick == conf.getCicle1()) phase = 1;
								if (tick > conf.getCicle1()) phase = 2;
								if (tick == conf.getCicle2()+conf.getCicle1()) phase = 1;
								if (tick > conf.getCicle2()+conf.getCicle1()) break;

								switch (phase){
									case 0: for (int a = 0; a< conf.getnants(); a++) {
											ants[a].move_ant_class();
											if ( ants[a].hasLoad())
												ants[a].drop_ant_class();
											else
												ants[a].pick_ant_class();
											}
											break;
											
									case 1: this.grid.kmeans_heaps();
											break;
										
									case 2: for (int a = 0; a< conf.getnants(); a++) {
											ants[a].move_ant_class();
									  		this.grid.decPheromone();
											if ( ants[a].hasHeap())
												ants[a].drop_ant_class_heap();
											else
												ants[a].pick_ant_class_heap();
											}
											break;
								}
								this.cleanMemories();
								break;
							  
		case ANTCLASS2 		: 	phase = 0;
								if (tick==conf.getCicle1()) phase=1;
								if (tick > conf.getCicle1()) break;
								switch (phase){
									case 0: for (int a = 0; a< conf.getnants(); a++) {
											ants[a].move_ant_class();
											if ( ants[a].hasLoad())
												ants[a].drop_ant_class();
											else
												ants[a].pick_ant_class();
											}
											break;
					
									case 1: this.grid.kmeans_heaps();
											this.grid.cluster_heaps();
											break;

									}
								this.cleanMemories();
								break;
		case ANTCLASS3 		: 	phase = 0;
								if (tick==conf.getCicle1()) phase=1;
								if (tick > conf.getCicle1()) break;
								switch (phase){
								case 0: for (int a = 0; a< conf.getnants(); a++) {
									ants[a].move_ant_class();
									if ( ants[a].hasLoad())
										ants[a].drop_ant_class();
									else
										ants[a].pick_ant_class();
								}
								break;

								case 1: this.grid.cluster_3_heaps();
										break;

			}
		this.cleanMemories();
		break;

		}	
	}
	
	
	
	/** Make the ants drop all the loads
	 */
	public void drop(){
		for (int a = 0; a< conf.getnants(); a++) ants[a].drop();
	}
	
	
	/** Clean the memory of the ants
	 */
	public void cleanMemories(){
		for (int a = 0; a< conf.getnants(); a++) ants[a].cleanMemory();
	}

}
