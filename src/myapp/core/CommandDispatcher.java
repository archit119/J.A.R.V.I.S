package myapp.core;

import java.io.Console;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.*;

import javafx.stage.Screen;
import myapp.output.ConsoleWindow;
import myapp.system.OutputControl;
import myapp.system.ScreenControl;
import myapp.system.VolumeControl;
import myapp.skills.*;

public class CommandDispatcher {

    ConsoleWindow consoleWindow = null;
    VolumeControl volumeControl;
    OutputControl outputControl;
    ScreenControl screenControl;

    ArrayList<Skill> skills = new ArrayList<Skill>();

    public CommandDispatcher (ConsoleWindow ConsoleWindow, ArrayList<Skill> skills, ScreenControl screenControl) {
        consoleWindow = ConsoleWindow;
        this.skills = skills;
        this.screenControl = screenControl;
    }

    NumberParser numberParser = new NumberParser();

    volatile boolean standby = false;
    volatile boolean clapHeard = false;

    double lastTimeArmed;
    boolean commandMode;
    boolean volumeChange = false;
    float commandModeTime = 5;

    long start;
    long timeToWait = 5;

    String wakeword = "wake up daddys home";

    Status currenStatus;

    public void HandleInput(String input)
    {
        boolean commandHandeled = false;
        String cleanInput = input.toLowerCase();
        String output = "";

        boolean armed = System.nanoTime()-start < TimeUnit.SECONDS.toNanos(timeToWait);

        if(cleanInput.contains(wakeword) && armed && clapHeard && !standby)
        {
            screenControl.ScreenOn();
        } 
        
        if(standby)
        {
            // DEBUG
            System.out.println("Clap heard: " + clapHeard);
            System.out.println("Armed status: " + armed);

            // do the check
            if(cleanInput.contains(wakeword) && armed && clapHeard)
            {
                System.out.println("Waking up");
                consoleWindow.SetText("> Waking up");
                standby = false;
                clapHeard = false;
                screenControl.ScreenOn();
                SetJarvisStatus(Status.LISTENING);
                commandHandeled = true;
            } 
            // else if(cleanInput.contains("wake up")) {
            //     System.out.println("Waking up");
            //     consoleWindow.SetText("> Waking up");
            //     standby = false;
            //     clapHeard = false;
            //     SetJarvisStatus(Status.LISTENING);
            // }
        }
        else
        {
            for (Skill skill : skills) {
                if(skill.canHandle(cleanInput)) {
                    output = skill.handleInput(cleanInput);
                    commandHandeled = true;
                }
            }

            if(cleanInput.contains("enough")) {
                standby = true;
                SetJarvisStatus(Status.STANDBY);
                System.out.println("Stading by");
                output = "> Standing by";
                commandHandeled = true;
            }

            if(commandMode && commandHandeled) exitCommandMode();
        }

        if(cleanInput.equals("terminate jarvis")) {
            System.exit(0); 
        }

        if(commandHandeled) {
            consoleWindow.SetText(cleanInput);
            consoleWindow.SetText(output);
        }
  
    }

    void StateHandler(Status targetStatus) {
        switch (targetStatus) {
            case Status.STANDBY:
                consoleWindow.SetStatus(Status.STANDBY);
                break;
            case Status.LISTENING:
                consoleWindow.SetStatus(Status.LISTENING);
                break;
            case Status.ARMED:
                consoleWindow.SetStatus(Status.ARMED);
                break;
        }
    }

    public void tick() {
        double currentTime = System.currentTimeMillis();
        if(commandMode && ((currentTime - lastTimeArmed) > commandModeTime * 1000)) {
            exitCommandMode();
        }
    }

    Integer currentVolume = 0;

    void enterCommandMode() {
        lastTimeArmed = System.currentTimeMillis();
        commandMode = true;
        try {
            currentVolume = volumeControl.GetCurrentVolume();
        } 
        catch (InterruptedException e) {
            System.out.print(e);
        }
        volumeControl.SetVolume(currentVolume / 2);
        SetJarvisStatus(Status.ARMED);
    }

    void exitCommandMode() {
        commandMode = false;
        SetJarvisStatus(standby ? Status.STANDBY : Status.LISTENING);
        lastInput = "";
        if(!volumeChange) {
            volumeControl.SetVolume(currentVolume); 
        }
        volumeChange = false;
    }

    public void ClapHeard()
    {
        start = System.nanoTime();
        clapHeard = true;
        // System.out.println("Setting clap heard to: " + value);
    }

    public boolean ShouldStandby()
    {
        return standby;
    }

    public void SetJarvisStatus(Status targetStatus) {
        currenStatus = targetStatus;
        StateHandler(currenStatus);
    }

    String lastInput = "";

    public void PartialHeard(String input) {
        String cleanInput = input.toLowerCase();
        if(cleanInput.equals(lastInput)) {
            return;
        }

        if(cleanInput.contains("jarvis") && !standby && !commandMode) {
            enterCommandMode();
        }

        lastInput = cleanInput;
    }
}
