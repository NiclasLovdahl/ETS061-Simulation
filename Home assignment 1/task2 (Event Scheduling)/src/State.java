import java.util.*;

class State extends GlobalSimulation {

	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int numberInQueue = 0, accumulated = 0, noMeasurements = 0;

	Random slump = new Random(); // This is just a random number generator
	public LinkedList<String> buffer = new LinkedList<String>();

	// The following method is called by the main program each time a new event
	// has been fetched
	// from the event list in the main loop.
	public void treatEvent(Event x) {
		switch (x.eventType) {
		case ARRIVALA:
			arrivalA();
			break;
		case ARRIVALB:
			arrivalB();
			break;
		case READY:
			ready();
			break;
		case ARRIVALAEXP:
			arrivalAExp();
			break;
		case ARRIVALBEXP:
			arrivalBExp();
			break;
		case READYEXP:
			readyExp();
			break;
		case ARRIVALAPRIOA:
			arrivalAPrioA();
			break;
		case ARRIVALBPRIOA:
			arrivalBPrioA();
			break;
		case READYPRIOA:
			readyPrioA();
			break;
		case MEASURE:
			measure();
			break;
		}
	}

	// The following methods defines what should be done when an event takes
	// place. This could
	// have been placed in the case in treatEvent, but often it is simpler to
	// write a method if
	// things are getting more complicated than this.

	private void arrivalA() {
		if (numberInQueue == 0)
			insertEvent(READY, time + 0.002);
		numberInQueue++;
		buffer.addFirst("a");
		insertEvent(ARRIVALA, time + expDist((double) 1 / 150));
	}

	private void arrivalB() {
		if (numberInQueue == 0)
			insertEvent(READY, time + 0.004);
		numberInQueue++;
		buffer.addLast("b");
	}

	private void ready() {
		numberInQueue--;
		if (numberInQueue > 0) {
			String temp = buffer.poll();
			if (temp.equals("a")) {
				insertEvent(READY, time + 0.002);
				insertEvent(ARRIVALB, time + 0.002 + 1.0);
			} else {
				insertEvent(READY, time + 0.004);
			}
		}
	}

	private void arrivalAExp() {
		if (numberInQueue == 0)
			insertEvent(READYEXP, time + 0.002);
		numberInQueue++;
		buffer.addFirst("a");
		insertEvent(ARRIVALAEXP, time + expDist((double) 1 / 150));
	}

	private void arrivalBExp() {
		if (numberInQueue == 0)
			insertEvent(READYEXP, time + 0.004);
		numberInQueue++;
		buffer.addLast("b");
	}

	private void readyExp() {
		numberInQueue--;
		if (numberInQueue > 0) {
			String temp = buffer.poll();
			if (temp.equals("a")) {
				insertEvent(READYEXP, time + 0.002);
				insertEvent(ARRIVALBEXP, time + 0.002 + expDist(1));
			} else {
				insertEvent(READYEXP, time + 0.004);
			}
		}
	}

	private void arrivalAPrioA() {
		if (numberInQueue == 0)
			insertEvent(READYPRIOA, time + 0.002);
		numberInQueue++;
		buffer.addLast("a");
		insertEvent(ARRIVALAPRIOA, time + expDist((double) 1 / 150));
	}

	private void arrivalBPrioA() {
		if (numberInQueue == 0)
			insertEvent(READYPRIOA, time + 0.004);
		numberInQueue++;
		buffer.addFirst("b");
	}

	private void readyPrioA() {
		numberInQueue--;
		if (numberInQueue > 0) {
			String temp = buffer.poll();
			if (temp.equals("a")) {
				insertEvent(READYPRIOA, time + 0.002);
				insertEvent(ARRIVALBPRIOA, time + 0.002 + 1.0);
			} else {
				insertEvent(READYPRIOA, time + 0.004);
			}
		}
	}

	private void measure() {
		accumulated = accumulated + numberInQueue;
		noMeasurements++;
		insertEvent(MEASURE, time + 0.1);
	}

	private double expDist(double mean) {
		return -(mean) * Math.log(slump.nextDouble());
	}
}