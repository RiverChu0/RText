/*
 * 10/26/2013
 *
 * SearchOptionPanel.java - Options related to searching.
 * Copyright (C) 2013 Robert Futrell
 * https://fifesoft.com/rtext
 * Licensed under a modified BSD license.
 * See the included license file for details.
 */
package org.fife.rtext.optionsdialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.fife.rtext.AbstractMainView;
import org.fife.rtext.RText;
import org.fife.rtext.SearchManager.SearchingMode;
import org.fife.ui.OptionsDialogPanel;
import org.fife.ui.SelectableLabel;
import org.fife.ui.LabelValueComboBox;
import org.fife.ui.UIUtil;
import org.fife.ui.app.GUIApplication;
import org.fife.util.TranslucencyUtil;


/**
 * An options panel that display all search-related options.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class SearchOptionPanel extends OptionsDialogPanel
		implements ActionListener, ChangeListener {

	private final JRadioButton dialogRB;
	private final JRadioButton toolbarRB;
	private final JCheckBox translucentSearchDialogsCB;
	private final JLabel ruleLabel;
	private final LabelValueComboBox<String, String> ruleCombo;
	private final JLabel opacityLabel;
	private final JSlider slider;
	private final JLabel opacityDisplay;

	private final DecimalFormat format;

	private static final String PROPERTY	= "property";


	/**
	 * Constructor.
	 *
	 * @param app The owner of the options dialog in which this panel appears.
	 * @param msg The resource bundle to use.
	 */
	SearchOptionPanel(GUIApplication app, ResourceBundle msg) {

		super(msg.getString("OptSearchOptionsName"));
		format = new DecimalFormat("0%");

		// Set up our border and layout.
		setBorder(UIUtil.getEmpty5Border());
		setLayout(new BorderLayout());

		// Create a panel for stuff aligned at the top.
		Box topPanel = Box.createVerticalBox();

		ComponentOrientation o = ComponentOrientation.
									getOrientation(getLocale());

		// A panel for toggling the search UI
		Box generalPanel = Box.createVerticalBox();
		generalPanel.setBorder(new OptionPanelBorder(
									msg.getString("OptGenTitle")));
		ButtonGroup bg = new ButtonGroup();
		dialogRB = UIUtil.newRadio(msg, "Search.UIType.Dialog",
				bg, this);
		addLeftAligned(generalPanel, dialogRB, COMPONENT_VERTICAL_SPACING);
		toolbarRB = UIUtil.newRadio(msg, "Search.UIType.Toolbar",
				bg, this);
		addLeftAligned(generalPanel, toolbarRB);
		topPanel.add(generalPanel);
		topPanel.add(Box.createVerticalStrut(SECTION_VERTICAL_SPACING));

		// A panel for "experimental" options.
		Box expPanel = Box.createVerticalBox();
		expPanel.setBorder(new OptionPanelBorder(msg.
										getString("OptExperimentalTitle")));
		SelectableLabel label = new SelectableLabel(
								msg.getString("ExperimentalDisclaimer"));
		addLeftAligned(expPanel, label, COMPONENT_VERTICAL_SPACING);
		translucentSearchDialogsCB = new JCheckBox(
								msg.getString("TranslucentSearchBoxes"));
		translucentSearchDialogsCB.setActionCommand("TranslucentSearchDialogsCB");
		translucentSearchDialogsCB.addActionListener(this);
		addLeftAligned(expPanel, translucentSearchDialogsCB, COMPONENT_VERTICAL_SPACING);

		ruleLabel = new JLabel(msg.getString("TranslucencyRule"));
		ruleCombo = new LabelValueComboBox<>();
		ruleCombo.addLabelValuePair(msg.getString("Translucency.Never"), "0");
		ruleCombo.addLabelValuePair(msg.getString("Translucency.WhenNotFocused"), "1");
		ruleCombo.addLabelValuePair(msg.getString("Translucency.WhenOverlappingApp"), "2");
		ruleCombo.addLabelValuePair(msg.getString("Translucency.Always"), "3");
		ruleCombo.setActionCommand("TranslucencyRuleChanged");
		ruleCombo.addActionListener(this);
		opacityLabel = new JLabel(msg.getString("Opacity"));
		slider = new JSlider(0, 100);
		slider.setMajorTickSpacing(20);
		slider.setPaintTicks(true);
		slider.setPaintLabels(false);
		slider.addChangeListener(this);
		opacityDisplay = new JLabel("100%") { // will be replaced with real value
			// hack to keep SpringLayout from shifting when # of digits changes in %
			@Override
			public Dimension getPreferredSize() {
				Dimension size = super.getPreferredSize();
				size.width = Math.max(50, size.width);
				return size;
			}
		};
		// Small border for spacing in both LTR and RTL locales
		opacityDisplay.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		Component filler = Box.createRigidArea(new Dimension(1, 1)); // Must have real size!
		JPanel temp = new JPanel(new SpringLayout());
		if (o.isLeftToRight()) {
			temp.add(ruleLabel);    temp.add(ruleCombo); temp.add(filler);
			temp.add(opacityLabel); temp.add(slider);    temp.add(opacityDisplay);
		}
		else {
			temp.add(filler);         temp.add(ruleCombo); temp.add(ruleLabel);
			temp.add(opacityDisplay); temp.add(slider);    temp.add(opacityLabel);
		}
		UIUtil.makeSpringCompactGrid(temp, 2,3, 0, 0, 5,5);
		addLeftAligned(expPanel, temp, 0, 20);
		expPanel.add(Box.createVerticalStrut(SECTION_VERTICAL_SPACING));
		topPanel.add(expPanel);

		JButton defaultsButton = new JButton(msg.getString("RestoreDefaults"));
		defaultsButton.setActionCommand("RestoreDefaults");
		defaultsButton.addActionListener(this);
		addLeftAligned(topPanel, defaultsButton);

		// Do this after everything else is created.
		if (TranslucencyUtil.get().isTranslucencySupported(false)) {
			setTranslucentSearchDialogsSelected(false);
		}

		add(topPanel, BorderLayout.NORTH);
		applyComponentOrientation(o);

	}


	@Override
	public void actionPerformed(ActionEvent e) {

		Object source = e.getSource();
		String command = e.getActionCommand();

		if (dialogRB==source) {
			setUseSearchToolbars(false);
			setDirty(true);
		}

		else if (toolbarRB==source) {
			setUseSearchToolbars(true);
			setDirty(true);
		}

		else if (translucentSearchDialogsCB==source) {
			boolean selected = translucentSearchDialogsCB.isSelected();
			setTranslucentSearchDialogsSelected(selected);
			setDirty(true);
		}

		else if (ruleCombo==source) {
			setDirty(true);
		}

		else if ("RestoreDefaults".equals(command)) {

			final int defaultOpacity = 60;

			if (dialogRB.isSelected() ||
					translucentSearchDialogsCB.isSelected() ||
					ruleCombo.getSelectedIndex()!=2 ||
					slider.getValue()!=defaultOpacity) {
				setUseSearchToolbars(true);
				setTranslucentSearchDialogsSelected(false);
				ruleCombo.setSelectedIndex(2);
				slider.setValue(defaultOpacity);
				setDirty(true);
			}
		}

	}


	@Override
	protected void doApplyImpl(Frame owner) {

		RText rtext = (RText)owner;

		SearchingMode mode = dialogRB.isSelected() ?
				SearchingMode.DIALOGS : SearchingMode.TOOLBARS;
		rtext.getMainView().getSearchManager().setSearchingMode(mode);

		// Experimental options
		rtext.setSearchWindowOpacityEnabled(translucentSearchDialogsCB.
															isSelected());
		int rule = Integer.parseInt(ruleCombo.getSelectedValue());
		rtext.setSearchWindowOpacityRule(rule);
		float opacity = slider.getValue() / 100f;
		rtext.setSearchWindowOpacity(opacity);

	}


	@Override
	protected OptionsPanelCheckResult ensureValidInputsImpl() {
		// They can't input invalid stuff on this options panel.
		return null;
	}


	@Override
	public JComponent getTopJComponent() {
		return dialogRB;
	}


	private void setTranslucentSearchDialogsSelected(boolean selected) {

		translucentSearchDialogsCB.setSelected(selected); // Probably already done

		// Not all systems support per-pixel translucency
		if (!TranslucencyUtil.get().isTranslucencySupported(false)) {
			translucentSearchDialogsCB.setEnabled(false);
			selected = false;
		}

		ruleLabel.setEnabled(selected);
		ruleCombo.setEnabled(selected);
		opacityLabel.setEnabled(selected);
		opacityDisplay.setEnabled(selected);
		slider.setEnabled(selected);

	}


	private void setUseSearchToolbars(boolean use) {
		if (use) {
			toolbarRB.setSelected(true);
			translucentSearchDialogsCB.setEnabled(false);
			ruleLabel.setEnabled(false);
			ruleCombo.setEnabled(false);
			opacityLabel.setEnabled(false);
			opacityDisplay.setEnabled(false);
			slider.setEnabled(false);
		}
		else {
			dialogRB.setSelected(true);
			translucentSearchDialogsCB.setEnabled(true);
			// Set related components to correct enabled state
			setTranslucentSearchDialogsSelected(
					translucentSearchDialogsCB.isSelected());
		}
	}


	@Override
	protected void setValuesImpl(Frame owner) {

		RText rtext = (RText)owner;
		AbstractMainView mainView = rtext.getMainView();

		// Experimental options
		setTranslucentSearchDialogsSelected(rtext.isSearchWindowOpacityEnabled());
		ruleCombo.setSelectedIndex(rtext.getSearchWindowOpacityRule());
		int percent = (int)(rtext.getSearchWindowOpacity()*100);
		slider.setValue(percent);
		opacityDisplay.setText(format.format(rtext.getSearchWindowOpacity()));

		// Do this after experimental section, since its toggling of enabled
		// states of components could override certain values.
		SearchingMode sm = mainView.getSearchManager().getSearchingMode();
		setUseSearchToolbars(sm==SearchingMode.TOOLBARS);

	}


	/**
	 * Called when the user plays with the opacity slider.
	 *
	 * @param e The change event.
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		float value = slider.getValue() / 100f;
		opacityDisplay.setText(format.format(value));
		setDirty(true);
	}


}
