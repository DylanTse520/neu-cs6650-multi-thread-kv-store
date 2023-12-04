// RMIClient.java: A key-value store client program using RMI.

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.Scanner;
import java.util.regex.PatternSyntaxException;

public class RMIClient {
    public static void main(String[] args) throws IOException {
        // Prompt user to input connection information
        Helper.log("Input the hostname or IP address and the port number of the server in the form of <hostname>" +
                " <port number>, for example: \"localhost 1099\".");
        // To enable user input from console using BufferReader
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // Reading connection info using readLine
        String input = reader.readLine();
        // Splitting the input line to get hostname or IP address and port number
        String[] parts;
        try {
            parts = input.split(" ");
        } catch (PatternSyntaxException e) {
            Helper.log("Cannot parse the input, please try again");
            throw new RuntimeException(e);
        }
        // The first part should be the hostname or IP address
        String host = parts[0];
        // The second part should be the port number
        String port = parts[1];

        Server server;
        try {
            // Getting the Sort class object
            server = (Server) Naming.lookup("rmi://" + host + ":" + port + "/RMIServer");
            Helper.log("Connected to server on " + host + ":" + port + ".");
        } catch (NotBoundException e) {
            Helper.log("Cannot connect to the given hostname or IP address and port number due to NotBoundException");
            throw new RuntimeException(e);
        }

        try {
            // Prepopulate server's key-value store
            Helper.log("Pre-populating server's key-value store.");
            // Read pre-population data
            File prePopulationFile = new File("resources/PRE_POPULATION");
            // Create the scanner
            Scanner sc = new Scanner(prePopulationFile);
            // Read each line
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                // Send data to server
                Helper.handleInput("PUT " + line, server);
            }
            sc.close();
            Helper.log("Finished pre-populating server's key-value store.");

            // Complete the minimum operation request
            Helper.log("Completing minimum operation request.");
            // Read minimum operation data
            File operationFile = new File("resources/MINIMUM_OPERATION");
            // Create the scanner
            sc = new Scanner(operationFile);
            // Read each line
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                // Send data to server
                Helper.handleInput(line, server);
            }
            sc.close();
            Helper.log("Finished minimum operations.");

            // Prompt user with input instructions
            Helper.log("Input your requests. Separate operation name (PUT, GET or DELETE), key (integers only) " + "and/or value (integers only) by space. For example: PUT 1 10; GET 1; DELETE 1.");
            // Process user input
            String line;
            while ((line = reader.readLine()) != null) {
                // Send data to server
                Helper.handleInput(line, server);
            }
        } catch (NumberFormatException e) {
            Helper.log("Failed: Illegal key or value, needed integer");
            throw new RuntimeException(e);
        } catch (ServerNotActiveException e) {
            Helper.log("Failed: Server is not active");
            throw new RuntimeException(e);
        } catch (MalformedURLException murle) {
            Helper.log("Failed: due to MalformedURLException");
            throw new RuntimeException(murle);
        } catch (RemoteException re) {
            Helper.log("Failed: due to RemoteException");
            throw new RuntimeException(re);
        } catch (ArithmeticException ae) {
            Helper.log("Failed: due to ArithmeticException");
            throw new RuntimeException(ae);
        } catch (IOException e) {
            Helper.log("Failed: due to IOException");
            throw new RuntimeException(e);
        } finally {
            // When done, close the connection and exit
            reader.close();
        }
    }
}