package Model;

import org.apache.commons.math3.analysis.function.Gaussian;

public class WinModel {

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
	
	public double logLikelihoodRatio( double s ) {
		return (Math.log( gRelated.value(s) ) - Math.log( gUnrelated.value(s) )) / Math.log(10);
	}
	
	private int winID;
	private double muUnrelated;
	private double muRelated;
	private Gaussian gRelated;
	private Gaussian gUnrelated;
	private double sigmaUnrelated;
	private double sigmaRelated;

}
