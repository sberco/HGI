package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

public class Relations {
	
	public Relations() {
		relations = new HashMap<String,Vector<String>>();
	}
	
	public void add( String ID1, String ID2 ) {
		addToVector( ID1, ID2 );
		addToVector( ID2, ID1 );
	}
	
	private void addToVector( String ID1, String ID2 ) {
		if ( !relations.containsKey(ID1) ) relations.put( ID1, new Vector<String>() );
		relations.get( ID1 ).add( ID2 );
	}
	
	public boolean hasRelation( String ID1 ) {
		return relations.containsKey( ID1 );
	}
	
	public Vector<String> getRelations( String ID1 ) {
		return relations.get( ID1 );
	}
	
	public static Relations load( String fileName ) throws IOException {
		Relations relations = new Relations();
		BufferedReader br = new BufferedReader( new FileReader( fileName ) );
		String line;
		while ( (line=br.readLine())!=null ) {
			StringTokenizer st = new StringTokenizer( line );
			String ID1 = st.nextToken();
			String ID2 = st.nextToken();
			relations.add( ID1, ID2 );
		}
		br.close();
		return relations;
	}
	
	private HashMap<String,Vector<String>> relations;
}
