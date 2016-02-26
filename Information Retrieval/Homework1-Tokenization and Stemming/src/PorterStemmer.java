import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class PorterStemmer {
	
	public HashMap<String, Integer> tokensMap = new HashMap<String, Integer>();
	public Map<Integer, ArrayList<String> > tokensFrequency = new TreeMap<Integer, ArrayList<String> >();
	int countTotalTokens = 0;
	int totalTokensEachFile=0, averageTokensPerFile = 0;
	Stemmer stemmer = new Stemmer();
	int numFiles;
	
	public PorterStemmer(int numFiles){
		this.numFiles = numFiles;
	}
	
	public void performStemming(HashMap<String, Integer> tokens){
		
		Set<String> keys = tokens.keySet();
		
		for(String key : keys){
			String key1 = stemmer.step1(key);
            String key2 = stemmer.step2(key1);
            String key3 = stemmer.step3(key2);
            String key4 = stemmer.step4(key3);
            String key5 = stemmer.step5(key4);
           
            if(!key5.equals("")){
            	
        		if(tokensMap.containsKey(key5)){
        			int count = tokensMap.get(key5);
        			tokensMap.put(key5, count+tokens.get(key));
        		} else {
        			tokensMap.put(key5, tokens.get(key));
        		}
            }
		}
		
		
		Set<String> keyList = tokensMap.keySet();
		for(String k : keyList){	
			if(tokensFrequency.containsKey(tokensMap.get(k))){
				tokensFrequency.get(tokensMap.get(k)).add(k);
			} else {
				ArrayList<String> list = new ArrayList<String>();
				list.add(k);
				tokensFrequency.put(tokensMap.get(k), list);
			}
		}
	}
	
	public void performStemmingEachFile(Set<String> tokensPerFile){
		
		Set<String> tokensEachFile = new HashSet<String>();
		for(String key : tokensPerFile){
			String key1 = stemmer.step1(key);
            String key2 = stemmer.step2(key1);
            String key3 = stemmer.step3(key2);
            String key4 = stemmer.step4(key3);
            String key5 = stemmer.step5(key4);
            
            if(!key5.equals("")){
            	if(!tokensEachFile.contains(key5)){
            		totalTokensEachFile++;
            		tokensEachFile.add(key5);
            	}
            }
		}
	}
	
	
	public int getUniqueTokensCount(){
		return tokensMap.size();
	}
	
	public int getAverageTokensPerFile(){
		return averageTokensPerFile = totalTokensEachFile/numFiles;
	}
	
	public void printTop30Tokens(){
		System.out.println(String.format("%-4s %4s %-15s %4s %-8s %s", "Rank", "|", " Token ", "|", "Frequency", "|"));
	    System.out.println("-------------------------------------------");
		Object[] ranks = tokensFrequency.keySet().toArray();
		for(int i = ranks.length-1; i >=ranks.length-30; --i){
			System.out.println(String.format("%-4s %4s %-15s %4s %-8s %s", ranks.length-i, "|", tokensFrequency.get((int)ranks[i]).get(0), "|", (int)ranks[i], "|"));
		}
	}
	
	public int getFrequencyOneWordCount(){
		return tokensFrequency.get(1).size();
	}
}
