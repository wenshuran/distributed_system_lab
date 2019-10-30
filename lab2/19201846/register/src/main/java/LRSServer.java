
import register.LocalRegisterService;
import service.core.Constants;
import service.core.QuotationService;
import service.core.RegisterService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LRSServer {
    public static void main(String args[]) {
        try {
            String host = "localhost";
            if (args.length > 0) {
                host = args[0];
            }
            // Connect to the RMI Registry - creating the registry will be the
            // responsibility of the broker.
//            Registry registry = LocateRegistry.createRegistry(1099);
//            Registry registry = LocateRegistry.getRegistry(host, 1099);
            RegisterService lrsService = new LocalRegisterService();
            // Create the Remote Object
            RegisterService registerService = (RegisterService) UnicastRemoteObject.exportObject(lrsService,4);
            // Register the object with the RMI Registry
            lrsService.registerService(Constants.REGISTER_SERVICE, registerService);
            System.out.println("STOPPING SERVER SHUTDOWN");
            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }
}
