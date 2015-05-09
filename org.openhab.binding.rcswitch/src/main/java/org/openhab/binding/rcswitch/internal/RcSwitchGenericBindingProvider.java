/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rcswitch.internal;

import java.util.BitSet;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.rcswitch.RcSwitchBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.pi3g.pi.rcswitch.RCSwitch;

/**
 * <p>
 * This class can parse information from the generic binding format and provides
 * RC Switch device binding information from it.
 * 
 * <p>
 * The syntax of the binding configuration strings accepted is the following:
 * <p>
 * <code>
 * 	rcswitch="&lt;group-address>:&lt;device-address>"
 * </code>
 * 
 * <p>
 * Examples for valid binding configuration strings:
 * 
 * <ul>
 * <li><code>rcswitch="10101:4"</code></li>
 * </ul>
 * 
 * @author Matthias Röckl
 * @since 1.0.0
 */
public class RcSwitchGenericBindingProvider extends
		AbstractGenericBindingProvider implements RcSwitchBindingProvider {

	static final Logger LOGGER = LoggerFactory
			.getLogger(RcSwitchGenericBindingProvider.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openhab.model.item.binding.BindingConfigReader#getBindingType()
	 */
	public String getBindingType() {
		return "rcswitch";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openhab.model.item.binding.BindingConfigReader#validateItemType(org
	 * .openhab.core.items.Item, java.lang.String)
	 */
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof SwitchItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only SwitchItem is allowed - please check your *.items configuration");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openhab.model.item.binding.AbstractGenericBindingProvider#
	 * processBindingConfiguration(java.lang.String,
	 * org.openhab.core.items.Item, java.lang.String)
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		if (item instanceof SwitchItem) {
			// Group address
			String groupAddressString = StringUtils.substringBefore(
					bindingConfig, ":");
			// Device address
			String deviceAddressString = StringUtils.substringAfter(
					bindingConfig, ":");
			if (deviceAddressString == null || deviceAddressString.isEmpty()
					|| groupAddressString == null
					|| groupAddressString.isEmpty()) {
				LOGGER.error("The item configuration '"
						+ bindingConfig
						+ "' is invalid. Valid format is <group-address>:<device-address>.");
				return;
			}
			// Store the configuration
			try {
				BitSet groupAddress = RCSwitch
						.getSwitchGroupAddress(groupAddressString);
				int deviceAddress = Integer.parseInt(deviceAddressString);
				RcSwitchBindingConfig config = new RcSwitchBindingConfig(
						groupAddress, deviceAddress);
				this.addBindingConfig(item, config);
			} catch (IndexOutOfBoundsException e) {
				LOGGER.error("The group address '"
						+ groupAddressString
						+ "' is invalid. The group address must have 5 bits, e.g. 10101.");
			} catch (NumberFormatException e) {
				LOGGER.error("The device address '"
						+ deviceAddressString
						+ "' is invalid. The device address must be an Integer value, e.g. 4.");
			}
		}
	}

	public RcSwitchBindingConfig getItemConfig(String itemName) {
		return (RcSwitchBindingConfig) bindingConfigs.get(itemName);
	}

}
