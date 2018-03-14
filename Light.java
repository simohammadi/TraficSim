package traficsim;

import java.util.ArrayList;

/**
 * Represents a traffic light. 
 */
public class Light {
	private ArrayList<String> lightArr = new ArrayList<String>();
	private ArrayList<Integer> clockArr = new ArrayList<Integer>();

	/**
	 * Constructs and initializes a light.
	 * @param period the total period
	 * @param green the number of time steps the signal is green
	 */
	public Light (int period, int green) {
		for (int i = 0; i < period; i++){

			clockArr.add(i);
 
			if (i < green){
				lightArr.add("G");
			}
			else {
				lightArr.add("R");
			}
		}
	}

	/**
	 * Advances the internal clock.
	 */
	public void step() {
		clockArr.add(clockArr.size() ,clockArr.get(0));
		clockArr.remove(0);
	}

	/**
	 * Checks if the light is green.
	 * @return true if the light is green else false
	 */
	public boolean isGreen() {
		if (lightArr.get(clockArr.get(0)) == "G"){
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Returns a string representation of the signal. 
	 * The representation indicates if the signal is green or red. 
	 * @return a string representation
	 */
	public String toString() {
		return  lightArr.get(clockArr.get(0));
	}

	/**
	 * Demonstrates the stepping of a signal.
	 */
	public static void main(String[] args) {
		Light s1 = new Light(4,2);
		System.out.println(s1.toString());
		s1.step();
		System.out.println(s1.toString());
		s1.step();
		System.out.println(s1.toString());

	}
}
