import java.io.IOException;


public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Tokenization t = new Tokenization();
		
		//t.tokenize("C://Cranfield");
		t.tokenize(args[0]);
			
		//Tokenization
		System.out.println("~~~~~~~~~~Tokenization~~~~~~~~~~");
		System.out.println("1. The number of tokens in the Cranfield text collection : "+t.getTotalTokensCount());
		System.out.println("2. The number of unique words in the Cranfield text collection : "+t.getUniqueTokensCount());
		System.out.println("3. The number of words that occur only once in the Cranfield text collection : "+t.getFrequencyOneWordCount());
		System.out.println("4. The 30 most frequent words in the Cranfield text collection :");
		t.printTop30Tokens();
		System.out.println("5. The average number of word tokens per document : "+t.getAverageTokensPerFile());
		
		System.out.println();
		System.out.println();
		
		//Stemming
		System.out.println("~~~~~~~~~~Porter Stemmer~~~~~~~~~~");
		System.out.println("1. The number of unique stems in the Cranfield text collection : "+t.ps.getUniqueTokensCount());
		System.out.println("2. The number of stems that occur only once in the Cranfield text collection : "+t.ps.getFrequencyOneWordCount());
		System.out.println("3. The 30 most frequent stems in the Cranfield text collection :");
		t.ps.printTop30Tokens();
		System.out.println("4. The average number of stems tokens per document : "+t.ps.getAverageTokensPerFile());
		
		System.out.println();
		System.out.println();
		
		System.out.println("~~~~~~~~~~~Running Times~~~~~~~~~~~");
        System.out.println("Tokenization running Time : " + t.getTimeForTokenization() + " msec.");
        System.out.println("Porter Stemmer running Time : " + t.getTimeForStemming() + " msec.");
	}
	

}
