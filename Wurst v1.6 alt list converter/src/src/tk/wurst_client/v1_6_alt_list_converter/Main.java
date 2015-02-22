/*
 * Copyright © 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package src.tk.wurst_client.v1_6_alt_list_converter;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import src.tk.wurst_client.v1_6_alt_list_converter.menu.Menu;
import src.tk.wurst_client.v1_6_alt_list_converter.menu.WelcomeMenu;

import com.google.gson.JsonObject;

public class Main extends JFrame implements Runnable
{
	public static Main instance = new Main();
	public JButton nextButton;
	public String path;
	public JsonObject options;
	
	@Override
	public void run()
	{
		setTitle("Wurst v1.6 alt list converter");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(640, 480);
		setLocationRelativeTo(null);
		setVisible(true);
		showMenu(new WelcomeMenu());
	}
	
	public void showMenu(Menu menu)
	{
		setContentPane(new JPanel(new BorderLayout()));
		if(menu.title != null && !menu.title.isEmpty())
			add(new JLabel("<html><h1>" + menu.title, SwingConstants.CENTER), BorderLayout.NORTH);
		add(menu, BorderLayout.CENTER);
		if(menu.hasNavButtons())
		{
			JPanel buttons = new JPanel();
			buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
			buttons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			if(menu.hasParent())
			{
				JButton backButton = new JButton();
				backButton.setText("Back");
				backButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						menu.showPrevMenu();
					}
				});
				buttons.add(backButton);
			}
			buttons.add(Box.createHorizontalGlue());
			if(menu.hasNextMenu())
			{
				nextButton = new JButton();
				nextButton.setText(menu.getNextButtonText());
				nextButton.setEnabled(menu.isNextButtonEnabled());
				nextButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						menu.showNextMenu();
					}
				});
				buttons.add(nextButton);
			}
			JButton cancelButton = new JButton();
			if(menu.hasNextMenu())
				cancelButton.setText("Cancel");
			else
				cancelButton.setText("Finish");
			cancelButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					System.exit(0);
				}
			});
			buttons.add(cancelButton);
			add(buttons, BorderLayout.SOUTH);
		}
		revalidate();
	}
	
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		new Thread(instance).start();
	}
}
