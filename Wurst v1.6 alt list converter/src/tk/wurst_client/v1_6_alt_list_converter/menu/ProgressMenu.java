/*
 * Copyright © 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.v1_6_alt_list_converter.menu;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import tk.wurst_client.v1_6_alt_list_converter.Converter;
import tk.wurst_client.v1_6_alt_list_converter.Main;

public class ProgressMenu extends Menu
{
	private JProgressBar progress;
	private JLabel action;
	
	public ProgressMenu()
	{
		super("Converting...", null);
		hasNavButtons = false;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		progress = new JProgressBar();
		progress.setStringPainted(false);
		progress.setPreferredSize(new Dimension(512, progress.getPreferredSize().height));
		progress.setIndeterminate(true);
		progress.setAlignmentX(CENTER_ALIGNMENT);
		add(progress);
		action = new JLabel("", JLabel.CENTER);
		action.setAlignmentX(CENTER_ALIGNMENT);
		add(action);
		new SwingWorker()
		{
			@Override
			protected Object doInBackground() throws Exception
			{
				new Converter(Main.instance.path, Main.instance.options, progress, action).run();
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
