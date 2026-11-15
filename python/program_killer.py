import sys
import os

def main():
    while True:
        line = sys.stdin.readline()
        print(f"Received: {line}")
        if(line == ""):
            os._exit(0)
        line = line.strip()