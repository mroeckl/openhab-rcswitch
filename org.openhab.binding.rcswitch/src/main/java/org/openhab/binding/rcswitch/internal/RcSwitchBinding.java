/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rcswitch.internal;

import java.util.Dictionary;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.rcswitch.RcSwitchBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiGpioProvider;
import com.pi4j.io.gpio.impl.PinImpl;

/**
 * <p>
 * Binding listening OpenHAB bus and send commands to RC switches when command is received.
 * 
 * @author Matthias Röckl
 * @since 1.0.0
 */
public class RcSwitchBinding extends AbstractBinding<RcSwitchBindingProvider> implements ManagedService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RcSwitchBinding.class);

	private static final Pattern CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(gpioPin|groupAddress|deviceAddress)$");
	
	protected Map<String, DeviceConfig> deviceConfigCache = new HashMap<String, DeviceConfig>();
	

	/*
	 * (non-Javadoc)
	 * @see org.openhab.core.binding.AbstractBinding#activate()
	 */
	@Override
	public void activate() {
		LOGGER.debug("RcSwitch activated");
	}

	/*
	 * (non-Javadoc)
	 * @see org.openhab.core.binding.AbstractBinding#deactivate()
	 */
	@Override
	public void deactivate() {
		LOGGER.debug("RcSwitch deactivated");
	}

	/*
	 * (non-Javadoc)
	 * @see org.openhab.core.binding.AbstractBinding#internalReceiveCommand(java.lang.String, org.openhab.core.types.Command)
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// Command has been received
		
		if (itemName != null) {
			RcSwitchBindingProvider provider = this.findFirstMatchingBindingProvider(itemName, command.toString());

			if (provider == null) {
				LOGGER.warn("Doesn't find matching binding provider [itemName={}, command={}]", itemName, command);
				return;
			}
			LOGGER.debug(
					"Received command (item='{}', state='{}', class='{}')",
					new Object[] { itemName, command.toString(), command.getClass().toString() });

			String commandString = provider.getRcSwitchCommand(itemName, command.toString());

			String[] commandParts = commandString.split(":");
			String deviceId = commandParts[0];

			LOGGER.debug("Get connection details for device id '{}'", deviceId);
			
			DeviceConfig tvConfig = deviceConfigCache.get(deviceId);

			if (tvConfig != null) {
				RcSwitchConnection remoteController = tvConfig.getConnection();

				if (remoteController != null) {
					remoteController.send(command.toString());
				}
				
			} else {
				LOGGER.warn("Cannot find connection details for device id '{}'", deviceId);
			}
		}
	}

	/**
	 * Find the first matching {@link RcSwitchBindingProvider} according to <code>itemName</code>.
	 * 
	 * @param itemName
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 *         provider could be found
	 */
	private RcSwitchBindingProvider findFirstMatchingBindingProvider(String itemName, String command) {
		
		RcSwitchBindingProvider firstMatchingProvider = null;

		for (RcSwitchBindingProvider provider : this.providers) {

			String commandString = provider.getRcSwitchCommand(itemName, command.toString());

			if (commandString != null) {
				firstMatchingProvider = provider;
				break;
			}
		}

		return firstMatchingProvider;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.service.cm.ManagedService#updated(java.util.Dictionary)
	 */
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		LOGGER.debug("New configuration received");
		if (config != null) {
			Enumeration<String> keys = config.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();

				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}

				Matcher matcher = CONFIG_PATTERN.matcher(key);

				if (!matcher.matches()) {
					LOGGER.debug("given config key '"
							+ key
							+ "' does not follow the expected pattern '<id>.<host|port>'");
					continue;
				}

				matcher.reset();
				matcher.find();

				String deviceId = matcher.group(1);

				DeviceConfig deviceConfig = deviceConfigCache.get(deviceId);

				if (deviceConfig == null) {
					deviceConfig = new DeviceConfig(deviceId);
					deviceConfigCache.put(deviceId, deviceConfig);
				}

				String configKey = matcher.group(2);
				String value = (String) config.get(key);

				if ("gpioPin".equals(configKey)) {
					int pinAddress = Integer.parseInt(value);
					deviceConfig.gpioPin = new PinImpl(RaspiGpioProvider.NAME, pinAddress, "GPIO_" + pinAddress, 
							EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT),
							PinPullResistance.all());
				} else if ("groupAddress".equals(configKey)) {
					deviceConfig.groupAddress = value;
				} else if ("deviceAddress".equals(configKey)) {
					deviceConfig.deviceAddress = Integer.parseInt(value);
				} else {
					throw new ConfigurationException(configKey, "the given configKey '" + configKey + "' is unknown");
				}
			}
		}
	}
}