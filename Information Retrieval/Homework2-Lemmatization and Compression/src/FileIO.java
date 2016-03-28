import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;


public class FileIO {
	public static int writeUncompressed(TreeMap<String, TermIndex> index, String file, TreeMap<Integer, DocInfo> docLen) throws FileNotFoundException{
		PrintWriter p = new PrintWriter(file);
		int sizeUnCompressed = 0;
        for (String key : index.keySet()) {
            TermIndex ti = index.get(key);
           
            TreeMap<Integer, Integer> pl = ti.postingList;
            p.print(key + "(" + ti.docFreq + ")" + "\t");
            sizeUnCompressed = sizeUnCompressed + (key.length() * 2) + Integer.toString(ti.docFreq).length() * 4;
            for (Integer docID : pl.keySet()) {
                sizeUnCompressed = sizeUnCompressed + (docID.toString().length() * 4) + (pl.get(docID).toString().length() * 4) + Integer.toString(docLen.get(docID).docLen).length()*4 + Integer.toString(docLen.get(docID).maxTf).length()*4;
                p.print(docID + "(" + pl.get(docID) +","+docLen.get(docID).maxTf+","+docLen.get(docID).docLen+ ")---> ");
            }
            p.println();
        }
        p.println("");
        p.close();
        
        return sizeUnCompressed;
	}
	
	public static int writeGammaCompressedPostingLists(TreeMap<String, TermIndex> index, String file, TreeMap<Integer, DocInfo> docLen) throws IOException{
		DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
		int sizeCompressed = 0;
        for (String key : index.keySet()) {
        	
            TermIndex ti = index.get(key);
            String term = key;
            out.write(term.getBytes());
            byte[] df = GammaCompression.gammaEncoding(ti.docFreq);
            out.write(df);
            TreeMap<Integer, Integer> pl = ti.postingList;
            
            sizeCompressed = sizeCompressed + (key.length() * 2) + 4;
            int gap = 0;
            
            for (Integer docID : pl.keySet()) {
            	gap = Math.abs(gap-docID);
            	byte[] gamma = GammaCompression.gammaEncoding(gap);
            	gap = docID;
            	byte[] maxTf = GammaCompression.gammaEncoding(docLen.get(docID).maxTf);
            	byte[] doclen = GammaCompression.gammaEncoding(docLen.get(docID).docLen);
            	
                sizeCompressed = sizeCompressed + (gamma.length + doclen.length + maxTf.length/8);
                out.write(gamma);
                out.write(maxTf);
                out.write(doclen);
            } 
        }
        out.close();
        
        return sizeCompressed;
		
		
	}
	
	public static int writeDeltaCompressedPostingLists(TreeMap<String, TermIndex> index, String file, TreeMap<Integer, DocInfo> docLen) throws IOException{
		DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
		int sizeCompressed = 0;
        for (String key : index.keySet()) {
        	
            TermIndex ti = index.get(key);
            String term = key;
            out.write(term.getBytes());
            byte[] df = DeltaCompression.deltaEncoding(ti.docFreq);
            out.write(df);
            TreeMap<Integer, Integer> pl = ti.postingList;
            
            sizeCompressed = sizeCompressed + (key.length() * 2) + 4;
            int gap = 0;
            
            for (Integer docID : pl.keySet()) {
            	gap = Math.abs(gap-docID);
            	byte[] delta = DeltaCompression.deltaEncoding(gap);
            	gap = docID;
            	byte[] maxTf = DeltaCompression.deltaEncoding(docLen.get(docID).maxTf);
            	byte[] doclen = DeltaCompression.deltaEncoding(docLen.get(docID).docLen);
            	
                sizeCompressed = sizeCompressed + (delta.length + doclen.length + maxTf.length)/8;
                out.write(delta);
                out.write(maxTf);
                out.write(doclen);
            } 
        }
        out.close();
        
        return sizeCompressed;
		
		
	}
}
