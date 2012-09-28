package Experiments.IBDMap.WTCCC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import Utils.Log;

public class IndividualList {
	
	private HashMap<String,Integer> map;
	private HashMap<Integer, String> rMap;
	private int globalCount;
	
	private IndividualList() {
		map = new HashMap<String,Integer>();
		rMap = new HashMap<Integer, String>();
		globalCount = 0;
	}
	
	public static IndividualList load( String fileName ) throws IOException  {
		
		Log.log("Loading ["+fileName+"]");
		
		BufferedReader br = new BufferedReader( new FileReader( fileName ) );
		
		IndividualList il = new IndividualList();
		
		String line;
		
		while ((line=br.readLine())!=null) {
			StringTokenizer st = new StringTokenizer( line );
			st.nextToken();
			String name = st.nextToken();
			il.addIndividual( name );			
		}
		
		br.close();
		
		return il;
	}
	
	public void addIndividual( String name ) {
		map.put( name, globalCount );
		rMap.put( globalCount, name );
		globalCount++;
	}
	
	public int getIndividualIndex( String name ) {
		return map.get( name );
	}
	
	public String getIndividual( int idx ) {
		return rMap.get( idx );
	}
	
	public int size() {
		return map.size();
	}
	
}

