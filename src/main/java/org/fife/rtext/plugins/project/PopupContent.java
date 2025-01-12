/*
 * 10/04/2013
 *
 * PopupContent - Content for a workspace tree node's context menu.
 * Copyright (C) 2013 Robert Futrell
 * https://fifesoft.com/rtext
 * Licensed under a modified BSD license.
 * See the included license file for details.
 */
package org.fife.rtext.plugins.project;

import javax.swing.Action;
import javax.swing.JMenu;


/**
 * Tag interface for something that can be content in a popup menu from a
 * workspace tree node.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public interface PopupContent {


	/**
	 * Defines a popup menu action.
	 */
	interface PopupAction extends Action, PopupContent {}


	/**
	 * Defines a submenu.
	 */
	class PopupSubMenu extends JMenu implements PopupContent {

		public PopupSubMenu(String text) {
			super(text);
		}

	}


}
