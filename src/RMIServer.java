// RMIServer.java: A key-value store server program using RMI.

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.util.HashMap;

public class RMIServer {

    HashMap<Integer, Integer> keyValueStore;

    public RMIServer() {
        this.keyValueStore = new HashMap<>();
    }

    public static void main(String[] args) throws IOException {
        // Init the server
        RMIServer rmiServer = new RMIServer();
        // Prompt user to input connection information
        Helper.log("Input the port number set for the rmi registry. By default it is 1099.");
        // To enable user input from console using BufferReader
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // Reading connection info using readLine
        String input = reader.readLine();
        Server server;
        try {
            // New the Server class object
            server = new ServerImpl(rmiServer.getKeyValueStore(), input);
            // Bind the object in the registry
            Naming.rebind("rmi://localhost:" + input + "/RMIServer", server);
        } catch (IOException e) {
            // Prompt user with input error
            Helper.log("Cannot connect to the given port number, please check your input and try again.");
            throw new RuntimeException(e);
        }
        // When done, close the connection and exit
        reader.close();
    }

    public HashMap<Integer, Integer> getKeyValueStore() {
        return keyValueStore;
    }

}