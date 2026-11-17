package myapp.output;

import java.io.IOException;

import javafx.application.Application;
import myapp.core.ClapListener;

public class JavaFXLauncher {
    public static void main(String[] args) {
        Application.launch(ConsoleWindow.class, args);
    }

    public void runConsole() {
        Thread backgroundThread = new Thread(() -> {
            Application.launch(ConsoleWindow.class);
        }); 
        backgroundThread.start();            
    }
}
