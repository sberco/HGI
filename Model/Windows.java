package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

public class Windows implements Iterable<Window> {
	
	public Windows() {
		windows = new Vector<Window>();
	}
	
	public void sortBlocks() {
		Collections.sort( windows );
	}
	
	public void add( Window window ) {
		windows.add( window );
	}

  public Window get(int w) {
    return windows.get(w);
  }
	
	public Iterator<Window> iterator() {
		return windows.iterator();
	}
	
	public static Windows load( String fileName )  throws IOException {
		BufferedReader br = new BufferedReader( new FileReader( fileName ) );
		
		Windows blocks = new Windows();
		
		String line;
		while ( (line=br.readLine())!=null ) {
			
			line.sp
			
			StringTokenizer st = new StringTokenizer( line ) ;
			int windowIdx = 
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
	
	private Vector<Block> windows;
	
}
