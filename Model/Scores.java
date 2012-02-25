package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Scores {
	
	public Scores( int windowSize ) {
		this.windowSize = windowSize;
	}
	
	public int getWindowSize() {
		return windowSize;
	}
	
	public static Scores load( String fileName, int windowSize ) throws IOException {
		Scores score = new Scores( windowSize );
		BufferedReader br = new BufferedReader( new FileReader( fileName ) );
		String line;
		while ((line=br.readLine())!=null ) {
			StringTokenizer st = new StringTokenizer( line );
		}
		br.close();
		return score;
	}
	
	private int windowSize;
	
}
