/*****************************************************************
	Julia Handl - 18004385
	Monash University, 1.7.2001

	File: AbortException.java
	Package: JavaAnts.Exception

	Description: Warns about illegal action due to wrong mode
	
                                                                                                                        	
*****************************************************************/
package antcolony.exception;

/** Thrown if user tries to perform Ant operations in MDS mode */
public class WrongModeException extends Throwable {
	public WrongModeException() {
	    super("Action can only be performed in ANT-Mode");
	}
}
