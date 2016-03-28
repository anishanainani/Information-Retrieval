import java.util.List;
import java.util.TreeMap;


public class Lemmatization {
	public TreeMap<String, TermIndex> lemmaMap = new TreeMap<String, TermIndex>();
	int countTotalTokens = 0;
	StanfordLemmatizer sl = new StanfordLemmatizer();
	
	public void storeLemmas(String docText, int docID){
		List<String> lemmas = sl.lemmatize(docText);
        
		for(String lemma : lemmas){
			if(!lemma.equals("") && !StopWords.isStopWord(lemma)){
	            if(lemmaMap.containsKey(lemma)){
	            	TermIndex ti = lemmaMap.get(lemma);
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
	        		lemmaMap.put(lemma, ti);
	        	}
	        }
		}
        
	}
}
