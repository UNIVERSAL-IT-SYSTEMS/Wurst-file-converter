/*
 * Copyright � 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.v1_7_keybind_converter.encryption;

/**
 * This is not a safe encryption at all. But it's still a lot better than
 * just saving your alts in plain text format like most clients do.
 * @author Alexander01998
 */
public class OldEncryption
{
	private static final String keyLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuwvxyz���0123456789!\"$%&/()=?[]{}+-*<>\\_.,|~`'�#";
	private static final String defaultKey = "9GjT.\\ACpb_>/!a,|~Hh�Il5XiJRBLK8`71zSdDn$Mmgq}PO=3Q\"{?Vru(�*y%]F'Yt�xEZ�<#W-U4k)eo2s+6&[cNfwv0";
	
	/**
	 * 
	 * @author Alexander01998
	 * @return random encryption key
	 */
	public static String generateKey()
	{
		String key = "";
		String rawKey = keyLetters;
		while(rawKey.length() > 0)
		{
			int index = (int) (Math.random() * (rawKey.length() - 1));
			String letter = rawKey.substring(index, index + 1);
			rawKey = rawKey.replace(letter, "");
			key += letter;
		}
		return key;
	}
	
	/**
	 * 
	 * @author Alexander01998
	 * @param string to encrypt
	 * @return encrypted string
	 */
	public static String encrypt(String string)
	{
		String key = defaultKey;
		String encryptedString = "";
		for(int i = 0; i < string.length(); i++)
		{
			String oldLetter = string.substring(i, i + 1);
			if(!key.contains(oldLetter))
				encryptedString = encryptedString.concat(oldLetter);
			else
			{
				int index = key.indexOf(oldLetter);
				String newLetter = "";
				int key2 = i + 1;
				while(index + key2 > key.length())
					key2 -= key.length();
				if(index + key2 == key.length())
					newLetter = key.substring(0, 1);
				else
					newLetter = key.substring(index + key2, index + key2 + 1);
				encryptedString = encryptedString.concat(newLetter);
			}
		}
		return encryptedString;
	}
	
	/**
	 * 
	 * @author Alexander01998
	 * @param string to encrypt
	 * @param key
	 * @return encrypted string
	 */
	public static String encrypt(String string, String key)
	{
		String encryptedString = "";
		for(int i = 0; i < string.length(); i++)
		{
			String oldLetter = string.substring(i, i + 1);
			if(!key.contains(oldLetter))
				encryptedString = encryptedString.concat(oldLetter);
			else
			{
				int index = key.indexOf(oldLetter);
				String newLetter = "";
				int key2 = i + 1;
				while(index + key2 > key.length())
					key2 -= key.length();
				if(index + key2 == key.length())
					newLetter = key.substring(0, 1);
				else
					newLetter = key.substring(index + key2, index + key2 + 1);
				encryptedString = encryptedString.concat(newLetter);
			}
		}
		return encryptedString;
	}
	
	/**
	 * 
	 * @author Alexander01998
	 * @param string to decrypt
	 * @return decrypted string
	 */
	public static String decrypt(String string)
	{
		String key = defaultKey;
		String decryptedString = "";
		for(int i = 0; i < string.length(); i++)
		{
			String oldLetter = string.substring(i, i + 1);
			if(!key.contains(oldLetter))
				decryptedString = decryptedString.concat(oldLetter);
			else
			{
				int index = key.indexOf(oldLetter);
				String newLetter = "";
				int key2 = i + 1;
				while(index - key2 < - 1)
					key2 -= key.length();
				if(index - key2 == - 1)
					newLetter = key.substring(key.length() - 1, key.length());
				else
					newLetter = key.substring(index - key2, index - key2 + 1);
				decryptedString = decryptedString.concat(newLetter);
			}
		}
		return decryptedString;
	}
	
	/**
	 * 
	 * @author Alexander01998
	 * @param string to decrypt
	 * @param key
	 * @return decrypted string
	 */
	public static String decrypt(String string, String key)
	{
		String decryptedString = "";
		for(int i = 0; i < string.length(); i++)
		{
			String oldLetter = string.substring(i, i + 1);
			if(!key.contains(oldLetter))
				decryptedString = decryptedString.concat(oldLetter);
			else
			{
				int index = key.indexOf(oldLetter);
				String newLetter = "";
				int key2 = i + 1;
				while(index - key2 < - 1)
					key2 -= key.length();
				if(index - key2 == - 1)
					newLetter = key.substring(key.length() - 1, key.length());
				else
					newLetter = key.substring(index - key2, index - key2 + 1);
				decryptedString = decryptedString.concat(newLetter);
			}
		}
		return decryptedString;
	}
}
