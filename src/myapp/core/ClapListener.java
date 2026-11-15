package myapp.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import myapp.windows.RunPython;

public class ClapListener extends Thread{

    CommandDispatcher commandDispatcher;

    public ClapListener(CommandDispatcher commandDispatcher)
    {
        this.commandDispatcher = commandDispatcher;
    }

    RunPython runPython = new RunPython();
    public void ListenForClaps()
    {
        Process process = runPython.RunPythonScript("clap_detector.py");
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));

        // start a new thread
        Thread backgroundThread = new Thread(() -> {
            System.out.println("Started python script");

            // in this thread constantly listen for clapdetector.py
            while(!Thread.currentThread().isInterrupted())
            {
                try{
                    String line = reader.readLine();

                    if(line.equals("CLAP"))
                    {
                        commandDispatcher.ClapHeard();
                        System.out.println("Heard double clap");
                    }
                }

                catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
        });

        backgroundThread.start();
    }
}
