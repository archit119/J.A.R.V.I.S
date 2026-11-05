package myapp.windows;

import java.io.IOException;
import java.util.List;

public class ProcessRunner {

    public void BuildProcess(List<String> commands)
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
