import java.util.*;
import javax.crypto.*;
import java.text.*;
import java.security.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;

public class Client {
  public static void main(String args[]) throws Exception {
    if (args.length != 3) {
      System.out.print("Usage: java Server <host> <port> <userid>");
      System.exit(0);
    }

    // Initialize command line arguments data
    final String host = args[0];
    final String userid = args[2];
    final int port = Integer.parseInt(args[1]);

    // Connect to server with provided data
    Socket s = new Socket(host, port);

    // Objects required for sending/receiving data to/from server
    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
    DataInputStream dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
    Scanner sc = new Scanner(System.in);

    // Send userID for session log
    dos.writeUTF(userid);

    // Get number of existing post from server
    int noOfPosts = dis.readInt();

    // Print exsting post(s) details
    System.out.print("There are " + noOfPosts + " post(s).\n\n");

    for (int i = 0; i < noOfPosts; i++) {
      String USER_ID, MSG, TMESTP;

      USER_ID = dis.readUTF();
      TMESTP = dis.readUTF();
      MSG = dis.readUTF();

      try {
        File f = new File(USER_ID + ".prv");
        if (!f.exists()) {
          // Generate RSA Key pair for the current user
          RSAKeyGen.main(new String[] {USER_ID});
        }

        // Retreive private key of the current user for decryption, and check if the post is 
        // intended for him/her
        FileInputStream filePrv = new FileInputStream(userid + ".prv");
        ObjectInputStream objPrv = new ObjectInputStream(new BufferedInputStream(filePrv));
    
        // Reading PrivateKey object from the <name>.prv
        PrivateKey privateKey = (PrivateKey) objPrv.readObject();
    
        // Try to decode the Base64 encoded string and decrypt the same.
        String decryptedMessage = decodeAndDecrypt(MSG, privateKey);
    
        // If successful, the encrypted message was intended for the current user
        // Print the details with decrypted message
        System.out.print("Sender  : " + USER_ID + "\n");
        System.out.print("Time    : " + TMESTP + "\n");
        System.out.print("Message : " + decryptedMessage + "\n\n");

        // Close file and object input stream
        objPrv.close();
        filePrv.close();
      }
      catch (Exception ex) {
        // If an exception was thrown, it was not encrypted at the first or the encrypted message
        // was not for the current user
        // Print the details with encrypted message
        System.out.print("Sender  : " + USER_ID + "\n");
        System.out.print("Time    : " + TMESTP + "\n");
        System.out.print("Message : " + MSG + "\n\n");
      }
    }

    String ch;
    System.out.print("\nDo you want to add a post? [Yes/No]\n");
    ch = sc.nextLine();
    dos.writeUTF(ch);

    if (ch.equals("y") || ch.equals("Y")) {
        String recipientUserID, message, pattern;

        // Read the recipient from keyboard
        System.out.print("Enter Recipient userid (type \"all\" for posting without encryption):\n");
        recipientUserID = sc.nextLine();
    
        // Read the message from keyboard
        System.out.print("Enter message:\n");
        message = sc.nextLine();
    
        // Instance of SimpleDateFormat used for formatting
        // the string representation of data
        // Used pattern accordingly to match the task specification
        pattern = "E MMM dd kk:mm:ss z yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        Date t = Calendar.getInstance().getTime();
        // Data and time string
        String dateStr = df.format(t);

        // If the recipient was not all, the message must be encrypted with the recipient's 
        // public key and should be Base64 encoded and then sent to the server

        if (!recipientUserID.equals("all")) {
          // Check if <recipient>.pub already exists
          File f = new File(recipientUserID + ".pub");
          if (!f.exists()) {
            // If it doesn't exist, create a new one
            RSAKeyGen.main(new String[]{recipientUserID});
          }
          // Retreive the recipient's public key to encrypt the message
          FileInputStream filePub = new FileInputStream(recipientUserID + ".pub");
          ObjectInputStream objPub = new ObjectInputStream(new BufferedInputStream(filePub));
    
          PublicKey publicKey = (PublicKey) objPub.readObject();
          
          String encryptedMessage = encryptAndEncode(message, publicKey);
    
          // change the original message to the encrypted one
          message = encryptedMessage;
    
          // Close the file and object input stream
          objPub.close();
          filePub.close();
        }

        // Check if private key exist for the current user
        File f = new File(userid + ".prv");
        if (!f.exists()) {
          // If it doesn't exist, create a new one
          RSAKeyGen.main(new String[]{userid});
        }

        // Generate signature for this post
        FileInputStream filePrv = new FileInputStream(userid + ".prv");
        ObjectInputStream objPrv = new ObjectInputStream(new BufferedInputStream(filePrv));
  
        // Get privatekey to sign the post
        PrivateKey privateKey = (PrivateKey) objPrv.readObject();

        // Concatenation of all the properties of this post
        String uniquePostID = (userid + dateStr + message);

        // Instantiating the signature
        Signature signature = Signature.getInstance("SHA256withRSA");

        // Initializing the signature object with the privateKey
        signature.initSign(privateKey);

        // Add the string to be signed
        signature.update(uniquePostID.getBytes(StandardCharsets.UTF_8));

        // Calculate a signature
        String sig = Base64.getEncoder().encodeToString(signature.sign());

        

        // If the message was not for all, then the message should already be encrypted,
        // else the original message is not modified
        // the userid, message (encrypted/original), timeStamp, uniquePostID and signature  is sent to the server

        dos.writeUTF(userid);
        dos.writeUTF(dateStr);
        dos.writeUTF(message);
        dos.writeUTF(uniquePostID);
        dos.writeUTF(sig);
    
        // Status for successful transmission
        System.out.print("\nSent encrypted message to the server.");

        // close object and file stream
        objPrv.close();
        filePrv.close();
    }
    
    // Print connection closed status
    System.out.print("\nConnection closed.");

    // Close all the opened connection objects.
    dis.close();
    dos.close();
    sc.close();
    s.close();
  }
  
  // Function to encryt a string with provided publicKey
  public static String encryptAndEncode(String plainText, PublicKey publicKey) throws Exception {
    Cipher encryptionCipher = Cipher.getInstance("RSA"); // We encrypt using RSA algorithm

    // Set Cipher flags to encryption mode and provide the publicKey
    encryptionCipher.init(Cipher.ENCRYPT_MODE, publicKey);

    // Convert plainText string to bytes and pass in doFinal() method to get encrypted bytes
    byte[] encryptionBytes = encryptionCipher.doFinal(plainText.getBytes());

    // Convert encrypted bytes to string using Base64 encoding
    String encryptedString = Base64.getEncoder().encodeToString(encryptionBytes);

    // return the encoded string
    return encryptedString;
  }

  // Function to decrypt a string with provided privateKey
  public static String decodeAndDecrypt(String encryptedText, PrivateKey privateKey) throws Exception {
    Cipher decryptionCipher = Cipher.getInstance("RSA"); // We decrypt using the same RSA algorithm
    // Set Cipher flags to decryption mode and provide the privateKey
    decryptionCipher.init(Cipher.DECRYPT_MODE, privateKey);

    // Decode the encoded string using Base64 decoder to get decoded bytes
    byte[] decryptionBytes = Base64.getDecoder().decode(encryptedText);

    // Decrypt the decoded bytes using doFinal()
    byte[] decryptedBytes = decryptionCipher.doFinal(decryptionBytes);

    // Conver the byte array to a string object using String() constructor
    // and return the decrypted string
    return new String(decryptedBytes);
  }
}
