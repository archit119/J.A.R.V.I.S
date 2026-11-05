package myapp.windows;
import com.sun.jna.platform.win32.WTypes;

public class ErrorBox {

    static final int MB_YESNOCANCEL = 0x00000003;
    static final int IDYES = 6;
    static final int IDNO  = 7;
    static final int IDCANCEL  = 2;

    public void DisplayError()
    {
        String error = "Test Error";
        String caption = "This is a test";

        WTypes.LPWSTR errorLP = new WTypes.LPWSTR(error);
        WTypes.LPWSTR captionLP = new WTypes.LPWSTR(caption);
        
        int result = User32.INSTANCE.MessageBoxW(null, errorLP, captionLP, MB_YESNOCANCEL);
        if(result == IDYES)
        {
            System.out.println("You said yes!");
        }
        else if (result == IDNO)
        {
            System.out.println("You said no!");
        }
        else
        {
            System.out.println("You canceled!");
        }
    }
}