import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;


public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Indexing in = new Indexing("C:/Cranfield");
		
		TreeMap<String, TermIndex> indexVersion1UnComp = in.Index_Version1UnComp();
		System.out.println("The size of the index Version 1 uncompressed (in bytes): "+in.sizeUnCompressedV1+" bytes");
		
		TreeMap<String, TermIndex> indexVersion2UnComp = in.Index_Version2UnComp();
		System.out.println("The size of the index Version 2 uncompressed (in bytes): "+in.sizeUnCompressedV2+" bytes");
		
		in.Index_Version1Comp();
		System.out.println("The size of the index Version 1 compressed (in bytes): "+in.sizeCompressedV1+" bytes");
		
		in.Index_Version2Comp();
		System.out.println("The size of the index Version 2 compressed (in bytes): "+in.sizeCompressedV2+" bytes");
		
		System.out.println("The number of inverted lists in version 1 of the index : "+indexVersion1UnComp.size());
		System.out.println("The number of inverted lists in version 2 of the index : "+indexVersion2UnComp.size());
		System.out.println();
		
		String[] terms = {"Reynolds", "NASA", "Prandtl", "flow", "pressure", "boundary", "shock"};
		System.out.println("The df, tf, and inverted list length (in bytes) for the terms with Lemmatization");
		in.printTermsInfoLemma(terms, indexVersion1UnComp);
		
		System.out.println("The df, tf, and inverted list length (in bytes) for the terms with Porter Stemmer");
		in.printTermsInfo(terms, indexVersion2UnComp);
		
		System.out.println("the df, for “NASA” as well as the tf, the doclen and the max_tf, for the first 3 entries in its posting list with Lemmatization");
		in.printTermsInfo2("Nasa", indexVersion1UnComp);
		
		System.out.println("the df, for “NASA” as well as the tf, the doclen and the max_tf, for the first 3 entries in its posting list with Porter Stemmer");
		in.printTermsInfo2("Nasa", indexVersion2UnComp);
		
		in.minmaxdfLemmas(indexVersion1UnComp);
		in.minmaxdfStems(indexVersion2UnComp);
		
		in.t.maxTfAndDocLenDocLemma();
		in.t.maxTfAndDoclenDocStem();
		
	}
	

}
