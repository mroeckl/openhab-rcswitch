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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.Pin;

import de.pi3g.pi.rcswitch.RCSwitch;


/**
 * This class opens a connection to the RC switch control module connected to the GPIO pins of the Raspberry Pi 
 * and sends On/Off commands.
 * 
 * @author Matthias Röckl
 * @since 1.0.0
 */
public class RcSwitchConnection {

	private static final Logger LOGGER = LoggerFactory.getLogger(RcSwitchConnection.class);
	
	private BitSet groupAddress;
	private int deviceAddress;
	private RCSwitch transmitter;
	
	/**
	 * Creates a new {@link RcSwitchConnection} for the given GPIO pin and the group and device address of the
	 * @param gpioPin
	 * @param groupAddress
	 * @param deviceAddress
	 */
	public RcSwitchConnection(Pin gpioPin, String groupAddress, int deviceAddress) {
		this.deviceAddress = deviceAddress;
		this.transmitter = new RCSwitch(gpioPin);
		this.groupAddress = RCSwitch.getSwitchGroupAddress(groupAddress);
	}

	/**
	 * Sends the command to the RC Switch.
	 * 
	 * @param cmd Command to send (only 'ON'/'OFF' supported)
	 */
	public void send(final String cmd) {
		LOGGER.trace("Setting " + this.toString() + " to " + cmd);

		if ("ON".equals(cmd)) {
			this.transmitter.switchOn(this.groupAddress, deviceAddress);
		} else {
			this.transmitter.switchOff(this.groupAddress, deviceAddress);
		}
	}
	
	@Override
	public String toString() {
		return "RcSwitch [groupAddress=" + this.groupAddress + ", deviceAddress=" + this.deviceAddress + "]";
	}

}