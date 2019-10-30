import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

import auldfellas.AFQService;
import service.core.QuotationService;
import service.core.Constants;
import service.core.RegisterService;

public class AFQServer {
    public static void main(String args[]) {
        QuotationService afqService = new AFQService();
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
            QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(afqService,1);
            // Register the object with the RMI Registry
            RegisterService register = (RegisterService) registry.lookup(Constants.REGISTER_SERVICE);
            register.registerService(Constants.AULD_FELLAS_SERVICE, quotationService);
            System.out.println("STOPPING SERVER SHUTDOWN");
            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }
}