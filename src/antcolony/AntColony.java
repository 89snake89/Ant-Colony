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
	 * @return a String with the model and phase executed
	 */
	public String sort(int tick) {
		String out= "";
		String diag="";
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
								out = "LUMERFAIETA_S phase 1 Complete";
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
								out = "LUMERFAIETA_M phase 1 Complete";
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
								out = "LUMERFAIETA_R phase 1 Complete";
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
											out = "ANTCLASS1 phase 1 Complete";
											break;
											
									case 1: diag = this.grid.kmeans_heaps();
											out = "ANTCLASS1 phase 2 and 4 "+ diag;
											break;
										
									case 2: for (int a = 0; a< conf.getnants(); a++) {
											ants[a].move_ant_class();
									  		this.grid.decPheromone();
											if ( ants[a].hasHeap())
												ants[a].drop_ant_class_heap();
											else
												ants[a].pick_ant_class_heap();
											}
											out = "ANTCLASS1 phase 3 Complete";
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
											out = "ANTCLASS2 phase 1";
											break;
					
									case 1: diag = this.grid.kmeans_heaps();
											out = "ANTCLASS2 phase 2 " + diag + this.grid.cluster_heaps();
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
								out = "ANTCLASS3 phase 1";
								break;

								case 1: diag = this.grid.cluster_3_heaps();
										out = "ANTCLASS3 phase 2 "+ diag;
										break;

								}
								this.cleanMemories();
								break;
		case ANTCLASS4 		: 	phase = 0;
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
								out = "ANTCLASS4 phase 1";
								break;

								case 1: diag = this.grid.cluster_4_heaps();
								out = "ANTCLASS4 phase 2 "+ diag;
								break;

								}
								this.cleanMemories();
								break;

		}
		return out;
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
