package Model;

public class Window implements Comparable<Window> {
	
	public Window( int ID, double startCM, double stopCM, int snpIdx[] ) {
		this.ID = ID;
		this.snpIdx = snpIdx;
		this.startCM = startCM;
		this.stopCM = stopCM;
	}
	
	public int[] getSNPs() {
		return snpIdx;
	}
	
	public int compareTo(Window o) {
		if ( startCM-o.startCM>0 ) return 1;
		if ( startCM-o.startCM<0 ) return -1;
		return 0;
	}
	
	public boolean equals(Object arg0) {
		return super.equals(arg0);
	}

	public int getID() {
		return ID;
	}
	
	public double getStartCM() {
		return startCM;
	}
	
	public double getEndCM() {
		return stopCM;
	}

  public int getFirstSnp() {
    return getSNPs()[0];
  }

  public int getLastSnp() {
    return getSNPs()[getSNPs().length - 1];
  }
		
	private int ID;
	private double startCM;
	private double stopCM;
	private int[] snpIdx;
}
