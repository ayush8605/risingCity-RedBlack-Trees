import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.TreeMap;

public class FileUtil {

    public static TreeMap<Integer, String> readFile(String inputFile) {

        TreeMap<Integer, String> inputSequence = new TreeMap<>();
        try {
            List<String> allLines = Files.readAllLines(Paths.get(System.getProperty("user.dir") + File.separator + inputFile));

            for (String line : allLines) {
                inputSequence.put(Integer.parseInt(line.substring(0, line.indexOf(':')).trim()), line.substring(line.indexOf(':') + 1).trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputSequence;
    }

    public static void writeOutputToFile(String output) {
        File fout = new File(System.getProperty("user.dir") + File.separator + "output.txt");
        FileWriter fr = null;
        try {
            fr = new FileWriter(fout, true);
            BufferedWriter bw = new BufferedWriter(fr);

            bw.write(output);
            bw.newLine();

            bw.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
