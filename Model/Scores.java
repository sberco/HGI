package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

public class Scores {

	public Scores(int windowSize, String fileName) throws IOException {
        this.rowsLoaded = 0;
        this.windowSize = windowSize;
        this.fileName = fileName;
		numConfs = (int)Math.pow(Constants.NUM_GENO_STATES, windowSize ); // Can have missing values as genotype
		scoreMap = new HashMap<Integer, double[]>();
		badSet = new HashSet<Integer>();

        resetReader();
	}
	
	public int getWindowSize() {
		return windowSize;
	}
	
	public boolean isBad( int index ) {
		return badSet.contains(index);
	}
	
	public void setBadIndex( int index ) {
		badSet.add( index );
	}
	
	
	public void setScore( int windowIdx, double scoresArr[] ) {
		scoreMap.put( windowIdx, scoresArr );
	}

    /**
     * Clients can let us know that they will no longer need access to
     * a window index, so that we can free up memory.
     */
    public void releaseScore(int windowIdx) {
        scoreMap.remove(windowIdx);
    }
	
	public double[] getScore(int windowIdx) throws IOException {
        boolean doneReset = false;
        while (true) {
            double[] s = scoreMap.get(windowIdx);
            if (s != null)
                return s;

            if (!loadNextBatch(BATCH_SIZE)) {
                if (doneReset) {
                    return null;
                } else {
                    resetReader();
                    doneReset = true;
                }
            }
        }
	}
	
	public double getScore(int windowIdx, int conf1, int conf2) throws IOException {
		double[] scoreArr = getScore(windowIdx);
        if (scoreArr != null) {
            return scoreArr[conf2 * numConfs + conf1];
        } else {
            String msg = "winIdx: " + windowIdx +
                " conf1: " + conf1 +
                " conf2: " + conf2 +
                " numConfs: " + numConfs +
                " idx:_" + (conf2 * numConfs + conf1) + "_";
            System.err.println(msg);
            throw new IllegalStateException(msg);
        }
	}

    public static Scores load(String fileName, int windowSize) throws IOException {
        Scores s = new Scores(windowSize, fileName);
        while (s.loadNextBatch(BATCH_SIZE));
        return s;
    }

    public static Scores lazyLoad(String fileName, int windowSize) throws IOException {
        return new Scores(windowSize, fileName);
    }

    private void resetReader() throws IOException {
        if (reader != null)
            reader.close();
        reader = new BufferedReader(new FileReader(fileName));
    }

    public boolean loadNextBatch(int numRows) throws IOException {
        System.err.println("Loading score batch.");

        int numConfs = (int) Math.pow(Constants.NUM_GENO_STATES, windowSize); // Can have missing values as genotype

        for (int n = 0; n < numRows; ++n) {
            String line = this.reader.readLine();
            if (line == null)
                return n != 0;

            StringTokenizer st = new StringTokenizer(line);
            int windowIdx = Integer.decode(st.nextToken());
            double fullScoreMatrix[] = new double[numConfs * numConfs];
            for (int i = 0; i < numConfs; i++) {
                for (int j = i; j < numConfs; j++) {
                    String valStr = st.nextToken();
                    if (isBad(valStr))
                        setBadIndex(windowIdx);
                    double v = Double.parseDouble(normalize(valStr));
                    fullScoreMatrix[i * numConfs + j] = v;
                    fullScoreMatrix[j * numConfs + i] = v;
                }
            }

            //  Inflate score to full matrix
            setScore(windowIdx, fullScoreMatrix);
            rowsLoaded++;
        }

        return true;
    }

	private static String normalize( String str ) {
		if (str.indexOf("inf")!=-1 || str.indexOf("nan")!=-1) return "0";
		return str;
	}
	
	private static boolean isBad( String str ) {
		if (str.indexOf("inf")!=-1 || str.indexOf("nan")!=-1) return true;
		return false;
	}

    private static final int BATCH_SIZE = 200;

	private int rowsLoaded;
    private int windowSize;
    private int numConfs;
    private String fileName;
    private BufferedReader reader;
	private HashMap<Integer, double[]> scoreMap;
	private HashSet<Integer> badSet;
	
}
