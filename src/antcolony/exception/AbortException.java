/*****************************************************************
	Julia Handl - 18004385
	Monash University, 1.7.2001

	File: AbortException.java
	Package: JavaAnts.Exception

	Description: Reaction to Cancel
                                                                                                                        	
*****************************************************************/

package antcolony.exception;

/** Exception thrown if user presses Cancel */
public class AbortException extends Throwable {
	public AbortException() {
	    super(" Action aborted by user");
	}
}
