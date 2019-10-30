package service.core;

import java.util.List;

/**
 * Interface for defining the behaviours of the broker service
 * @author Rem
 *
 */
public interface RegisterService extends java.rmi.Remote {
    public void registerService(String name, java.rmi.Remote remote)  throws java.rmi.RemoteException;
}
