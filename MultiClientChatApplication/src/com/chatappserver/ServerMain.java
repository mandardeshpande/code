package com.chatappserver;

public class ServerMain
{
	private int port;
	private Server server;
	
	ServerMain(int port)
	{
		this.port = port;
		server = new Server(port);
	}
	
	public static void main(String args[])
	{
		int port_args = 0;
		if(args.length > 1)
		{
			System.err.println("More parameters entered");
			return;
		}
		port_args = Integer.parseInt(args[0]);
		new ServerMain(port_args);
		
	}
}
