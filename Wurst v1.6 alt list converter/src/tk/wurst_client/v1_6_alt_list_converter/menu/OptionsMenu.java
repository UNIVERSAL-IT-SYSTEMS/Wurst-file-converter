/*
 * Copyright © 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.v1_6_alt_list_converter.menu;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import tk.wurst_client.v1_6_alt_list_converter.Main;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class OptionsMenu extends Menu
{
	public OptionsMenu(Menu parent)
	{
		super("Options", parent);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel header = new JLabel("<html>"
			+ "<p>Please select the files you want to convert.<p>", SwingConstants.CENTER);
		header.setAlignmentX(CENTER_ALIGNMENT);
		add(header);
		Main.instance.options = new JsonParser().parse(new InputStreamReader(
			getClass().getClassLoader().getResourceAsStream("tk/wurst_client/v1_5_config_file_converter/resources/options.json"))).getAsJsonObject()
			.get("options").getAsJsonObject();
		JPanel optionsPanel = new JPanel(new GridLayout(0, 1));
		optionsPanel.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 50));
		Iterator<Entry<String, JsonElement>> itr = Main.instance.options.entrySet().iterator();
		while(itr.hasNext())
		{
			Entry<String, JsonElement> entry = itr.next();
			JsonObject jsonOption = entry.getValue().getAsJsonObject();
			JCheckBox option = new JCheckBox(
				jsonOption.get("label").getAsString(),
				jsonOption.get("enabled").getAsBoolean());
			option.setName(entry.getKey());
			option.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					JsonObject jsonObject = Main.instance.options.get(option.getName()).getAsJsonObject();
					jsonObject.remove("enabled");
					jsonObject.addProperty("enabled", option.isSelected());
				}
			});
			optionsPanel.add(option);
		}
		optionsPanel.setAlignmentX(CENTER_ALIGNMENT);
		add(optionsPanel);
	}
	
	@Override
	public void showNextMenu()
	{
		showMenu(new ProgressMenu());
	}
}
