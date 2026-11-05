package myapp.core;

import myapp.skills.VolumeControl;
import myapp.skills.OutputControl;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class CommandDispatcher {
    public void HandleInput(String input)
    {
        VolumeControl volumeControl = new VolumeControl();
        OutputControl outputControl = new OutputControl();

        // volume control
        if(input.toLowerCase().contains("volume"))
        {
            VolumeControlHandle(input, volumeControl);
        }
        // output control
        else if(input.toLowerCase().contains("change"))
        {
            if(input.toLowerCase().contains("speakers"))
            {
                outputControl.ChangeOutput("Speakers");
            }
            else if(input.toLowerCase().contains("headphones"))
            {
                outputControl.ChangeOutput("Headphones");
            }
            else
            {
                // toggle feature maybe
                outputControl.ToggleOutputDevice();
            }
        }
    }

    void VolumeControlHandle(String input, VolumeControl volumeControl)
    {
        List<Integer> finalNumbers = new ArrayList<Integer>();
        int finalNumber = 0;

        // text contains volume
        // now time to find the number
        for(int i = 0; i < input.length(); i++)
        {
            String[] numbers = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};


            int pos = Arrays.asList(numbers).indexOf(String.valueOf(input.charAt(i)));

            if(pos == -1)
            {
                continue; // go to the next iteration
            }

            // System.out.println(pos);

            finalNumbers.add(pos); // add the value
        }

        // System.out.println(finalNumbers);

        // now to make the array an actual number
        int j = 0;

        for(int i = finalNumbers.size() - 1; i >= 0; i--)
        {
            finalNumber += (finalNumbers.get(j) * Math.pow(10, i));
            j++;
        }

        System.out.println(finalNumber);

        // limit the final number
        if(finalNumber > 100)
        {
            finalNumber = 100;
        }

        volumeControl.SetVolume(finalNumber);
    }
}
