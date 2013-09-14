package robots.util;

public class VisitCounter {
	public static int NUM_BINS = 47; // aparentemente, o povo do robocode gosta
										// dessa constante... vai ela mesmo
	public double[] bins = null;
	
	public VisitCounter() {
		bins = new double[NUM_BINS];
		for (int i = 0; i < bins.length; i++) {
			bins[i] = 0;
		}
	}
	
	

}
