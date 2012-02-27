package Utils;

import java.util.Vector;

public class Arrays {
	
	public static int[] toPrimitive( Vector<Integer> vals ) {
		int res[] = new int[ vals.size() ];
		int i=0;
		for ( int v : vals ) {
			res[i] = v;
			i++;
		}
		return res;
	}
	
	public static double[] toPrimitive( Vector<Double> vals ) {
		double res[] = new double[ vals.size() ];
		int i=0;
		for ( double v : vals ) {
			res[i] = v;
			i++;
		}
		return res;
	}
	
	
}
