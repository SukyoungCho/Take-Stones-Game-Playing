public class Helper {

	/**
	 * Class constructor.
	 */
	private Helper() {
	}

	/**
	 * This method is used to check if a number is prime or not
	 * 
	 * @param x
	 *            A positive integer number
	 * @return boolean True if x is prime; Otherwise, false
	 */
	public static boolean isPrime(int x) {

		// TODO Add your code here
		// If x is less or equal to 1
		if (x <= 1)
			return false;

		// Check if x is not prime, then return false
		for (int i = 2; i < x; i++)
			if (x % i == 0)
				return false;
		// if x is prime
		return true;
	}

	/**
	 * This method is used to get the largest prime factor
	 * 
	 * @param x
	 *            A positive integer number
	 * @return int The largest prime factor of x
	 */
	public static int getLargestPrimeFactor(int x) {

		// TODO Add your code here

		// Initialize the maximum prime factor variable
		int maxPrime = -1;

		// Print the number of 2s that divide n
		while (x % 2 == 0) {
			maxPrime = 2;
			// equivalent to n /= 2
			x >>= 1;
		}

		// n must be odd at this point, thus skip the even numbers
		// and iterate only for odd integers
		for (int i = 3; i <= Math.sqrt(x); i += 2) {
			while (x % i == 0) {
				maxPrime = i;
				x = x / i;
			}
		}

		// When n is a prime number greater than 2
		if (x > 2)
			maxPrime = x;

		return maxPrime;
	}
}