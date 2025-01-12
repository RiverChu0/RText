/*
 * 08/14/2009
 *
 * HomePageAction.java - Action used to open RText's home page.
 * Copyright (C) 2009 Robert Futrell
 * https://fifesoft.com/rtext
 * Licensed under a modified BSD license.
 * See the included license file for details.
 */
package org.fife.rtext.actions;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.Icon;
import javax.swing.UIManager;

import org.fife.rtext.RText;
import org.fife.ui.UIUtil;
import org.fife.ui.app.AppAction;


/**
 * Action that opens a web browser to RText's home page.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class HomePageAction extends AppAction<RText> {


	/**
	 * Constructor.
	 *
	 * @param owner The parent RText instance.
	 * @param msg The resource bundle to use for localization.
	 * @param icon The icon associated with the action.
	 */
	HomePageAction(RText owner, ResourceBundle msg, Icon icon) {
		super(owner, msg, "HomePageAction");
		setIcon(icon);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (!UIUtil.browse("https://github.com/bobbylight/RText")) {
			RText app = getApplication();
			UIManager.getLookAndFeel().provideErrorFeedback(app);
		}
	}


}
