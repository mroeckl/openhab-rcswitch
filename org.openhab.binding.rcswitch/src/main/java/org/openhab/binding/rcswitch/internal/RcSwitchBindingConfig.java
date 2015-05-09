package org.openhab.binding.rcswitch.internal;

import java.util.BitSet;

import org.openhab.core.binding.BindingConfig;

/**
 * An {@link RcSwitchBindingConfig} stores group and device address of one RC
 * switch.
 * 
 * @author Matthias Röckl
 * @since 1.1.0
 */
public class RcSwitchBindingConfig implements BindingConfig {

	private BitSet groupAddress;
	private int deviceAddress;

	/**
	 * Creates a new {@link RcSwitchBindingConfig} with the given group and
	 * device address.
	 * 
	 * @param groupAddress
	 *            the group address, e.g. 10101
	 * @param deviceAddress
	 *            the device address, e.g. 4
	 */
	public RcSwitchBindingConfig(BitSet groupAddress, int deviceAddress) {
		this.groupAddress = groupAddress;
		this.deviceAddress = deviceAddress;
	}

	/**
	 * Returns the group address, e.g. 10101.
	 * 
	 * @return the group address
	 */
	public BitSet getGroupAddress() {
		return groupAddress;
	}

	/**
	 * Sets the group address, e.g. 10101.
	 * 
	 * @param groupAddress
	 *            the group address
	 */
	public void setGroupAddress(BitSet groupAddress) {
		this.groupAddress = groupAddress;
	}

	/**
	 * Returns the device address, e.g. 4.
	 * 
	 * @return the device address
	 */
	public int getDeviceAddress() {
		return deviceAddress;
	}

	/**
	 * Sets the device address, e.g. 4.
	 * 
	 * @param deviceAddress
	 *            the device address
	 */
	public void setDeviceAddress(int deviceAddress) {
		this.deviceAddress = deviceAddress;
	}

}
