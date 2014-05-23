import java.io.*;
import java.net.*;
import java.util.*;

public class Server
{
	public static void main(String args[])
	{
		new ServerThread().start();
		
	}

}
 
class ServerThread extends Thread 
{
    private DatagramSocket socket = null;
    BufferedReader qfs = null;
    private boolean moreQuotes = true;
    private int bufLength = 256;
 
    ServerThread() 
    {
        super("QuoteServer");
        
        try 
        {
            socket = new DatagramSocket(4445);
            System.out.println("QuoteServer listening on port: " + socket.getLocalPort());
        } 
        catch (java.io.IOException e) 
        {
            System.err.println("Could not create datagram socket.");
        }
        this.openInputFile();
    }
 
    public void run() 
    {
        if (socket == null)
            return;
 
        while (moreQuotes) 
        {
            try 
            {
                byte[] buf = new byte[bufLength];
                DatagramPacket packet;
                InetAddress address;
                int port;
                String dString = null;
 
                    // receive request
                packet = new DatagramPacket(buf, bufLength);
                socket.receive(packet);
                address = packet.getAddress();
                port = packet.getPort();
 
                    // send response
                if (qfs == null)
                    dString = new Date().toString();
                else
                    dString = getNextQuote();
                buf = dString.getBytes();
                packet = new DatagramPacket(buf, buf.length, address, port);                
                socket.send(packet);
            } 
            catch (IOException e) 
            {
                System.err.println("IOException:  " + e);
                moreQuotes = false;
                e.printStackTrace();
            }
        }
        socket.close();
    }
 
    private void openInputFile() {
        try {
            qfs = new BufferedReader(new FileReader("/home/mandar/workspace/UDPThreadedClientServer/src/one-liners.txt"));
        } catch (java.io.FileNotFoundException e) {
            System.err.println("Could not open quote file. Serving time instead.");
        }
    }
    private String getNextQuote() {
        String returnValue = null;
        try {
            if ((returnValue = qfs.readLine()) == null) {
                qfs.close();
                moreQuotes = false;
                returnValue = "No more quotes. Goodbye.";
 
            }           
        } catch (IOException e) {
            returnValue = "IOException occurred in server.";
        }
        return returnValue;
    }
 
}
