import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * References: Stackoverflow
 * 				Java Docs
 * 
 * Creator: Mandar Deshpande	
 * 
 */

public class Server implements Runnable
{  
	public static int clientId=0;
	
		public static ArrayList num_clients = new ArrayList();
		public static ArrayList num_onlineclients = new ArrayList();
        public static void main(String[] args) throws Exception
        {
            new Thread(new Server()).start();
        }

        @Override
        public void run()
        {
            try 
            {
            	DatagramSocket serverSocket = new DatagramSocket(10007);
                byte[] receiveData = new byte[65508];
                byte[] sendData = new byte[65508];
                int sendPort=0;
                String clientName = "";
                
                System.out.println("Server Started ");
                String[] tempList = new String[(num_onlineclients.size())];
                String welmsg = "";
            	
                while(true)
                { 
                	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);                   
                	serverSocket.receive(receivePacket);
                	
                	String Msgdata = new String(receivePacket.getData(),0,receivePacket.getLength());
                	InetAddress IPAddress = receivePacket.getAddress();
                	int port = receivePacket.getPort(); 
                	clientName = Msgdata.split(":")[0];
                	String msg = Msgdata.split(":")[1];
                	
                	System.out.println(Msgdata);
                	
                	if(msg.equals("Connect"))
                	{
                		welmsg = clientName+" is now connected to server";
                		num_clients.add(port);
                		num_onlineclients.add(clientName);
	                    	
                		int portindex = num_clients.indexOf(port);
                		sendData = welmsg.getBytes();
                		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, (Integer)num_clients.get(portindex));                   
                  		serverSocket.send(sendPacket);
                	}
                	else if(msg.equals("bye"))
                	{
                		if(num_clients.size() > 1)
                		{
                			int portindex = num_clients.indexOf(port);
                			num_clients.remove(portindex);
                			sendData = (num_onlineclients.get(portindex)+" Disconnected from server").getBytes();
                    		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, (Integer)num_clients.get(0));                   
                			serverSocket.send(sendPacket);
                			
                			
                		}
                	}
                	else 
                	{
	                	int portindex = num_clients.indexOf(port);
	                	sendPort = ( portindex == 0)?(Integer)num_clients.get(1):(Integer)num_clients.get(0);
	                	sendData = Msgdata.getBytes(); 
		                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, sendPort);                   
		                serverSocket.send(sendPacket);
                	} 
                		
                }
            } 

              catch (IOException ex) 
              {
                      Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
              }
       }
}