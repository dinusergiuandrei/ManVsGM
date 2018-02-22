package MoveGenerator.UI;

import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class AlertBox {

    public static void display(String title, String message){
           Stage window = new Stage();

           window.initModality(Modality.APPLICATION_MODAL);
           window.setTitle(title);
           window.setMinWidth(250);

           Label label = new Label();
           label.setText(message);
           Button closeButton  = new Button("OK");
           closeButton.setOnAction(event -> window.close());

           VBox layout = new VBox(10);
           layout.getChildren().addAll(label, closeButton);
           layout.setAlignment(Pos.CENTER);

           Scene scene = new Scene(layout);
           window.setScene(scene);
           window.showAndWait();
    }
}
