/*
 * Copyright © 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package src.tk.wurst_client.v1_6_alt_list_converter;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JProgressBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Converter implements Runnable
{
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private String path;
	private JsonObject options;
	private JProgressBar progress;
	private long total;
	private long converted;
	
	public Converter(String path, JsonObject options, JProgressBar progress)
	{
		this.path = path;
		this.options = options;
		this.progress = progress;
	}
	
	@Override
	public void run()
	{
		progress.setMinimum(0);
		progress.setMaximum(1000);
		Iterator<Entry<String, JsonElement>> itr = options.entrySet().iterator();
		while(itr.hasNext())
		{
			Map.Entry<String, JsonElement> entry = itr.next();
			File file = new File(path, entry.getKey() + ".txt");
			if(file.exists())
				total += file.length();
		}
		itr = options.entrySet().iterator();
		while(itr.hasNext())
		{
			Map.Entry<String, JsonElement> entry = itr.next();
			File oldFile = new File(path, entry.getValue().getAsJsonObject().get("oldfile").getAsString());
			File newFile = new File(path, entry.getValue().getAsJsonObject().get("newfile").getAsString());
			if(entry.getValue().getAsJsonObject().get("enabled").getAsBoolean()
				&& oldFile.exists())
			{
				long fileSize = oldFile.length();
				try
				{
					convert(oldFile, newFile);
					if(oldFile.exists())
						oldFile.delete();
					converted += fileSize;
				}catch(IOException e)
				{
					e.printStackTrace();
				}finally
				{
					updateProgress();
				}
			}
		}
	}
	
	private void convert(File oldFile, File newFile) throws IOException
	{
		if(oldFile.getName().equals("alts.wurst"))
		{	
			
		}
	}
	
	private void updateProgress()
	{
		progress.setValue((int)(converted * 1000 / total));
	}
}
