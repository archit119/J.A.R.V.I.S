package myapp.skills;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VolumeControl {
    
    public void SetVolume(Integer volume)
    {
        List<String> commands = new ArrayList<String>();

        double nirVolume = volume.doubleValue();
        nirVolume = nirVolume * 655.35;

        System.out.println(nirVolume);

        commands.add("tools/nircmd.exe");
        commands.add("setsysvolume");
        commands.add(String.valueOf(nirVolume));

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
