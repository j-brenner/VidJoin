package vidjoin;

import java.io.File;

public class JaRun {
    static int runCommand(String command, String logFilename) {

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(System.getProperty("user.dir")));
        File log = new File(logFilename);
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
        int exitCode = 0;
        try {
            final Process process = pb.start();
            exitCode = process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exitCode;
    }
}
