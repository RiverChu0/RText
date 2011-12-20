/*
 * 12/17/2010
 *
 * ConsoleWindow.java - Text component for the console.
 * Copyright (C) 2010 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://rtext.sfifesoft.com
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
package org.fife.rtext.plugins.console;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import org.fife.rtext.RText;
import org.fife.ui.dockablewindows.DockableWindow;


/**
 * A dockable window that acts as a console.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class ConsoleWindow extends DockableWindow implements PropertyChangeListener {

//	private Plugin plugin;
	private CardLayout cards;
	private JPanel mainPanel;
	private SystemShellTextArea shellTextArea;
	private JavaScriptShellTextArea jsTextArea;

	private JToolBar toolbar;
	private JComboBox shellCombo;
	private StopAction stopAction;


	public ConsoleWindow(RText app, Plugin plugin) {

//		this.plugin = plugin;
		setDockableWindowName(plugin.getString("DockableWindow.Title"));
		setIcon(plugin.getPluginIcon());
		setPosition(DockableWindow.BOTTOM);
		setLayout(new BorderLayout());

		Listener listener = new Listener();

		// Create the main panel, containing the shells.
		cards = new CardLayout();
		mainPanel = new JPanel(cards);
		add(mainPanel);

		shellTextArea = new SystemShellTextArea(plugin);
		shellTextArea.addPropertyChangeListener(
							ConsoleTextArea.PROPERTY_PROCESS_RUNNING, this);
		JScrollPane sp = new JScrollPane(shellTextArea);
		mainPanel.add(sp, "System");

		jsTextArea = new JavaScriptShellTextArea(plugin);
		sp = new JScrollPane(jsTextArea);
		mainPanel.add(sp, "JavaScript");

		// Create a "toolbar" for the shells.
		toolbar = new JToolBar();
		toolbar.setFloatable(false);

		JLabel label = new JLabel(plugin.getString("Shell"));
		Box temp = Box.createHorizontalBox();
		temp.add(label);
		temp.add(Box.createHorizontalStrut(5));
		shellCombo = new JComboBox();
		shellCombo.addItemListener(listener);
		shellCombo.addItem(plugin.getString("System"));
		shellCombo.addItem(plugin.getString("JavaScript"));
		temp.add(shellCombo);
		temp.add(Box.createHorizontalGlue());
		JPanel temp2 = new JPanel(new BorderLayout());
		temp2.add(temp, BorderLayout.LINE_START);
		toolbar.add(temp2);
		toolbar.add(Box.createHorizontalGlue());

		stopAction = new StopAction(app, Plugin.msg, plugin);
		JButton b = new JButton(stopAction);
		b.setText(null);
		toolbar.add(b);
		add(toolbar, BorderLayout.NORTH);

	}


	/**
	 * Clears any text from all consoles.
	 */
	public void clearConsoles() {
		jsTextArea.clear();
		shellTextArea.clear();
	}


	/**
	 * Returns the color used for a given type of text in the consoles.
	 *
	 * @param style The style; e.g. {@link ConsoleTextArea#STYLE_STDOUT}.
	 * @return The color, or <code>null</code> if the system default color
	 *         is being used.
	 * @see #setForeground(String, Color)
	 */
	public Color getForeground(String style) {
		Color c = null;
		Style s = jsTextArea.getStyle(style);
		if (s!=null) {
			c = StyleConstants.getForeground(s);
		}
		return c;
	}


	/**
	 * Returns whether a special style is used for a given type of text in
	 * the consoles.
	 *
	 * @param style The style of text.
	 * @return Whether a special style is used.
	 */
	public boolean isStyleUsed(String style) {
		return jsTextArea.getStyle(style).isDefined(StyleConstants.Foreground);
		//return getForeground(style)!=null;
	}


	/**
	 * Called whenever a process starts or completes.
	 */
	public void propertyChange(PropertyChangeEvent e) {

		String prop = e.getPropertyName();

		if (ConsoleTextArea.PROPERTY_PROCESS_RUNNING.equals(prop)) {
			boolean running = ((Boolean)e.getNewValue()).booleanValue();
			stopAction.setEnabled(running);
		}

	}


	/**
	 * Sets the color used for a given type of text in the consoles.
	 *
	 * @param style The style; e.g. {@link ConsoleTextArea#STYLE_STDOUT}.
	 * @param fg The new foreground color to use, or <code>null</code> to
	 *        use the system default foreground color.
	 * @see #getForeground(String)
	 */
	public void setForeground(String style, Color fg) {
		setForegroundImpl(style, fg, jsTextArea);
		setForegroundImpl(style, fg, shellTextArea);
	}


	/**
	 * Sets a color for a given type of a text in a single console.
	 *
	 * @param style
	 * @param fg
	 * @param textArea
	 */
	private void setForegroundImpl(String style, Color fg,
									ConsoleTextArea textArea) {
		Style s = textArea.getStyle(style);
		if (s!=null) {
			if (fg!=null) {
				StyleConstants.setForeground(s, fg);
			}
			else {
				s.removeAttribute(StyleConstants.Foreground);
			}
		}
	}


	/**
	 * Stops the currently running process, if any.
	 */
	public void stopCurrentProcess() {
		shellTextArea.stopCurrentProcess();
	}


	/**
	 * Listens for events in this dockable window.
	 */
	private class Listener implements ItemListener {

		public void itemStateChanged(ItemEvent e) {

			JComboBox source = (JComboBox)e.getSource();
			if (source==shellCombo) {
				int index = shellCombo.getSelectedIndex();
				if (index==0) {
					cards.show(mainPanel, "System");
				}
				else {
					cards.show(mainPanel, "JavaScript");
				}
			}

		}

	}


}