package maze;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class ControlPane {
	Stage stage = new Stage();
	BorderPane mainPane = new BorderPane();
	FlowPane pane = new FlowPane();
	public ControlPane(){
		stage.setTitle("…Ë÷√");
		stage.setScene(new Scene(mainPane,250,180));
		pane.setCache(false);
		pane.setPadding(new Insets(20,20,20,20));
		pane.setHgap(40);
		pane.setVgap(30);
		mainPane.getChildren().add(new ImageView(new Image("tupian/shezhibeijing1.jpg")));
		mainPane.setCenter(pane);
	}
	
	public void addNode(Node...node) {
		pane.getChildren().addAll(node);
	}
	
	public void show() {
		stage.show();
	}
	
	public void close() {
		stage.close();
	}
}
