package Utils;

import java.util.Vector;

public class Arrays {
	
	public static int[] toPrimitiveInteger( Vector<Integer> vals ) {
		int res[] = new int[ vals.size() ];
		int i=0;
		for ( int v : vals ) {
			res[i] = v;
			i++;
		}
		return res;
	}
	
	public static double[] toPrimitiveDouble( Vector<Double> vals ) {
		double res[] = new double[ vals.size() ];
		int i=0;
		for ( double v : vals ) {
			res[i] = v;
			i++;
		}
		return res;
	}
	
	
}
