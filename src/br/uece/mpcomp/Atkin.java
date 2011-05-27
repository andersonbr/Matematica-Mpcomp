package br.uece.mpcomp;

import java.util.ArrayList;
import java.util.List;

public class Atkin {
	private List<Integer> primes;
	private Integer limit;

	public Atkin(Integer limit) {
		this.limit = limit;
		primes = new ArrayList<Integer>();
		this.encontrarPrimos();
	}

	public List<Integer> getPrimes() {
		return this.primes;
	}

	private void encontrarPrimos() {
		Boolean[] isPrime = new Boolean[this.limit + 1];
		for (int i = 0; i < isPrime.length; i++) {
			isPrime[i] = false;
		}
		int sqrt = (int) Math.sqrt(limit);
		for (int x = 1; x <= sqrt; x++) {
			for (int y = 1; y <= sqrt; y++) {
				int n = 4 * x * x + y * y;
				if (n <= limit && (n % 12 == 1 || n % 12 == 5))
					isPrime[n] ^= true; // flip

				n = 3 * x * x + y * y;
				if (n <= limit && n % 12 == 7)
					isPrime[n] ^= true;

				n = 3 * x * x - y * y;
				if (x > y && n <= limit && n % 12 == 11)
					isPrime[n] ^= true;
			}
		}
		for (int n = 5; n <= sqrt; n++)
			if (isPrime[n]) {
				int s = n * n;
				for (int k = s; k <= limit; k += s)
					isPrime[k] = false;
			}
		primes.add(2);
		primes.add(3);
		for (int n = 5; n <= limit; n += 2)
			if (isPrime[n])
				primes.add(n);
	}

	public static void main(String[] args) {
		Atkin atkin = new Atkin(30);
		List<Integer> primes = atkin.getPrimes();
		int n = 1;
		for (Integer i : primes) {
			System.out.println("primo ("+n+"): " + i);
			n++;
		}
	}
}