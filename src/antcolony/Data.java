/*  
    Copyright (C) 2004 Julia Handl
    Email: Julia.Handl@gmx.de

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
	Julia Handl - 18004385
	Monash University, 1.7.2001
	"Data.java"
	Package: JavaAnts
	
	Super Class for the Representation of Document Collections
*****************************************************************/

package antcolony;

import java.io.Serializable;

public enum Dataset {STANDARD, RANDOM};

/**
* Super Class for the Representation of Document Collections
*/
public class Data implements Serializable {

	// Member variables containing main data
	public static int nkeys;                	// number of keywords
	public static int ndocs;                        // number of documents        	
	public static Document [] documents;          	// document collection
	public static String [] keys;                   // keywords                   	

	/** 
	* Create a new empty document dollection */
	public Data() {
		nkeys = 0;
		ndocs = 0;
		documents = null;
		keys = null;
	}

    /** 
    * Create new document collection from provided data */
    
	public Data(int nkeys, int ndocs, String [] keys, Document[] documents) {
		this.nkeys = nkeys;
		this.ndocs = ndocs;
		this.documents = documents;
		this.keys = keys;
	}

    /** get the dimensionality of the document space */
	public int getnkeys() {
		return this.nkeys;
	}
	/** get the number of documents */
	public int getndocs() {
		return this.ndocs;
	}
	/** retrieve all documents */
	public Document [] getDocs() {
		return this.documents;
	}
	/** retrieve the keyword assigned to each dimension */
	public String [] getKeys() {
		return this.keys;
	}
}
