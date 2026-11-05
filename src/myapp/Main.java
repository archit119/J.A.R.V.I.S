package myapp;

import myapp.core.CommandDispatcher;
import myapp.skills.OutputControl;
import myapp.input.TextInput;

public class Main {
    
    public static void main(String[] args){
        // int targetVolume = 75;

        // VolumeControl volumeControl = new VolumeControl();
        // int result = volumeControl.SetVolume(targetVolume);
        // System.out.print(result);

        OutputControl outputControl = new OutputControl();
        // outputControl.ChangeOutput("Headphones");

        // outputControl.GetOutputDevice();

        TextInput textInput = new TextInput();
        CommandDispatcher commandDispatcher = new CommandDispatcher();

        String input = textInput.GetInput();
        commandDispatcher.HandleInput(input);
    }
}
