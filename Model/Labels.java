package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Labels {
	
	public Labels() {
		labelMap = new HashMap<String,Integer>();
		globalIndex = 0;
	}
	
	public void add( String id ) {
		labelMap.put( id, globalIndex );
		globalIndex++;
	}
	
	public int getIndex( String id ) {
		return labelMap.get( id );
	}
	
	public boolean hasIndex( String id ) {
		return labelMap.containsKey( id );
	}
	
	public int size() {
		return labelMap.size();
	}
	
	public static Labels load( String fileName ) throws IOException {
		Labels labels = new Labels();
		BufferedReader br = new BufferedReader( new FileReader( fileName ) ) ;
		String line;
		while ( (line=br.readLine())!=null ) {
			labels.add( line );
		}
		br.close();
		return labels;
	}
	
	private HashMap<String,Integer> labelMap;
	private int globalIndex;
}
