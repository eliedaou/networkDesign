package daoumoyer.tcp;

/**
 * @author Grant Moyer
 * @since 2015-12-06
 */
public class EstimatedRTT {
	//Constants
	private static final double alpha = 0.125;
	private static final double beta = 0.25;

	private double rtt;
	private double deviation;
	private boolean timeout;

	public EstimatedRTT() {
		rtt = 1000;
		deviation = 0;
		timeout = false;
	}

	public void addSample(int sample) {
		deviation = (1 - beta) * deviation + beta * Math.abs(sample - rtt);
		rtt = (1 - alpha) * rtt + alpha * sample;
	}

	public void setTimeout(boolean timeout) {
		this.timeout = timeout;
	}

	public int getEstimate() {
		return (int) rtt;
	}

	public double getEstimateDouble() {
		return rtt;
	}

	public double getDeviation() {
		return deviation;
	}

	public int getTimeoutInterval() {
		return (int) Math.ceil((rtt + 4 * deviation) * (timeout ? 2.0 : 1.0));
	}
}
