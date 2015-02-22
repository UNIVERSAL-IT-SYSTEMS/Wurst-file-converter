/*
 * Copyright © 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package src.tk.wurst_client.v1_6_alt_list_converter.menu;

import java.awt.Dimension;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import src.tk.wurst_client.v1_6_alt_list_converter.Converter;
import src.tk.wurst_client.v1_6_alt_list_converter.Main;

public class ProgressMenu extends Menu
{
	private JProgressBar progress;
	
	public ProgressMenu()
	{
		super("Converting...", null);
		hasNavButtons = false;
		progress = new JProgressBar();
		progress.setStringPainted(true);
		progress.setPreferredSize(new Dimension(512, progress.getPreferredSize().height));
		add(progress);
		new SwingWorker()
		{
			@Override
			protected Object doInBackground() throws Exception
			{
				new Converter(Main.instance.path, Main.instance.options, progress).run();
				showNextMenu();
				return null;
			}
		}.execute();
	}
	
	@Override
	public void showNextMenu()
	{
		showMenu(new DoneMenu());
	}
}
