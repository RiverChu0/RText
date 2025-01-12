/*
 * 11/08/2009
 *
 * ViewTasksAction - Toggles visibility of the "Tasks" dockable window.
 * Copyright (C) 2009 Robert Futrell
 * https://fifesoft.com/rtext
 * Licensed under a modified BSD license.
 * See the included license file for details.
 */
package org.fife.rtext.plugins.tasks;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import org.fife.rtext.RText;
import org.fife.ui.app.AppAction;


/**
 * Toggles the display of the "Tasks" dockable window.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class ViewTasksAction extends AppAction<RText> {

	/**
	 * The tasks plugin.
	 */
	private final TasksPlugin plugin;


	/**
	 * Constructor.
	 *
	 * @param owner The parent RText instance.
	 * @param msg The resource bundle to use for localization.
	 * @param plugin The tasks plugin.
	 */
	ViewTasksAction(final RText owner, ResourceBundle msg,
							TasksPlugin plugin) {
		super(owner, msg, "ViewTasksAction");
		this.plugin = plugin;
	}


	/**
	 * Called when this action is performed.
	 *
	 * @param e The event.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		plugin.toggleTaskWindowVisible();
	}


}
