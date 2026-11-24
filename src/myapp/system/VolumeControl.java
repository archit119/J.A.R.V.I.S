package myapp.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import myapp.windows.ProcessRunner;

public class VolumeControl {

    ProcessRunner processRunner = new ProcessRunner();

    public void SetVolume(Integer volume)
    {
        List<String> commands = new ArrayList<String>();

        double nirVolume = volume.doubleValue();
        nirVolume = nirVolume * 655.35;

        System.out.println(nirVolume);
        long finalNirVolume = Math.round(nirVolume);

        commands.add("tools/nircmd.exe");
        commands.add("setsysvolume");
        commands.add(String.valueOf(finalNirVolume));

        processRunner.BuildProcess(commands);
    }

    public Integer GetCurrentVolume() throws InterruptedException{
        List<String> commands = new ArrayList<String>();

        commands.add("tools/SoundVolumeView.exe");
        commands.add("/GetPercent");
        commands.add("DefaultRenderDevice");

        Process process = processRunner.BuildProcess(commands);
        int exitCode = process.waitFor();

        return exitCode / 10;
    }
}
