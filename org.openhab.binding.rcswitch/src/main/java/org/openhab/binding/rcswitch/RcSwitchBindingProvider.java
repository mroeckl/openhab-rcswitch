/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rcswitch;

import org.openhab.binding.rcswitch.internal.RcSwitchBindingConfig;
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
	 * Returns the configuration for the item with the given name
	 * @param itemName  the name of the item
	 * @return  the configuration of the item
	 */
	RcSwitchBindingConfig getItemConfig(String itemName);
}
