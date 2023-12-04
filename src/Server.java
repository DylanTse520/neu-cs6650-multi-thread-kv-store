// Server.java: the server interface

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public interface Server extends Remote {

    // Define the put value method
    StoreOperationResult putValue(Integer key, Integer value) throws RemoteException, ServerNotActiveException;

    // Define the get value method
    StoreOperationResult getValue(Integer key) throws RemoteException, ServerNotActiveException;

    // Define the delete value method
    StoreOperationResult deleteValue(Integer key) throws RemoteException, ServerNotActiveException;

}
