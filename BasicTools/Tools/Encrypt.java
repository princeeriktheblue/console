package Tools;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import console.Command;
import console.commands.exceptions.EncryptionException;
import console.display.Console;
import console.exceptions.EException;

public class Encrypt extends Command
{
	private final static String[] help = 
		{
				""
		};
	
	public Encrypt(Console console) 
	{
		super("encrypt", console, new char[]{'e', 'd'}, help);
	}

	@Override
	public void execute(String[] args) throws EException 
	{
		
	}
	
	@SuppressWarnings("unused")
	private synchronized EncryptionData encrypt(String toEncrypt, String password) throws EncryptionException
    {
    	/* Derive the key, given password and salt. */
    	SecureRandom random = new SecureRandom();
    	byte[] salt = new byte[8];
    	random.nextBytes(salt);
    	SecretKeyFactory factory;
    	SecretKey tmp;
		try 
		{
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
			tmp = factory.generateSecret(spec);
	    	SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
	    	/* Encrypt the message. */
	    	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    	cipher.init(Cipher.ENCRYPT_MODE, secret);
	    	AlgorithmParameters params = cipher.getParameters();
	    	byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
	    	byte[] ciphertext = cipher.doFinal(toEncrypt.getBytes("UTF-8"));
	    	return new EncryptionData(password, ciphertext, salt, iv);
		} 
		catch (InvalidKeySpecException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException
				| UnsupportedEncodingException | InvalidKeyException | NoSuchPaddingException | InvalidParameterSpecException e) 
		{
			throw new EncryptionException(e.getMessage());
		}
    }
    
    @SuppressWarnings("unused")
	private synchronized String decrypt(EncryptionData data) throws EncryptionException
    {
    	/* Decrypt the message, given derived key and initialization vector. */
    	try 
		{
	    	SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
	    	KeySpec spec = new PBEKeySpec(data.getPass().toCharArray(), data.getSalt(), 65536, 256);
	    	SecretKey tmp = factory.generateSecret(spec);
	    	SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
	    	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    	cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(data.getIV()));
	    	String plaintext = new String(cipher.doFinal(data.getData()), "UTF-8");
	    	return plaintext;
		}
    	catch (InvalidKeySpecException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException
				| UnsupportedEncodingException | InvalidKeyException | NoSuchPaddingException | InvalidAlgorithmParameterException e) 
		{
			throw new EncryptionException(e.getMessage());
		}
    }
    
    private final class EncryptionData
    {
    	public EncryptionData(String password, byte[] data, byte[] salt, byte[] iv)
    	{
    		this.password = password;
    		this.data = data;
    		this.salt = salt;
    		this.iv = iv;
    	}
    	
    	public String getPass()
    	{
    		return password;
    	}
    	
    	public byte[] getData()
    	{
    		return data;
    	}
    	
    	public byte[] getSalt()
    	{
    		return salt;
    	}
    	
    	public byte[] getIV()
    	{
    		return iv;
    	}
    	
    	private final String password;
    	private final byte[] data;
    	private final byte[] salt;
    	private final byte[] iv;
    }
}