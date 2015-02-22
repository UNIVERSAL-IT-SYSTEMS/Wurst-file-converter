/*
 * Copyright © 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package src.tk.wurst_client.v1_6_alt_list_converter.menu;

import javax.swing.JLabel;

public class DoneMenu extends Menu
{
	public DoneMenu()
	{
		super("Done!", null);
		hasNextMenu = false;
		add(new JLabel("<html>"
			+ "<p>Your config files have been successfully converted.</p>"));
	}
	
	@Override
	public void showNextMenu()
	{	
		
	}
}
