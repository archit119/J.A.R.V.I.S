package myapp.output;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ConsoleWindow extends Application {

    //define your offsets here
    private double xOffset = 0;
    private double yOffset = 0;

    private double resX = 160;
    private double resY = 90;
    
    @Override
    public void start(Stage primaryStage) throws Exception 
    {
        primaryStage = SetScene(primaryStage);
    }

    Stage SetScene(Stage primaryStage)
    {
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        BorderPane root = new BorderPane();
        
        root.setStyle("-fx-background-color: #000000; -fx-padding: 10; -fx-text-fill: white; -fx-background-radius: 15");

        // capture initial click position relative to the scene
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        // move the stage based on screen coordinates minus the initial offset
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        Scene scene = new Scene(root, resX*4, resY*4);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);

        double bottomRightX = Screen.getPrimary().getVisualBounds().getMaxX();
        double bottomRightY = Screen.getPrimary().getVisualBounds().getMaxY();

        double offSetX = 25;
        double offSetY = 50;

        primaryStage.show();

        // set the offset 
        primaryStage.setX(bottomRightX - primaryStage.getWidth() - offSetX);
        primaryStage.setY(bottomRightY - primaryStage.getHeight() - offSetY);

        primaryStage.setAlwaysOnTop(true);

        return primaryStage;
    }

}
