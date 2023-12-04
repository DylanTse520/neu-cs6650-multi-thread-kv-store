# Project 2: Multi-Threaded RPC Key-Value Store

## Assignment overview

The purpose of the assignment is to extend what was built in project 1,
which is a server program that can receive requests from a single client
to perform three basic operations: PUT, GET, and DELETE in a key-value store.
In this assignment, we need to extend project 1 with two requirements.
First, instead of being single-threaded, the server now should be multithreaded
and can respond to multiple client requests at the same time.
By enabling multithreading, we need to handle mutual exclusion on the operation
to the key-value. Second, instead of using basic socket programming between
the client and server, we now use RPC communication.

## Technical impression

After evaluated the project requirement, I identified two major differences
between project 1 and project 2, which are using RPC communication and enabling
multithreading and mutual exclusion. Because I have been using Java as the
framework, I decided to use Java RMI for RPC communication because this is the
mainstream solution. In order to migrate the project from socket programming,
which is more serialized programming, to RMI, which requires classes and interfaces,
I need to first adapt my project to a more object-oriented fashion.
This is done by creating Server interface and implementing ServerImpl class.
Then I can use RMI to register the class and interface in my RMIServer class.
In order to allow passing store operation result back to the client, I created
a Record and soon realized that it need to implement Serializable to enable
unmarshalling. After updating project 1 using RMI, the server is automatically
enabled with multithreading and can take multiple requests at the same time.
Now I just need to figure out how to enable mutual exclusion on the operation
of the store. After some research on the Internet, I think Synchronization in
Java is an easy way to achieve this. With the 'synchronized' keyword,
the operation is protected by a lock. Every time clients request an operation,
server need to obtain the lock first and then operated accordingly. After my
experiment with multiple clients making requests, I observed consistency in
my program.

## Build and run the code

### Build the server and client

Under ```/src``` directory, open a terminal and compile 6 java programs with the following commands:

```shell
javac Helper.java
javac StoreOperationResult.java
javac Server.java
javac ServerImpl.java
javac RMIServer.java
javac RMIClient.java
```

Create the jar file to execute with the following commands:

```shell
jar cmf RMIServer.mf RMIServer.jar RMIServer.class Helper.class StoreOperationResult.class Server.class ServerImpl.class
jar cmf RMIClient.mf RMIClient.jar RMIClient.class Helper.class StoreOperationResult.class Server.class PRE_POPULATION MINIMUM_OPERATION
```

### Run the server and client

Under ```/src``` directory, open a terminal to start the rmi registry
with an optional port number with the following command.
If the port number is missing, it will be defaulted to 1099.

```shell
rmiregistry 1099
```

Keep the current terminal, open one more terminal and navigate to the same directory.
Start the RMI server and enter the rmi registry port number with the following commands:

```shell
java -jar RMIServer.jar
1099
```

Open one more terminal and navigate to the same directory.
Start the RMI client and enter the hostname and port number with the following commands:

```shell
java -jar RMIClient.jar
localhost 1099
```

Now the client should be able to connect to the server.
To put new records in the store, try "PUT 1 10".
Note that both the key and the value should be integers.
To get a value by a key, try "GET 1".
To delete a key, try "DELETE 1".

### MISC

The PRE_POPULATION file is for storing the pre-population records.
The MINIMUM_OPERATION file is for storing the operations to be completed by the clients.
