package client;

import java.text.NumberFormat;
import java.util.HashMap;

import org.apache.activemq.ActiveMQConnectionFactory;
import service.core.*;
import javax.jms.*;

//docker run --name=activemq -it --rm -p 8161:8161 -p 61616:61616 rmohr/activemq:latest
public class Main {
	private static int SEED_ID = 0;
	private static HashMap<Long, ClientInfo> cache = new HashMap<>();
	/**
	 * This is the starting point for the application. Here, we must
	 * get a reference to the Broker Service and then invoke the
	 * getQuotations() method on that service.
	 * 
	 * Finally, you should print out all quotations returned
	 * by the service.
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws JMSException {
		String host = "localhost";
		if (args.length > 0) {
			host = args[0];
		}

		ConnectionFactory factory =  new ActiveMQConnectionFactory("failover://tcp://"+host+":61616");
		Connection connection = factory.createConnection();
		connection.setClientID("client");
		Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

		Queue requestQueue = session.createQueue("REQUEST");
		Queue responseQueue = session.createQueue("RESPONSE");
		MessageProducer producer = session.createProducer(requestQueue);
		MessageConsumer consumer = session.createConsumer(responseQueue);

		connection.start();

		QuotationRequestMessage quotationRequest =  new QuotationRequestMessage(SEED_ID++, clients[0]);
		Message request = session.createObjectMessage(quotationRequest);
		cache.put(quotationRequest.id, quotationRequest.info);
		producer.send(request);

		Message message = consumer.receive();
		if (message instanceof ObjectMessage) {
			Object content = ((ObjectMessage) message).getObject();
			if (content instanceof ClientApplicationMessage) {
				ClientApplicationMessage response  = (ClientApplicationMessage) content;
				ClientInfo info = response.clientInfo;
				displayProfile(info);
				for (Quotation quotation : response.quotations)
					displayQuotation(quotation);
				System.out.println("\n");
			}
			message.acknowledge();
		} else {
			System.out.println("Unknown message type: " + message.getClass().getCanonicalName());
		}

		//connection.close();

//		// Create the broker and run the test data
//		for (ClientInfo info : clients) {
//			displayProfile(info);
//
//			// Retrieve quotations from the broker and display them...
//			for(Quotation quotation : brokerService.getQuotations(info)) {
//				displayQuotation(quotation);
//			}
//
//			// Print a couple of lines between each client
//			System.out.println("\n");
//		}
	}
	
	/**
	 * Display the client info nicely.
	 * 
	 * @param info
	 */
	public static void displayProfile(ClientInfo info) {
		System.out.println("|=================================================================================================================|");
		System.out.println("|                                     |                                     |                                     |");
		System.out.println(
				"| Name: " + String.format("%1$-29s", info.name) + 
				" | Gender: " + String.format("%1$-27s", (info.gender==ClientInfo.MALE?"Male":"Female")) +
				" | Age: " + String.format("%1$-30s", info.age)+" |");
		System.out.println(
				"| License Number: " + String.format("%1$-19s", info.licenseNumber) + 
				" | No Claims: " + String.format("%1$-24s", info.noClaims+" years") +
				" | Penalty Points: " + String.format("%1$-19s", info.points)+" |");
		System.out.println("|                                     |                                     |                                     |");
		System.out.println("|=================================================================================================================|");
	}

	/**
	 * Display a quotation nicely - note that the assumption is that the quotation will follow
	 * immediately after the profile (so the top of the quotation box is missing).
	 * 
	 * @param quotation
	 */
	public static void displayQuotation(Quotation quotation) {
		System.out.println(
				"| Company: " + String.format("%1$-26s", quotation.company) + 
				" | Reference: " + String.format("%1$-24s", quotation.reference) +
				" | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.price))+" |");
		System.out.println("|=================================================================================================================|");
	}
	
	/**
	 * Test Data
	 */
	public static final ClientInfo[] clients = {
		new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1"),
		new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4"),
		new ClientInfo("Hannah Montana", ClientInfo.FEMALE, 16, 10, 0, "HMA304/9"),
		new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 5, 3, "COL123/3"),
		new ClientInfo("Jim Quinn", ClientInfo.MALE, 55, 4, 7, "QUN987/4"),
		new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9")		
	};
}
