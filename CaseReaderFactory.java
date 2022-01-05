// Mira Kasari
// mkasari

package hw3;


public class CaseReaderFactory {


	public CaseReader createCaseReader(String filename) {
		
		// get extension of file to find type
		String extension = filename.split("\\.")[1];
		
		// call respective child
		if (extension.equals("tsv")) return new TSVCaseReader(filename);
		else return new CSVCaseReader(filename);
	}


}
