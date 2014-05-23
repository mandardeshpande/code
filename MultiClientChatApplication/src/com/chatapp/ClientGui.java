package com.chatapp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ClientGui extends JFrame implements Runnable
{
	private JPanel contentPane;
	private JTextField txtMessage;
	private JButton btnSend;
	JTextArea textArea;
	
	private Client client; 
	private Thread run,listenThread;
	
	
	public ClientGui(String name,String host,int port)
	{
		client = new Client(name, host,port);
		BuildWindow(name);
		String connMsg = "/connect/"+name; 
		client.getConnected(host);
		client.sendData(connMsg.getBytes());
		String onlineUsers = "/Online/"; 
		client.sendData(onlineUsers.getBytes());
		run = new Thread(this,"Listen Thread Main");
		run.start();
	}

	public void BuildWindow(String name)
	{
		try 
		{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		
		setTitle(name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(850, 550);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{20,800,20,10};
		gbl_contentPane.rowHeights = new int[]{20,520, 0, 10};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE, 0.0};
		contentPane.setLayout(gbl_contentPane);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		JScrollPane scroll = new JScrollPane(textArea);
		GridBagConstraints textAreaConstraints = new GridBagConstraints();
		textAreaConstraints.insets = new Insets(0, 0, 5, 5);
		textAreaConstraints.fill = GridBagConstraints.BOTH;
		textAreaConstraints.gridx = 0;
		textAreaConstraints.gridy = 1;
		textAreaConstraints.gridwidth = 2;
		contentPane.add(scroll, textAreaConstraints);
		
		txtMessage = new JTextField();
		txtMessage.setText("@");
		GridBagConstraints gbc_txtMessage = new GridBagConstraints();
		gbc_txtMessage.insets = new Insets(0, 0, 5, 5);
		gbc_txtMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMessage.gridx = 1;
		gbc_txtMessage.gridy = 2;
		contentPane.add(txtMessage, gbc_txtMessage);
		txtMessage.setColumns(10);
		
		btnSend = new JButton("Send");
		txtMessage.addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent e) 
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					send(txtMessage.getText());
				}
			}
		});
		btnSend.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				send(txtMessage.getText());
				
			}
		});
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 5, 0);
		gbc_btnSend.gridx = 3;
		gbc_btnSend.gridy = 2;
		contentPane.add(btnSend, gbc_btnSend);
		
		txtMessage.requestFocusInWindow();
		
		setVisible(true);
	}
	
	public void updateChat(String msg)
	{
		textArea.append(msg+"\n\r");
	}
	
	public void send(String message)
	{
		if(message.equals("")) return;
		
		if(message.startsWith("@"))
		{
			String name = client.getName();
			message = "/chat/"+name+":"+message+"/end/";
		}
		client.sendData(message.getBytes());
		txtMessage.setText("@");
	}
	
	
	public void listen()
	{
		listenThread = new Thread("Listen Thread"){
	
			public void run()
			{
				while(true)
				{
					String rcvMsg = client.receive();
					
					if(rcvMsg.startsWith("/connectS/"))
					{	
						String successMsg = "Connected to Server successfully with ClientID: "+rcvMsg.split("/connectS/|/end/")[1];
						updateChat(successMsg);
					}
					else if (rcvMsg.startsWith("/chat/"))
					{
						System.out.println(rcvMsg);
						String sendMsg = rcvMsg.split("/chat/|/end/")[1];
						updateChat(sendMsg);
					}
					
				}
			}
		};
		
		listenThread.start();
	}

	public void run()
	{
		listen();
		
	}


	


}
