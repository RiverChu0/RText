/*
 * 12/10/2006
 *
 * ViewSplitAction.java - Action to split the editor view.
 * Copyright (C) 2006 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://rtext.fifesoft.com
 *
 * This file is a part of RText.
 *
 * RText is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * RText is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.fife.rtext;

import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import org.fife.ui.app.StandardAction;


/**
 * Action that splits the editor view either vertically or horizontally.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class ViewSplitAction extends StandardAction {

	//private int splitType;


	/**
	 * Creates a new <code>ViewSplitAction</code>.
	 *
	 * @param owner the main window of this rtext instance.
	 * @param text The text associated with the action.
	 * @param icon The icon associated with the action.
	 * @param desc The description of the action.
	 * @param mnemonic The mnemonic for the action.
	 * @param accelerator The accelerator key for the action.
	 * @param splitType One of
	 *        <code>AbstractMainView.VIEW_SPLIT_HORIZ_ACTION</code>,
	 *        <code>AbstractMainView.VIEW_SPLIT_NONE_ACTION</code>, or
	 *        <code>AbstractMainView.VIEW_SPLIT_VERT_ACTION</code>.
	 */
	 public ViewSplitAction(RText owner, String text, Icon icon, String desc,
	 			int mnemonic, KeyStroke accelerator, int splitType) {
		super(owner, text, icon, desc, mnemonic, accelerator);
		//this.splitType = splitType;
	}


	public void actionPerformed(ActionEvent e) {
		// TODO
		//owner.setEditorSplitType(splitType);
		//owner.getMainView().setEditorSplitType(splitType);
	}


}