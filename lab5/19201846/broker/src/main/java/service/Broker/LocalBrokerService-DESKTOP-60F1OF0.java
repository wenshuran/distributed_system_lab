package service.Broker;

import org.apache.activemq.ActiveMQConnectionFactory;
import service.core.ClientApplicationMessage;
import service.core.ClientInfo;
import service.core.QuotationRequestMessage;
import service.core.QuotationResponseMessage;

import javax.jms.*;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

public class LocalBrokerService {
    private static Session session;
    private static HashMap<Long, ClientApplicationMessage> hashMap = new HashMap<>();
    private static MessageConsumer clientConsumer;
    private static MessageConsumer serverConsumer;
    private static MessageProducer clientProducer;
    private static MessageProducer serverProducer;

    public static void main(String[] args) {
        try {
            String host = "localhost";
            ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://" + host + ":61616");
            Connection connection = factory.createConnection();
            connection.setClientID("broker");
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            Queue requestQueue = session.createQueue("REQUEST");
            clientConsumer = session.createConsumer(requestQueue);
            Queue responseQueue = session.createQueue("RESPONSE");
            clientProducer = session.createProducer(responseQueue);
            Queue serverQueue = session.createQueue("SERVER_RESPONSE");
            serverConsumer = session.createConsumer(serverQueue);
            Topic serverTopic = session.createTopic("APPLICATIONS");
            serverProducer = session.createProducer(serverTopic);

            connection.start();

            while (true){
                Message message = clientConsumer.receive();
                if (message instanceof ObjectMessage) {
                    Object content = ((ObjectMessage) message).getObject();
                    if (content instanceof QuotationRequestMessage) {
                        QuotationRequestMessage requestMessage = (QuotationRequestMessage) content;
                        RequestProducer requestProducer = new RequestProducer(requestMessage);
                        requestProducer.start();
                    }
                    message.acknowledge();
                } else {
                    System.out.println("Unknown message type: " + message.getClass().getCanonicalName());
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    static class RequestProducer extends Thread{
        private QuotationRequestMessage requestMessage;
        public RequestProducer(QuotationRequestMessage requestMessage){
            this.requestMessage = requestMessage;
        }
        public void run() {
            super.run();
            try {
                QuotationRequestMessage quotationRequest = new QuotationRequestMessage(requestMessage.id, requestMessage.info);
                Message request = session.createObjectMessage(quotationRequest);
                serverProducer.send(request);
                long startTime=System.currentTimeMillis();

                sleep(1);

                while (true){
                    Message message = serverConsumer.receive();
                    if (message instanceof ObjectMessage) {
                        Object content = ((ObjectMessage) message).getObject();
                        if (content instanceof QuotationResponseMessage) {
                            QuotationResponseMessage response  = (QuotationResponseMessage) content;
                            if (hashMap.containsKey(response.id)){
                                hashMap.get(response.id).addQuotation(response.quotation);
                            }
                            else {
                                hashMap.put(response.id, new ClientApplicationMessage(response.id, requestMessage.info, response.quotation));
                            }
                        }
                        message.acknowledge();
                        long endTime=System.currentTimeMillis();
                        if ((endTime - startTime) > 1000)
                            break;
                    } else {
                        System.out.println("Unknown message type: " + message.getClass().getCanonicalName());
                    }
                }

                ClientApplicationMessage clientApplicationMessage = hashMap.get(requestMessage.id);
                hashMap.remove(requestMessage.id);

                Message response = session.createObjectMessage(clientApplicationMessage);
                clientProducer.send(response);

            } catch (JMSException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
