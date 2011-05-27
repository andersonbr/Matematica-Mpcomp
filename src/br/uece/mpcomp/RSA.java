package br.uece.mpcomp;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Geração de chave privada e pública usando algoritmo RSA
 * 
 * @author Anderson Bezerra Calixto
 */
public class RSA {

	private BigInteger n, d, e;
	int maxencoded = 0;

	public RSA(int numBits) {

		/**
		 * gera dois numeros primos p e q de tamanho bitlen/2
		 */
		SecureRandom r = new SecureRandom();
		r.generateSeed((numBits/8));
		BigInteger p = new BigInteger(numBits / 2, 100, r);
		BigInteger q = null;
		boolean primosDiferentes = false;
		while(!primosDiferentes) {
			r.generateSeed((numBits/8));
			q = new BigInteger(numBits / 2, 100, r);
			if (q.compareTo(p)!=0)
				primosDiferentes ^= true;
		}

		/**
		 * n = p * q
		 */
		n = p.multiply(q);
		
		/**
		 * tamanho maximo da msg codificada
		 */
		this.maxencoded = n.toByteArray().length;

		/**
		 * phi = (p-1)*(q-1)
		 */
		BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q
				.subtract(BigInteger.ONE));

		/**
		 * e -> aleatorio > 1 e < phi
		 * e -> deve ser coprimo de phi
		 */
		e = new BigInteger(numBits, r).mod(phi);
		if (e.mod(BigInteger.ONE.add(BigInteger.ONE)).compareTo(BigInteger.ZERO)==0) {
			e = e.add(BigInteger.ONE);
		}
		while (phi.gcd(e).intValue() > 1) {
			e = e.add(new BigInteger("2"));
		}
		
		/**
		 * d = (e^-1) mod phi
		 */
		d = e.modInverse(phi);
		System.out.println("p: "+p);
		System.out.println("q: "+q);
		System.out.println("n: "+n);
		System.out.println("phi: "+phi);
		System.out.println("e: "+e);
		System.out.println("d: "+d);
		System.out.println("n: "+n);
	}

	/**
	 * Chave pública - todos podem codificar a mensagem
	 */
	public BigInteger encrypt(BigInteger m) {
		/**
		 * (m ^ e) mod n
		 */
		return m.modPow(e, n);
	}

	/**
	 * Somente quem tem a chave privada pode descodificá-la
	 */
	public BigInteger decrypt(BigInteger m) {
		/**
		 * (m ^ d) mod n
		 */
		return m.modPow(d, n);
	}
	
	public String printHex(byte[] bytes) {
		String ret = "";
		int incr = 0;
		for (int i=0; i<bytes.length; i++) {
			int ival = bytes[i];
			String m = "0"+Long.toHexString(ival&0xFF).toUpperCase(); 
			ret += (m.length()==3)?m.substring(1, 3):m;
			if (i!=(bytes.length-1)) ret+="";
			incr++;
			if (incr==1) {
				ret += " ";
				incr = 0;
			}
		}
		return ret;
	}
	
	public String printHexCoded(byte[] bytes) {
		String ret = "";
		int incr = 0;
		for (int i=0; i<bytes.length; i++) {
			int ival = bytes[i];
			String m = "0"+Long.toHexString(ival&0xFF).toUpperCase(); 
			ret += (m.length()==3)?m.substring(1, 3):m;
			if (i!=(bytes.length-1)) ret+="";
			incr++;
			if (incr==maxencoded) {
				ret += " ";
				incr = 0;
			}
		}
		return ret;
	}

	private byte[] getEncodedBytes(byte[] bnome) {
		byte[] ret = new byte[bnome.length*maxencoded];
		int retIndex = 0;
		for (int i=0; i<bnome.length; i++) {
			Integer currentByte = (int)bnome[i];
			BigInteger b = new BigInteger(currentByte.toString());
			byte[] bval = this.encrypt(b).toByteArray();
			retIndex += (maxencoded-bval.length);
			for (int z = 0; z< bval.length; z++) {
				ret[retIndex] = (byte) (bval[z] & 0xFF);
				retIndex++;
			}
		}
		return ret;
	}

	private byte[] getDecodedBytes(byte[] bnome) {
		byte[] ret = new byte[bnome.length/maxencoded];
		int retIndex = 0;
		for (int i=0; i<bnome.length;) {
			byte[] bval = new byte[maxencoded];
			for (int z = 0; z< bval.length; z++) {
				bval[z] = bnome[i];
				i++;
			}
			BigInteger b = new BigInteger(bval);
			ret[retIndex] = (byte) this.decrypt(b).intValue();
			retIndex++;
		}
		return ret;
	}

	public static void main(String[] args) {
		RSA rsa = new RSA(32);
		String nome = "Anderson";
		byte[] bnome = nome.getBytes();
		byte[] bcodificado = rsa.getEncodedBytes(bnome);
		byte[] bdescodificado = rsa.getDecodedBytes(bcodificado);
		System.out.println("Texto Original: "+nome);
		System.out.println("Original (Hex): "+rsa.printHex(bnome));
		System.out.println("Codificado (Hex): "+rsa.printHexCoded(bcodificado));
		System.out.println("Descodificado: "+new String(bdescodificado));
	}

}
