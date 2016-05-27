package DataManager;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import java.io.IOException;
import java.util.Objects;

/**
 * Created by George on 27.05.2016.
 */
public class FileUtil {

    public static String getFileDataAsString(String fileName) {
        Objects.nonNull(fileName);
        try {
            String data = new String(readAllBytes(get(fileName)));
            return data;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
