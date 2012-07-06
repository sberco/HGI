package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class Windows implements Iterable<Window> {

	public Windows() {
		windows = new Vector<Window>();
		windowMap = new HashMap<Integer,Window>();
	}

	public void sortWindows() {
		Collections.sort( windows );
	}

	public void add( Window window ) {
		windows.add( window );
		windowMap.put( window.getID(), window );
	}

	public Window get( int wID ) {
		return windowMap.get( wID );
	}

	public Iterator<Window> iterator() {
		return windows.iterator();
	}

	public static Windows load( String fileName )  throws IOException {
		BufferedReader br = new BufferedReader( new FileReader( fileName ) );

		Windows windows = new Windows();

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
			String snpListStr = fields[3];

			//
			//	Determine the SNPs within the window
			String snpList[] = snpListStr.split(",",-1);
			int snpIdx[] = new int[ snpList.length ];
			for ( int i=0;i<snpList.length;i++ ) {
				snpIdx[i] = Integer.parseInt( snpList[i] );
			}

			//
			//	Generate the window
			Window w = new Window( winID, startCM, endCM, snpIdx );

			//
			//	Add to windows
			windows.add( w );
		}
		br.close();

		return windows;
	}

	private Vector<Window> windows;
	private HashMap<Integer,Window> windowMap;

}
