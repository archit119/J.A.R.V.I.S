package myapp.skills;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutputControl {
    
    public void ChangeOutput(String outputDevice)
    {
        List<String> commands = new ArrayList<String>();

        commands.add("tools/nircmd.exe");
        commands.add("setdefaultsounddevice");
        commands.add(outputDevice);

        BuildProcess(commands);
    }

    public void ToggleOutputDevice()
    {
        List<String> commands = new ArrayList<String>();

        commands.add("tools/SoundVolumeView.exe");
        commands.add("/SwitchDefault");
        commands.add("Realtek(R) Audio\\Device\\Headphones\\Render");
        commands.add("Realtek(R) Audio\\Device\\Speakers\\Render");

        BuildProcess(commands);
    }

    void BuildProcess(List<String> commands)
    {
        ProcessBuilder pb = new ProcessBuilder(commands);

        try {
            pb.start(); 
            System.out.println("Process started successfully.");
        } 
        catch (IOException e) {
            System.err.println("Failed to start the process: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 