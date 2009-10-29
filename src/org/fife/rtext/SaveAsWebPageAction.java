/*
 * 03/30/2004
 *
 * SaveAsWebPageAction.java - Action to save a copy of the current
 * file as HTML in RText.
 * Copyright (C) 2004 Robert Futrell
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

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.fife.ui.UIUtil;
import org.fife.ui.app.StandardAction;
import org.fife.ui.rtextfilechooser.filters.HTMLFileFilter;
import org.fife.ui.rsyntaxtextarea.Token;


/**
 * Action used by an <code>RText</code> to save a copy
 * of the current document as HTML.
 *
 * @author Robert Futrell
 * @version 1.0
 */
/*
 * TODO: Use CSS; have one CSS class per token type and just do
 *    <div class=\"" + token.type + "\"> + text + "</div>"
 */
class SaveAsWebPageAction extends StandardAction {


	/**
	 * Creates a new <code>SaveAsWebPageAction</code>.
	 *
	 * @param text The text associated with the action.
	 * @param icon The icon associated with the action.
	 * @param desc The description of the action.
	 * @param mnemonic The mnemonic for the action.
	 * @param accelerator The accelerator key for the action.
	 * @param owner the main window of this rtext instance.
	 */
	public SaveAsWebPageAction(RText owner, String text, Icon icon,String desc,
					int mnemonic, KeyStroke accelerator) {
		super(owner, text, icon, desc, mnemonic, accelerator);
	}


	public void actionPerformed(ActionEvent e) {

		RText owner = (RText)getApplication();

		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle(owner.getString("SaveAsWebPage"));
		chooser.addChoosableFileFilter(new HTMLFileFilter());
		chooser.removeChoosableFileFilter(chooser.getChoosableFileFilters()[0]);

		RTextEditorPane editor = owner.getMainView().getCurrentTextArea();
		String htmlFileName = editor.getFileFullPath();
		int extensionStart = htmlFileName.lastIndexOf('.');
		if (extensionStart!=-1) {
			htmlFileName = htmlFileName.substring(0,extensionStart) + ".html";
		}
		else {
			htmlFileName += ".html";
		}
		chooser.setSelectedFile(new File(htmlFileName));
		chooser.setComponentOrientation(owner.getComponentOrientation());
		chooser.setDragEnabled(true);

		if (chooser.showSaveDialog(owner)==JFileChooser.APPROVE_OPTION) {

			File chosenFile = chooser.getSelectedFile();
			String chosenFilePath = chosenFile.getAbsolutePath();

			// Prompt before overwriting file if it already exists.
			if (chosenFile.exists()) {
				String temp = owner.getString("FileAlreadyExists",
									chosenFile.getAbsolutePath());
				if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(
						owner, temp,
						owner.getString("ConfDialogTitle"),
						JOptionPane.YES_NO_OPTION)) {
					return;
						}
			}

			// Ensure that it has an HTML extension.
			if (!chosenFilePath.matches("[^\\.]*\\.htm[l]?"))
				chosenFilePath = chosenFilePath + ".html";

			// Try and write output to the current filename.
			try {
				saveAs(chosenFilePath);
			} catch (IOException ioe) {
				String desc = owner.getString("ErrorWritingFile",
									chosenFilePath, ioe.getMessage());
				JOptionPane.showMessageDialog(owner, desc,
								owner.getString("ErrorDialogTitle"),
								JOptionPane.ERROR_MESSAGE);
				owner.setMessages(null, "ERROR:  Could not save file!");
				ioe.printStackTrace();
			}

		}

	}


	private void saveAs(String path) throws IOException {

		String[] styles = new String[Token.NUM_TOKEN_TYPES];
		StringBuffer temp = new StringBuffer();
		StringBuffer sb = new StringBuffer();

		PrintWriter out = new PrintWriter(new BufferedWriter(
			new OutputStreamWriter(new FileOutputStream(path), "UTF-8")));
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		out.println("<head>");
		out.println("<!-- Generated by RText -->");
		out.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />");
		out.println("<title>" + path + "</title>");

		RText rtext = (RText)getApplication();
		RTextEditorPane textArea = rtext.getMainView().getCurrentTextArea();
		int lineCount = textArea.getLineCount();
		for (int i=0; i<lineCount; i++) {
			Token token = textArea.getTokenListForLine(i);
			while (token!=null && token.isPaintable()) {
				if (styles[token.type]==null) {
					temp.setLength(0);
					temp.append(".s").append(token.type).append(" {\n");
					Font font = textArea.getFontForTokenType(token.type);
					if (font.isBold()) {
						temp.append("font-weight: bold;\n");
					}
					if (font.isItalic()) {
						temp.append("font-style: italic;\n");
					}
					Color c = textArea.getForegroundForToken(token);
					temp.append("color: ").append(UIUtil.getHTMLFormatForColor(c)).append(";\n");
					temp.append("}");
					styles[token.type] = temp.toString();
				}
				sb.append("<span class=\"s" + token.type + "\">");
				sb.append(RTextUtilities.escapeForHTML(token.getLexeme(), "\n", true));
				sb.append("</span>");
				token = token.getNextToken();
			}
			sb.append('\n');
		}

		// Print CSS styles
		out.println("<style type=\"text/css\">");
		for (int i=0; i<styles.length; i++) {
			if (styles[i]!=null) {
				out.println(styles[i]);
			}
		}
		out.println("</style>");

		// Print the body
		out.println("</head>");
		out.println("<body>\n<pre>");
		out.println(sb.toString());
		out.println("</pre>\n</body>\n</html>");

		out.close();

	}


}