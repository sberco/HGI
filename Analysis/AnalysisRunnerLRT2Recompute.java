package Analysis;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import Model.Block;
import Model.Blocks;
import Model.Index;
import Model.Labels;
import Model.Query;
import Model.Relations;
import Model.Scores;
import Model.WinModels;
import Model.Windows;
import Model.Result;

import Utils.Common;

/**
 * 
 * Under this analysis running, LRT1 scores for larger windows are recomputed on 
 * of the fly based on LRT1 scores. Based on the LRT1 score on the extended
 * window, we compute the LRT2 and examine the corresponding Tau threshold.
 *
 */
public class AnalysisRunnerLRT2Recompute {

	public static void main(String[] args) throws java.io.IOException {

		//
		//	Initialize logging
		//initLog();

		//
		//	Start analysis
		System.err.println("Starting analysis.");

		////////////////////////////////////////////////////////////////////////////////
		//
		//	Load experiment configuration.
		//
		////////////////////////////////////////////////////////////////////////////////		
		if ( args.length < 1 || args[0].equals("-h") ) {
			System.err.println("args: <config_file>");
			System.exit(-1);
		}
		
		String confFileName = args[0];
		BufferedWriter calledBlockIO = null;

		System.err.println("Loading experiment properties:");
		Properties experimentConf = new Properties();
		try {
			experimentConf.load(new FileReader( confFileName ));
		} catch ( IOException exp ) {
			System.err.println("Could not load input files.");
			exp.printStackTrace();
			System.exit(-1);
		}

		String callFN = experimentConf.getProperty("outCallFile");
		calledBlockIO = new BufferedWriter(new FileWriter(callFN));

		//
		//	Compute window size
		windowSize = Integer.parseInt( experimentConf.getProperty("windowSize") );
		numConfigurations = (int)Math.pow(3, windowSize );


		////////////////////////////////////////////////////////////////////////////////
		//
		//	Load data files.
		//
		////////////////////////////////////////////////////////////////////////////////
		try {
			System.err.println("Loading input files.");
			loadInputFiles(experimentConf);						
		} catch ( IOException exp ) {
			System.err.println("Could not load input files.");
			exp.printStackTrace();
			System.exit(-1);
		}

		////////////////////////////////////////////////////////////////////////////////
		//
		//	Perform the analysis.
		//
		////////////////////////////////////////////////////////////////////////////////		
		System.err.println("Applying analysis.");
		LinearAnalysisComputeLRT1 la = new LinearAnalysisComputeLRT1();

		calledBlockIO.write("#queryName\thitName\tisCorrectPair\tblockIdx\twinStart\twinEnd\tsnpStart\tsnpEnd\tcallScore\tnumRHs\n"); // header

		// for ( String queryID : query ) {
		for (int q = 0; q < query.getNumIndividuals(); ++q) {

			String queryID = query.getName(q);
			System.err.println("Analyzing "+ queryID );
			int queryConfs[] = query.getIndConf( queryID );

			Vector<Vector<Result> > results = new Vector<Vector<Result> >();

			int relatedIndividuals[] = la.getRelated( labels, queryConfs, snpIndex, blocks, windows, winModels, w1Scores, relations, queryID, results );

			int i = 0;
			for ( int ind : relatedIndividuals ) {
				System.out.println("FOUND RELATED:\t"+ queryID+"\t"+labels.getString(ind) );

				for (Result result : results.get(ind))
				{
					String correct = "F";
					String indName = labels.getString(ind);

					if (relations.isRelated(queryID, indName))
						correct = "T";

					Block block = result.block;

					int wstart = block.getFirstWindow();
					int wend   = block.getLastWindow() + 1;
					int mstart = windows.get(wstart).getFirstSnp();
					int mend   = windows.get(wend - 1).getLastSnp() + 1;

					calledBlockIO.write(queryID + "\t" + indName + "\t" + correct + "\t" + result.block.getID()
					    + "\t" + wstart + "\t" + wend
					    + "\t" + mstart + "\t" + mend 
					    + "\t" + result.score
					    + "\t" + Common.CountReverseHomozygotes(windows, block, query, snpIndex, q, ind)
					    + "\n");
				}

				++i;
			}

			//			break;
		}

		calledBlockIO.close();


		//
		//	Analysis done
		System.err.println("Analysis done.");
	}

	private static void loadInputFiles(Properties experimentConf)
			throws IOException {

		//
		//	Load index labels
		String labelFN = experimentConf.getProperty("labelFile");
		System.err.println("Loading index-individuals labels:"+ labelFN );
		labels = Labels.load( labelFN );
		
		//
		//	Load index file
		String w1IndexFN = experimentConf.getProperty("w1IndexFile");
		System.err.println("Loading indexed individuals"+ w1IndexFN );
		snpIndex = Index.load( w1IndexFN, windowSize );


		//
		//	Load query individuals
		String queryFN = experimentConf.getProperty("queryFile");
		System.err.println("Loading query individuals "+ queryFN );
		query = Query.load( queryFN );

		//
		//	Load relationship file
		String relationshipFN = experimentConf.getProperty("relationshipFile");
		System.err.println("Loading relationship file.");
		relations = Relations.load( relationshipFN );
		
		//
		//	Load window file : which snps are in which window
		String windowFN = experimentConf.getProperty("windowFile");
		System.err.println("Loading window-information file"+windowFN);
		windows = Windows.load( windowFN );
		
		//
		//	Load window LRT2 file
		String windowModelFN = experimentConf.getProperty("winModel");
		System.err.println("Loading window-information file"+windowModelFN);
		winModels = WinModels.load( windowModelFN );
		

		//
		//	Load block file
		String blockFN = experimentConf.getProperty("blockFile");
		System.err.println("Loading block-information file"+blockFN);
		blocks = Blocks.load( blockFN );
		
		//
		//	LRT1 score of W=1
		String w1ScoreFN = experimentConf.getProperty("w1ScoreFile");
		System.err.println("Loading single-snp window LRT1 scores"+ w1ScoreFN );
		int w1WindowSize = 1;
		w1Scores = Scores.load( w1ScoreFN, w1WindowSize );

	}

	private static Labels labels = null;
	private static Index snpIndex = null;
	private static Query query = null;
	private static Relations relations = null;
	private static Blocks blocks = null;
	private static Windows windows = null;
	private static WinModels winModels = null;
	private static Scores w1Scores = null;

	private static int windowSize;
	private static int numConfigurations;

}
