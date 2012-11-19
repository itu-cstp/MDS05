package macgyvers.mds05;
import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;


/**
 * Code from http://www.code2learn.com/2011/06/encryption-and-decryption-of-data-using.html
 * @author Ellen
 *
 */
public class EncryptionService {

	private static final String ALGO = "AES";
	private static final byte[] keyValue = 
			new byte[] { 'T', 'h', 'e', 'M', 'a', 'c', 'G', 'y', 'v', 'e', 'r','s', 'T', 'e', 'a', 'm' };

	public static String encrypt(String Data) throws Exception 
	{
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(Data.getBytes());
		String encryptedValue = DatatypeConverter.printBase64Binary(encVal);
		return encryptedValue;
	}

	public static String decrypt(String encryptedData) throws Exception 
	{
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = DatatypeConverter.parseBase64Binary(encryptedData);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;


	}

	private static Key generateKey() throws Exception {
		Key key = new SecretKeySpec(keyValue, ALGO);
		return key;
	}
}
