// Mira Kasari
// mkasari

package hw3;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Case implements Comparable<Case>{
	StringProperty caseDate;
	StringProperty caseTitle;
	StringProperty caseType;
	StringProperty caseNumber;
	StringProperty caseLink;
	StringProperty caseCategory;
	StringProperty caseNotes; 
	
	public Case(String caseDate_, String caseTitle_, String caseType_, String caseNumber_, String caseLink_, String caseCategory_, String caseNotes_) {
		this.caseDate = new SimpleStringProperty(caseDate_);
		this.caseType = new SimpleStringProperty(caseType_);
		this.caseTitle = new SimpleStringProperty(caseTitle_);
		caseNumber_ = caseNumber_.replace(" ", "");
		caseNumber_ = caseNumber_.replace("-", "");
		this.caseNumber = new SimpleStringProperty(caseNumber_);
		this.caseLink = new SimpleStringProperty(caseLink_);
		this.caseCategory = new SimpleStringProperty(caseCategory_);
		this.caseNotes = new SimpleStringProperty(caseNotes_);
	}
	
	
	public String toString() {
		return caseNumber.getValue().trim();
	}
	
	
	// case date  
	public String getCaseDate() {
		return this.caseDate.getValue().trim();
	}
	
	public void setCaseDate(String date) {
		this.caseDate = new SimpleStringProperty(date);
	}
	
	public StringProperty caseDateProperty() {
		return this.caseDate;
	}
	
	
	
	
	// case title
	public String getCaseTitle() {
		return this.caseTitle.getValue().trim();
	}
	
	public void setCaseTitle(String title) {
		this.caseTitle = new SimpleStringProperty(title);
	}
	
	public StringProperty caseTitleProperty() {
		return this.caseTitle;
	}
	
	
	
	
	// case type
	public String getCaseType() {
		return this.caseType.getValue().trim();
	}
	
	public void setCaseType(String type) {
		this.caseType = new SimpleStringProperty(type);
	}
	
	public StringProperty caseTypeProperty() {
		return this.caseType;
	}
	
	
	
	
	// case number
	public String getCaseNumber() {
		return this.caseNumber.getValue().trim();
	}
	
	public void setCaseNumber(String number) {
		this.caseNumber = new SimpleStringProperty(number);
	}
	
	public StringProperty caseNumberProperty() {
		return this.caseNumber;
	}
	
	
	
	
	// case link 
	public String getCaseLink() {
		return this.caseLink.getValue().trim();
	}
	
	public void setCaseLink(String link) {
		this.caseLink = new SimpleStringProperty(link);
	}
	
	public StringProperty caseLinkProperty() {
		return this.caseLink;
	}
	
	
	
	
	// case category
	public String getCaseCategory() {
		return this.caseCategory.getValue().trim();
	}
	
	public void setCaseCategory(String category) {
		this.caseCategory = new SimpleStringProperty(category);
	}
	
	public StringProperty caseCategoryProperty() {
		return this.caseCategory;
	}
	
	
	
	
	
	// case notes
	public String getCaseNotes() {
		return this.caseNotes.getValue().trim();
	}
	
	public void setCaseNotes(String notes) {
		this.caseNotes = new SimpleStringProperty(notes);
	}
	
	public StringProperty caseNotesProperty() {
		return this.caseNotes;
	}


	@Override
	public int compareTo(Case o) {
		return o.caseDate.getValue().compareTo(this.caseDate.getValue());
	}
	
	



}
