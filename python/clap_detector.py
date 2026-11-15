import time
import sys
import os
from clapDetector import ClapDetector, printDeviceInfo
import program_killer
import threading

print("""
      --------------------------------
      The application initially attempts to use the system's default audio device. If this doesn't work or if you prefer to use a different device, you can change it. Below are the available audio devices. Find the one you are using and change the 'inputDevice' variable to the name or index of your preferred audio device. Then, restart the program, and it should properly capture audio.
      --------------------------------
      """)
printDeviceInfo()

thresholdBias = 6000
lowcut=500               #< increase this to make claps detection more strict 
highcut=10000             #< decrease this to make claps detection more strict
clapDetector = ClapDetector(inputDevice=-1, logLevel=10)
clapDetector.initAudio()

# initialize the program killer in a seperate thread
t = threading.Thread(target=program_killer.main, daemon=True)
t.start()

try:
   while True:
      audioData = clapDetector.getAudio()

      result = clapDetector.run(thresholdBias=thresholdBias, lowcut=lowcut, highcut=highcut, audioData=audioData)
      resultLength = len(result)
      if resultLength >= 1:
            print("CLAP", flush=True)
            # clapDetector.saveAudio(folder="./")
      time.sleep(1/60)

except KeyboardInterrupt:
   print("Exited gracefully")
   sys.exit(0)
except Exception as e:
   print(f"error: {e}")
finally:
   clapDetector.stop()