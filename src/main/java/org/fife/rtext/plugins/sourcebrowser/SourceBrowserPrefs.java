/*
 * 05/03/2005
 *
 * SourceBrowserPrefs.java- Preferences for the source browser.
 * Copyright (C) 2005 Robert Futrell
 * https://fifesoft.com/rtext
 * Licensed under a modified BSD license.
 * See the included license file for details.
 */
package org.fife.rtext.plugins.sourcebrowser;

import javax.swing.KeyStroke;

import org.fife.ui.app.prefs.Prefs;
import org.fife.ui.dockablewindows.DockableWindowConstants;


/**
 * Preferences for the source browser.
 *
 * @author Robert Futrell
 * @version 0.8
 */
public class SourceBrowserPrefs extends Prefs {

	/**
	 * Whether the GUI plugin window is active (visible).
	 */
	public boolean active;

	/**
	 * The GUI plugin window 's position.
	 */
	public int position;

	/**
	 * Key stroke that toggles the dockable window's visibility.
	 */
	public KeyStroke windowVisibilityAccelerator;

	/**
	 * The filename of the Exuberant CTags executable.
	 */
	public String ctagsExecutable;

	/**
	 * The type of ctags.
	 */
	public String ctagsType;

	/**
	 * Whether HTML should be used to make tool tips look nicer.
	 */
	public boolean useHTMLToolTips;


	@Override
	public void setDefaults() {
		active = true;
		position = DockableWindowConstants.LEFT;
		windowVisibilityAccelerator = null;
		ctagsExecutable = "/usr/contrib/bin/ctags";
		ctagsType = SourceBrowserPlugin.CTAGS_TYPE_EXUBERANT;
		useHTMLToolTips = true;
	}


}
