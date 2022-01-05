// Mira Kasari
// mkasari 
package hw3;


import java.time.LocalDate;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class AddCaseView extends CaseView {
	AddCaseView(String header) {
		super(header);
	}
	@Override
	Stage buildView() {
		updateButton.setText(stage.getTitle());
		Scene scene = new Scene(updateCaseGridPane, this.CASE_WIDTH, this.CASE_HEIGHT);
		caseDatePicker.setValue(LocalDate.now());
		stage.setScene(scene);
		return stage;
	}
	
	

	
}
