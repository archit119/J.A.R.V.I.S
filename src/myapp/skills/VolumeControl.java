package myapp.skills;

import java.lang.Thread;

import com.sun.jna.platform.win32.WinUser.INPUT;

import myapp.windows.User32;

import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.WORD;
import com.sun.jna.platform.win32.BaseTSD;

public class VolumeControl {

    static final int VK_VOLUME_UP = 0xAF;
    static final int VK_VOLUME_DOWN = 0xAE;
    static final int INPUT_KEYBOARD = 1;

    public int SetVolume(int volume)
    {
        int result = 0;
        // // first decrease the volume to 0
        for(int i=0; i < 50; i++)
        {
            ControlVolume(VK_VOLUME_DOWN);
            sleep(5);
        }

        // then set it to desired volume
        for(int i=0; i < volume/2; i++)
        {
            result += ControlVolume(VK_VOLUME_UP);
            sleep(5);
        }

        return result;
    }
    
    int ControlVolume(int key)
    {
        // preparing the input
        INPUT input = new INPUT();

        input.type = new DWORD(INPUT.INPUT_KEYBOARD);
        input.input.setType("ki");
        input.input.ki.wScan = new WORD(0);
        input.input.ki.time = new DWORD(0);
        input.input.ki.dwExtraInfo = new BaseTSD.ULONG_PTR(0);

        // pressing volume up
        input.input.ki.wVk = new WORD(key);
        input.input.ki.dwFlags = new DWORD(0); // keydown

        int result = User32.INSTANCE.SendInput(new DWORD(1), (INPUT[]) input.toArray(1), input.size()); // sends the input

        // release volume up
        input.input.ki.wVk = new WORD(key);
        input.input.ki.dwFlags = new DWORD(1); // keyup

        return result;
    }

    void sleep(int time)
    {
        try {
            Thread.sleep(time); 
        } 
        catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
            System.out.println("Thread was interrupted!");
        }
    }
}
