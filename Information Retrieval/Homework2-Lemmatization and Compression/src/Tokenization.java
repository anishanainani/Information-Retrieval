import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Tokenization {
	
	int countTotalTokens;
	PorterStemmer ps;
	TreeMap<Integer, DocInfo> docLen = new TreeMap<Integer, DocInfo>();
	TreeMap<Integer, DocInfo> docLenLemmas = new TreeMap<Integer, DocInfo>();
	Lemmatization lm;
	
	public Tokenization(){
		
		countTotalTokens = 0;
		ps = new PorterStemmer();
		lm = new Lemmatization();
	}
	
	public void tokenize(String folderName) throws IOException{
		File folder = new File(folderName);
		String[] files = folder.list();
		
		for(int i = 0; i < files.length; ++i){
			String text = readFile(folderName+"/"+files[i]);
			String parsedText = parse(text);
			storeTokens(parsedText, i);
			lm.storeLemmas(parsedText.replaceAll("\n", " "), i);
			storeLemmaDocInfo(parsedText,i);
			storeStemDocInfo(parsedText, i);
		}
	}
	
	public String readFile(String file) throws IOException{
		String text = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
		return text;
	}
	
	public String parse(String text){
		String regex = "\\<.*?>";
		String parsedText = text.replaceAll(regex, " ").toLowerCase();
		
		//regex = "[,=\\/\\-\\(\\)]";
		regex = "[^A-Za-z]+";
		parsedText = parsedText.replaceAll(regex, " ");
		
		//regex = "[']";
		//parsedText = parsedText.replaceAll(regex, "");
		return parsedText;
	}
	
	public void storeTokens(String text, int docID){
		String[] tokens = text.split("[ \n]");
		
		for(int i = 0; i < tokens.length; ++i){
			if(tokens[i].equals("") || tokens[i].equals(" ")){
				continue;
			}
			countTotalTokens++;
			
			ps.storeStem(tokens[i], docID);
		}
	}
	
	public void storeStemDocInfo(String text, int docID){
		String[] tokens = text.split("[ \n]");
		HashMap<String, Integer> toks = new HashMap<String, Integer>();
		int maxTf = Integer.MIN_VALUE;
		Stemmer stemmer = new Stemmer();
		for(int i = 0; i < tokens.length; ++i){
			if(tokens[i].equals("") || tokens[i].equals(" ")){
				continue;
			}
			String key1 = stemmer.step1(tokens[i]);
	        String key2 = stemmer.step2(key1);
	        String key3 = stemmer.step3(key2);
	        String key4 = stemmer.step4(key3);
	        String key5 = stemmer.step5(key4);
	       
			if(toks.containsKey(key5.trim())){
				toks.put(key5.trim(),toks.get(key5.trim())+1);
				int tf = toks.get(key5.trim());
				if(tf > maxTf){
					maxTf = tf;
				}
			} else {
				toks.put(key5.trim(), 1);
			}
		}
		
		DocInfo di = new DocInfo(maxTf, toks.size());
		
		docLen.put(docID,  di);
	}
	
	public void storeLemmaDocInfo(String text, int docID){
		text = text.replaceAll("\n", " ");
		HashMap<String, Integer> toks = new HashMap<String, Integer>();
		int maxTf = Integer.MIN_VALUE;
		StanfordLemmatizer sl = new StanfordLemmatizer();
		List<String> lemmas = sl.lemmatize(text);
		for(String lemma : lemmas){
			if(lemma.equals("") || lemma.equals(" ")){
				continue;
			}
	       
			if(toks.containsKey(lemma.trim())){
				toks.put(lemma.trim(),toks.get(lemma.trim())+1);
				int tf = toks.get(lemma.trim());
				if(tf > maxTf){
					maxTf = tf;
				}
			} else {
				toks.put(lemma.trim(), 1);
			}
		}
		
		DocInfo di = new DocInfo(maxTf, toks.size());
		
		docLen.put(docID,  di);
	}
	
	public int getTotalTokensCount(){
		return countTotalTokens;
	}
	
	public TreeMap<String, TermIndex> getStemIndex(){
		return ps.getStemIndex();
	}
	
	public TreeMap<String, TermIndex> getLemmaIndex(){
		return lm.lemmaMap;
	}
	
	public void maxTfAndDoclenDocStem(){
		
		int maxTf = Integer.MIN_VALUE, maxDoclen = Integer.MIN_VALUE;
		for(Integer docID : docLen.keySet()){
			if(docLen.get(docID).maxTf > maxTf){
				maxTf = docLen.get(docID).maxTf;
			}
			
			if(docLen.get(docID).docLen > maxDoclen){
				maxDoclen = docLen.get(docID).docLen;
			}
		}
		
		System.out.println("Porter Stemmer");
		System.out.println("-------------------------------------------");
		System.out.println("The document with maximum max_tf:");
		for(Integer docID : docLen.keySet()){
			if(docLen.get(docID).maxTf == maxTf){
				System.out.print(docID + " ");
			}
		}
		System.out.println();
		System.out.println("The document with maximum doclen:");
		for(Integer docID : docLen.keySet()){
			if(docLen.get(docID).docLen == maxDoclen){
				System.out.print(docID + " ");
			}
		}
		
	}
	
	public void maxTfAndDocLenDocLemma(){
		int maxTf = Integer.MIN_VALUE, maxDoclen = Integer.MIN_VALUE;
		for(Integer docID : docLenLemmas.keySet()){
			if(docLenLemmas.get(docID).maxTf > maxTf){
				maxTf = docLen.get(docID).maxTf;
			}
			
			if(docLenLemmas.get(docID).docLen > maxDoclen){
				maxDoclen = docLenLemmas.get(docID).docLen;
			}
		}
		
		System.out.println("Lemmatization");
		System.out.println("-------------------------------------------");
		System.out.println("The document with maximum max_tf:");
		for(Integer docID : docLenLemmas.keySet()){
			if(docLenLemmas.get(docID).maxTf == maxTf){
				System.out.print(docID + " ");
			}
		}
		System.out.println();
		System.out.println("The document with maximum doclen:");
		for(Integer docID : docLenLemmas.keySet()){
			if(docLenLemmas.get(docID).docLen == maxDoclen){
				System.out.print(docID + " ");
			}
		}
	}
}
