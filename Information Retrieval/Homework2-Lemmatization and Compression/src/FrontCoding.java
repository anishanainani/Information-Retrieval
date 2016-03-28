

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

public class FrontCoding {

    public static void compress(TreeMap<String, TermIndex> index, String file) throws IOException {
        FileWriter fw = new FileWriter(new File(file));
        BufferedWriter bw = new BufferedWriter(fw);
        String compFront = "";
        int count = 0;
        String s1 = "", s2 = "", s3 = "", s4 = "";
        for (String key : index.keySet()) {
            //System.out.println(i);
            if (count == 0) {
                s1 = key;
                count++;
               
            } else if (count == 1) {
                
                s2 = key;
                count++;
            } else if (count == 2) {
                
                s3 = key;
                count++;
            } else if (count == 3) {
                
                s4 = key;
                count++;
            } else if (count == 4) {
                //System.out.println("In loop4");
                int j = 0;
                String commonString = "";
                while (true) {
                    if (s1.length() > j && s2.length() > j && s3.length() > j && s4.length() > j) {
                        if ((s1.charAt(j) == s2.charAt(j)) && (s1.charAt(j) == s3.charAt(j)) && (s1.charAt(j) == s4.charAt(j))) {
                            commonString = commonString + s1.charAt(j);
                            j++;
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                String extraS1 = s1.substring(j, s1.length());
                String extraS2 = s2.substring(j, s2.length());
                String extraS3 = s3.substring(j, s3.length());
                String extraS4 = s4.substring(j, s4.length());
                compFront = compFront + commonString.length() + commonString + "*" + extraS1 + extraS1.length() + "/" + extraS2 + extraS2.length() + "/" + extraS3 + extraS3.length() + "/" + extraS4 + extraS4.length() + "/";
             
                bw.write(compFront + "\n");
                compFront = "";
                count = 0;
            }
        }
        bw.close();
    }
}
