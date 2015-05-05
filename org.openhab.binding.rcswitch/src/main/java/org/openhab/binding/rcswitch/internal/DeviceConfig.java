package org.openhab.binding.rcswitch.internal;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Device configuration which carries the connection details of one device (there could be several).
 */
public class DeviceConfig {

	Pin gpioPin = RaspiPin.GPIO_00;
	String groupAddress;
	int deviceAddress;

	RcSwitchConnection connection = null;
	String deviceId;

	/**
	 * Creates a new {@link DeviceConfig} for the given deviceId.
	 * @param deviceId  the ID of the device.
	 */
	public DeviceConfig(String deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public String toString() {
		return "Device [id=" + deviceId + ", groupAddress=" + groupAddress + ", deviceAddress=" + deviceAddress + ", gpioPin=" + gpioPin.getAddress() + "]";
	}

	/**
	 * Returns the {@link RcSwitchConnection} to switch on or off this device. 
	 * @return  the {@link RcSwitchConnection}
	 */
	public RcSwitchConnection getConnection() {
		if (connection == null) {
			connection = new RcSwitchConnection(gpioPin, groupAddress, deviceAddress);
		}
		return connection;
	}
}
