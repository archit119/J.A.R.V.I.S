package myapp;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import myapp.core.CommandDispatcher;
import myapp.skills.OutputControl;
import myapp.input.TextInput;
import myapp.input.VoiceInput;

public class Main {
    
    public static void main(String[] args){
        // int targetVolume = 75;

        // VolumeControl volumeControl = new VolumeControl();
        // int result = volumeControl.SetVolume(targetVolume);
        // System.out.print(result);

        OutputControl outputControl = new OutputControl();
        // outputControl.ChangeOutput("Headphones");

        // outputControl.GetOutputDevice();

        VoiceInput voiceInput = new VoiceInput();
        TextInput textInput = new TextInput();
        CommandDispatcher commandDispatcher = new CommandDispatcher();

        // String input = textInput.GetInput();
        voiceInput.Record();
        String voice = voiceInput.ConvertText();

        commandDispatcher.HandleInput(voice);
    }
}
