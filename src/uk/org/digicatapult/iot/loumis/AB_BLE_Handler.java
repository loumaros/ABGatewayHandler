package uk.org.digicatapult.iot.loumis;


import java.util.Map;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.msgpack.core.MessagePack;
import org.msgpack.value.ArrayValue;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

import com.google.gson.Gson;

/**
 * Java application that handles messages broadcasted from the
 * <a href="https://blog.aprbrother.com/product/ab-ble-gateway-4-0">April
 * Brother BLE gateway v4</a>. <br>
 * The application connects to an MQTT server and registers to the same topic
 * the BLE gateway defines in its dashboard. <br>
 * 
 * @author Nikos Loumis
 * @version v.0.1
 *
 */
public class AB_BLE_Handler {

	static MqttServer mMqttServer;
	static MessagePack messagePack;

	public static void main(String[] args) {
		mMqttServer = new MqttServer();
		new Thread(mMqttServer).start();

	}
	
	ABgatewayBroadcast mABgateway;

	static String firmware_version;
	static int messageID;
	static int timeSinceBoot;
	static String gatewayIP;
	static String gatewayMac;

	public static class MqttServer extends Thread implements MqttCallback {
		private MqttClient client;
		private String brokerUrl = MyConstants.PROTOCOL + MyConstants.BROKER_HOST_NAME + ":"
				+ MyConstants.BROKER_PORT_NUMBER;
		//
		private MqttConnectOptions conOpt;
		private boolean clean = MyConstants.cleanSession;
		private String password = MyConstants.password;
		private String userName = MyConstants.userName;
		private String clientId = MyConstants.CLIENT_ID;

		MessagePack mPack;

		public MqttServer() {

		}

		@Override
		public void run() {
			try {
				// Construct the connection options object that contains
				// connection parameters
				// such as cleansession and LWAT
				conOpt = new MqttConnectOptions();
				conOpt.setCleanSession(clean);
				if (password != null) {
					conOpt.setPassword(this.password.toCharArray());
				}
				if (userName != null) {
					conOpt.setUserName(this.userName);
				}

				// Construct an MQTT blocking mode client
				client = new MqttClient(this.brokerUrl, clientId);

				// Set this wrapper as the callback handler
				client.setCallback(this);
				client.connect(conOpt);

				System.out.println("Connected to " + brokerUrl + " with client ID " + client.getClientId());
				subscribeToTopics(MyConstants.MQTT_TOPIC_SUBSCRIBE, MyConstants.QOS);

			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		public void subscribeToTopics(final String topicName, int qos) throws MqttException {

			client.subscribe(topicName, MyConstants.QOS);
			System.out.println("Subscribed to topic \"" + topicName + "");

		}

		public void publishToBroker(String topicName, String messageToPublish) {

			try {

				MqttMessage message = new MqttMessage(messageToPublish.getBytes());
				message.setQos(MyConstants.QOS);

				client.publish(topicName, message);

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}// end of publish

		@Override
		public void connectionLost(Throwable arg0) {
		}

		@Override
		public void deliveryComplete(IMqttDeliveryToken arg0) {
		}

		@Override
		public void messageArrived(final String topic, final MqttMessage message) {

			try {
				ABgatewayBroadcast mABgateway	 = new ABgatewayBroadcast();

				Map<Value, Value> map = MessagePack.newDefaultUnpacker(message.getPayload()).unpackValue().asMapValue()
						.map();

				firmware_version = map.get(ValueFactory.newString("v")).asStringValue().asString();
				messageID = map.get(ValueFactory.newString("mid")).asIntegerValue().asInt();
				timeSinceBoot = map.get(ValueFactory.newString("time")).asIntegerValue().asInt();
				gatewayIP = map.get(ValueFactory.newString("ip")).asStringValue().asString();
				gatewayMac = map.get(ValueFactory.newString("mac")).asStringValue().asString();

				mABgateway.setFirmware_version(firmware_version);
				mABgateway.setMessageID(messageID);
				mABgateway.setTimeSinceBoot(timeSinceBoot);
				mABgateway.setGatewayIP(gatewayIP);
				mABgateway.setGatewayMac(gatewayMac);

				for (Map.Entry<Value, Value> entry : map.entrySet()) {
					// Devices come as a array of binaries

					if (entry.getKey().toString().equals("devices")) {

						// Passing the Array to a Value variable in order to manipulate it
						Value temp = entry.getValue();
						ArrayValue mArrayValue = temp.asArrayValue();

						for (int i = 0; i < mArrayValue.size(); i++) {

							byte[] loco = mArrayValue.get(i).asBinaryValue().asByteArray();
							StringBuilder sb = new StringBuilder(loco.length * 2);
							for (byte b : loco)
								sb.append(String.format("%02x", b));
							// System.out.println("Value of" + i + ": " + sb.toString());

							;

							mABgateway.getDevices().add(handleRawBleAdvertisment(sb.toString()));
						}

					}

				}

				Gson gson = new Gson();
				String gsonString = gson.toJson(mABgateway);
				System.out.println("Broadcast object to String: \n" + gsonString);

				System.out.println("-----------------");

			} catch (

			Exception ex) {
				System.out.println(ex.toString());

				ex.printStackTrace();
			}
		}
	}

	// byte 1 advertising type, see the table below
	// byte 2 - 7 mac address for BLE device
	// byte 8 RSSI, minus 256 for real value
	// byte 9 - Advertisement data
	/**
	 * TODO Need to clean up this code
	 * 
	 * @param input
	 * @return
	 */
	private static DeviceMessage handleRawBleAdvertisment(String input) {

		DeviceMessage mDeviceMessage = new DeviceMessage();
		String rawData;
		char[] mData = input.toCharArray();
		String advertisingType = Character.toString(mData[0]) + Character.toString(mData[1]);

		mDeviceMessage.setAdvertisingType(advertisingType);

		StringBuilder sb = new StringBuilder();

		// getting Mac address
		for (int k = 2; k < 14; k++) {
			sb.append(mData[k]);
		}
		String deviceMAC = sb.toString();
		mDeviceMessage.setDeviceMAC(deviceMAC);

		// emptying String Builder
		sb.setLength(0);

		// getting RSSI
		for (int k = 14; k < 16; k++) {
			sb.append(mData[k]);
		}

		int rssi = (Integer.parseInt(sb.toString(), 16) - 256);
		mDeviceMessage.setRssi("" + rssi);

		// emptying String Builder
		sb.setLength(0);

		// getting RAW data
		for (int k = 16; k < mData.length; k++) {
			sb.append(mData[k]);
		}
		rawData = sb.toString();
		mDeviceMessage.setRawData(rawData);
		sb.setLength(0);

		// Converting RAW data to an array that holds 1 byte per cell
		char[] iBeaconData = rawData.toCharArray();
		// getting minor value
		// We know that minor is occupying positions 54-57
		for (int k = 54; k < 58; k++) {
			sb.append(iBeaconData[k]);
		}

		int minor = (Integer.parseInt(sb.toString(), 16));
		mDeviceMessage.setMinor("" + minor);
		sb.setLength(0);

		return mDeviceMessage;
	}
}
