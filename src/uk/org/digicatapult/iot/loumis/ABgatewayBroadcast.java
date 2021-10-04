package uk.org.digicatapult.iot.loumis;


import java.util.LinkedList;
import java.util.List;

/**
 * Object that holds all information included with every broadcast from the BLE
 * gateway. <br>
 * 
 * The AB gateway is posting data using the
 * <a href="https://msgpack.org/">MessagePack format</a>. The actual sequence of
 * the information sent can be found here: <a href=
 * "https://wiki.aprbrother.com/en/User_Guide_For_AB_BLE_Gateway_V4.html#data-format">April
 * Brother Wiki</a>
 * 
 * 
 * @author Nikos Loumis
 * @version v.0.1
 * 
 * @see {@link uk.org.digicatapult.iot.loumis.DeviceMessage}
 *
 */
public class ABgatewayBroadcast {

	String firmware_version;
	int messageID;
	int timeSinceBoot;
	String gatewayIP;
	String gatewayMac;

	List<DeviceMessage> devices;

	public ABgatewayBroadcast() {
		super();
		devices = new LinkedList<DeviceMessage>();

	}

	public String getFirmware_version() {
		return firmware_version;
	}

	public void setFirmware_version(String firmware_version) {
		this.firmware_version = firmware_version;
	}

	public int getMessageID() {
		return messageID;
	}

	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}

	public int getTimeSinceBoot() {
		return timeSinceBoot;
	}

	public void setTimeSinceBoot(int timeSinceBoot) {
		this.timeSinceBoot = timeSinceBoot;
	}

	public String getGatewayIP() {
		return gatewayIP;
	}

	public void setGatewayIP(String gatewayIP) {
		this.gatewayIP = gatewayIP;
	}

	public String getGatewayMac() {
		return gatewayMac;
	}

	public void setGatewayMac(String gatewayMac) {
		this.gatewayMac = gatewayMac;
	}

	public List<DeviceMessage> getDevices() {
		return devices;
	}

	public void setDevices(List<DeviceMessage> devices) {
		this.devices = devices;
	}

}
