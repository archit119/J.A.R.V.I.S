package myapp;

import java.util.ArrayList;

import javafx.application.Application;
import myapp.core.ClapListener;
import myapp.core.CommandDispatcher;
import myapp.core.ModelInitializer;
import myapp.output.ConsoleWindow;
import myapp.skills.OutputSkill;
import myapp.skills.Skill;
import myapp.skills.VolumeSkill;
import myapp.core.Status;
import myapp.system.OutputControl;
import myapp.system.ScreenControl;
import myapp.system.VolumeControl;

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

        OutputControl outputControl = new OutputControl();
        VolumeControl volumeControl = new VolumeControl();
        ScreenControl screenControl = new ScreenControl();

        // all skills here
        VolumeSkill volumeSkill = new VolumeSkill(volumeControl);
        OutputSkill outputSkill = new OutputSkill(outputControl);
        // the idea is to loop through all the skills in the list and call canHandle, and if that returns true call handle
        ArrayList<Skill> skills = new ArrayList<Skill>();
        skills.add(volumeSkill);
        skills.add(outputSkill);

        CommandDispatcher commandDispatcher = new CommandDispatcher(console, volumeControl, skills, screenControl);

        ClapListener clapListener = new ClapListener(commandDispatcher);
        clapListener.ListenForClaps();
        
        ModelInitializer modelInitializer = new ModelInitializer();
        modelInitializer.InitalizeModel(commandDispatcher);
    }
} 
