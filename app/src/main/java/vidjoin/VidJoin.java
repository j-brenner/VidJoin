package vidjoin;

import java.util.Properties;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class VidJoin {
    // Жестко вшитые свойства >>>
    private final String DLM = "_";
    private final String EXT = ".mp4";
    private final String RES_MOV_NAME = "res.mp4";
    final String TMP_PART_LIST_FILE = "tmp_list.txt";
    final String TMP_BAT_FILE = "tmp.bat";
    final String TMP_FFMPEG_LOG_FILE_NAME = "tmp_ffmpeg.log";

    // Переменные из config-файла. Инициализировать только в конструкторе >>>
    private String KURS;
    private String LESSON;
    private String ENDPAGE;
    private String JOINER_PATH;
    private boolean DEBUG_MODE;

    // Переменные для работы. Инициализировать только в конструкторе >>>
    private String[] sortedPieceNames;
    private String outMovieFileName;
    private String exeStr;

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// API
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    VidJoin(Properties props) throws Exception {
        KURS = props.getProperty("KURS");
        LESSON = props.getProperty("LESSON_NAME");
        ENDPAGE = props.getProperty("ENDPAGE");
        JOINER_PATH = props.getProperty("JOINER_PATH");
        DEBUG_MODE = props.getProperty("DEBUG_MODE").equalsIgnoreCase("y");

        checkAndMakeArrayMovieNames(); // Внутри устанавливается sortedPieceNames
        exeStr = make_exeStr();
        make_TMP_PART_LIST_FILE();
        make_TMP_BAT_FILE();
        // Имя выходного файла >>>
        String lessDate = JaDateToStr.toYYyMMmDD(JaFiles.lastModified(sortedPieceNames[0]));
        outMovieFileName = KURS + DLM + lessDate + DLM + LESSON + DLM + "endpage" + ENDPAGE + EXT;
    }

    void deleteAndBakFiles() throws Exception {
        String currDir = System.getProperty("user.dir") + "\\";
        String kursDir = currDir + KURS + "\\";
        String kursBakDir = currDir + KURS + "_bak\\";
        // Переименовать итоговое видео >
        JaFiles.renameFile(RES_MOV_NAME, outMovieFileName);
        // переместить склеенное видео в каталог языка >>>
        JaFiles.moveFile(outMovieFileName, currDir, kursDir);
        // Переместить все исходные куски в bak >>>
        for (String one : sortedPieceNames) {
            JaFiles.moveFile(one, currDir, kursBakDir);
        }
        // Удалить временные файлы >>
        new File(currDir + TMP_PART_LIST_FILE).delete();
        new File(currDir + TMP_BAT_FILE).delete();

    }

    String[] get_sortedPieceNames() {
        return sortedPieceNames;
    }

    String get_exeStr() {
        return exeStr;
    }

    boolean get_DEBUG_MODE() {
        return DEBUG_MODE;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// private
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    private void checkAndMakeArrayMovieNames() throws Exception {
        sortedPieceNames = sortedMovieFileNames();
        // Если есть пробелы в именах - убрать пробелы >>>
        boolean rereadPieceNames = false;
        for (String oneof : sortedPieceNames) {
            if (oneof.contains(" ")) {
                rereadPieceNames = true;
                JaFiles.renameFile(oneof, oneof.replace(" ", "_"));
            }
        }
        if (rereadPieceNames) {
            sortedPieceNames = sortedMovieFileNames();
        }
    }

    private String[] sortedMovieFileNames() throws Exception {
        ArrayList<String> res = new ArrayList<>();
        File folder = new File(System.getProperty("user.dir"));
        for (File one : folder.listFiles()) {
            String name = one.getName();
            if (name.endsWith(".mp4")) {
                res.add(name);
            } else if (name == RES_MOV_NAME) {
                throw new Exception("res.mp4 detected. this name reserved.");
            }
        }
        Collections.sort(res);
        String[] array = new String[res.size()];
        res.toArray(array);
        return array;
    }

    private String make_exeStr() {
        // ffmpeg -f concat -i files.txt -c copy output.mp4 >
        return JOINER_PATH + " -f concat -i " + TMP_PART_LIST_FILE + " -c copy " + RES_MOV_NAME;
    }

    private void make_TMP_PART_LIST_FILE() throws IOException {
        // ffmpeg требует свой формат строк. Переделать >>>
        String[] forWriteToFile = new String[sortedPieceNames.length];
        for (int i = 0; i < sortedPieceNames.length; ++i) {
            forWriteToFile[i] = "file '" + sortedPieceNames[i] + "'";
        }

        JaFiles.writeStrArrayToFile(TMP_PART_LIST_FILE, forWriteToFile);

    }

    private void make_TMP_BAT_FILE() throws Exception {
        String[] forWriteToFile = { "chcp 65001\n", exeStr };
        JaFiles.writeStrArrayToFile(TMP_BAT_FILE, forWriteToFile);

    }

}
