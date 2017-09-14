package com.ey.subscriber;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


public class EncryptDecrypt {
	private static final String ALGORITHM = "AES";
	private static final String KEY = "1Hbfh667adfDEJ78";
	
	public static String encrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(EncryptDecrypt.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        byte[] bencryptedValue64 = new Base64().encode(encryptedByteValue);
        String encryptedValue64 = new String(bencryptedValue64);
        return encryptedValue64;
               
    }
	
	 public static String decrypt(String value) throws Exception
	    {
	        Key key = generateKey();
	        Cipher cipher = Cipher.getInstance(EncryptDecrypt.ALGORITHM);
	        cipher.init(Cipher.DECRYPT_MODE, key);
			byte [] decryptedValue64 = new Base64().decode(value);
	        byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
	        String decryptedValue = new String(decryptedByteValue,"utf-8");
	        return decryptedValue;
	                
	    }
	 
	 private static Key generateKey() throws Exception 
	    {
	        Key key = new SecretKeySpec(EncryptDecrypt.KEY.getBytes(),EncryptDecrypt.ALGORITHM);
	        return key;
	    }
}