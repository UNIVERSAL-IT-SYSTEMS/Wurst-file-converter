/*
 * Copyright © 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.v1_5_config_file_converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JProgressBar;

import org.lwjgl.input.Keyboard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
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
		if(oldFile.getName().equals("automaximize.txt"))
			oldFile.renameTo(new File(path, newFile.getName()));
		else if(oldFile.getName().equals("friends.txt"))
		{
			ArrayList<String> friends = new ArrayList<String>();
			BufferedReader load = new BufferedReader(new FileReader(oldFile));
			for(String line = ""; (line = load.readLine()) != null;)
				friends.add(line);
			load.close();
			friends.sort(new Comparator<String>()
			{
				@Override
				public int compare(String o1, String o2)
				{
					return o1.compareToIgnoreCase(o2);
				}
			});
			PrintWriter save = new PrintWriter(new FileWriter(newFile));
			save.println(gson.toJson(friends));
			save.close();
		}else if(oldFile.getName().equals("gui.txt"))
		{
			BufferedReader load = new BufferedReader(new FileReader(oldFile));
			JsonObject json = new JsonObject();
			for(String line = ""; (line = load.readLine()) != null;)
			{
				String data[] = line.split("§");
				JsonObject jsonFrame = new JsonObject();
				jsonFrame.addProperty("minimized", Boolean.valueOf(data[1]));
				jsonFrame.addProperty("pinned", Boolean.valueOf(data[2]));
				jsonFrame.addProperty("posX", Integer.parseInt(data[3]));
				jsonFrame.addProperty("posY", Integer.parseInt(data[4]));
				json.add(data[0], jsonFrame);
			}
			load.close();
			PrintWriter save = new PrintWriter(new FileWriter(newFile));
			save.println(gson.toJson(json));
			save.close();
		}else if(oldFile.getName().equals("modules.txt"))
		{
			JsonObject json = new JsonObject();
			BufferedReader load = new BufferedReader(new FileReader(oldFile));
			for(String line = ""; (line = load.readLine()) != null;)
			{
				String data[] = line.split("§");
				JsonObject jsonModule = new JsonObject();
				jsonModule.addProperty("enabled", Boolean.valueOf(data[1]));
				jsonModule.addProperty("keybind", Keyboard.getKeyName(Integer.valueOf(data[2])));
				json.add(data[0], jsonModule);
			}
			load.close();
			PrintWriter save = new PrintWriter(new FileWriter(newFile));
			save.println(gson.toJson(json));
			save.close();
		}else if(oldFile.getName().equals("sliders.txt"))
		{
			ArrayList<Module> modules = new ArrayList<Module>();
			modules.add(new Module("ArenaBrawl", "ArenaBrawl level"));
			modules.add(new Module("FastBreak", "FastBreak speed"));
			modules.add(new Module("Flight", "Flight speed"));
			modules.add(new Module("Kaboom", "Kaboom power"));
			modules.add(new Module("Killaura", "Killaura speed", "Killaura range"));
			modules.add(new Module("Nuker", "Nuker range"));
			modules.add(new Module("Timer", "Timer speed"));
			BufferedReader load = new BufferedReader(new FileReader(oldFile));
			ArrayList<Double> allSliders = new ArrayList<Double>();
			for(String line = ""; (line = load.readLine()) != null;)
			{
				String data[] = line.split("§");
				allSliders.add(gson.fromJson(data[1], Double.class));
			}
			load.close();
			JsonObject json = new JsonObject();
			for(int i = 0; i < modules.size(); i++)
			{
				Module module = modules.get(i);
				JsonObject jsonModule = new JsonObject();
				for(String slider : module.sliders)
					jsonModule.addProperty(slider, allSliders.get(i));
				json.add(module.name, jsonModule);
			}
			PrintWriter save = new PrintWriter(new FileWriter(newFile));
			save.println(gson.toJson(json));
			save.close();
		}else if(oldFile.getName().equals("values.txt"))
		{
			Options tOptions = new Options();
			BufferedReader load = new BufferedReader(new FileReader(oldFile));
			for(String line = ""; (line = load.readLine()) != null;)
			{
				String data[] = line.split("§");
				for(Field field : tOptions.getClass().getFields())
					if(data[0].equals(field.getName()))
					{
						try
						{
							if(field.getType().getName().equals("boolean"))
								field.setBoolean(tOptions, Boolean.valueOf(data[1]));
							else if(field.getType().getName().equals("int"))
								field.setInt(tOptions, Integer.valueOf(data[1]));
							else if(field.getType().getName().equals("java.lang.String"))
								field.set(tOptions, data[1]);
						}catch(Exception e)
						{
							e.printStackTrace();
						}
						break;
					}
			}
			load.close();
			PrintWriter save = new PrintWriter(new FileWriter(newFile));
			save.println(gson.toJson(tOptions));
			save.close();
		}else if(oldFile.getName().equals("xray.txt"))
		{
			JsonArray json = new JsonArray();
			BufferedReader load = new BufferedReader(new FileReader(oldFile));
			for(String line = ""; (line = load.readLine()) != null;)
				json.add(gson.toJsonTree(Integer.valueOf(line)));
			load.close();
			PrintWriter save = new PrintWriter(new FileWriter(newFile));
			save.println(gson.toJson(json));
			save.close();
		}
	}
	
	private class Module
	{
		public final String name;
		public final ArrayList<String> sliders = new ArrayList<String>();
		
		public Module(String name, String... sliders)
		{
			this.name = name;
			for(String slider : sliders)
				this.sliders.add(slider);
		}
	}
	
	public class Options
	{
		public boolean autoReconnect = false;
		public boolean cleanupFailed = false;
		public boolean cleanupOutdated = true;
		public boolean cleanupRename = true;
		public boolean cleanupUnknown = true;
		public boolean forceOPDontWait = false;
		public boolean middleClickFriends = true;
		public boolean spamFont = false;
		public boolean renameForceOPEvenThoughTheNameIsTechnicallyCorrect = false;
		public boolean WIP = false;
		
		public int arrayListMode = 0;
		public int autobuildMode = 1;
		public int targetMode = 0;
		public int fastbreakMode = 0;
		public int forceOPDelay = 1000;
		public int nukerMode = 0;
		public int searchID = 116;
		public int serverFinderThreads = 64;
		public int spamDelay = 1000;
		public int throwAmount = 16;
		
		public String forceOPList = ".\\wurst";
	}
	
	private void updateProgress()
	{
		progress.setValue((int)(converted * 1000 / total));
	}
}
