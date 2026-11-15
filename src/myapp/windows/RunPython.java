package myapp.windows;

import java.util.Arrays;
import java.util.List;

public class RunPython {
    public Process RunPythonScript(String script)
    {
        ProcessRunner processRunner = new ProcessRunner();

        List<String> command = Arrays.asList("venv/Scripts/python.exe", "python/" + script, "Java");
        return processRunner.BuildProcess(command);
    }
}
