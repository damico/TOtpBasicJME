package com.tix11.jme.totp;



import javame.helpers.BigInteger;

import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;


/**
 * This is a minimal implementation of the OATH TOTP algorithm with HMAC_SHA1 for JME.
 * This code is based on RFC6238 (http://tools.ietf.org/html/rfc6238)
 * and it is a derivation from a previous code written by Johan Rydell
 *
 * @author Jose Damico <jd.comment@gmail.com>
 */
public class TotpImpl {
									//  0 1  2   3    4     5      6       7        8
	private final int[] DIGITS_POWER = {1,10,100,1000,10000,100000,1000000,10000000,100000000 };
	private static TotpImpl INSTANCE = null;
	public static TotpImpl getInstance(){
		if(INSTANCE == null) INSTANCE = new TotpImpl();
		return INSTANCE;
	}
   
	private TotpImpl() {}

	/**
	 * This method uses the JCE to provide the crypto algorithm.
	 * HMAC computes a Hashed Message Authentication Code with the
	 * crypto hash algorithm as a parameter.
	 * @param crypto     the crypto algorithm (HmacSHA1, HmacSHA256, HmacSHA512)
	 * @param keyBytes   the bytes to use for the HMAC key
	 * @param text       the message or text to be authenticated.
	 * @throws UndeclaredThrowableException
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	public byte[] hmac_sha1(byte[] keyBytes, byte[] text) throws Exception {
            
            HMac hmac = new HMac(new SHA1Digest());
            byte[] resBuf = new byte[hmac.getMacSize()];
            hmac.init(new KeyParameter(keyBytes));
            hmac.update(text, 0, text.length);
            hmac.doFinal(resBuf, 0);
     
            return resBuf;
	}
	
	/**
	 * This method converts HEX string to byte[]
	 * @param hex   the HEX string
	 * @return      A byte array
	 */
	public byte[] hexStr2Bytes(String hex){
		// Adding one byte to get the right conversion
		// values starting with "0" can be converted
		byte[] bArray = new BigInteger("10" + hex,16).toByteArray();
		// Copy all the REAL bytes, not the "first"
		byte[] ret = new byte[bArray.length - 1];
		for (int i = 0; i < ret.length ; i++) ret[i] = bArray[i+1];
		return ret;
	}

	

	public String generateTOTP(byte[] key, String time, int codeDigits) throws Exception {
		
		// Using the counter
		// First 8 bytes are for the movingFactor
		// Complaint with base RFC 4226 (HOTP)

		while(time.length() < 16 ) time = "0" + time;
		
		byte[] msg = hexStr2Bytes(time); // Get the HEX in a byte[]
		
		

		// Adding one byte to get the right conversion

		byte[] hash = hmac_sha1(key, msg);
		
		//System.out.println("hash size = "+hash.length);
		
		int offset = hash[hash.length - 1] & 0xf; // put selected bytes into result int
		int binary =
				((hash[offset] & 0x7f) << 24) |
				((hash[offset + 1] & 0xff) << 16) |
				((hash[offset + 2] & 0xff) << 8) |
				(hash[offset + 3] & 0xff);
		int otp = binary % DIGITS_POWER[codeDigits];
		String result = Integer.toString(otp);
		while (result.length() < codeDigits) result = "0" + result;
		return result;
	}
}