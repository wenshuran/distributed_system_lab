package broker;

import service.core.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the broker service that uses the Service Registry.
 * 
 * @author Rem
 *
 */
public class LocalBrokerService implements BrokerService {
	public List<Quotation> getQuotations(ClientInfo info) {
		List<Quotation> quotations = new LinkedList<Quotation>();
		try {
			String host = "register";
			Registry registry = LocateRegistry.getRegistry(host, 1099);
			for (String name : registry.list()){
				if (name.startsWith("qs-")){
					QuotationService service= (QuotationService) registry.lookup(name);
					quotations.add(service.generateQuotation(info));
				}
			}
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
		return quotations;
	}
}

