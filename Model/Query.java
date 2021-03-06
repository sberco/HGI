package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import Utils.Arrays;

public class Query implements Iterable<String> {
	
	public Query() {
		indConfs = new HashMap<String,int[]>();
		indNames = new Vector<String>();
	}
	
	public int getNumIndividuals() {
		return indConfs.size();
	}
	
	public int[] getIndConf( int idx ) {
		return indConfs.get( indNames.get(idx) );
	}

	public int[] getIndConf( String ID ) {
		return indConfs.get( ID );
	}

  public String getName(int idx)
  {
    return indNames.get(idx);
  }
	
	public void add( String ID, int [] confs ) {
		indConfs.put( ID, confs );
    indNames.add(ID);
	}
	
	public Iterator<String> iterator() {
		return indConfs.keySet().iterator();
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
			int confArray[] = Arrays.toPrimitiveInteger( configurations );
			
			//
			//	Add to query
			query.add( ID, confArray );
			
		}
		br.close();
		return query;
	}
	
	private HashMap<String,int[]> indConfs;
  private Vector<String> indNames;
}
