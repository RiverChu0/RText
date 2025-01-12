/*
 * 02/26/2010
 *
 * ToolsPrefs.java - Preferences for the tools plugin.
 * Copyright (C) 2010 Robert Futrell
 * https://fifesoft.com/rtext
 * Licensed under a modified BSD license.
 * See the included license file for details.
 */
package org.fife.rtext.plugins.tools;

import java.io.IOException;
import java.io.InputStream;
import javax.swing.KeyStroke;

import org.fife.ui.app.prefs.Prefs;
import org.fife.ui.dockablewindows.DockableWindow;
import org.fife.ui.dockablewindows.DockableWindowConstants;


/**
 * Preferences for the tools plugin.
 *
 * @author Robert Futrell
 * @version 0.8
 */
public class ToolsPrefs extends Prefs {

	/**
	 * Whether the GUI plugin window is active (visible).
	 */
	public boolean windowVisible;

	/**
	 * The location of the dockable tool output window.
	 */
	public int windowPosition;

	/**
	 * Key stroke that toggles the task window's visibility.
	 */
	public KeyStroke windowVisibilityAccelerator;

	/**
	 * Accelerator for the "New Tool..." action.
	 */
	public KeyStroke newToolAccelerator;

	/**
	 * Accelerator for the "Edit Tools..." action.
	 */
	public KeyStroke editToolsAccelerator;


	/**
	 * Overridden to validate the task identifiers value.
	 */
	@Override
	public void load(InputStream in) throws IOException {
		super.load(in);
		// Ensure window position is valid.
		if (!DockableWindow.isValidPosition(windowPosition)) {
			windowPosition = DockableWindowConstants.BOTTOM;
		}
	}


	@Override
	public void setDefaults() {
		windowVisible = false;
		windowPosition = DockableWindowConstants.BOTTOM;
		windowVisibilityAccelerator = null;
		newToolAccelerator = null;
		editToolsAccelerator = null;
	}


}
