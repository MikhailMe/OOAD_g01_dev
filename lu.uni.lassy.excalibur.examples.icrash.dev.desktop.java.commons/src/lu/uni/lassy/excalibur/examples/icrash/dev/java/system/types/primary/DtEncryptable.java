package lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.design.JIntIs;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.DtString;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.PtBoolean;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.PtString;

public class DtEncryptable extends DtString implements JIntIs {

	public DtEncryptable(PtString s) {
		super(s);
	}

	private static final long serialVersionUID = 7375848948087219774L;
	
	@Override
	public PtBoolean is() {
		return new PtBoolean(true);
	}
	
	public PtBoolean encrypt(DtKey aKey) {
		try {
			SecretKeySpec key = new SecretKeySpec(aKey.value.getValue().getBytes(), "AES");
			Cipher encryptor = Cipher.getInstance("AES");
			encryptor.init(Cipher.ENCRYPT_MODE, key);
			byte[] encrypted = encryptor.doFinal(value.getValue().getBytes());
			value = new PtString(new String(encrypted));
			return new PtBoolean(true);
		} catch (IllegalBlockSizeException | InvalidKeyException 
				| BadPaddingException | NoSuchAlgorithmException
				| NoSuchPaddingException e) {
			e.printStackTrace();
		}
		return new PtBoolean(false);
	}
	
	public PtBoolean decrypt(DtKey aKey) {
		try {
			SecretKeySpec key = new SecretKeySpec(aKey.value.getValue().getBytes(), "AES");
			Cipher decryptor = Cipher.getInstance("AES");
			decryptor.init(Cipher.DECRYPT_MODE, key);
			byte[] encrypted = decryptor.doFinal(value.getValue().getBytes());
			value = new PtString(new String(encrypted));
			return new PtBoolean(true);
		} catch (InvalidKeyException 
				| NoSuchAlgorithmException
				| NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
	return new PtBoolean(false);
	}
	
}
