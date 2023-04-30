package vidjoin;

import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Calendar;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JaFiles {

    static boolean renameFile(String oldFileName, String newFileName) {
        File oldFile = new File(oldFileName);
        File newFile = new File(newFileName);
        return oldFile.renameTo(newFile);
    }

    // xxx Подумать: можно ли без catch но с finaly >>>
    static void writeStrArrayToFile(String fullfileName, String[] strArray) throws IOException {
        OutputStream stream = null;
        OutputStreamWriter writer = null;
        try {
            stream = new FileOutputStream(new File(fullfileName));
            writer = new OutputStreamWriter(stream, "UTF-8");
            for (String oneof : strArray) {
                writer.append(oneof + "\n");
            }
            writer.flush();
            writer.close();
        } finally {
            stream.close();
            if (writer != null) {
                writer.close();
            }
        }
    }

    static Calendar lastModified(String fileName) {
        // xxx Дата файла - дата модификации пока >>>
        File ff = new File(fileName);
        Date date = new Date(ff.lastModified());
        Calendar cal = Calendar.getInstance();
        cal.setLenient(true);
        cal.setTime(date);
        return cal;
    }

    static void moveFile(String fileName, String src, String dest) throws Exception {
        Path result = null;
        try {
            result = Files.move(Paths.get(src + fileName), Paths.get(dest + fileName));
            if (result == null) {
                throw new Exception("File not moved.");
            }
        } catch (IOException e) {
            System.out.println("Exception while moving file: " + e.getMessage());
        }
    }
}
