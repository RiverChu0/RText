/*
 * 10/12/2012
 *
 * ProjectPluginOptionPanel - Options panel for the Projects plugin.
 * Copyright (C) 2012 Robert Futrell
 * https://fifesoft.com/rtext
 * Licensed under a modified BSD license.
 * See the included license file for details.
 */
package org.fife.rtext.plugins.project;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ResourceBundle;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.fife.ui.UIUtil;
import org.fife.ui.app.PluginOptionsDialogPanel;
import org.fife.ui.dockablewindows.DockableWindow;
import org.fife.ui.dockablewindows.DockableWindowConstants;


/**
 * Options panel for the Projects plugin,=.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class ProjectPluginOptionPanel extends PluginOptionsDialogPanel<ProjectPlugin>
		implements ItemListener, ActionListener {

	private final JCheckBox visibleCB;
	private final JLabel locationLabel;
	private final JComboBox<String> locationCombo;


	/**
	 * Constructor.
	 *
	 * @param plugin The plugin whose options we're displaying.
	 */
	ProjectPluginOptionPanel(ProjectPlugin plugin) {

		super(plugin);
		setName(Messages.getString("ProjectPlugin.Name"));

		ResourceBundle gpb = ResourceBundle.getBundle(
				"org/fife/ui/app/GUIPlugin");
		ComponentOrientation orientation = ComponentOrientation
				.getOrientation(getLocale());

		setLayout(new BorderLayout());
		setBorder(UIUtil.getEmpty5Border());
		Box topPanel = Box.createVerticalBox();
		topPanel.setBorder(new OptionPanelBorder(Messages
			.getString("OptionPanel.Title")));

		// A check box toggling the plugin's visibility.
		visibleCB = new JCheckBox(gpb.getString("Visible"));
		visibleCB.addActionListener(this);
		addLeftAligned(topPanel, visibleCB, COMPONENT_VERTICAL_SPACING);

		// A combo in which to select the dockable window's placement.
		Box locationPanel = createHorizontalBox();
		locationCombo = new JComboBox<>();
		UIUtil.fixComboOrientation(locationCombo);
		locationCombo.addItem(gpb.getString("Location.top"));
		locationCombo.addItem(gpb.getString("Location.left"));
		locationCombo.addItem(gpb.getString("Location.bottom"));
		locationCombo.addItem(gpb.getString("Location.right"));
		locationCombo.addItem(gpb.getString("Location.floating"));
		locationCombo.addItemListener(this);
		locationLabel = new JLabel(gpb.getString("Location.title"));
		locationLabel.setLabelFor(locationCombo);
		locationPanel.add(locationLabel);
		locationPanel.add(Box.createHorizontalStrut(5));
		locationPanel.add(locationCombo);
		locationPanel.add(Box.createHorizontalGlue());
		addLeftAligned(topPanel, locationPanel);

		// Put it all together!
		add(topPanel, BorderLayout.NORTH);
		applyComponentOrientation(orientation);

	}


	@Override
	public void actionPerformed(ActionEvent e) {

		Object source = e.getSource();

		if (visibleCB==source) {
			setVisibleCBSelected(visibleCB.isSelected());
			setDirty(true);
		}

	}


	@Override
	protected void doApplyImpl(Frame owner) {
		ProjectPlugin pp = getPlugin();
		DockableWindow wind = pp.getDockableWindow();
		wind.setActive(visibleCB.isSelected());
		wind.setPosition(getDockableWindowPlacement());
	}


	@Override
	protected OptionsPanelCheckResult ensureValidInputsImpl() {
		// They can't input invalid stuff on this options panel.
		return null;
	}


	/**
	 * Returns the selected placement for this plugin's dockable window.
	 *
	 * @return The selected placement.
	 * @see #setDockableWindowPlacement(int)
	 */
	private int getDockableWindowPlacement() {
		return locationCombo.getSelectedIndex();
	}


	@Override
	public JComponent getTopJComponent() {
		return visibleCB;
	}


	/**
	 * Gets notified when the user selects an item in the location combo box.
	 *
	 * @param e The event.
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource()==locationCombo &&
				e.getStateChange()==ItemEvent.SELECTED) {
			setDirty(true);
		}
	}


	/**
	 * Sets the location of this plugin's dockable window.
	 *
	 * @param placement The new dockable window location; should be one of the
	 *        constants in {@link DockableWindowConstants}.
	 * @see #getDockableWindowPlacement()
	 */
	private void setDockableWindowPlacement(int placement) {
		if (!DockableWindow.isValidPosition(placement)) {
			placement = DockableWindowConstants.LEFT;
		}
		locationCombo.setSelectedIndex(placement);
	}


	/**
	 * Updates this panel's displayed parameter values to reflect those of
	 * this plugin.
	 */
	@Override
	protected void setValuesImpl(Frame frame) {
		ProjectPlugin pp = getPlugin();
		DockableWindow wind = pp.getDockableWindow();
		setVisibleCBSelected(wind.isActive());
		setDockableWindowPlacement(wind.getPosition());
	}


	private void setVisibleCBSelected(boolean selected) {
		visibleCB.setSelected(selected);
		locationLabel.setEnabled(selected);
		locationCombo.setEnabled(selected);
	}


}
