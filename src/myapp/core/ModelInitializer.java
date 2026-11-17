package myapp.core;

import java.io.IOException;

import org.vosk.Model;
import org.vosk.Recognizer;

import com.google.gson.Gson;

import myapp.input.VoiceInput;
import myapp.skills.OutputControl;
import myapp.skills.VolumeControl;
import myapp.input.TextInput;

public class ModelInitializer {

    boolean useVoice = false; // just for debugging purposes

    public void InitalizeModel(CommandDispatcher commandDispatcher)
    {
        VoiceInput voiceInput = new VoiceInput();
        TextInput textInput = new TextInput();
        OutputControl outputControl = new OutputControl();
        VolumeControl volumeControl = new VolumeControl();
    
        String[] keywords = {
            // wake word
            "jarvis",

            // general command words
            "set", "change", "switch", "toggle", "turn", "make", "put",
            "to", "the", "my", "it", "up", "down", "on", "off", "please",

            // volume
            "volume", "sound", "louder", "quieter", "mute", "unmute", "percent", "max", "maximum", "minimum",

            // numbers
            "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
            "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen",
            "sixteen", "seventeen", "eighteen", "nineteen",
            "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety", "hundred",

            // output device
            "output", "device", "audio", "speakers", "speaker", "headphones", "headset",

            // stopping
            "stop", "enough", "shut", "goodbye", "exit", "that's", "all",

            // phrases
            "change the output device", "switch to headphones", "switch to speakers", "set the volume to fifty", "wake up daddys home",

            // unknown words / noise
            "[unk]"
        };
        String grammar = new Gson().toJson(keywords);   

        try (Model model = new Model("tools/vosk-model-en-us-0.22-lgraph");
            Recognizer recognizer = new Recognizer(model, 16000f, grammar))
        {
            // String input = textInput.GetInput();
            while(true)
            {
                String voice;
                if(useVoice)
                {
                    voice = voiceInput.GetInput(model, recognizer);
                }
                else
                {
                    voice = textInput.GetInput();
                }
                commandDispatcher.HandleInput(volumeControl, outputControl, voice);
            }
        }
        catch (IOException e)
        {
            System.err.println("Voice input failed: " + e.getMessage());
        }
    }
}
