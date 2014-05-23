import java.io.*;
import java.net.*;
/*
 * References: Stackoverflow
 * 				Java Docs
 * 
 * Creator: Mandar Deshpande	
 * 
 */


public class Client 
{
	volatile static boolean stop = false;
		public static void main(String[] args) throws Exception
        {
        	    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));       
                try
                {
                		DatagramSocket clientSocket = new DatagramSocket();
                        InetAddress IPAddress = InetAddress.getByName("localhost");       
                        byte[] sendData = new byte[65508];       
                        byte[] receiveData = new byte[65508];
                        String welcomeclient = "";
                        System.out.println("Enter a username: ");
                        String clientUsername = inFromUser.readLine();
                        
                        
                        welcomeclient = clientUsername+":Connect";
                        DatagramPacket sendPacket = new DatagramPacket(welcomeclient.getBytes(), welcomeclient.getBytes().length, IPAddress, 10007);       
                        clientSocket.send(sendPacket);     
                        
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);       
                        clientSocket.receive(receivePacket);       
                        
                        String clientId = new String(receivePacket.getData(),0,receivePacket.getLength()); 
                        System.out.println(clientId);
                        
                        System.out.println("Send message...");
                        
                        while(true)
                        {   
                        	Receivemessage sdc = new Receivemessage(clientSocket,receiveData);
                        	Thread t = new Thread(sdc);
                        	t.start();
                        	
                        	String clientSentence = clientUsername+":"+inFromUser.readLine();
                        	
                        	if(clientSentence.split(":")[1].equals("bye"))
                        	{
                        		System.out.println("Disconnected");
                        		break;
                        	}

                        	sendData = clientSentence.getBytes();
                            DatagramPacket dataPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 10007);       
	                        clientSocket.send(dataPacket);
	                    }
                }

                catch(Exception e)
                {
                        e.getMessage();
                }
        }
        
        public static class Receivemessage implements Runnable
        {
        	DatagramSocket clientSocket = null;
        	byte[] received = new byte[65508];
        	DatagramPacket receivethreadpacket = null;
        	public static String quit_send = "";
        	
        	
        	Receivemessage(DatagramSocket clientSocket, byte[] receiveData)
        	{
        		this.clientSocket = clientSocket;
        		this.received = receiveData;
        	}
			
			public void run()
			{
				try 
                {	
					DatagramPacket receivethreadpacket = new DatagramPacket(received, received.length);       
					clientSocket.receive(receivethreadpacket);
					String Sentence = new String(receivethreadpacket.getData(),0,receivethreadpacket.getLength());
					System.out.println(Sentence);
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				
			}
	    }
	}

