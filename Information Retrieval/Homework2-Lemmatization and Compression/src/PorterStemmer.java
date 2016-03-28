
import java.util.TreeMap;

public class PorterStemmer {
	
	public TreeMap<String, TermIndex> stemsMap = new TreeMap<String, TermIndex>();
	int countTotalTokens = 0;
	Stemmer stemmer = new Stemmer();
	
	public void storeStem(String token, int docID){
		String key1 = stemmer.step1(token);
        String key2 = stemmer.step2(key1);
        String key3 = stemmer.step3(key2);
        String key4 = stemmer.step4(key3);
        String key5 = stemmer.step5(key4);
        
        if(!key5.equals("") && !StopWords.isStopWord(key5)){
            if(stemsMap.containsKey(key5)){
            	TermIndex ti = stemsMap.get(key5);
        		if(ti.postingList.containsKey(docID)){
         			ti.postingList.put(docID, ti.postingList.get(docID)+1);
         		} else {
         			ti.postingList.put(docID, 1);
         			ti.docFreq++;
         		}
        	} else {
        		TermIndex ti = new TermIndex();
        		ti.postingList.put(docID, 1);
        		ti.docFreq++;
        		stemsMap.put(key5, ti);
        	}
        }
	}
	
	public TreeMap<String, TermIndex> getStemIndex(){
		return stemsMap;
	}
}
