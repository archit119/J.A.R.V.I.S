package myapp;

import javafx.application.Application;
import myapp.core.ClapListener;
import myapp.core.CommandDispatcher;
import myapp.core.ModelInitializer;
import myapp.core.Status;
import myapp.output.ConsoleWindow;

public class Main {
    
    public static void main(String[] args) throws InterruptedException
    {
        new Thread(() -> Application.launch(ConsoleWindow.class)).start();

        // wait until JavaFX finishes initializing the object
        while (ConsoleWindow.getInstance() == null) {
            Thread.sleep(50);
        }

        ConsoleWindow console = ConsoleWindow.getInstance();
        console.SetStatus(Status.LISTENING);

        CommandDispatcher commandDispatcher = new CommandDispatcher(console);

        ClapListener clapListener = new ClapListener(commandDispatcher);
        clapListener.ListenForClaps();
        
        ModelInitializer modelInitializer = new ModelInitializer();
        modelInitializer.InitalizeModel(commandDispatcher);
    }
} 
