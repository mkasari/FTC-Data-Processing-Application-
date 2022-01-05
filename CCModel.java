// Mira Kasari
// mkasari

package hw3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.DataFormatException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class CCModel {
	ObservableList<Case> caseList = FXCollections.observableArrayList(); 			//a list of case objects
	ObservableMap<String, Case> caseMap = FXCollections.observableHashMap();		//map with caseNumber as key and Case as value
	ObservableMap<String, List<Case>> yearMap = FXCollections.observableHashMap();	//map with each year as a key and a list of all cases dated in that year as value. 
	ObservableList<String> yearList = FXCollections.observableArrayList();			//list of years to populate the yearComboBox in ccView

	/** readCases() performs the following functions:
	 * It creates an instance of CaseReaderFactory, 
	 * invokes its createReader() method by passing the filename to it, 
	 * and invokes the caseReader's readCases() method. 
	 * The caseList returned by readCases() is sorted 
	 * in the order of caseDate for initial display in caseTableView. 
	 * Finally, it loads caseMap with cases in caseList. 
	 * This caseMap will be used to make sure that no duplicate cases are added to data
	 * @param filename
	 */
	void readCases(String filename) {
		CaseReaderFactory csf = new CaseReaderFactory();
		CaseReader reader = csf.createCaseReader(filename);
		try {
			caseList = FXCollections.observableArrayList(reader.readCases());
		} catch (DataFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.sort(caseList);
		for (Case c: caseList) {
			caseMap.put(c.getCaseNumber(), c);
		}

	}

	/** buildYearMapAndList() performs the following functions:
	 * 1. It builds yearMap that will be used for analysis purposes in Cyber Cop 3.0
	 * 2. It creates yearList which will be used to populate yearComboBox in ccView
	 * Note that yearList can be created simply by using the keySet of yearMap.
	 */
	void buildYearMapAndList() { 

		// create new hashset and fill in with distinct values of year by iterating thru caseList
		Set<String> years = new HashSet<>();
		for (int i = 0; i < caseList.size(); i++) {
			String currYear = caseList.get(i).getCaseDate().split("-")[0]; 
			if (!years.contains(currYear)) years.add(currYear);
		}

		// iterate thru caseList to find all cases with respective years and add to list
		// populate new hashmap with distinct years as keys and the list of cases as values for each key 
		for (int i = 0; i < caseList.size(); i++) {
			List<Case> currCases = new ArrayList<>();
			String currYear = caseList.get(i).getCaseDate().split("-")[0]; 
			for (Case c : caseList) {
				if (currYear.equals(c.getCaseDate().split("-")[0])) currCases.add(caseList.get(i));
			}
			yearMap.put(currYear, currCases);
		}



		// set yearList to keys of the hashmap to get distinct year values
		yearList = FXCollections.observableArrayList(yearMap.keySet());
		Collections.sort(yearList);
	}

	/**searchCases() takes search criteria and 
	 * iterates through the caseList to find the matching cases. 
	 * It returns a list of matching cases.
	 */
	List<Case> searchCases(String title, String caseType, String year, String caseNumber) {

		// clean data
		List<Case> matching = FXCollections.observableArrayList();
		if (title == null)
			title = "";
		if (caseType == null) 
			caseType = "";
		if (year == null)
			year = "";
		if (caseNumber == null) {
			caseNumber = "";
		} else {
			caseNumber = caseNumber.replace("-", "");
			caseNumber = caseNumber.replace(" ", "");
		}


		// iterate thru caseList to find cases matching criteria  
		for (Case c : caseList) {
			if (c.getCaseTitle().toLowerCase().contains(title.toLowerCase()) &&
					c.getCaseType().toLowerCase().contains(caseType.toLowerCase())
					&& c.getCaseDate().toLowerCase().startsWith(year.toLowerCase()) &&
					c.getCaseNumber().toLowerCase().contains(caseNumber.toLowerCase())) {
				matching.add(c);
			}
		}


		return matching;
	

	}

	
	public boolean writeCases(String filename) {
		
		// create new file using user-given filename
		File file = new File(filename);
		if (file != null) {
			
			// print each case property to tsv format for each case
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
				for (Case c : caseList) {
					writer.write(String.format("%s \t%s \t%s \t%s \t%s \t%s \t%s \t\n", c.getCaseDate(), c.getCaseTitle(), c.getCaseType(), 
							c.getCaseNumber(), c.getCaseLink(), c.getCaseCategory(), c.getCaseNotes()));

				}
				writer.close();
				
			// file save failed 
			} catch (IOException e) {
				System.out.println("File save failed.");
				return false;
			}

		}
		return true;

	}




}
