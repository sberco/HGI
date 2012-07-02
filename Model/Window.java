package Model;

public class Window implements Comparable<Window> {
	
	public Window( int ID, int firstWindow, int lastWindow, double threshold, double distCM, double startCM, double stopCM ) {
		this.ID = ID;
		this.firstWindow = firstWindow;
		this.lastWindow = lastWindow;
		this.threshold = threshold;
		this.distCM = distCM;
		this.startCM = startCM;
		this.stopCM = stopCM;
	}
	
	public int compareTo(Window o) {
		return firstWindow-o.firstWindow;
	}
	
	public boolean equals(Object arg0) {
		return super.equals(arg0);
	}

	public int getID() {
		return ID;
	}
	
	public int getFirstWindow() {
		return firstWindow;
	}
	
	public int getLastWindow() {
		return lastWindow;
	}
	
	public double getThreshold() {
		return threshold;
	}
	
	public double getStartCM() {
		return startCM;
	}
	
	public double getStopCM() {
		return stopCM;
	}
	
	public double getDistCM() {
		return distCM;
	}
	
	private int ID;
	private int firstWindow;
	private int lastWindow;
	private double threshold;
	private double distCM;
	private double startCM;
	private double stopCM;
}
