/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rcswitch;

import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and RcSwitch devices.
 * 
 * @author Matthias Röckl
 * @since 1.0.0
 */
public interface RcSwitchBindingProvider extends BindingProvider {

	/**
	 * Returns the command to the given <code>itemName</code>.
	 * 
	 * @param itemName
	 *            the item for which to find a command.
	 * @param command
	 *            the openHAB command for which to find a RC switch command
	 * 
	 * @return the corresponding command or <code>null</code> if no matching
	 *         device id could be found.
	 */
	public String getRcSwitchCommand(String itemName, String command);

}
