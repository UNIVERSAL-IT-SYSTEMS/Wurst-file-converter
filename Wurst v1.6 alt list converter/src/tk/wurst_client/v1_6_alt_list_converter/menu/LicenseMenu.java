/*
 * Copyright © 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.v1_6_alt_list_converter.menu;

import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import tk.wurst_client.v1_6_alt_list_converter.Main;

public class LicenseMenu extends Menu
{
	public LicenseMenu(Menu parent)
	{
		super("License", parent);
		nextButtonText = "Agree";
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		String license = "";
		try
		{
			BufferedReader load = new BufferedReader(
				new InputStreamReader(
					getClass().getClassLoader().getResourceAsStream(Main.RESOURCES + "LICENSE")));
			license = load.readLine();
			for(String line; (line = load.readLine()) != null;)
				license += "\n" + line;
		}catch(Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(
				Main.instance,
				"No license found.",
				"Error",
				JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
		JTextArea textarea = new JTextArea(license);
		textarea.setFont(new Font("Arial", Font.PLAIN, 12));
		textarea.setEditable(false);
		JScrollPane scrollpane = new JScrollPane(textarea);
		Dimension size = new Dimension(520, 300);
		scrollpane.setMaximumSize(size);
		scrollpane.setPreferredSize(size);
		scrollpane.setAlignmentX(CENTER_ALIGNMENT);
		add(scrollpane);
	}
	
	@Override
	public void showNextMenu()
	{
		showMenu(new FolderPathMenu(this));
	}
}
