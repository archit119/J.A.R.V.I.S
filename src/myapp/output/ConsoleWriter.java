package myapp.output;

import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class ConsoleWriter {

    Text t = new Text();

    public Text InitializeText(double resX) {
        t.setText("> ");

        t.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 10));
        t.setFill(Color.WHITE);
        
        t.setWrappingWidth(resX);

        t.setTextAlignment(TextAlignment.LEFT);

        t.setX(20);
        t.setY(30);
        
        return t;
    }

    public void SetText(String text) {
        t.setText(t.getText() + "\n" + ">" + text);
    }
    
}
