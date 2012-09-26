/*****************************************************************
	Julia Handl - 18004385
	Monash University, 1.7.2001

	File: NoInitException.java
	Package: JavaAnts.Exception

	Description: Warns about illegal actions due to missing initialisation
	
                                                                                                                        	
*****************************************************************/

package antcolony.exception;

/** Thrown if user tries to perform illegal operation on uninitialised topic map */
public class NoInitException extends Throwable {
	public NoInitException() {
		super("Map has not been initialized");
	}
}
