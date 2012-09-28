package Utils;

import java.util.Calendar;

public class Log {
	
	public static void turnOff() {
		shouldLog = false;
	}
	
	public static void turnOn() {
		shouldLog = true;	
	}
	
	public static void enter() {
		indent+=2;
	}
	
	public static void exit() {
		indent-=2;
		if ( indent<0 ) indent=0;
	}

	public static void err( String message ) {
		if ( shouldLog ) {
			String spc = "";
			for ( int i=0;i<indent;i++ ) spc+=" "; 
			String msg = "["+Calendar.getInstance().getTime().toLocaleString()+"]\t" + spc + message;
			System.out.flush();
			System.err.println( msg );
			System.err.flush();
		}
	}
		
	public static void log( String message ) {
		if ( shouldLog ) {
			String spc = "";
			for ( int i=0;i<indent;i++ ) spc+=" "; 
			String msg = "["+Calendar.getInstance().getTime().toLocaleString()+"]\t" + spc + message;
			System.err.flush();
			System.out.println( msg );
			System.out.flush();
		}
	}
	
	public static void logConcat( String message ) {
		if ( shouldLog ) {
			String spc = "";
			for ( int i=0;i<indent;i++ ) spc+=" "; 
			String msg = "["+Calendar.getInstance().getTime().toLocaleString()+"]\t" + spc + message;
			System.out.print( msg );
		}
	}
	
	public static void reportMemory() {
		long totalMemory = Runtime.getRuntime().totalMemory();    
	    long freeMemory = Runtime.getRuntime().freeMemory();
	    
	    Log.log("Total memory: " + totalMemory + " Free memory: " + freeMemory );
	}
	
	
	private static boolean shouldLog = true;
	private static int indent = 0;
}
