import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerChatwindow implements Runnable
{  
		public static int clientId=0;
	
		public static ArrayList num_clients = new ArrayList();
		public static ArrayList num_onlineclients = new ArrayList();
		FileOutputStream output = null;
		String outputLog = "";
		
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
                byte[] Newconn = new byte[65508];
                int sendPort=0;
                String clientName = "";
                
                System.out.println("Server Started ");
                
                
                String[] tempList = new String[(num_onlineclients.size())];
                String welmsg = "";
                String newConnection = "";
            	
                while(true)
                { 
                	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);                   
                	serverSocket.receive(receivePacket);
                	
                	String Msgdata = new String(receivePacket.getData(),0,receivePacket.getLength());
                	InetAddress IPAddress = receivePacket.getAddress();
                	System.out.println(Msgdata);
                	int port = receivePacket.getPort(); 
                	clientName = Msgdata.split(":")[0];
                	String msg = Msgdata.split(":")[1];
                	num_clients.add(port);
                	num_onlineclients.add(clientName);
                	
                	/*
                	for(int i=0;i<num_clients.size(); i++)
            		{
                		newConnection = num_onlineclients.get(i)+":Connect:port:"+port+"::";
            			System.out.println("This is integer "+(Integer)num_clients.get(i));
            			Newconn = newConnection.getBytes();
            			DatagramPacket sendPacket = new DatagramPacket(Newconn, Newconn.length, IPAddress, (Integer)num_clients.get(i));                   
            			serverSocket.send(sendPacket);
            		}
                	*/
                	
                	if(msg.equals("Connect"))
                	{
                		welmsg += clientName+":Connect:port:"+port+"::";
                		int portindex = num_clients.indexOf(port);
                		sendData = welmsg.getBytes();
                		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, (Integer)num_clients.get(portindex));                   
                		serverSocket.send(sendPacket);
                		
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