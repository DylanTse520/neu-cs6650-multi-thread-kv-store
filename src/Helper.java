import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.text.SimpleDateFormat;

public class Helper {

    // Create a date time formatter
    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    // Log the input with timestamp
    public static void log(String log) {
        // Log with current time in milliseconds
        System.out.println("<Log at " + formatter.format(System.currentTimeMillis()) + "> " + log);
    }

    // Get input and server and work with the server accordingly
    public static void handleInput(String input, Server server) throws ServerNotActiveException, RemoteException {
        // Split the operation from the key and value
        String[] inputParts = input.split(" ");
        switch (inputParts[0]) {
            case "PUT":
                // PUT needs key and value
                if (inputParts.length >= 3) {
                    // Store the key value in the store
                    StoreOperationResult result = server.putValue(Integer.parseInt(inputParts[1]), Integer.parseInt(inputParts[2]));
                    Helper.log("Succeeded: " + result.value());
                } else {
                    // Not having key or value
                    Helper.log("Failed: Missing key or value from input");
                }
                break;
            case "GET":
                // GET needs key
                if (inputParts.length >= 2) {
                    // Get the value from the store
                    StoreOperationResult result = server.getValue(Integer.parseInt(inputParts[1]));
                    Helper.log((result.success() ? "Succeeded: " : "Failed: ") + result.value());
                } else {
                    // Not having key
                    Helper.log("Failed: Missing key from input");
                }
                break;
            case "DELETE":
                // DELETE needs key
                if (inputParts.length >= 2) {
                    StoreOperationResult result = server.deleteValue(Integer.parseInt(inputParts[1]));
                    Helper.log((result.success() ? "Succeeded: " : "Failed: ") + result.value());
                } else {
                    // Not having key
                    Helper.log("Failed: Missing key from input");
                }
                break;
            default:
                // When the operation name is wrong
                Helper.log("Failed: invalid operation name");
        }
    }

}
