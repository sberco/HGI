package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

public class Labels {
	
	public Labels() {
		labelMap = new HashMap<String,Integer>();
		labelVec = new Vector<String>();
		globalIndex = 0;
	}
	
	public void add( String id ) {
		labelMap.put( id, globalIndex );
    labelVec.add(id);
		globalIndex++;
	}
	
	public int getIndex( String id ) {
		return labelMap.get( id );
	}

	public String getString( int idx ) {
		return labelVec.get(idx);
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
  private Vector<String> labelVec;
	private int globalIndex;
}
