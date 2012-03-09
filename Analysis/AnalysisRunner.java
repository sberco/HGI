package Analysis;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

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
		initLog();
		
		//
		//	Start analysis
		logger.info("Starting analysis.");
		
		////////////////////////////////////////////////////////////////////////////////
		//
		//	Load experiment configuration.
		//
		////////////////////////////////////////////////////////////////////////////////		
		if ( args.length!=1 ) {
			logger.error("Configuration file name is missing.");
			System.exit(-1);
		}
		String confFileName = args[0];
		logger.info("Loading experiment properties:");
		Properties experimentConf = new Properties();
		try {
			experimentConf.load(new FileReader( confFileName ));
		} catch ( IOException exp ) {
			logger.error("Could not load input files.");
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
			logger.info("Loading input files.");
			loadInputFiles(experimentConf);						
		} catch ( IOException exp ) {
			logger.error("Could not load input files.");
			exp.printStackTrace();
			System.exit(-1);
		}
		
		////////////////////////////////////////////////////////////////////////////////
		//
		//	Perform the analysis.
		//
		////////////////////////////////////////////////////////////////////////////////		
		logger.info("Applying analysis.");
		LinearAnalysis la = new LinearAnalysis();
		for ( String queryID : query ) {
			logger.info("Analyzing "+ queryID );
			int queryConfs[] = query.getIndConf( queryID );
			int relatedIndividuals[] = la.getRelated( labels, queryConfs, index, blocks, scores );
			for ( int i : relatedIndividuals ) {
				logger.info("FOUND RELATED:"+ queryID+"\t"+i );
			}
			break;
		}
		
		
		//
		//	Analysis done
		logger.info("Analysis done.");
	}

	private static void loadInputFiles(Properties experimentConf)
			throws IOException {
		//
		//	Load index labels
		String labelFN = experimentConf.getProperty("labelFile");
		logger.info("Loading index-individuals labels:"+ labelFN );
		labels = Labels.load( labelFN );
		
		//
		//	Load index file
		String indexFN = experimentConf.getProperty("indexFile");
		logger.info("Loading indexed individuals"+ indexFN );
		index = Index.load( indexFN, windowSize );
		
		//
		//	Load query individuals
		String queryFN = experimentConf.getProperty("queryFile");
		logger.info("Loading query individuals "+ queryFN );
		query = Query.load( queryFN );
		
		//
		//	Load relationship file
		String relationshipFN = experimentConf.getProperty("relationshipFile");
		logger.info("Loading relationship file.");
		relations = Relations.load( relationshipFN );
		
		//
		//	Load block file
		String blockFN = experimentConf.getProperty("blockFile");
		logger.info("Loading block-informatiopn file"+blockFN);
		blocks = Blocks.load( blockFN );
		
		//
		//	Load score files
		String scoreFN = experimentConf.getProperty("scoreFile");
		logger.info("Loading window scores"+ scoreFN );
		scores = Scores.load( scoreFN, windowSize );
	}
	
	private static void initLog() {
		BasicConfigurator.configure();
		logger = Logger.getLogger("HGI.Analysis.Runner");
	}
	
	private static Logger logger;
	
	private static Labels labels = null;
	private static Index index = null;
	private static Query query = null;
	private static Relations relations = null;
	private static Blocks blocks = null;
	private static Scores scores = null;

	private static int windowSize;
	private static int numConfigurations;
	
}
