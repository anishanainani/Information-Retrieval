import java.util.TreeMap;


public class TermIndex {
	public int docFreq;
	public TreeMap<Integer, Integer> postingList;
	
	public TermIndex(){
		docFreq = 0;
		postingList = new TreeMap<Integer, Integer>();
	}
}
