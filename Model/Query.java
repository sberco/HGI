package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

public class Query {
	
	public Query() {
		
	}
	
	public int getNumIndividuals() {
		return numIndividuals;
	}
	
	public void add( String ID, int [] confs ) {
		
	}
	
	public static Query load( String fileName ) throws IOException {
		Query query = new Query();
		BufferedReader br = new BufferedReader( new FileReader( fileName ) ) ;
		String line;
		while ( (line=br.readLine())!=null ) {
			
			//
			//	Determine sample identifier
			StringTokenizer st = new StringTokenizer( line );
			String ID = st.nextToken();
			
			//
			//	Load configurations
			Vector<Integer> configurations = new Vector<Integer>();
			while (st.hasMoreTokens()) {
				configurations.add( Integer.parseInt(st.nextToken()) );
			}
			int confArray[] = new int[ configurations.size() ];
			int cIdx = 0;
			for ( int c : configurations ) {
				confArray[cIdx] = c;
				cIdx++;
			}
			
			//
			//	Add to query
			query.add( ID, confArray );
			
		}
		br.close();
		return query;
	}
	
	private int numIndividuals;
}
