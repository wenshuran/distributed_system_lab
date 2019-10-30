import service.core.*;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;
import java.text.NumberFormat;
import java.util.LinkedList;

public class Client {
    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 9000;

        // More Advanced flag-based configuration
        for (int i=0; i < args.length; i++) {
            switch (args[i]) {
                case "-h":
                    host = args[++i];
                    break;
                case "-p":
                    port = Integer.parseInt(args[++i]);
                    break;
                default:
                    System.out.println("Unknown flag: " + args[i] +"\n");
                    System.out.println("Valid flags are:");
                    System.out.println("\t-h <host>\tSpecify the hostname of the target service");
                    System.out.println("\t-p <port>\tSpecify the port number of the target service");
                    System.exit(0);
            }
        }
        // More Advanced flag-based configuration
        // [ copy this from the ws-quote example client ]
        URL wsdlUrl = new URL("http://" + host + ":" + port + "/quote?wsdl");
        QName serviceName = new QName("http://core.service/", "BrokerService");
        Service service = Service.create(wsdlUrl, serviceName);
        QName portName = new QName("http://core.service/", "BrokerPort");
        QuoterService quotationService = service.getPort(portName, QuoterService.class);

        Thread.sleep(3000);
        for (ClientInfo info : clients) {
            displayProfile(info);
            LinkedList<Quotation> quotations = quotationService.getQuotations(info);
            for (Quotation quotation : quotations)
                displayQuotation(quotation);

            System.out.println("\n");
        }
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
