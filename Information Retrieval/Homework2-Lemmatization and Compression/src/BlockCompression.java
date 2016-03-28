

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

public class BlockCompression {

    public static void compress(TreeMap<String, TermIndex> index, int k, String file) throws IOException {
        FileWriter fw = new FileWriter(new File(file));
        BufferedWriter bw = new BufferedWriter(fw);
        String compBlock = "";
        int count = 0;
        for (String key : index.keySet()) {
            compBlock = compBlock + Integer.toString(key.length()) + key;
            count++;
            if (count == k) {
                bw.write(compBlock + "\n");
                //System.out.println(compressedStringBlock);
                compBlock = "";
                count = 0;
            }
        }
        bw.close();
    }
}
