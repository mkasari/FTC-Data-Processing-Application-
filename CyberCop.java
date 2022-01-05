// Mira Kasari
// mkasari

package hw3;


import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;



import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class CyberCop extends Application{

	public static final String DEFAULT_PATH = "data"; //folder name where data files are stored
	public static final String DEFAULT_HTML = "CyberCop.html"; //local HTML
	public static final String APP_TITLE = "Cyber Cop"; //displayed on top of app

	CCView ccView = new CCView();
	CCModel ccModel = new CCModel();

	CaseView caseView; //UI for Add/Modify/Delete menu option

	GridPane cyberCopRoot;
	Stage stage;

	static Case currentCase; //points to the case selected in TableView.

	public static void main(String[] args) {
		launch(args);
	}



	/** start the application and show the opening scene */
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		primaryStage.setTitle("Cyber Cop");
		cyberCopRoot = ccView.setupScreen();  
		setupBindings();
		Scene scene = new Scene(cyberCopRoot, ccView.ccWidth, ccView.ccHeight);
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		ccView.closeFileMenuItem.setDisable(true);
		ccView.addCaseMenuItem.setDisable(true);
		ccView.modifyCaseMenuItem.setDisable(true);
		ccView.deleteCaseMenuItem.setDisable(true);
		ccView.caseCountChartMenuItem.setDisable(true);
		ccView.saveFileMenuItem.setDisable(true);

		ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());
		primaryStage.show();



	}

	/** setupBindings() binds all GUI components to their handlers.
	 * It also binds disableProperty of menu items and text-fields 
	 * with ccView.isFileOpen so that they are enabled as needed
	 */
	void setupBindings() {

		// set selected item in tableview to show up in text fields and in the web page
		ccView.caseTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {

				// get selected item fields
				currentCase = newSelection;
				ccView.titleTextField.setText(newSelection.getCaseTitle());
				ccView.caseTypeTextField.setText(newSelection.getCaseType());
				ccView.caseNumberTextField.setText(newSelection.getCaseNumber());
				ccView.caseNotesTextArea.setText(newSelection.getCaseNotes());
				ccView.yearComboBox.getSelectionModel().select(newSelection.getCaseDate().split("-")[0]);


				// load web page
				if (currentCase.getCaseLink() == null || currentCase.getCaseLink().isBlank()) {  //if no link in data
					URL url = getClass().getClassLoader().getResource(DEFAULT_HTML);  //default html
					if (url != null) ccView.webEngine.load(url.toExternalForm());
				} else if (currentCase.getCaseLink().toLowerCase().startsWith("http")){  //if external link
					ccView.webEngine.load(currentCase.getCaseLink());
				} else {
					URL url = getClass().getClassLoader().getResource(currentCase.getCaseLink().trim());  //local link
					if (url != null) ccView.webEngine.load(url.toExternalForm());
				}
			}
		});




		ccView.openFileMenuItem.setOnAction(new EventHandler<ActionEvent> () {

			@Override
			public void handle(ActionEvent arg0) {

				// open file chooser upon click
				FileChooser fc = new FileChooser();
				fc.setTitle("Open Resource File");
				File selected = fc.showOpenDialog(stage);
				if (selected != null) {
					ccModel.readCases(selected.getAbsolutePath());
				} 


				// build lists and maps according to info in selected file
				ccModel.buildYearMapAndList();

				// set tableview and year combo box to appropriate info 
				ccView.caseTableView.setItems(ccModel.caseList);
				ccView.yearComboBox.getItems().addAll(ccModel.yearList);


				// set current case to default first item and highlight
				// set appropriate fields to current case info
				currentCase = ccView.caseTableView.getItems().get(0);
				ccView.caseTableView.requestFocus();
				ccView.caseTableView.getSelectionModel().select(0);
				ccView.caseTableView.getFocusModel().focus(0); 
				ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());


				// set boolean to true
				ccView.isFileOpen.setValue(true);

				// disable/enable menu items
				ccView.addCaseMenuItem.setDisable(false);
				ccView.modifyCaseMenuItem.setDisable(false);
				ccView.deleteCaseMenuItem.setDisable(false);
				ccView.openFileMenuItem.setDisable(true);
				ccView.closeFileMenuItem.setDisable(false);
				ccView.caseCountChartMenuItem.setDisable(false);
				ccView.saveFileMenuItem.setDisable(false);

				// set message label to show number of cases shown
				ccView.messageLabel.setText(ccModel.caseList.size() + " cases.");

			}

		});



		ccView.closeFileMenuItem.setOnAction(new EventHandler<ActionEvent> () {

			@Override
			public void handle(ActionEvent arg0) {

				// clear all text fields 
				ccView.caseTableView.getItems().clear();
				ccView.titleTextField.clear();
				ccView.caseTypeTextField.clear();
				ccView.caseNumberTextField.clear();
				ccView.yearComboBox.getSelectionModel().clearSelection();
				ccView.caseNotesTextArea.clear();
				ccView.messageLabel.setText("");

				// disable/enable menu items
				ccView.isFileOpen.setValue(false);
				ccView.openFileMenuItem.setDisable(false);
				ccView.addCaseMenuItem.setDisable(true);
				ccView.modifyCaseMenuItem.setDisable(true);
				ccView.deleteCaseMenuItem.setDisable(true);
				ccView.closeFileMenuItem.setDisable(true);
				ccView.caseCountChartMenuItem.setDisable(true);
				ccView.saveFileMenuItem.setDisable(true);
				
				// clear current populated lists of file's info
				ccModel.caseList.clear();
				ccModel.yearList.clear();
				ccModel.yearMap.clear();
			}

		});

		ccView.exitMenuItem.setOnAction(new EventHandler<ActionEvent> () {

			@Override
			public void handle(ActionEvent arg0) {

				// close current stage
				stage.close();

			}

		});

		ccView.searchButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// get user input criteria to search
				String title = ccView.titleTextField.getText().trim();
				String type = ccView.caseTypeTextField.getText().trim();
				String year = ccView.yearComboBox.getSelectionModel().getSelectedItem().trim();
				String number = ccView.caseNumberTextField.getText().trim();
				
				// call searchCases to find matching cases based on user input
				List<Case> matchingCases;
					matchingCases = ccModel.searchCases(title, type, year, number);
				
				// display matching cases on tableview
				ccView.caseTableView.setItems(FXCollections.observableArrayList(matchingCases));
				
				// highlight first case and update message label
				ccView.caseTableView.getSelectionModel().select(0);
				ccView.messageLabel.setText(matchingCases.size()+" cases.");
			}
		});


		ccView.clearButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) { 

				// clear all items in text fields
				ccView.titleTextField.clear();
				ccView.caseTypeTextField.clear();
				ccView.yearComboBox.getSelectionModel().clearSelection();
				ccView.caseNumberTextField.clear();
				ccView.messageLabel.setText("");

			}

		});
		
		ccView.caseCountChartMenuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				
				// open chart when menu item is selected
				ccView.showChartView(ccModel.yearMap);
				
			}
			
		});
		
		ccView.saveFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) { 
				
				// create new dialog box
				FileChooser fileChooser = new FileChooser();
			    fileChooser.setTitle("Save");
			    fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
			    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("TSV Files", "*.tsv"),
			    		new ExtensionFilter("CSV Files", "*.csv"),
			    		new ExtensionFilter("All Files", "*.*"));
			    
			    // get file path user inputted 
			    File file = fileChooser.showSaveDialog(stage);
			    String filename = file.getPath();
			    
			    // write cases and if successful then alert user 
			    boolean flag = ccModel.writeCases(filename);
			    if (flag) ccView.messageLabel.setText(file.getName() + " saved.");
				
			}
			
		});


		ccView.addCaseMenuItem.setOnAction(new CaseMenuItemHandler());
		ccView.modifyCaseMenuItem.setOnAction(new CaseMenuItemHandler());
		ccView.deleteCaseMenuItem.setOnAction(new CaseMenuItemHandler());

	}
	private class CaseMenuItemHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			MenuItem menuItem = (MenuItem) event.getSource(); 
			String choice = menuItem.getText(); 
			final Stage caseMenuStage; 
			switch(choice) {
			case "Add case": 
				caseView = new AddCaseView(choice);
				caseMenuStage = caseView.buildView();
				caseMenuStage.initOwner(stage);
				caseView.updateButton.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						// get user input from text fields 
						String title = caseView.titleTextField.getText();
						String date = caseView.caseDatePicker.getValue().toString();
						String type = caseView.caseTypeTextField.getText();
						String number = caseView.caseNumberTextField.getText();
						String category = caseView.categoryTextField.getText();
						String link = caseView.caseLinkTextField.getText();
						String caseNotes = caseView.caseNotesTextArea.getText();

						// check necessary properties are filled
						if (title.trim().equals("") || type.trim().equals("") || number.trim().equals("")) {
							new DataException("Case must have date, title, type and number");
						}
						
						// ensure no duplicate numbers
						for (Case c : ccModel.caseList) {
							number = number.replace(" ", "");
							number = number.replace("-", "");
							if (c.getCaseNumber().equals(number.trim())) {
								new DataException("Duplicate case number");
							}
						}
						
						// create new case and add to tableview
						Case c = new Case(date, title, type, number, link, category, caseNotes);
						ccModel.caseList.add(c);
						ccView.caseTableView.setItems(ccModel.caseList);

						// update text areas 
						ccView.caseNotesTextArea.setText(caseNotes);
						ccView.messageLabel.setText(ccView.caseTableView.getItems().size() + " cases.");



					}
				});
				break;

			case "Modify case": 
				caseView = new ModifyCaseView(choice);
				caseMenuStage = caseView.buildView();
				caseMenuStage.initOwner(stage);
				// get info from current case to put in stage text fields 
				caseView.titleTextField.setText(currentCase.getCaseTitle());
				caseView.caseTypeTextField.setText(currentCase.getCaseType());
				caseView.caseNumberTextField.setText(currentCase.getCaseNumber());
				caseView.categoryTextField.setText(currentCase.getCaseCategory());
				caseView.caseLinkTextField.setText(currentCase.getCaseLink());
				caseView.caseNotesTextArea.setText(currentCase.getCaseNotes());
				if(currentCase.getCaseDate()!=null)
                {
                    caseView.caseDatePicker.setValue(
                            LocalDate.of(Integer.parseInt(currentCase.getCaseDate().split("-")[0]),
                                    Integer.parseInt(currentCase.getCaseDate().split("-")[1]),
                                    Integer.parseInt(currentCase.getCaseDate().split("-")[2]
                    )));
                }
				caseView.updateButton.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {

						// allow current tableview to be edited
						ccView.caseTableView.setEditable(true);

						// get user input from text fields 
						String title = caseView.titleTextField.getText();
						String date = caseView.caseDatePicker.getValue().toString();
						String type = caseView.caseTypeTextField.getText();
						String number = caseView.caseNumberTextField.getText();
						
						if (title.trim().equals("") || type.trim().equals("") || number.trim().equals("")) {
							new DataException("Case must have date, title, type and number");
						}
						
						for (Case c : ccModel.caseList) {
							if (!number.equals(currentCase.getCaseNumber()) && number.equals(c.getCaseNumber())) {
								new DataException("Duplicate case number");
							}
						}
						currentCase.setCaseTitle(title);
						currentCase.setCaseDate(date);
						currentCase.setCaseType(type);
						currentCase.setCaseNumber(number);
						currentCase.setCaseCategory(caseView.categoryTextField.getText());
						currentCase.setCaseLink(caseView.caseLinkTextField.getText());
						currentCase.setCaseNotes(caseView.caseNotesTextArea.getText());
						
						

						// refresh tableview with updated user inputs
						ccView.caseTableView.refresh();
						ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());

					}

				});
				

				break; 

			case "Delete case":
				caseView = new DeleteCaseView(choice);
				caseMenuStage = caseView.buildView();
				caseMenuStage.initOwner(stage);
				// get current case info from tableview 
				caseView.titleTextField.setText(currentCase.getCaseTitle());
				// mv.caseDatePicker.setValue(currentCase.getCaseDate())
				caseView.caseTypeTextField.setText(currentCase.getCaseType());
				caseView.caseNumberTextField.setText(currentCase.getCaseNumber());
				caseView.categoryTextField.setText(currentCase.getCaseCategory());
				caseView.caseLinkTextField.setText(currentCase.getCaseLink());
				caseView.caseNotesTextArea.setText(currentCase.getCaseNotes());

				caseView.updateButton.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						// remove current case's index from table view and clear its case notes area
						ccView.caseNotesTextArea.clear();
						ccModel.caseList.remove(currentCase);
			            ccView.caseTableView.setItems(ccModel.caseList);
						ccView.messageLabel.setText(ccModel.caseList.size() + " cases.");


					}

				});
				break;

			default:
				return;

			}

			if (caseMenuStage!=null) caseMenuStage.show();


			caseView.clearButton.setOnAction(event1 -> {
				// clear all text fields
				caseView.titleTextField.clear();
				caseView.caseDatePicker.setValue(LocalDate.now());
				caseView.caseTypeTextField.clear();
				caseView.caseNumberTextField.clear();
				caseView.categoryTextField.clear();
				caseView.caseLinkTextField.clear();
				caseView.caseNotesTextArea.clear();
			});

			caseView.closeButton.setOnAction(event1 -> {
				// close stage
				caseMenuStage.close();
			});
		}
	}
}

















