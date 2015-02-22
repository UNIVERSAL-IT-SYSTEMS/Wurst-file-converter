/*
 * Copyright � 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.v1_6_alt_list_converter.encryption;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

import tk.wurst_client.v1_6_alt_list_converter.Main;

public class NewEncryption
{
	private static SecretKey aesKey;
	private static File aesFile = new File(Main.instance.path, "key");
	
	private static KeyPair keypair;
	private static File privateFile = System.getProperty("user.home") != null ? new File(System.getProperty("user.home") + "\\.ssh\\wurst_rsa") : null;
	private static File publicFile = System.getProperty("user.home") != null ? new File(System.getProperty("user.home") + "\\.ssh\\wurst_rsa.pub") : null;
	
	public static final String CHARSET = "UTF-8";
	
	public static String encrypt(String string)
	{
		checkKeys();
		try
		{
			Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, aesKey, new IvParameterSpec(aesKey.getEncoded()));
			return Base64.getEncoder().encodeToString(cipher.doFinal(string.getBytes(CHARSET)));
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static String decrypt(String string)
	{
		checkKeys();
		try
		{
			Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(aesKey.getEncoded()));
			return new String(cipher.doFinal(Base64.getDecoder().decode(string)), CHARSET);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	private static void checkKeys()
	{
		if(!hasKeyFiles())
			generateKeys();
		if(!hasKey())
			loadKeys();
	}
	
	private static boolean hasKeyFiles()
	{
		return privateFile != null
			&& privateFile.exists()
			&& publicFile != null
			&& publicFile.exists()
			&& aesFile != null
			&& aesFile.exists();
	}
	
	private static boolean hasKey()
	{
		return keypair != null
			&& keypair.getPrivate().getEncoded() != null
			&& keypair.getPublic().getEncoded() != null
			&& aesKey.getEncoded() != null;
	}
	
	public static void generateKeys()
	{
		try
		{
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			keygen.init(128);
			aesKey = keygen.generateKey();
			KeyPairGenerator keypairgen = KeyPairGenerator.getInstance("RSA");
			keypairgen.initialize(1024);
			keypair = keypairgen.generateKeyPair();
			if(publicFile == null || privateFile == null)
			{
				JOptionPane.showMessageDialog(null, "Cannot create RSA key.\nThis is a bug, please report it!", "Error", JOptionPane.ERROR_MESSAGE);
				Desktop.getDesktop().browse(new URI("https://github.com/Wurst-Imperium/Wurst-Client/issues?q=cannot+create+RSA+key"));
				System.exit(-1);
				return;
			}
			if(!publicFile.exists())
			{
				if(!publicFile.getParentFile().exists())
					publicFile.getParentFile().mkdirs();
				ObjectOutputStream save = new ObjectOutputStream(new FileOutputStream(publicFile));
				save.writeObject(KeyFactory.getInstance("RSA").getKeySpec(keypair.getPublic(), RSAPublicKeySpec.class).getModulus());
				save.writeObject(KeyFactory.getInstance("RSA").getKeySpec(keypair.getPublic(), RSAPublicKeySpec.class).getPublicExponent());
				save.close();
			}
			if(!privateFile.exists())
			{
				if(!privateFile.getParentFile().exists())
					privateFile.getParentFile().mkdirs();
				ObjectOutputStream save = new ObjectOutputStream(new FileOutputStream(privateFile));
				save.writeObject(KeyFactory.getInstance("RSA").getKeySpec(keypair.getPrivate(), RSAPrivateKeySpec.class).getModulus());
				save.writeObject(KeyFactory.getInstance("RSA").getKeySpec(keypair.getPrivate(), RSAPrivateKeySpec.class).getPrivateExponent());
				save.close();
			}
			if(aesFile == null)
			{
				JOptionPane.showMessageDialog(null,
					"Cannot create AES key.\nThis is a bug, please report it!", "Error", JOptionPane.ERROR_MESSAGE);
				Desktop.getDesktop().browse(new URI("https://github.com/Wurst-Imperium/Wurst-Client/issues?q=cannot+create+AES+key"));
				System.exit(-1);
				return;
			}
			if(!aesFile.exists())
			{
				if(!aesFile.getParentFile().exists())
					aesFile.getParentFile().mkdirs();
				Cipher rsaCipher = Cipher.getInstance("RSA");
				rsaCipher.init(Cipher.ENCRYPT_MODE, keypair.getPublic());
				Files.write(aesFile.toPath(), rsaCipher.doFinal(aesKey.getEncoded()));
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static void loadKeys()
	{
		try
		{
			if(privateFile == null || publicFile == null)
			{
				JOptionPane.showMessageDialog(null, "Cannot load RSA key.\nThis is a bug, please report it!", "Error", JOptionPane.ERROR_MESSAGE);
				Desktop.getDesktop().browse(new URI("https://github.com/Wurst-Imperium/Wurst-Client/issues?q=cannot+load+RSA+key"));
				System.exit(-1);
			}
			if(!hasKeyFiles())
				generateKeys();
			else
			{
				ObjectInputStream publicLoad = new ObjectInputStream(new FileInputStream(publicFile));
				PublicKey loadedPublicKey = KeyFactory.getInstance("RSA")
					.generatePublic(new RSAPublicKeySpec(
						(BigInteger)publicLoad.readObject(),
						(BigInteger)publicLoad.readObject()));
				publicLoad.close();
				ObjectInputStream privateLoad = new ObjectInputStream(new FileInputStream(privateFile));
				PrivateKey loadedPrivateKey = KeyFactory.getInstance("RSA")
					.generatePrivate(new RSAPrivateKeySpec(
						(BigInteger)privateLoad.readObject(),
						(BigInteger)privateLoad.readObject()));
				privateLoad.close();
				keypair = new KeyPair(loadedPublicKey, loadedPrivateKey);
				Cipher rsaCipher = Cipher.getInstance("RSA");
				rsaCipher.init(Cipher.DECRYPT_MODE, keypair.getPrivate());
				aesKey = new SecretKeySpec(rsaCipher.doFinal(Files.readAllBytes(aesFile.toPath())), "AES");
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
