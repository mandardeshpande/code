package com.chatappserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;

public class Server implements Runnable
{
	private List<HandleClient> clients = new ArrayList<HandleClient>();
	private int port;
	private DatagramSocket socket;
	private Thread serverTh,handleTh,sendTh,receiveTh;
	int clientId=0;
	
	private boolean serverStarted = false;
	
	Server(int portNum)
	{
		this.port = portNum;
		try
		{
			socket = new DatagramSocket(port);
		}
		catch(Exception e)
		{	
			e.printStackTrace();
		}
		
		serverTh = new Thread(this,"Server");
		serverTh.start();
	}
	
	public void run()
	{
		serverStarted = true;
		System.out.println("Server is now running in port "+port);
		//handleClients();
		receive();
	}
	
	private void handleClients()
	{
		handleTh = new Thread("handle")
		{
			public void run()
			{
				while(serverStarted)
				{
					
				}
			}
		};
		
		handleTh.start();
	}
	
	private void receive()
	{
		receiveTh = new Thread("receive")
		{
			public void run()
			{
				while(serverStarted)
				{
					byte[] receiveByte = new byte[1024];
					DatagramPacket packet = new DatagramPacket(receiveByte,receiveByte.length);
					try
					{
						socket.receive(packet);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					processPacket(packet);
				}
			}
		};
		
		receiveTh.start();
		
	}
	
	private  void send(String msg, InetAddress address, int port)
	{
		msg+="/end/";
		send(msg.getBytes(),address,port);
	}
	
	private void processPacket(DatagramPacket packet)
	{
		String receive_str = new String(packet.getData());
		
		System.out.println("this is receive "+receive_str);
		if(receive_str.startsWith("/connect/"))
		{
			clientId++;
			String strClient = "/connectS/"+Integer.toString(clientId);
			send(strClient,packet.getAddress(),packet.getPort());
			clients.add(new HandleClient(receive_str,packet.getAddress(),packet.getPort(),clientId));
		}
		else if(receive_str.startsWith("/chat/"))
		{
			System.out.println("got it "+receive_str);
			String santizeStr = receive_str.split("/chat/|/end/")[1];
			tellEveryClient(santizeStr);
		}
		
		
	}
	
	private void send(final byte[] data, final InetAddress address, final int port) 
	{
		sendTh = new Thread("Send") 
		{
			public void run() 
			{
				DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
				try 
				{
					socket.send(packet);
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		};
		sendTh.start();
	}

	
	
	private void tellEveryClient(String message)
	{
		String sendingClient = message.split(":")[0];
		String tempMsg = message.split(":")[1];
		String clientName = tempMsg.split("::")[0].substring(1);
		
		String sendingmsg = message.split("::")[1];
		for (int i = 0; i < clients.size(); i++) 
		{
			HandleClient client = clients.get(i);
			if(client.name.contains(clientName))
			{
				message = "/chat/"+sendingClient+" said >>"+sendingmsg;
				System.out.println(message);
				send(message, client.address, client.port);
			}
			
		}
	}

}
