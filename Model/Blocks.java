package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

public class Blocks implements Iterable<Block> {
	
	public Blocks() {
		blocks = new Vector<Block>();
	}
	
	public void sortBlocks() {
		Collections.sort( blocks );
	}
	
	public void add( Block block ) {
		blocks.add( block );
	}
	
	public Iterator<Block> iterator() {
		return blocks.iterator();
	}
	
	public static Blocks load( String fileName )  throws IOException {
		BufferedReader br = new BufferedReader( new FileReader( fileName ) );
		
		Blocks blocks = new Blocks();
		
		String line;
		while ( (line=br.readLine())!=null ) {
			StringTokenizer st = new StringTokenizer( line ) ;
			int blockID = Integer.decode( st.nextToken() );
			int firstWindow = Integer.decode( st.nextToken() );
			int lastWindow = Integer.decode( st.nextToken() )-1;
			double threshold = Double.parseDouble( st.nextToken() );
			double distCM = Double.parseDouble( st.nextToken() );			
			double startCM = Double.parseDouble( st.nextToken() );
			double stopCM = Double.parseDouble( st.nextToken() );
			blocks.add( new Block  ( blockID, firstWindow, lastWindow, threshold, distCM, startCM, stopCM ) );
		}
		br.close();
		
		return blocks;
	}
	
	private Vector<Block> blocks;
	
}
