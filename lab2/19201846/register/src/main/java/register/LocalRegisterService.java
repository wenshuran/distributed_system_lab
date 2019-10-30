package register;

import service.core.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LocalRegisterService implements RegisterService{
    private Registry registry;
    public LocalRegisterService() throws RemoteException {
        registry = LocateRegistry.createRegistry(1099);
    }

    public void registerService(String name, Remote service) {
        try {
            System.out.println("Registering: " + name);
            registry.bind(name, service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
