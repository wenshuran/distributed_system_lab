package service.core;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;



@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public class Broker implements ServiceListener{
    private static TreeSet<String> paths = new TreeSet<String>();
    public static void main(String[] args) throws MalformedURLException {
        try {

            // Create a JmDNS instance
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
            // Add a service listener
            Broker broker = new Broker();
            jmdns.addServiceListener("_http._tcp.local.", broker);
            Endpoint.publish("http://0.0.0.0:9000/quote", broker);
            // Wait a bit
            Thread.sleep(30000);
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @WebMethod
    public LinkedList<Quotation> getQuotations(ClientInfo info) throws IOException {
        LinkedList<Quotation> quotations = new LinkedList<Quotation>();
        for (String url : paths){
            URL wsdlUrl = new URL(url);
            QName serviceName = new QName("http://core.service/", "QuoterService");
            Service service = Service.create(wsdlUrl, serviceName);
            QName portName = new QName("http://core.service/", "QuoterPort");
            QuoterService quoteService = service.getPort(portName, QuoterService.class);
            quotations.add(quoteService.generateQuotation(info));
        }
        return quotations;
    }

    @Override
    public void serviceAdded(ServiceEvent event) {
        //System.out.println("Service added: " + event.getInfo());
    }
    @Override
    public void serviceRemoved(ServiceEvent event) {
        System.out.println("Service removed: " + event.getInfo());
    }
    @Override
    public void serviceResolved(ServiceEvent event) {
        System.out.println("Service resolved: " + event.getInfo());
        String path = event.getInfo().getPropertyString("path");
        if (path != null) {
            try {
                paths.add(path);
            } catch (Exception e) {
                System.out.println("Problem with service: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
