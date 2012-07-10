package Model;

import org.apache.commons.math3.analysis.function.Gaussian;

public class WinModel {

  public static final double GAUSS_MAX_Z = 8; // Highest z-score
  public static final double GAUSS_MIN_PROB = (new Gaussian(0, 1)).value(GAUSS_MAX_Z); // Lowestp rob we can get out of GSL
  public static final double MIN_SD = 1e-9;
	

	public WinModel( int winID, double muUnrelated, double muRelated, double sigmaUnrelated, double sigmaRelated ) {
		this.winID = winID;
		this.muUnrelated = muUnrelated;
		this.muRelated = muRelated;
		this.sigmaRelated = sigmaRelated;
		this.sigmaUnrelated = sigmaUnrelated;
		
		gRelated = new Gaussian( muRelated, sigmaRelated );
		gUnrelated = new Gaussian( muUnrelated, sigmaUnrelated );
	}
	
	public int getWinID() {
		return winID;
	}
	
	public double getMuUnrelated() {
		return muUnrelated;
	}
	
	public double getMuRelated() {
		return muRelated;
	}
	
	public double getSigmaUnrelated() {
		return sigmaUnrelated;
	}
	
	public double getSigmaRelated() {
		return sigmaRelated;
	}
	
	public double logLikelihoodRatio( double lrt1 ) {

    double prob_u = gUnrelated.value(lrt1);
    double prob_r = gRelated.value(lrt1);
    
    if (prob_u < GAUSS_MIN_PROB || getSigmaUnrelated() <= MIN_SD) // If sd_u/r == 0, then gauss returns nan...
      prob_u = GAUSS_MIN_PROB;
    if (prob_r < GAUSS_MIN_PROB || getSigmaRelated() <= MIN_SD) // If sd_u/r == 0, then gauss returns nan...
      prob_r = GAUSS_MIN_PROB;

    double lrt2 = (Math.log(prob_r) - Math.log(prob_u)) / Math.log(10);

    // Avoid tail issues (if it's obviously far to the right side, it should show up as related not unrelated)
    if (lrt1 > getMuRelated() && prob_r < prob_u && getMuRelated() > getMuUnrelated())
      lrt2 *= -1;

		return lrt2;
	}
	
	private int winID;
	private double muUnrelated;
	private double muRelated;
	private Gaussian gRelated;
	private Gaussian gUnrelated;
	private double sigmaUnrelated;
	private double sigmaRelated;

}
