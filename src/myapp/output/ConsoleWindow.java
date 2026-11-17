package myapp.output;

import javax.swing.GroupLayout.Alignment;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ConsoleWindow extends Application {

   //define your offsets here
   private double xOffset = 0;
   private double yOffset = 0;

   private double resX = 640;
   private double resY = 360;

   private double textOffsetX = 180;
   private double textOffsetY = 180;

   private static ConsoleWindow instance;

   public static ConsoleWindow getInstance() {
      return instance;
   }

   @Override
   public void init() {
      instance = this;
   }
   
   @Override
   public void start(Stage primaryStage) throws Exception 
   {
      Text text = InitializeText(resX);
      primaryStage = SetScene(primaryStage, text);
   }

   Stage SetScene(Stage primaryStage, Text text)
   {
      primaryStage.initStyle(StageStyle.TRANSPARENT);

      Pane root = new Pane(text);
      
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

      Scene scene = new Scene(root, resX, resY);
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

   Text t = new Text();

    public Text InitializeText(double resX) {
        t.setText("JARVIS Ver. 1.0 \n");

        t.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 10));
        t.setFill(Color.WHITE);
        
        t.setWrappingWidth(resX);

        t.setTextAlignment(TextAlignment.LEFT);

        t.setX(20);
        t.setY(30);

        t.setLineSpacing(2);
        
        return t;
    }

    public void SetText(String text) {
      if(t.getText().equals("JARVIS Ver. 1.0 \n")) {
         t.setText("JARVIS Ver. 1.0 \n" + text);
      }
      else {
         t.setText(t.getText() + "\n" + text);
      }
    }

}
