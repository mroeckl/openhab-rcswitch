/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rcswitch.internal;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.rcswitch.RcSwitchBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 * This class can parse information from the generic binding format and provides
 * RC Switch device binding information from it.
 * 
 * <p>
 * The syntax of the binding configuration strings accepted is the following:
 * <p>
 * <code>
 * 	rcswitch="&lt;openHAB-command>:&lt;device-id>[,&lt;openHAB-command>:&lt;device-id>][,...]"
 * </code>
 * <p>
 * where parts in brackets [] signify an optional information.
 * </p>
 * 
 * <p>
 * Examples for valid binding configuration strings:
 * 
 * <ul>
 * <li><code>rcswitch="ON:Livingroom, OFF:Livingroom"</code></li>
 * </ul>
 * 
 * @author Matthias Röckl
 * @since 1.0.0
 */
public class RcSwitchGenericBindingProvider extends AbstractGenericBindingProvider implements RcSwitchBindingProvider {

	static final Logger logger = LoggerFactory.getLogger(RcSwitchGenericBindingProvider.class);

	/*
	 * (non-Javadoc)
	 * @see org.openhab.model.item.binding.BindingConfigReader#getBindingType()
	 */
	public String getBindingType() {
		return "rcswitch";
	}

	/*
	 * (non-Javadoc)
	 * @see org.openhab.model.item.binding.BindingConfigReader#validateItemType(org.openhab.core.items.Item, java.lang.String)
	 */
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
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
	 * @see org.openhab.model.item.binding.AbstractGenericBindingProvider#processBindingConfiguration(java.lang.String, org.openhab.core.items.Item, java.lang.String)
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		RcSwitchBindingConfig config = new RcSwitchBindingConfig();
		this.parseBindingConfig(bindingConfig, config);
		this.addBindingConfig(item, config);
	}

	/**
	 * Parses the given bindingConfigs 
	 * @param bindingConfigs
	 * @param config
	 * @throws BindingConfigParseException
	 */
	protected void parseBindingConfig(String bindingConfigs, RcSwitchBindingConfig config) throws BindingConfigParseException {

		String bindingConfig = StringUtils.substringBefore(bindingConfigs, ",");
		String bindingConfigTail = StringUtils.substringAfter(bindingConfigs, ",");

		String[] configParts = bindingConfig.trim().split(":");

		if (configParts.length != 2) {
			throw new BindingConfigParseException("RC Switch binding must contain two parts separated by ':'");
		}

		String command = StringUtils.trim(configParts[0]);
		String switchId = StringUtils.trim(configParts[1]);

		// if there are more commands to parse do that recursively ...
		if (StringUtils.isNotBlank(bindingConfigTail)) {
			parseBindingConfig(bindingConfigTail, config);
		}

		config.put(command, switchId);
	}

	public String getRcSwitchCommand(String itemName, String command) {
		RcSwitchBindingConfig config = (RcSwitchBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.get(command) : null;
	}

	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the RC Switch
	 * binding provider.
	 */
	static class RcSwitchBindingConfig extends HashMap<String, String> implements BindingConfig {

		/** generated serialVersion UID */
		private static final long serialVersionUID = 861870438027351568L;
	}

}
