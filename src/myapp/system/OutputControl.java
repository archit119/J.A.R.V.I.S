package myapp.system;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.Strictness;
import com.google.gson.stream.JsonReader;

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

    // TODO: only supports toggling between 2 output devices.
    // With 3+ devices, this picks the first two. Could become "switch to <name>" or cycling.
    public void ToggleOutputDevice()
    {
        GetOutputDevices();

        ArrayList<String> outputDevices = new ArrayList<>();

        try {
            Reader reader = new FileReader("devices.json");
            JsonArray devices = JsonParser.parseReader(reader).getAsJsonArray();

            // now loop through the json objects
            for (JsonElement deviceElement : devices) {
                JsonObject device = deviceElement.getAsJsonObject();

                String name = device.get("Command-Line Friendly ID").getAsString();
                String type = device.get("Type").getAsString();
                String direction = device.get("Direction").getAsString();

                if(type.equals("Device") && direction.equals("Render")) {
                    outputDevices.add(name);
                }
            }

            System.out.println(outputDevices.toString()); // debug statment

        } 
        catch (JsonSyntaxException | FileNotFoundException e) {
            System.out.println(e);
        }

        // only switch output devices if you actually have 2 output devices

        if(outputDevices.size() < 2) return;
        List<String> commands = new ArrayList<String>();

        commands.add("tools/SoundVolumeView.exe");
        commands.add("/SwitchDefault");
        commands.add(outputDevices.get(0));
        commands.add(outputDevices.get(1));

        processRunner.BuildProcess(commands);
    }

    public void GetOutputDevices() {
        // SoundVolumeView.exe /stext devices.txt /Columns "Name,Type,Device Name,Command-Line Friendly ID"
        
        List<String> commands = new ArrayList<String>();

        commands.add("tools/SoundVolumeView.exe");
        commands.add("/SaveFileEncoding");
        commands.add("3"); // make it uft 8 so the code doesent break
        commands.add("/sjson");
        commands.add("devices.json");
        commands.add("/Columns");
        commands.add("Name, Type, Direction, Device Name, Command-Line Friendly ID");

        Process process = processRunner.BuildProcess(commands);
        try {
            process.waitFor();
        }
        catch (InterruptedException e) {
            System.out.println(e);
        }
    }
} 