import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;


public class Indexing {
	public Tokenization t;
	public int sizeUnCompressedV1 = 0,sizeUnCompressedV2 = 0, sizeCompressedV1 = 0,sizeCompressedV2 = 0;
	
	
	public Indexing(String folderName) throws IOException{
		t = new Tokenization();
		t.tokenize(folderName);
	}
	
	public TreeMap<String, TermIndex> Index_Version1UnComp() throws FileNotFoundException{
		TreeMap<String, TermIndex> lemmaIndex = t.getLemmaIndex();
		sizeUnCompressedV1 = FileIO.writeUncompressed(lemmaIndex, "Index_Version1.uncompress", t.docLen);
		return lemmaIndex;
	}
	
	
	public TreeMap<String, TermIndex> Index_Version2UnComp() throws FileNotFoundException{
		TreeMap<String, TermIndex> stemIndex = t.getStemIndex();
		sizeUnCompressedV2 = FileIO.writeUncompressed(stemIndex, "Index_Version2.uncompress", t.docLen);
		return stemIndex;
	}
	
	public void Index_Version1Comp() throws IOException{
		TreeMap<String, TermIndex> lemmaIndex = t.getLemmaIndex();
		sizeCompressedV1 = FileIO.writeGammaCompressedPostingLists(lemmaIndex, "Index_Version1.compress", t.docLen);
		BlockCompression.compress(lemmaIndex, 8, "Index_Version1_Terms.compress");
	}
	
	
	public void Index_Version2Comp() throws IOException{
		TreeMap<String, TermIndex> stemIndex = t.getStemIndex();
		sizeCompressedV2 = FileIO.writeDeltaCompressedPostingLists(stemIndex, "Index_Version2_PostingList.compress", t.docLen);
		FrontCoding.compress(stemIndex, "Index_Version2_Dictionary.compress");
	}
	
	public void printTermsInfo(String[] terms, TreeMap<String, TermIndex> index){
	    System.out.println("-------------------------------------------");
		Stemmer stemmer = new Stemmer();
		
		for(int i = 0; i < terms.length; ++i){
			terms[i] = terms[i].toLowerCase();
			String key1 = stemmer.step1(terms[i]);
	        String key2 = stemmer.step2(key1);
	        String key3 = stemmer.step3(key2);
	        String key4 = stemmer.step4(key3);
	        String key5 = stemmer.step5(key4);
	        terms[i] = key5;
			System.out.println("Term : "+terms[i]);
			TermIndex ti = index.get(terms[i]);
			int tf = 0, invertedSize = index.get(terms[i]).docFreq;
			for(Integer docID : ti.postingList.keySet()){
				tf+=ti.postingList.get(docID);
				invertedSize+=docID.toString().length()*4 + ti.postingList.get(docID).toString().length()*4+t.docLen.get(docID).maxTf.toString().length()*4 + t.docLen.get(docID).docLen.toString().length()*4;
			}
			System.out.println("df : "+ti.docFreq);
			System.out.println("tf : "+tf);
			System.out.println("Inverted List Length : "+invertedSize);
			System.out.println();
			
		}
	}
	
	public void printTermsInfoLemma(String[] terms, TreeMap<String, TermIndex> index){
	    System.out.println("-------------------------------------------");
		String termsString = "";
		for(int i = 0; i < terms.length; ++i){
			termsString+=terms[i].toLowerCase();
		}
		StanfordLemmatizer sl = new StanfordLemmatizer();;
		List<String> termsList = sl.lemmatize(termsString);
		for(String lemma : termsList){

			System.out.println("Term : "+lemma);
			TermIndex ti = index.get(lemma);
			int tf = 0, invertedSize = index.get(lemma).docFreq;
			for(Integer docID : ti.postingList.keySet()){
				tf+=ti.postingList.get(docID);
				invertedSize+=docID.toString().length()*4 + ti.postingList.get(docID).toString().length()*4+t.docLen.get(docID).maxTf.toString().length()*4 + t.docLen.get(docID).docLen.toString().length()*4;
			}
			System.out.println("df : "+ti.docFreq);
			System.out.println("tf : "+tf);
			System.out.println("Inverted List Length : "+invertedSize);
			System.out.println();
			
		}
	}
	
	public void printTermsInfo2(String term, TreeMap<String, TermIndex> index){
		System.out.println("-------------------------------------------");
		term = term.toLowerCase();
		Stemmer stemmer = new Stemmer();
		String key1 = stemmer.step1(term);
        String key2 = stemmer.step2(key1);
        String key3 = stemmer.step3(key2);
        String key4 = stemmer.step4(key3);
        String key5 = stemmer.step5(key4);
        
		System.out.println("Term : "+term);
		TermIndex ti = index.get(term);
		int tf = 0, count = 1;
		System.out.println("df : "+ti.docFreq);
		for(Integer docID : ti.postingList.keySet()){
			if(count == 4){
				break;
			}
			
			System.out.println(count+". DocId : "+docID);
			tf=ti.postingList.get(docID);
			System.out.println("tf : "+tf);
			System.out.println("maxTf : "+t.docLen.get(docID).maxTf);
			System.out.println("doclen : "+t.docLen.get(docID).docLen);
			System.out.println();
			count++;
		}
		
	}
	
	public void minmaxdfStems(TreeMap<String, TermIndex> index){
		int maxdf = Integer.MIN_VALUE, mindf = Integer.MAX_VALUE;
		for(String key : index.keySet()){
			if(index.get(key).docFreq > maxdf){
				maxdf = index.get(key).docFreq;
			}
			
			if(index.get(key).docFreq < mindf){
				mindf = index.get(key).docFreq;
			}
		}
		
		System.out.println("The stem from index 2 with the largest df: " );
		for(String key : index.keySet()){
			if(index.get(key).docFreq == maxdf){
				System.out.print(key+" ");
			}
		}
		System.out.println();
		System.out.println("The stem from index 2 with the smallest df: " );
		for(String key : index.keySet()){
			if(index.get(key).docFreq == mindf){
				System.out.print(key+" ");
			}
		}
		
	}
	
	public void minmaxdfLemmas(TreeMap<String, TermIndex> index){
		int maxdf = Integer.MIN_VALUE, mindf = Integer.MAX_VALUE;
		for(String key : index.keySet()){
			if(index.get(key).docFreq > maxdf){
				maxdf = index.get(key).docFreq;
			}
			
			if(index.get(key).docFreq < mindf){
				mindf = index.get(key).docFreq;
			}
		}
		
		System.out.println("The terms from index 1 with the largest df" );
		for(String key : index.keySet()){
			if(index.get(key).docFreq == maxdf){
				System.out.print(key+" ");
			}
		}
		System.out.println();
		System.out.println("The terms from index 1 with the smallest df" );
		for(String key : index.keySet()){
			if(index.get(key).docFreq == mindf){
				System.out.print(key+" ");
			}
		}
		
	}
}
