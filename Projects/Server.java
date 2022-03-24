import java.util.*;
import java.security.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;


// Class for post with userid, message and timeStamp attributes.
class Post {
    String userid, message, timeStamp;
    Post(String USER_ID, String MSG, String TMESTP) {
        userid = USER_ID;
        message = MSG;
        timeStamp = TMESTP;
    }
    String getUserID() {
      return userid;
    }
    String getTimeStamp() {
        return timeStamp;
    }
    String getMessage() {
        return message;
    }
}
 
public class Server {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.print("Usage: java server <port>");
            System.exit(0);
        }

        final int port = Integer.parseInt(args[0]);

        ServerSocket ss = new ServerSocket(port);

        // Collection of posts
        List<Post> Posts = new ArrayList<Post>();

        System.out.print("Servering listing for connection requests.\n");

        while (true) {
            // Wait for client connection request
            Socket s = ss.accept();

            // Objects required for two-way communication b/w Server-Client
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            DataInputStream dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

            // Session join status
            String userSessionID;
            userSessionID = dis.readUTF();

            System.out.print(userSessionID + " has joined the server.\n\n");

            // Send number of existing post to client
            int noOfPosts = Posts.size();
            dos.writeInt(noOfPosts);

            // Send all existing post details to client
            for (int i = 0; i < noOfPosts; i++) {
                Post p = Posts.get(i);
                
                dos.writeUTF(p.getUserID());
                dos.writeUTF(p.getTimeStamp());
                dos.writeUTF(p.getMessage());
            }

            // Check if client wants to add a post
            String ch;
            ch = dis.readUTF();

            if (ch.equals("y") || ch.equals("Y")) {
                String userID, message, timeStamp, verificationSig, uniquePostID;

                // Read post details from client
                userID = dis.readUTF();
                timeStamp = dis.readUTF();
                message = dis.readUTF();
                uniquePostID = dis.readUTF();
                verificationSig = dis.readUTF();

                // Check if public key for the user exists (Will always exists, but in case if deleted)
                File f = new File(userID + ".pub");
                if (!f.exists()) {
                  // If it doesn't exist, create a new one
                  RSAKeyGen.main(new String[]{userID});
                }
                
                // Stream to read the public key file to verify signature
                FileInputStream filePub = new FileInputStream(userID + ".pub");
                ObjectInputStream objPub = new ObjectInputStream(new BufferedInputStream(filePub));
          
                PublicKey publicKey = (PublicKey) objPub.readObject();
                
                // Create signature instance with SHA256withRSA Algorithm
                Signature signature = Signature.getInstance("SHA256withRSA");
                
                // Initialize the signature object with the publicKey to verify signature
                signature.initVerify(publicKey);

                // Add content for which the signature needs to verified
                signature.update(uniquePostID.getBytes(StandardCharsets.UTF_8));

                // Decode Base74 encoded signature to character byte array
                byte[] signatureBytes = Base64.getDecoder().decode(verificationSig);

                // Verify signature
                boolean isVerified = signature.verify(signatureBytes);

                // Signature verification status
                if (isVerified) {
                    System.out.print("Signature verification successful - " + verificationSig + "\n\n");
                    System.out.print("A post by " + userID + " on " + timeStamp + " is accepted.\n\n");

                    // Create a post object
                    Post P = new Post(userID, message, timeStamp);
                    // Add post to the list of posts
                    Posts.add(P);
                }
                else {
                    System.out.print("Signature verification failed - " + verificationSig + "\n\n");
                    System.out.print("A post by " + userID + " on " + timeStamp + " is discarded.\n\n");
                    // Do not add the post to the existing posts
                }

                // Close object and file stream
                objPub.close();
                filePub.close();
            }
            
            br.close();
            dis.close();
            dos.close();
            s.close();

            // User session end log
            System.out.print(userSessionID + " has left the server.\n\n");
        }
    }
}