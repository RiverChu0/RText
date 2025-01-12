/*
 * 09/20/2005
 *
 * HeapIndicatorPreferences.java - Preferences for the Heap Indicator.
 * Copyright (C) 2005 Robert Futrell
 * https://fifesoft.com/rtext
 * Licensed under a modified BSD license.
 * See the included license file for details.
 */
package org.fife.rtext.plugins.heapindicator;

import org.fife.ui.app.prefs.Prefs;


/**
 * Preferences for the heap indicator.
 *
 * @author Robert Futrell
 * @version 0.8
 */
public class HeapIndicatorPrefs extends Prefs {

	public boolean visible;
	public int refreshInterval;

	private static final boolean DEFAULT_VISIBLE = true;
	private static final int DEFAULT_REFRESH_INTERVAL = 10000;

	@Override
	public void setDefaults() {
		visible         = DEFAULT_VISIBLE;
		refreshInterval = DEFAULT_REFRESH_INTERVAL;
	}


}
