// Mira Kasari
// mkasari

package hw3;


import javafx.scene.Scene;
import javafx.stage.Stage;

public class DeleteCaseView extends CaseView {
	DeleteCaseView(String header) {
		super(header);
	}
	
	@Override
	Stage buildView() {
		updateButton.setText(stage.getTitle());
		Scene scene = new Scene(updateCaseGridPane, this.CASE_WIDTH, this.CASE_HEIGHT);
		stage.setScene(scene);
		return stage;
	}
	
}
