package myapp.system;

import java.util.ArrayList;
import java.util.List;
import myapp.windows.ProcessRunner;

public class ScreenControl {

    ProcessRunner processRunner = new ProcessRunner();

    public void ScreenOff() 
    {
        List<String> commands = new ArrayList<String>();

        commands.add("tools/nircmd.exe");
        commands.add("monitor");
        commands.add("async_off");

        processRunner.BuildProcess(commands);
    }

    public void ScreenOn()
    {
        List<String> commands = new ArrayList<String>();

        commands.add("tools/nircmd.exe");
        commands.add("sendkeypress");
        commands.add("shift");
        commands.add("&");
        commands.add("tools/nircmd.exe");
        commands.add("monitor");
        commands.add("async_on");

        processRunner.BuildProcess(commands);
    }
    
}
