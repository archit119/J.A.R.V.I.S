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
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import javax.swing.JOptionPane;

import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import com.google.gson.Gson;

public class VoiceInput {

    public void Record()
    {
        try{
            AudioFormat audioFormat = new AudioFormat(16000f, 16, 1, true, false);

            DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            if(!AudioSystem.isLineSupported(dataInfo))
            {
                System.out.println("Not supported");
            }

            TargetDataLine targetLine = (TargetDataLine)AudioSystem.getLine(dataInfo);
            targetLine.open(); // get ready to start

            // this is just a place holder to start the recording
            // gotta replace it with audio is always on but only record when i actually start speaking
            JOptionPane.showMessageDialog(null, "Hit ok to start recording"); 

            targetLine.start(); // this starts the recording

            Thread audioRecorderThread = new Thread(){
                @Override public void run()
                {
                    AudioInputStream recordingStream = new AudioInputStream(targetLine);
                    File outputFile = new File("tempRecordings/record.wav");
                    try
                    {
                        AudioSystem.write(recordingStream, AudioFileFormat.Type.WAVE, outputFile);
                    }
                    catch (IOException ex)
                    {
                        System.out.println(ex);
                    }

                    System.out.println("Stopped recording");
                }
            };

            audioRecorderThread.start();
            JOptionPane.showMessageDialog(null, "Hit ok to stop recording");
            targetLine.stop();
            targetLine.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    String ConvertToText() throws IOException, UnsupportedAudioFileException 
    {
        String rawResult;
        String finalResult;
        LibVosk.setLogLevel(LogLevel.DEBUG);

        String[] keywordList = {"jarvis", "change", "output", "device", "volume", "the"};

        Gson gson = new Gson();
        String jsonKeywords = gson.toJson(keywordList);

        try (Model model = new Model("tools/vosk-model-small-en-us-0.15");

        InputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream("tempRecordings/record.wav")));
        Recognizer recognizer = new Recognizer(model, 16000, jsonKeywords)) {

            int nbytes;
            byte[] b = new byte[4096];
            while ((nbytes = ais.read(b)) >= 0) {
                if (recognizer.acceptWaveForm(b, nbytes)) {
                    recognizer.getResult();
                } else {
                    recognizer.getPartialResult();
                }
            }

            rawResult = recognizer.getFinalResult();
        }

        java.util.regex.Matcher m = java.util.regex.Pattern.compile("\"text\"\\s*:\\s*\"([^\"]*)\"").matcher(rawResult);

        finalResult = m.find() ? m.group(1) : "";
        System.out.println(finalResult);
        return finalResult;
    }

    public String ConvertText()
    {
        try{
            return ConvertToText();
        }
        catch (IOException | UnsupportedAudioFileException e)
        {
            System.err.println("Failed to read the file: " + e.getMessage());
            return "";
        }
    }
}   
