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

  public Block get(int b) {
    return blocks.get(b);
  }
	
	public Iterator<Block> iterator() {
		return blocks.iterator();
	}
	
	public static Blocks load( String fileName )  throws IOException {
		BufferedReader br = new BufferedReader( new FileReader( fileName ) );
		
		Blocks blocks = new Blocks();
		
		String line;
		while ( (line=br.readLine())!=null ) {
			
			//
			//	Split line according to tabs
			String[] fields = line.split("\t", -1);

			//
			//	Set the fields
			int winID = Integer.parseInt( fields[0] );
			double startCM = Double.parseDouble( fields[1] );
			double endCM = Double.parseDouble( fields[2] );
			String winListStr = fields[3];
			
			//
			//	Determine if thresholds are available
			String thresholdsStr = null;
			if ( fields.length==5 ) {
				thresholdsStr = fields[4];
			}

			//
			//	Determine the SNPs within the window
			String winList[] = winListStr.split(",",-1);
			int winIdx[] = new int[ winList.length ];
			for ( int i=0;i<winList.length;i++ ) {
				winIdx[i] = Integer.parseInt( winList[i] );
			}
			
			//
			//	Determine list of thresholds
			double th[] = null;
			if ( thresholdsStr!=null ) {
				String thList[] = thresholdsStr.split(",",-1);
				th = new double[ thList.length ];
				for ( int i=0;i<thList.length;i++ ) {
					th[i] = Double.parseDouble( thList[i] );
				}
				
			}
			
			//
			//	Generate the window
			Block b = new Block( winID, startCM, endCM, winIdx, th );

			//
			//	Add to windows
			blocks.add( b );
			
			
		}
		br.close();
		
		return blocks;
	}
	
	private Vector<Block> blocks;
	
}
