// Mira Kasari
// mkasari

package hw3;

import java.util.List;
import java.util.zip.DataFormatException;


/**CaseReader class is used by CaseReaderFactory 
 * to initialize the data file to be read. 
 * Its readCases() method is implemented by 
 * CSVCaseReader and TSVCaseReader classes
 */
public abstract class CaseReader {

	String filename;

	CaseReader(String filename) {
		this.filename = filename;
	}

	abstract List<Case> readCases() throws DataFormatException;

}
