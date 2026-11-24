package myapp.core;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.vosk.Model;
import org.vosk.Recognizer;

import com.google.gson.Gson;

import myapp.input.VoiceInput;
import myapp.input.TextInput;

public class ModelInitializer {

    boolean useVoice = true; // just for debugging purposes

    public void InitalizeModel(CommandDispatcher commandDispatcher)
    {
        VoiceInput voiceInput = new VoiceInput(commandDispatcher);
        TextInput textInput = new TextInput();

        float sampleRate = voiceInput.GetSampleRate();
        int sampleSizeInBits = voiceInput.GetSampleSizeInBits();
        int channels = voiceInput.GetNoOfChannels();
        
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
            "stop", "enough", "shut", "goodbye", "exit", "thats", "all",

            // phrases
            "change the output device", "switch to headphones", "switch to speakers", "set the volume to fifty", "wake up daddys home",

            // some debug statments
            "terminate",

            // unknown words / noise
            "[unk]"
        };
        String grammar = new Gson().toJson(keywords);   

        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        try (Model model = new Model("tools/vosk-model-en-us-0.22-lgraph");
            Recognizer recognizer = new Recognizer(model, 16000f, grammar);
            TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);)
        {
            microphone.open(format, 64000);
            microphone.start();
            // String input = textInput.GetInput();
            while(true)
            {
                String voice;

                if(useVoice)
                {
                    voice = voiceInput.GetInput(model, recognizer, microphone);
                }
                else
                {
                    voice = textInput.GetInput();
                }
                
                commandDispatcher.HandleInput(voice);
            }
        }
        catch (IOException | LineUnavailableException e)
        {
            System.err.println("Voice input failed: " + e.getMessage());
        }
    }
}