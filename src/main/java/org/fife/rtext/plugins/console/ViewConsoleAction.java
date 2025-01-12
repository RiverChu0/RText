/*
 * 12/18/2010
 *
 * ViewConsoleAction.java - Toggles visibility of the console dockable window.
 * Copyright (C) 2010 Robert Futrell
 * https://fifesoft.com/rtext
 * Licensed under a modified BSD license.
 * See the included license file for details.
 */
package org.fife.rtext.plugins.console;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import org.fife.rtext.RText;
import org.fife.ui.app.AppAction;


/**
 * Toggles visibility of the console dockable window.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class ViewConsoleAction extends AppAction<RText> {

	/**
	 * The parent plugin.
	 */
	private final Plugin plugin;


	/**
	 * Constructor.
	 *
	 * @param owner The parent RText instance.
	 * @param msg The resource bundle to use for localization.
	 * @param plugin The parent plugin.
	 */
	ViewConsoleAction(RText owner, ResourceBundle msg, Plugin plugin) {
		super(owner, msg, "Action.ViewConsole");
		this.plugin = plugin;
	}


	/**
	 * Called when this action is performed.
	 *
	 * @param e The event.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		plugin.setConsoleWindowVisible(!plugin.isConsoleWindowVisible());
	}


}
