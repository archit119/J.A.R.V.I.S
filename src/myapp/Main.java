package myapp;

import myapp.core.ClapListener;
import myapp.core.CommandDispatcher;
import myapp.core.ModelInitializer;

public class Main {
    
    public static void main(String[] args)
    {
        CommandDispatcher commandDispatcher = new CommandDispatcher();

        ClapListener clapListener = new ClapListener(commandDispatcher);
        clapListener.ListenForClaps();
        
        ModelInitializer modelInitializer = new ModelInitializer();
        modelInitializer.InitalizeModel(commandDispatcher);
    }
} 
