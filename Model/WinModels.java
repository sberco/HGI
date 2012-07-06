package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class WinModels {
	
	public WinModels() {
		modelMap = new HashMap<Integer, WinModel>();
	}
	
	public void add( WinModel m ) {
		modelMap.put( m.getWinID(), m );
	}
	
	public WinModel getModel( int winID ) {
		return modelMap.get( winID );
	}
	
	public static WinModels load( String fileName )  throws IOException {
		BufferedReader br = new BufferedReader( new FileReader( fileName ) );

		WinModels winLRTModels = new WinModels();

		String line;
		while ( (line=br.readLine())!=null ) {

			//
			//	Split line according to tabs
			String[] fields = line.split("\t", -1);

			//
			//	Set the fields
			int winID = Integer.parseInt( fields[0] );
			double muU = Double.parseDouble( fields[1] );
			double muR = Double.parseDouble( fields[2] );
			double sU = Double.parseDouble( fields[3] );
			double sR = Double.parseDouble( fields[4] );

			//
			//	Set up a model
			WinModel m = new WinModel( winID, muU, muR, sU, sR );

			//
			//	Add to windows
			winLRTModels.add( m );
		}
		br.close();

		return winLRTModels;
	}
	
	private HashMap<Integer, WinModel> modelMap;
	
}
