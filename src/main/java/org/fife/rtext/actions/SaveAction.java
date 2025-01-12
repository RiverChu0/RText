/*
 * 11/14/2003
 *
 * SaveAction.java - Action to save the current document in RText.
 * Copyright (C) 2003 Robert Futrell
 * https://fifesoft.com/rtext
 * Licensed under a modified BSD license.
 * See the included license file for details.
 */
package org.fife.rtext.actions;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.Icon;

import org.fife.rtext.RText;
import org.fife.ui.app.AppAction;


/**
 * Action used by an <code>RTextTabbedPane</code> to save the current document.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class SaveAction extends AppAction<RText> {


	/**
	 * Constructor.
	 *
	 * @param owner The parent RText instance.
	 * @param msg The resource bundle to use for localization.
	 * @param icon The icon associated with the action.
	 */
	SaveAction(RText owner, ResourceBundle msg, Icon icon) {
		super(owner, msg, "SaveAction");
		setIcon(icon);
	}


	/**
	 * Called when this action is performed.
	 *
	 * @param e The event.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		getApplication().getMainView().saveCurrentFile();
	}


}
