// Mira Kasari
// mkasari

package hw3;

import java.io.File;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class TSVCaseReader extends CaseReader{

	TSVCaseReader(String filename) {
		super(filename);
	}

	@SuppressWarnings("finally")
	@Override
	List<Case> readCases() throws DataException  {
		String caseDate, caseTitle, caseType, caseNumber, caseLink, caseCategory, caseNotes; 
		ArrayList<Case> cases = new ArrayList<>(); 
		int rejected = 0; 

		// load in file using StringBuilder
		StringBuilder sb = new StringBuilder();
		Scanner sc = null;
		try {
			sc = new Scanner(new File(filename)).useDelimiter("\\t");
			while (sc.hasNextLine()) {
				sb.append(sc.nextLine() + "\n");
			}

			String[] fileData = sb.toString().split("\n");

			// for each line in fileData, split by tab to get date, title, and case number
			for (int i = 0; i < fileData.length; i++) {
				String[] currCase = fileData[i].split("\t");

				// if empty, set to null, else set to value
				if (currCase[0] == "") {
					caseDate = null;
				} else {
					caseDate = currCase[0].trim();
				}

				if (currCase[1] == "") {
					caseTitle = null;
				} else {
					caseTitle = currCase[1].trim();
				}

				if (currCase[2] == "") {
					caseType = null;
				} else {
					caseType = currCase[2].trim();
				}

				if (currCase[3] == "") {
					caseNumber = null;
				} else {
					caseNumber = currCase[3].trim();
				}

				if (currCase[4] == "") {
					caseLink = null;
				} else {
					caseLink = currCase[4].trim();
				}


				if (currCase[5] == "") {
					caseCategory =  null;
				} else {
					caseCategory = currCase[5].trim();
				}


				if (currCase[6] == "") {
					caseNotes = null;
				} else {
					caseNotes = currCase[6].trim();
				}


				// create new Case and append to case list
				if (caseDate == null || caseTitle == null || caseType == null || caseNumber == null
						|| caseDate.equals("") || caseTitle.equals("") ||
						caseType.equals("") || caseNumber.equals("")) {
					rejected++; 
				} else {
					cases.add(new Case(caseDate, caseTitle, caseType, caseNumber, caseLink, caseCategory, caseNotes));
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally { 
			sc.close(); 
			
			// send alert if there are any rejected cases
			if (rejected != 0) {
				new DataException(rejected + " cases rejected." + "\n" + "The file must have cases with" + "\n" +
			"tab separated date, title, type, and case number!");
			}
			return cases;


		}

	}
}