/*
 * Copyright © 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.v1_7_keybind_converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JLabel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
				}catch(Exception e)
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
			try
			{
				setCurrentAction("Reading old keybinds");
				BufferedReader load = new BufferedReader(new FileReader(oldFile));
				JsonObject json = (JsonObject)new JsonParser().parse(load);
				load.close();
				setCurrentAction("Converting keybinds");
				TreeMap<String, String> keybinds = new TreeMap<String, String>();
				Iterator<Entry<String, JsonElement>> itr1 = json.entrySet().iterator();
				while(itr1.hasNext())
				{
					Entry<String, JsonElement> entry = itr1.next();
					JsonObject jsonModule = (JsonObject)entry.getValue();
					keybinds.put(jsonModule.get("keybind").getAsString(), ".t " + entry.getKey().toLowerCase());
				}
				json = new JsonObject();
				Iterator<Entry<String, String>> itr2 = keybinds.entrySet().iterator();
				while(itr2.hasNext())
				{
					Entry<String, String> entry = itr2.next();
					json.addProperty(entry.getKey(), entry.getValue());
				}
				setCurrentAction("Saving converted keybinds");
				PrintWriter save = new PrintWriter(new FileWriter(newFile));
				save.println(gson.toJson(json));
				save.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
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
