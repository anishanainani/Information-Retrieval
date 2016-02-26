import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class Tokenization {
	
	public HashMap<String, Integer> tokensMap = new HashMap<String, Integer>();
	public Map<Integer, ArrayList<String> > tokensFrequency = new TreeMap<Integer, ArrayList<String> >();
	int countTotalTokens = 0;
	int totalTokensEachFile=0, averageTokensPerFile = 0;
	PorterStemmer ps;
	long timeToTokenize, timeForStemming;
	
	public void tokenize(String folderName) throws IOException{
		Timer.timer();
		File folder = new File(folderName);
		String[] files = folder.list();
		ps = new PorterStemmer(files.length);
		
		for(int i = 0; i < files.length; ++i){
			String text = readFile(folderName+"/"+files[i]);
			String parsedText = parse(text);
			storeTokens(parsedText);
		}
		
		
		
		Set<String> keys = tokensMap.keySet();
		for(String k : keys){	
			if(tokensFrequency.containsKey(tokensMap.get(k))){
				tokensFrequency.get(tokensMap.get(k)).add(k);
			} else {
				ArrayList<String> list = new ArrayList<String>();
				list.add(k);
				tokensFrequency.put(tokensMap.get(k), list);
			}
		}

		averageTokensPerFile = totalTokensEachFile/files.length;
		timeToTokenize = Timer.timer();
		
		Timer.timer();
		ps.performStemming(tokensMap);
		timeForStemming = Timer.timer();
	}
	
	public String readFile(String file) throws IOException{
		String text = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
		return text;
	}
	
	public String parse(String text){
		String regex = "<.*>";
		String parsedText = text.replaceAll(regex, " ").toLowerCase();
		
		regex = "[,=\\/]";
		parsedText = parsedText.replaceAll(regex, " ");
		
		regex = "[^\\w\\s]";
		parsedText = parsedText.replaceAll(regex, "");
		
		return parsedText;
	}
	
	public void storeTokens(String text){
		String[] tokens = text.split("[ \n]");
		Set<String> tokensEachFile = new HashSet<String>();
		
		for(int i = 0; i < tokens.length; ++i){
			if(tokens[i].equals("") || tokens[i].equals(" ")){
				continue;
			}
			
			countTotalTokens++;
			if(tokensMap.containsKey(tokens[i])){
				int count = tokensMap.get(tokens[i]);
				tokensMap.put(tokens[i], count+1);
			} else {
				tokensMap.put(tokens[i], 1);
				totalTokensEachFile++;
			}
			
			if(!tokensEachFile.contains(tokens[i])){
				tokensEachFile.add(tokens[i]);
				totalTokensEachFile++;
			}
		}
		
		ps.performStemmingEachFile(tokensEachFile);
	}
	
	public int getTotalTokensCount(){
		return countTotalTokens;
	}
	
	public int getUniqueTokensCount(){
		return tokensMap.size();
	}
	
	public int getAverageTokensPerFile(){
		return averageTokensPerFile;
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

	public long getTimeForTokenization(){
		return timeToTokenize;
	}
	
	public long getTimeForStemming(){
		return timeForStemming;
	}
}
