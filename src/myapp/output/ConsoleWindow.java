package myapp.output;

import java.util.List;

import javax.swing.GroupLayout.Alignment;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.text.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import myapp.core.Status;

public class ConsoleWindow extends Application {

   // define your offsets here
   private double xOffset = 0;
   private double yOffset = 0;

   private double resX = 640;
   private double resY = 360;

   private double textOffsetX = 180;
   private double textOffsetY = 180;

   String STANDBY = "Standing By";
   String LISTENING = "Listening";
   String ARMED = "Armed";

   private static ConsoleWindow instance;

   public static ConsoleWindow getInstance() {
      return instance;
   }

   @Override
   public void start(Stage primaryStage) throws Exception {
      TextArea text = InitializeText(resX);
      Text designText = InitializeDesign(resX);
      Text title = InitializeTitle(resX);
      Text status = InitalizeStatus(resX);
      // ImageView arc = InitializeImage();

      Font customFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/console.ttf"),
            18);

      if (customFont != null) {
         text.setFont(customFont);
         // designText.setFont(customFont);
         title.setFont(customFont);
      } else {
         text.setFont(Font.font("Consolas", FontPosture.REGULAR, 10));
         designText.setFont(Font.font("Consolas", FontWeight.BOLD, FontPosture.REGULAR, 3));
         title.setFont(Font.font("Consolas", FontPosture.REGULAR, 20));
         status.setFont(Font.font("Consolas", FontPosture.REGULAR, 15));
      }

      primaryStage = SetScene(primaryStage, text, title, designText, status);
      instance = this;
   }

   Stage SetScene(Stage primaryStage, TextArea text, Text title, Text designText, Text status) {
      primaryStage.initStyle(StageStyle.TRANSPARENT);

      Pane root = new Pane(text, title, designText, status);

      // radius makes the corners curved
      root.setStyle("-fx-background-color: #000000; -fx-padding: 10; -fx-text-fill: white; -fx-background-radius: 0");

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
      // scene.getStylesheets().add(getClass().getResource("/fontstyle.css").toExternalForm());
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

   TextArea t = new TextArea();
   Text dt = new Text();
   Text title = new Text();
   Text status = new Text();

   public Text InitializeTitle(double resX) {
      title.setText("JARVIS Ver. 1.0 \n");

      // title.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR,
      // 10));
      title.setFill(Color.WHITE);

      title.setWrappingWidth(resX);

      title.setTextAlignment(TextAlignment.LEFT);

      title.setX(20);
      title.setY(35);

      title.setLineSpacing(2);

      return title;
   }

   public TextArea InitializeText(double resX) {

      // 1. Position and Margins
      double xPos = 20;
      double yPos = 50;
      double rightMargin = 250;
      double bottomMargin = 20;

      t.setLayoutX(xPos);
      t.setLayoutY(yPos);

      // 2. Autosize to fit within resX and resY
      t.setPrefWidth(resX - xPos - rightMargin);
      t.setPrefHeight(resY - yPos - bottomMargin);

      // 3. Text Wrapping & Styling (Black text, 2px line spacing, inner top padding)
      t.setWrapText(true);
      t.setStyle(
            "-fx-text-fill: white;" +
                  "-fx-line-spacing: 2px;" +
                  "-fx-control-inner-background: black;" +
                  "-fx-padding: 0px 0px 0px 0px;");

      t.setEditable(false);
      t.setFocusTraversable(false);

      return t;
   }

   public void SetText(String text) {
      // if (t.getText().equals("JARVIS Ver. 1.0 \n")) {
      // t.setText("JARVIS Ver. 1.0 \n" + text);
      // } else {
      // t.setText(t.getText() + "\n" + text);
      // }
      Platform.runLater(() -> t.appendText(text + "\n"));
   }

   public Text InitializeDesign(double resX) {
      AsciiArt asciiArt = new AsciiArt();
      dt.setText(asciiArt.asciiArt());

      dt.setFill(Color.CYAN);

      dt.setWrappingWidth(resX);

      dt.setTextAlignment(TextAlignment.LEFT);

      dt.setX(450);
      dt.setY(150);

      dt.setLineSpacing(-0.4);

      return dt;
   }

   private static final double STATUS_X = 450;
   private static final String MARKER = "+ ";
   private static final String NO_MARKER = "  ";

   public Text InitalizeStatus(double resX) {
      status.setFill(Color.WHITE);
      status.setTextAlignment(TextAlignment.LEFT);
      status.setY(35);
      status.setLineSpacing(5);
      return status;
   }

   public void SetStatus(Status targetStatus) {
      Platform.runLater(() -> {
         applyStatus(targetStatus);
      });
   }

   public void applyStatus(Status active) {
      String text = (active == Status.STANDBY ? MARKER : NO_MARKER) + STANDBY + "\n" +
            (active == Status.LISTENING ? MARKER : NO_MARKER) + LISTENING + "\n" +
            (active == Status.ARMED ? MARKER : NO_MARKER) + ARMED;
      status.setText(text);

      Text measure = new Text(MARKER);
      measure.setFont(status.getFont());
      status.setX(STATUS_X - measure.getLayoutBounds().getWidth());
   }

   // public ImageView InitializeImage() {
   // Image arc = new Image("my/resources/arc.png", 100, 150, false, false);
   // ImageView imageView = new ImageView(arc);

   // // Setting the position of the image
   // imageView.setX(50);
   // imageView.setY(25);

   // // setting the fit height and width of the image view
   // imageView.setFitHeight(455);
   // imageView.setFitWidth(500);

   // // Setting the preserve ratio of the image view
   // imageView.setPreserveRatio(true);

   // return imageView;
   // }
}
