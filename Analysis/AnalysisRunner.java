package Analysis;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import Model.Block;
import Model.Blocks;
import Model.Index;
import Model.Labels;
import Model.Query;
import Model.Relations;
import Model.Scores;

public class AnalysisRunner {

	public static void main(String[] args) {

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
		if ( args.length!=1 ) {
			System.err.println("Configuration file name is missing.");
			System.exit(-1);
		}
		String confFileName = args[0];
		System.err.println("Loading experiment properties:");
		Properties experimentConf = new Properties();
		try {
			experimentConf.load(new FileReader( confFileName ));
		} catch ( IOException exp ) {
			System.err.println("Could not load input files.");
			exp.printStackTrace();
			System.exit(-1);
		}
		
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
		LinearAnalysis la = new LinearAnalysis();
		// for ( String queryID : query ) {
    for (int q = 0; q < query.getNumIndividuals(); ++q) {
      String queryID = query.getName(q);
			System.err.println("Analyzing "+ queryID );
			int queryConfs[] = query.getIndConf( queryID );

			int relatedIndividuals[] = la.getRelated( labels, queryConfs, index, blocks, scores, relations, queryID );
			for ( int i : relatedIndividuals ) {
				System.out.println("FOUND RELATED:\t"+ queryID+"\t"+labels.getString(i) );
			}
//			break;
		}
		
		
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
		String indexFN = experimentConf.getProperty("indexFile");
		System.err.println("Loading indexed individuals"+ indexFN );
		index = Index.load( indexFN, windowSize );
		
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
		//	Load block file
		String blockFN = experimentConf.getProperty("blockFile");
		System.err.println("Loading block-information file"+blockFN);
		blocks = Blocks.load( blockFN );
		
		//
		//	Load score files
		String scoreFN = experimentConf.getProperty("scoreFile");
		System.err.println("Loading window scores"+ scoreFN );
		scores = Scores.load( scoreFN, windowSize );
	}
	
  /*
	private static void initLog() {
		BasicConfigurator.configure();
		System.err.println("HGI.Analysis.Runner");
	}
  */
	
	// private static Logger logger;
	
	private static Labels labels = null;
	private static Index index = null;
	private static Query query = null;
	private static Relations relations = null;
	private static Blocks blocks = null;
	private static Scores scores = null;

	private static int windowSize;
	private static int numConfigurations;
	
}
