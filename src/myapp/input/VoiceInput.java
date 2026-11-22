package myapp.input;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.vosk.Model;
import org.vosk.Recognizer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import myapp.core.CommandDispatcher;

public class VoiceInput {

    CommandDispatcher commandDispatcher;

    public VoiceInput (CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    boolean isRecording = false;

    float sampleRate = 16000f;
    int bufferSize = 4096;
    int sampleSizeInBits = 16;
    int channels = 1;

    public String GetInput(Model model, Recognizer recognizer) {
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        try (TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info)) {

            microphone.open(format);
            microphone.start();
            System.out.println("Listening...");

            byte[] buffer = new byte[bufferSize];
            while (true) {

                int n = microphone.read(buffer, 0, buffer.length);
                commandDispatcher.tick();
                if (n > 0) {
                    if (recognizer.acceptWaveForm(buffer, n)) {

                        JsonObject json = JsonParser.parseString(recognizer.getResult()).getAsJsonObject();
                        String text = json.get("text").getAsString().replace("[unk]", "").trim();

                        System.out.println(text);

                        if (!text.isEmpty()) {
                            return text;
                        }
                    } else {
                        // here listen for jarvis?
                        JsonObject json = JsonParser.parseString(recognizer.getPartialResult()).getAsJsonObject();
                        String text = json.get("partial").getAsString().replace("[unk]", "").trim();

                        if (!text.isEmpty()) {
                            commandDispatcher.PartialHeard(text);
                        }
                    }
                }

            }
        }

        catch (LineUnavailableException e) {
            System.err.println("Voice input failed: " + e.getMessage());
            return "";
        }
    }

    public boolean StopInputs() {
        return true;
    }

    public float GetSampleRate() {
        return sampleRate;
    }

    public int GetSampleSizeInBits() {
        return sampleSizeInBits;
    }

    public int GetNoOfChannels() {
        return channels;
    }

    public int GetBufferSize() {
        return bufferSize;
    }
}
