// ServerImpl.java: the class implementing the server interface

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

// Extending UnicastRemoteObject to allow RMI registry
public class ServerImpl extends UnicastRemoteObject implements Server {

    // The mutual exclusion lock to allow synchronization
    final Object mutex;
    HashMap<Integer, Integer> keyValueStore;
    String port;

    // Write explicit constructor to declare the RemoteException exception
    public ServerImpl(HashMap<Integer, Integer> keyValueStore, String port) throws RemoteException {
        super();
        this.keyValueStore = keyValueStore;
        this.port = port;
        this.mutex = new Object();
    }

    @Override
    // Put the value into the key value store
    public StoreOperationResult putValue(Integer key, Integer value) throws RemoteException, ServerNotActiveException {
        // To put values in the store, need to get the mutex lock first
        synchronized (mutex) {
            Helper.log("Received request from " + getClientHost() + ":" + this.port + " for PUT operation with key = " + key + " and value = " + value + ".");
            // Store the key value in the store
            keyValueStore.put(key, value);
            // Creating the result object
            StoreOperationResult result = new StoreOperationResult(true, "Put " + key + ":" + value);
            Helper.log(result.toString());
            return result;
        }
    }

    @Override
    // Get the value from the key value store
    public StoreOperationResult getValue(Integer key) throws RemoteException, ServerNotActiveException {
        // To get values from the store, need to get the mutex lock first
        synchronized (mutex) {
            Helper.log("Received request from " + getClientHost() + ":" + this.port + " for GET operation with key = " + key + ".");
            // Get the value from the store
            Integer value = keyValueStore.get(key);
            // Creating the result object
            StoreOperationResult result;
            if (value == null) {
                // If the key is not in the store
                result = new StoreOperationResult(false, "Key does not exist in map");
            } else {
                // Otherwise return the value
                result = new StoreOperationResult(true, value.toString());
            }
            Helper.log(result.toString());
            return result;
        }
    }

    @Override
    // Delete the value from the key value store
    public StoreOperationResult deleteValue(Integer key) throws RemoteException, ServerNotActiveException {
        // To get values from the store, need to get the mutex lock first
        synchronized (mutex) {
            Helper.log("Received request from " + getClientHost() + ":" + this.port + " for DELETE operation with key = " + key + ".");
            // Creating the result object
            StoreOperationResult result;
            if (keyValueStore.containsKey(key)) {
                // If the key is in the store, remove the key
                keyValueStore.remove(key);
                result = new StoreOperationResult(true, "Deleted " + key);
            } else {
                // Otherwise log error
                result = new StoreOperationResult(false, "Key does not exist in map");
            }
            Helper.log(result.toString());
            return result;
        }
    }

}
