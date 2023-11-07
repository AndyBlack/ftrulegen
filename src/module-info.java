module RuleGen {
	exports org.sil.ftrulegen.view;

	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.web;
	
	opens org.sil.ftrulegen to javafx.graphics, javafx.fxml;
	opens org.sil.ftrulegen.view to javafx.fxml;
}
