package myapp.skills;

import myapp.system.*;

public class OutputSkill implements Skill{

    OutputControl outputControl;

    public OutputSkill (OutputControl outputControl) {
        this.outputControl = outputControl;
    }


    @Override 
    public boolean canHandle(String input) {
        if(input.contains("change")) return true;
        return false;
    }

    @Override 
    public String handleInput(String input) {
        outputControl.ToggleOutputDevice();
        return "> Changing the output device";
    }
}
