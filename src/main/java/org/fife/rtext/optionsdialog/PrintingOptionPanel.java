/*
 * 02/27/2004
 *
 * PrintingOptionPanel.java - Options panel containing printing options.
 * Copyright (C) 2004 Robert Futrell
 * https://fifesoft.com/rtext
 * Licensed under a modified BSD license.
 * See the included license file for details.
 */
package org.fife.rtext.optionsdialog;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import javax.swing.*;

import org.fife.rtext.AbstractMainView;
import org.fife.rtext.RText;
import org.fife.ui.FontSelector;
import org.fife.ui.OptionsDialogPanel;
import org.fife.ui.UIUtil;


/**
 * Options panel for options dialog giving the printing options available.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class PrintingOptionPanel extends OptionsDialogPanel
							implements PropertyChangeListener {

	private final FontSelector fontSelector;

	//private boolean useHeader;		// Internal variable used to remember what the user clicked.
	//private boolean useFooter;		// Internal variable used to remember what the user clicked.


	/**
	 * Constructor.
	 *
	 * @param rtext The parent RText instance.
	 * @param msg The resource bundle to use.
	 */
	PrintingOptionPanel(final RText rtext, final ResourceBundle msg) {

		super(msg.getString("OptPrName"));

		ComponentOrientation orientation = ComponentOrientation.
									getOrientation(getLocale());

		setLayout(new BorderLayout());
		setBorder(UIUtil.getEmpty5Border());
		Box topPanel = Box.createVerticalBox();

		JPanel printFontPanel = new JPanel(new BorderLayout());
		printFontPanel.setBorder(new OptionPanelBorder(msg.getString("OptPrFTitle")));
		fontSelector = new FontSelector();
		fontSelector.addPropertyChangeListener(FontSelector.FONT_PROPERTY, this);
		printFontPanel.add(fontSelector);
		topPanel.add(printFontPanel);
		topPanel.add(Box.createVerticalStrut(SECTION_VERTICAL_SPACING));

		Box printHeaderFooterPanel = Box.createVerticalBox();
		printHeaderFooterPanel.setBorder(new OptionPanelBorder(msg.getString("OptPrHFL")));
		JCheckBox headerCheckBox = new JCheckBox(msg.getString("OptPrPH"));
		JCheckBox footerCheckBox = new JCheckBox(msg.getString("OptPrPF"));
		headerCheckBox.setEnabled(false);
		footerCheckBox.setEnabled(false);
		addLeftAligned(printHeaderFooterPanel, headerCheckBox, COMPONENT_VERTICAL_SPACING);
		addLeftAligned(printHeaderFooterPanel, footerCheckBox);
		topPanel.add(printHeaderFooterPanel);

		add(topPanel, BorderLayout.NORTH);
		applyComponentOrientation(orientation);
		setIcon((Icon)rtext.getAction(RText.PRINT_ACTION).getValue(Action.SMALL_ICON));
		rtext.addPropertyChangeListener(RText.ICON_STYLE_PROPERTY, (e) -> {
			setIcon((Icon)rtext.getAction(RText.PRINT_ACTION).getValue(Action.SMALL_ICON));
		});
	}


	/**
	 * Applies the settings entered into this dialog on the specified
	 * application.
	 *
	 * @param owner The application.
	 */
	@Override
	protected void doApplyImpl(Frame owner) {
		RText rtext = (RText)owner;
		AbstractMainView mainView = rtext.getMainView();
		mainView.setPrintFont(getPrintFont());			// Doesn't effect GUI.
		//mainView.setUsePrintHeader(pop.getUsePrintHeader());
		//mainView.setUsePrintFooter(pop.getUsePrintFooter());
	}


	/**
	 * Listens for actions in this panel.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent e) {

		String propertyName = e.getPropertyName();

		// If the user changed the print font...
		if (propertyName.equals(FontSelector.FONT_PROPERTY)) {
			setDirty(true);
		}

	}


	@Override
	protected OptionsPanelCheckResult ensureValidInputsImpl() {
		// They can't input invalid stuff on this options panel.
		return null;
	}


	/**
	 * Returns the font the user selected for printing.
	 *
	 * @return A font to use for printing.
	 * @see #setPrintFont
	 */
	private Font getPrintFont() {
		return fontSelector.getDisplayedFont();
	}


	@Override
	public JComponent getTopJComponent() {
		return fontSelector;
	}


	/**
	 * Sets the font currently being displayed as the font used for printing.
	 *
	 * @param printFont The font to display as the current font used for
	 *        printing.  If <code>null</code>, then a default is used.
	 * @see #getPrintFont
	 */
	private void setPrintFont(Font printFont) {
		if (printFont==null)
			printFont = new Font("Monospaced", Font.PLAIN, 9);
		fontSelector.setDisplayedFont(printFont, false);
	}


	/**
	 * Sets the values displayed by this panel to reflect those in the
	 * application.  Child panels are not handled.
	 *
	 * @param owner The parent application.
	 * @see #setValues(Frame)
	 */
	@Override
	protected void setValuesImpl(Frame owner) {
		RText rtext = (RText)owner;
		AbstractMainView mainView = rtext.getMainView();
		setPrintFont(mainView.getPrintFont());
		//setPrintHeader(mainView.printHeader());
		//setPrintFooter(mainView.printFooter());
	}


}
