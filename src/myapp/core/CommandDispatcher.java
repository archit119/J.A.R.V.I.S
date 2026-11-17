package myapp.core;

import myapp.skills.VolumeControl;

import java.io.Console;
import java.util.concurrent.TimeUnit;

import myapp.skills.OutputControl;

import myapp.output.ConsoleWindow;
import myapp.output.ConsoleWriter;

public class CommandDispatcher {

    ConsoleWindow consoleWindow = null;

    public CommandDispatcher (ConsoleWindow ConsoleWindow) {
        consoleWindow = ConsoleWindow;
    }

    NumberParser numberParser = new NumberParser();

    volatile boolean standby = false;
    volatile boolean clapHeard = false;

    long start;
    long timeToWait = 5;

    String wakeword = "wake up daddys home";

    public void HandleInput(VolumeControl volumeControl, OutputControl outputControl, String input)
    {
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
            }
            else if(cleanInput.contains("wake up")) {
                System.out.println("Waking up");
                consoleWindow.SetText("> Waking up");
                standby = false;
                clapHeard = false;
            }
        }
        else
        {
            // volume control
            if(cleanInput.contains("volume"))
            {
                VolumeControlHandle(input, volumeControl);
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
            }
            else if(cleanInput.contains("enough"))
            {
                // tell the script to stop somehow?
                standby = true;
                System.out.println("Stading by");
                consoleWindow.SetText("> Standing by");
            } 
        }
  
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

    void VolumeControlHandle(String rawInput, VolumeControl volumeControl)
    {
        int finalNumber = numberParser.NumberConverter(rawInput);
        System.out.println(finalNumber);

        // limit the final number
        if(finalNumber > 100)
        {
            finalNumber = 100;
        }

        if(finalNumber != -1) volumeControl.SetVolume(finalNumber);
        else 
        {
            // dont do anything
            // here i would probably somehow ask it to speakback saying i didnt get the volume
            System.out.println("I didn't quite get that, what do you want the volume set too?");
        }
    }
}
