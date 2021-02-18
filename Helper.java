public class Helper {
	
	/** 
    * Class constructor.
    */
	public Helper () {}

	/**
	* This method is used to check if a number is prime or not
	* @param x A positive integer number
	* @return boolean True if x is prime; Otherwise, false
	*/
	public static boolean isPrime(int x) {
		boolean prime = true;
		for(int i = 2; i <= x/2; i++) { // check the factors from 2 to n/2
			if(x % i == 0) { // if x divisible by any of the number between 2 and x/2, x is not prime
				prime = false;
				break;
			}
		}
		if(prime) {
			return true;
		}
		return false;
	}

	/**
	* This method is used to get the largest prime factor 
	* @param x A positive integer number
	* @return int The largest prime factor of x
	*/
	public static int getLargestPrimeFactor(int x) {
		int factor = 0;
		if(isPrime(x)) {
			return x;
		}
		for(int i = 2; i <= x/2; i++) {
			if(x % i == 0) {
				factor = i;
				x = x/i;
				i--;
			}
		}
		return factor;

    }
}