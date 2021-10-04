package uk.org.digicatapult.iot.loumis;


/**
 * Class that contains publicly accessible variables
 * 
 * @author Nikos Loumis
 *
 */
public class MyConstants {

	////////////////////////////////////
	////// MQTT RELATED VARIABLES /////
	///////////////////////////////////

	public static final int BROKER_PORT_NUMBER = 1883;
	public static final boolean cleanSession = true; // Non durable
														// subscriptions
	public static final boolean ssl = false;
	public static final String password = null;
	public static final String userName = null;

	public static final int QOS = 2; // check if QoS=2 is needed
	public static final boolean quietMode = false;
	public static final String PROTOCOL = "tcp://";
	public static final String CLIENT_ID = "Eclipse Server";

	public static final String BROKER_USERNAME = "lightricity";
	public static final char[] BROKER_PASSWORD = "bolekLolek".toCharArray();

	public static final String BROKER_HOST_NAME = "tcp://iot.beaconyun.com";

	/**
	 * Topic to subscribe
	 */
	public static final String MQTT_TOPIC_SUBSCRIBE = "/bolek/#";

}
