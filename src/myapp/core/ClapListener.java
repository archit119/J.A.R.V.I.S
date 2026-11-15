package myapp.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

import myapp.windows.RunPython;

public class ClapListener{

    CommandDispatcher commandDispatcher;

    public ClapListener(CommandDispatcher commandDispatcher)
    {
        this.commandDispatcher = commandDispatcher;
    }

    RunPython runPython = new RunPython();
    Process process;

    public void ListenForClaps()
    {
        process = runPython.RunPythonScript("clap_detector.py");
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
                    else if(line.equals(null))
                    {
                        Logger logger
                            = Logger.getLogger(
                                ClapListener.class.getName());

                        // Set Logger level()
                        logger.setLevel(Level.WARNING);

                        // Call warning method
                        logger.warning("Clap detection stopped working");
                    }
                }

                catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
        });

        backgroundThread.start();
    }

    public void DestroyProcess()
    {
        System.out.println("BEFORE Process alive: " + process.isAlive());
        process.destroy();
        System.out.println("AFTER Process alive: " + process.isAlive());
    }
}
