/*
 * 10/09/2012
 *
 * MoveFocusLeftAction - Focuses the dockable window group to the left of
 * the currently focused component.
 * Copyright (C) 2012 Robert Futrell
 * https://fifesoft.com/rtext
 * Licensed under a modified BSD license.
 * See the included license file for details.
 */
package org.fife.rtext.actions;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import org.fife.rtext.RText;
import org.fife.ui.app.AppAction;
import org.fife.ui.dockablewindows.DockableWindowConstants;


/**
 * Focuses the dockable window group to the left of the currently focused
 * component.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class MoveFocusLeftAction extends AppAction<RText> {


	/**
	 * Constructor.
	 */
	MoveFocusLeftAction(RText app, ResourceBundle msg) {
		super(app, msg, "MoveFocusLeftAction");
	}


	@Override
	public void actionPerformed(ActionEvent e) {

		Component focused = KeyboardFocusManager.
				getCurrentKeyboardFocusManager().getFocusOwner();
		if (focused==null) {
			return;
		}

		RText rtext = getApplication();
		int focusedGroup = rtext.getFocusedDockableWindowGroup();
		int toFocus;

		switch (focusedGroup) {
			case DockableWindowConstants.TOP:
			case DockableWindowConstants.BOTTOM:
			default: // In editor
				if (rtext.hasDockableWindowGroup(DockableWindowConstants.LEFT)) {
					toFocus = DockableWindowConstants.LEFT;
				}
				else if (rtext.hasDockableWindowGroup(DockableWindowConstants.RIGHT)) {
					toFocus = DockableWindowConstants.RIGHT;
				}
				else {
					return; // Don't change focused docked window
				}
				break;
			case DockableWindowConstants.LEFT:
				if (rtext.hasDockableWindowGroup(DockableWindowConstants.RIGHT)) {
					toFocus = DockableWindowConstants.RIGHT;
				}
				else {
					toFocus = -1; // Focus currentTextArea
				}
				break;
			case DockableWindowConstants.RIGHT:
				toFocus = -1; // Focus currentTextArea
				break;
		}

		if (toFocus==-1) {
			rtext.getMainView().getCurrentTextArea().requestFocusInWindow();
		}
		else {
			rtext.focusDockableWindowGroup(toFocus);
		}

	}


}
