import broker.LocalBrokerService;
import service.core.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LBSServer {
    public static void main(String args[]) {
        BrokerService lbsService = new LocalBrokerService();
        try {
            String host = "register";
            if (args.length > 0) {
                host = args[0];
            }
            // Connect to the RMI Registry - creating the registry will be the
            // responsibility of the broker.
//            Registry registry = LocateRegistry.createRegistry(1099);
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            // Create the Remote Object
            BrokerService brokerService = (BrokerService) UnicastRemoteObject.exportObject(lbsService,0);
            // Register the object with the RMI Registry
            RegisterService register = (RegisterService) registry.lookup(Constants.REGISTER_SERVICE);
            register.registerService(Constants.BROKER_SERVICE, brokerService);
            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }
}
