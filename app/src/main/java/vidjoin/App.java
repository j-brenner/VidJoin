/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package vidjoin;

// xxx0 
// проверь выходной файл размер  import java.io.*;
// Проверь что дата файла верно определяется

import java.util.Properties;
import java.util.Date;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

public class App {

    
    private static final String CONFIG_NAME = "VidJoin.txt";

    public static void main(String[] args) {
        Properties props = getAllProperties();

        try {
            VidJoin vj = new VidJoin(props);
            printArray(vj.get_sortedPieceNames());
            System.out.println("command: " + vj.get_exeStr());

            // Run
            System.out.println(">>>>>>>>>>>>>>> Join. Start: " + new Date());
            int exitCode = JaRun.runCommand(vj.TMP_BAT_FILE, vj.TMP_FFMPEG_LOG_FILE_NAME);
            System.out.println(">>>>>>>>>>>>>>> Join. End: " + new Date());
            System.out.println(">>>>>>>>>>>>>>> Exit code: " + exitCode);

            //
            if (vj.get_DEBUG_MODE()) {
                vj.deleteAndBakFiles();
            }
        } catch (Exception e) {
            System.out.println(">>>>>>>>>>>>>>> EROR");
            System.out.println(e);
        }

    }

    private static Properties getAllProperties() {
        Properties resProps = new Properties();
        InputStream stream = null;
        InputStreamReader reader = null;
        try {
            stream = new FileInputStream(new File(System.getProperty("user.dir") + "\\" + CONFIG_NAME));
            reader = new InputStreamReader(stream, "UTF-8");
            resProps.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resProps;
    }

    private static void printArray(String[] inArray) {
        for (String one : inArray) {
            System.out.println("--- " + one);
        }
    }
}
