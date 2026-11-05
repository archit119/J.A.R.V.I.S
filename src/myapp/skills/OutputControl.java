package myapp.skills;

import java.util.ArrayList;
import java.util.List;
import myapp.windows.ProcessRunner;

public class OutputControl {

    ProcessRunner processRunner = new ProcessRunner();
    
    public void ChangeOutput(String outputDevice)
    {
        List<String> commands = new ArrayList<String>();

        commands.add("tools/nircmd.exe");
        commands.add("setdefaultsounddevice");
        commands.add(outputDevice);

        processRunner.BuildProcess(commands);
    }

    public void ToggleOutputDevice()
    {
        List<String> commands = new ArrayList<String>();

        commands.add("tools/SoundVolumeView.exe");
        commands.add("/SwitchDefault");
        commands.add("Realtek(R) Audio\\Device\\Headphones\\Render");
        commands.add("Realtek(R) Audio\\Device\\Speakers\\Render");

        processRunner.BuildProcess(commands);
    }
} 