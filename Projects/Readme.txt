Checked on Windows 10 (Java 11 - openjdk 17.0.2)

1. Place RSAKeygen.java in the same directory of Server.java and Client.java
2. Compile both Server.java and Client.java using "Javac" command
3. After successful class creation, use "java Server <port>" and "java Client <host> <port> <userid>
Example:
java Server 8080 // Listens for incoming requests on port 8080

 // Connect to localhost on port 8080
java Client 127.0.0.1 8080 Methran
(or)
java Client localhost 8080 Methran

java Client (external address, if connected) 8080 Methran
4. The server only handles only one user at any given point of time as mentioned in the specification, So, multiple clients cannot be handled (No multithreading)