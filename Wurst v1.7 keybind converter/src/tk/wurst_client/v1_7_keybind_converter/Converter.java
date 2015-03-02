/*
 * Copyright © 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.v1_7_keybind_converter;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JLabel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Converter implements Runnable
{
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private String path;
	private JsonObject options;
	private JLabel action;
	
	public Converter(String path, JsonObject options, JLabel action)
	{
		this.path = path;
		this.options = options;
		this.action = action;
	}
	
	@Override
	public void run()
	{
		Iterator<Entry<String, JsonElement>> itr = options.entrySet().iterator();
		while(itr.hasNext())
		{
			Map.Entry<String, JsonElement> entry = itr.next();
			File oldFile = new File(path, entry.getValue().getAsJsonObject().get("oldfile").getAsString());
			File newFile = new File(path, entry.getValue().getAsJsonObject().get("newfile").getAsString());
			if(entry.getValue().getAsJsonObject().get("enabled").getAsBoolean()
				&& oldFile.exists())
			{
				setCurrentAction("Converting " + oldFile.getName() + " to " + newFile.getName());
				try
				{
					convert(oldFile, newFile);
					setCurrentAction("Deleting old file (" + oldFile.getName() + ")");
					if(oldFile.exists())
						oldFile.delete();
				}catch(IOException e)
				{
					e.printStackTrace();
				}finally
				{
					setCurrentAction("");
				}
			}
		}
	}
	
	private void convert(File oldFile, File newFile) throws IOException
	{
		if(oldFile.getName().equals("modules.json"))
		{
			
		}
	}
	
	private void setCurrentAction(String action)
	{
		this.action.setText("<html>"
			+ "<body width=512>"
			+ "<center>"
			+ action);
	}
}
