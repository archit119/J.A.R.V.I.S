package myapp.windows;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WTypes.LPWSTR;
import com.sun.jna.platform.win32.WinUser.INPUT;
import com.sun.jna.platform.win32.WinDef.DWORD;

public interface User32 extends Library{
    User32 INSTANCE = Native.load("User32.dll", User32.class);

    int MessageBoxW(HWND hWnd, LPWSTR lpText, LPWSTR lpCaption, int uType);
    int SendInput(DWORD cInputs, INPUT[] pInputs, int cbSize);
}
