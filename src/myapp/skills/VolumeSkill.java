package myapp.skills;

import myapp.core.NumberParser;
import myapp.system.VolumeControl;

public class VolumeSkill implements Skill{

    VolumeControl volumeControl;

    public VolumeSkill (VolumeControl volumeControl) {
        this.volumeControl = volumeControl;
    }

    String output;
    NumberParser numberParser = new NumberParser();

    @Override 
    public boolean canHandle(String input) {
        if(input.contains("volume") || input.contains("set the one")) return true;
        return false;
    }

    @Override 
    public String handleInput(String input) {
        if(input.contains("set the one")) input = input.replaceFirst("one", "");
        return VolumeControlHandle(input, volumeControl);
    }

    String VolumeControlHandle(String rawInput, VolumeControl volumeControl)
    {
        int finalNumber = numberParser.NumberConverter(rawInput);
        String output = "";
        System.out.println(finalNumber);

        // limit the final number
        if(finalNumber > 100)
        {
            finalNumber = 100;
        }

        if(finalNumber != -1) {
            volumeControl.SetVolume(finalNumber);
            output = "> Set the volume to " + finalNumber;
        }
        else 
        {
            // dont do anything
            // here i would probably somehow ask it to speakback saying i didnt get the volume
            System.out.println("I didn't quite get that, what do you want the volume set too?");
            output = "> I didn't quite get that, what do you want the volume set too?"; 
        }

        return output;
    }
}
