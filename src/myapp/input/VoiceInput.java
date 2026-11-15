package myapp.input;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import javax.swing.JOptionPane;

import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchProcessor;

public class VoiceInput {

    boolean isRecording = false;

    float sampleRate = 16000f;
    int bufferSize = 4096;
    int sampleSizeInBits = 16;
    int channels = 1;

    public String GetInput(Model model, Recognizer recognizer)
    {
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        try (TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info)){
                
                microphone.open(format);
                microphone.start();
                System.out.println("Listening...");

                byte[] buffer = new byte[bufferSize];
                while (true) {
                    

                    int n = microphone.read(buffer, 0, buffer.length);

                    if (n > 0 && recognizer.acceptWaveForm(buffer, n)) {
                        JsonObject json = JsonParser.parseString(recognizer.getResult()).getAsJsonObject();
                        String text = json.get("text").getAsString().replace("[unk]", "").trim();

                        

                        System.out.println(text);

                        if (!text.isEmpty()) {
                            return text;
                        }
                    }
                }
            } 
        
        catch (LineUnavailableException e)
        {
            System.err.println("Voice input failed: " + e.getMessage());
            return "";
        }
    }

    public boolean StopInputs()
    {
        return true;
    }

    public float GetSampleRate() {return sampleRate;}
    public int GetSampleSizeInBits() {return sampleSizeInBits;}
    public int GetNoOfChannels() {return channels;}
    public int GetBufferSize() {return bufferSize;}
}   
