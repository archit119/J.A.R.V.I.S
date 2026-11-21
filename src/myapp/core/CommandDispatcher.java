package myapp.core;

import myapp.skills.VolumeControl;

import java.io.Console;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import myapp.skills.OutputControl;

import myapp.output.ConsoleWindow;

public class CommandDispatcher {

    ConsoleWindow consoleWindow = null;
    VolumeControl volumeControl;
    OutputControl outputControl;

    public CommandDispatcher (ConsoleWindow ConsoleWindow, VolumeControl volumeControl, OutputControl outputControl) {
        consoleWindow = ConsoleWindow;
        this.volumeControl = volumeControl;
        this.outputControl = outputControl; 
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
        consoleWindow.SetText(cleanInput);
        if(standby)
        {
            // DEBUG
            boolean armed = System.nanoTime()-start < TimeUnit.SECONDS.toNanos(timeToWait);
            System.out.println("Clap heard: " + clapHeard);
            System.out.println("Armed status: " + armed);

            // do the check
            if(cleanInput.contains(wakeword) && armed && clapHeard)
            {
                System.out.println("Waking up");
                consoleWindow.SetText("> Waking up");
                standby = false;
                clapHeard = false;
                SetJarvisStatus(Status.LISTENING);
            }
            // else if(cleanInput.contains("wake up")) {
            //     System.out.println("Waking up");
            //     consoleWindow.SetText("> Waking up");
            //     standby = false;
            //     clapHeard = false;
            //     SetJarvisStatus(Status.LISTENING);
            // }
            commandHandeled = true;
        }
        else
        {
            // volume control
            if(cleanInput.contains("volume"))
            {
                VolumeControlHandle(input, volumeControl);
                commandHandeled = true;
            }
            // output control
            else if(cleanInput.contains("change"))
            {
                if(cleanInput.contains("speakers"))
                {
                    outputControl.ChangeOutput("Speakers");
                }
                else if(cleanInput.contains("headphones"))
                {
                    outputControl.ChangeOutput("Headphones");
                }
                else
                {
                    // toggle feature maybe
                    outputControl.ToggleOutputDevice();
                    consoleWindow.SetText("> Changing the output device");
                }
                commandHandeled = true;
            }
            else if(cleanInput.contains("enough"))
            {
                // tell the script to stop somehow?
                standby = true;
                SetJarvisStatus(Status.STANDBY);
                System.out.println("Stading by");
                consoleWindow.SetText("> Standing by");
                commandHandeled = true;
            } 

            if(commandMode && commandHandeled) exitCommandMode();
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

    void VolumeControlHandle(String rawInput, VolumeControl volumeControl)
    {
        int finalNumber = numberParser.NumberConverter(rawInput);
        System.out.println(finalNumber);

        // limit the final number
        if(finalNumber > 100)
        {
            finalNumber = 100;
        }

        if(finalNumber != -1) {
            volumeControl.SetVolume(finalNumber);
            volumeChange = true;
        }
        else 
        {
            // dont do anything
            // here i would probably somehow ask it to speakback saying i didnt get the volume
            System.out.println("I didn't quite get that, what do you want the volume set too?");
        }
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
