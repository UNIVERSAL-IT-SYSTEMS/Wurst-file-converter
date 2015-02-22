/*
 * Copyright © 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.v1_5_config_file_converter.menu;

import javax.swing.JPanel;

import tk.wurst_client.v1_5_config_file_converter.Main;

public abstract class Menu extends JPanel
{
	public final String title;
	private Menu parent;
	protected boolean hasNavButtons = true;
	protected boolean nextButtonEnabled = true;
	protected String nextButtonText = "Next";
	protected boolean hasNextMenu = true;
	
	protected Menu(String title, Menu parent)
	{
		this.title = title;
		this.parent = parent;
	}
	
	public boolean hasParent()
	{
		return parent != null;
	}
	
	public boolean hasNavButtons()
	{
		return hasNavButtons;
	}
	
	public boolean isNextButtonEnabled()
	{
		return nextButtonEnabled;
	}
	
	public String getNextButtonText()
	{
		return nextButtonText;
	}
	
	public boolean hasNextMenu()
	{
		return hasNextMenu;
	}
	
	public void setNextButtonEnabled(boolean enabled)
	{
		Main.instance.nextButton.setEnabled(enabled);
	}
	
	public abstract void showNextMenu();
	
	public void showPrevMenu()
	{
		showMenu(parent);
	}
	
	protected void showMenu(Menu menu)
	{
		Main.instance.showMenu(menu);
	}
}
