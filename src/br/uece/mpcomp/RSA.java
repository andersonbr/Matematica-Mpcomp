package br.uece.mpcomp;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Gera‹o de chave privada e pœblica usando algoritmo RSA
 * 
 * @author Anderson Bezerra Calixto
 */
public class RSA {

	private BigInteger n, d, e;

	public RSA(int bitlen) {
		SecureRandom r = new SecureRandom();
		BigInteger p = new BigInteger(bitlen / 2, 100, r);
		BigInteger q = new BigInteger(bitlen / 2, 100, r);
		System.out.println("p: "+p+", q: "+q);
		n = p.multiply(q);
		BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q
				.subtract(BigInteger.ONE));
		e = new BigInteger("3");
		while (m.gcd(e).intValue() > 1)
			e = e.add(new BigInteger("2"));
		d = e.modInverse(m);
	}

	public BigInteger encrypt(BigInteger message) {
		return message.modPow(e, n);
	}

	public BigInteger decrypt(BigInteger message) {
		return message.modPow(d, n);
	}

	public static void main(String[] args) {
		RSA rsa = new RSA(1024);
		BigInteger original = new BigInteger("1024");
		BigInteger codificado = rsa.encrypt(original);
		BigInteger descodificado = rsa.decrypt(codificado);
		System.out.println(original+" - "+codificado+" - "+descodificado);
	}
}
