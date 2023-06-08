package com.synectiks.asset.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class CryptoUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(CryptoUtil.class);
			
	private static final String SECRET_KEY = "6MgBSOpp8kiJ8zb7XHW7yRUCUXyT8ck7kjWltYTGOHU=";
	private static final String SALTVALUE = "G1GMMhcI6lk47FwtsCOy6Ew5hT+gqtKbXmFP8ZJi/YI=";
	private static final String PBE_KEY = "PBKDF2WithHmacSHA256";
	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
	private static final String ALGO = "AES";
	
	public static String getSecretEncryptionKey() throws Exception{
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(256); // The AES key size in number of bits
        SecretKey secKey = generator.generateKey();
        String secretKeyString = Base64.getEncoder().encodeToString(secKey.getEncoded());
        return secretKeyString;
    }
	
	private static IvParameterSpec getIvParameterSpec() {
		byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		IvParameterSpec ivspec = new IvParameterSpec(iv);
		return ivspec;
	}
	
	private static SecretKeySpec getSecretKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance(PBE_KEY);
		KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALTVALUE.getBytes(), 65536, 256);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), ALGO);
		return secretKey;
	}
	
	public static String encrypt(String strToEncrypt) {
		try {
			IvParameterSpec ivspec = getIvParameterSpec();
			SecretKeySpec secretKey = getSecretKey();
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
		} catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException
				| InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException
				| NoSuchPaddingException e) {
			logger.error("Error occured during encryption: " + e.toString());
		}
		return null;
	}

	public static String decrypt(String strToDecrypt) {
		try {
			IvParameterSpec ivspec = getIvParameterSpec();
			SecretKeySpec secretKey = getSecretKey();
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException
				| InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException
				| NoSuchPaddingException e) {
			logger.error("Error occured during decryption: " + e.toString());
		}
		return null;
	}
	
	public static String encodeBase64(String source) {
		return Base64.getEncoder().encodeToString(source.getBytes());
	}
	
	public static String decodeBase64(String source) {
		byte[] decodedBytes = Base64.getDecoder().decode(source);
		return new String(decodedBytes);
	}
	
	public static SealedObject encryptObject(Serializable object) {
		try {
			IvParameterSpec ivspec = getIvParameterSpec();
			SecretKeySpec secretKey = getSecretKey();
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
			SealedObject sealedObject = new SealedObject(object, cipher);
			return sealedObject;
		}catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException
				| InvalidKeySpecException | IllegalBlockSizeException
				| NoSuchPaddingException | IOException e) {
			logger.error("Error occured during object encription: " + e.toString());
		} 
		return null;
	}
	
	public static Serializable decryptObject(SealedObject sealedObject) {
		try {
			IvParameterSpec ivspec = getIvParameterSpec();
			SecretKeySpec secretKey = getSecretKey();
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
			Serializable unsealObject = (Serializable) sealedObject.getObject(cipher);
		    return unsealObject;
		}catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException
				| InvalidKeySpecException | IllegalBlockSizeException
				| NoSuchPaddingException | IOException | ClassNotFoundException | BadPaddingException e) {
			logger.error("Error occured during object decription: " + e.toString());
		} 
		return null;
	}
	
//	public static void main(String a[]) throws Exception {
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("Ok", "Google");
//		SealedObject so = encryptObject(map);
//		System.out.println(so);
//		Serializable s = decryptObject(so);
//		System.out.println(s.toString());
//	}
}