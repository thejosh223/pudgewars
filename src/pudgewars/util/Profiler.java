package pudgewars.util;

public class Profiler {

	public String names[];
	public double avg[];
	public long total[];
	public int count[];

	// Temps
	private long tempTime;
	private int current;
	private int tickCounter;

	public Profiler(String[] names) {
		this.names = names;

		avg = new double[names.length];
		count = new int[names.length];
		total = new long[names.length];

		current = 0;
	}

	public void start() {
		tempTime = System.nanoTime();
	}

	public void end() {
		total[current] += System.nanoTime() - tempTime;
		tempTime = 0;

		current = (current + 1) % avg.length;
		if (current == 0) tickCounter++;
	}

	public void printResults() {
		System.out.println("---");
		for (int i = 0; i < names.length; i++) {
			avg[i] = ((avg[i] * count[i]) + total[i] / 1000000000.0) / (count[i] + tickCounter);
			count[i] += tickCounter;
			total[i] = 0;
			System.out.println(names[i] + ": " + (avg[i] * Time.TICKS_PER_SECOND));
		}
		tickCounter = 0;
	}
}
