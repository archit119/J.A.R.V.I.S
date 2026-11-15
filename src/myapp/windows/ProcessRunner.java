package myapp.windows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ProcessRunner {

    public Process BuildProcess(List<String> commands)
    {
        ProcessBuilder pb = new ProcessBuilder(commands);
        Process process = null;

        try {
            pb.redirectErrorStream(true); 

            process = pb.start(); 
            System.out.println("Process started successfully.");
        } 
        catch (IOException e) {
            System.err.println("Failed to start the process: " + e.getMessage());
            e.printStackTrace();
        }
        

        return process;
    }
}
