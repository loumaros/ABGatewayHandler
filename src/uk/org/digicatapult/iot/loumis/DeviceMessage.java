package uk.org.digicatapult.iot.loumis;


/**
 * 
 * Every BLE broadcast/message picked up by the April Brother BLE Gateway v.4
 * comes encoded in a series of serialised data. This class is used to store
 * data for every message under the "devices" array as seen <a href=
 * "https://wiki.aprbrother.com/en/User_Guide_For_AB_BLE_Gateway_V4.html#keys">
 * here</a>.
 * 
 * <br>
 * Used together with {@link uk.org.digicatapult.iot.loumis.ABgatewayBroadcast}
 * 
 * @author Nikos Loumis
 * @version v.0.1
 *
 */
public class DeviceMessage {

	String advertisingType;
	String deviceMAC;
	String rssi;
	String minor;
	String rawData;

	DeviceMessage(String advertisingType, String deviceMAC, String rssi, String minor, String rawData) {
		this.advertisingType = advertisingType;
		this.deviceMAC = deviceMAC;
		this.rssi = rssi;
		this.minor = minor;
		this.rawData = rawData;

	}

	DeviceMessage() {

	}

	public String getAdvertisingType() {
		return advertisingType;
	}

	public void setAdvertisingType(String advertisingType) {
		this.advertisingType = advertisingType;
	}

	public String getDeviceMAC() {
		return deviceMAC;
	}

	public void setDeviceMAC(String deviceMAC) {
		this.deviceMAC = deviceMAC;
	}

	public String getRssi() {
		return rssi;
	}

	public void setRssi(String rssi) {
		this.rssi = rssi;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public String getMinor() {
		return minor;
	}

	public void setMinor(String minor) {
		this.minor = minor;
	}

}
