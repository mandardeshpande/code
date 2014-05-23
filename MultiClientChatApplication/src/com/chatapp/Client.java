package com.chatapp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client
{
	private String username = "";
	private String host;
	private int port;
	private DatagramSocket clientSocket;
	private InetAddress ip;
	private Thread sendthread;
	
	int clientId = 0;
	
	Client(String name, String host, int port)
	{
		this.username = name;
		this.host = host;
		this.port = port;
	
	}
	
	public String getName()
	{
		return username;
	}
	
	public String getAddress()
	{
		return host;
	}
	
	public int getPort()
	{
		return port;
	}
	
	
	public boolean getConnected(String host)
	{
		try
		{
			clientSocket = new DatagramSocket();
			ip = InetAddress.getByName(host);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}
	
	
	
	public String receive()
	{
		byte receivedata[] = new byte[1024];
		DatagramPacket packet= new DatagramPacket(receivedata, receivedata.length);
		try
		{
			clientSocket.receive(packet);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		String str = new String(packet.getData());
		
		return str;
		
	}
	
	public void sendData(final byte[] senddata)
	{
		sendthread = new Thread("Send Thread")
		{
			public void run()
			{
				DatagramPacket send_packet = new DatagramPacket(senddata,senddata.length,ip,port);
				try 
				{
					clientSocket.send(send_packet);
				} 
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		sendthread.start();
	
	}
	
		
	
	
	
}
