import java.io.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

//import Client.SendReceiveClient;
/*
 * Stopped at making datagram connection incomingreader class. Take on from there.
 * 
 * */
 



import java.util.*;
import java.net.*;

public class ClientWindow extends JFrame
{
	String username, serverIP = "127.0.0.1";
    int Port = 5000;
    
    public static DatagramSocket clientSocket = null;
    BufferedReader reader;
    public static byte receivedData[] = new byte[65508];
    public static byte sendData[] = new byte[65508];
    
    ArrayList<String> userList = new ArrayList<String>();
    public static int num=0;
    Boolean isConnected = false;

	
    
    /**
	 * Create the application.
	 */
	public ClientWindow()
	{
		setSize(new Dimension(800, 500));
		setAlwaysOnTop(true);
		initialize();
	}
	
	/**
	 * Launch the application.
	 */
	public class IncomingReader implements Runnable
	{
		public  String connected_msg="";
		
		IncomingReader(String welcome_msg)
		{
			this.connected_msg = welcome_msg;
		}
        public void run() 
        {
            String[] data;
            String stream, done = "Done", connect = "Connect", disconnect = "Disconnect", chat = "Chat";
            String clientname = "";
            String message = "";
            int port = 0;

            try 
            {
            	System.out.println(connected_msg);
            	data = connected_msg.split("::");
            	
            	for(String out: data)
            	{
            		clientname = out.split(":")[0];
            		message = out.split(":")[1];
            		port = Integer.parseInt(out.split(":")[3]);
	                if (message.equals(connect)) 
	                {
	                        chatTextArea.append(clientname+ " is now Online \n");
	                        chatTextArea.setCaretPosition(chatTextArea.getDocument().getLength());
	                        chatTextArea.removeAll();
	                        usersList.setText("");
	                        userAdd(clientname);
	                        writeUsers();
	                        
	                 }
            	}
            	
            	
            }
            catch(Exception ex)
            {
            	ex.printStackTrace();
            }
        }
    }

    public void ListenThread(String welcome, byte[] receivedData1) {
         Thread IncomingReader = new Thread(new IncomingReader(welcome));
         IncomingReader.start();
     	SendReceiveClient sdc = new SendReceiveClient(clientSocket,receivedData1);
		Thread t = new Thread(sdc);
		t.start();
	
    }

    public void userAdd(String data)
    {
         userList.add(data);
     }

    public void userRemove(String data) {
         chatTextArea.append(data + " has disconnected.\n");

     }

    public void writeUsers() 
    {
         String[] tempList = new String[(userList.size())];
         userList.toArray(tempList);
         for (String token:tempList)
         {
             usersList.append(token + "\n");
         }

     }

    public void Disconnect() {
    	
        try
        {
          String bye = (username + ": :Disconnect");
          chatTextArea.append("Disconnected.\n");
          clientSocket.close();
        }
        catch(Exception ex)
        {
        	chatTextArea.append("Failed to disconnect. \n");
        }
        isConnected = false;
        usernameField.setEditable(true);
        usersList.setText("");

      }

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		jScrollPane1 = new javax.swing.JScrollPane();
        inputTextArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        chatTextArea = new javax.swing.JTextArea();
        chatTextArea.setSize(new Dimension(400, 40));
        jLabel1 = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        connectButton = new javax.swing.JButton();
        connectButton.setText("Join");
        disconnectButton = new javax.swing.JButton();
        disconnectButton.setText("Leave");
        sendButton = new javax.swing.JButton("Send");
        jScrollPane3 = new javax.swing.JScrollPane();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat Client");

        inputTextArea.setColumns(20);
        inputTextArea.setLineWrap(true);
        inputTextArea.setRows(5);
        jScrollPane1.setViewportView(inputTextArea);

        chatTextArea.setColumns(20);
        chatTextArea.setEditable(false);
        chatTextArea.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        chatTextArea.setLineWrap(true);
        chatTextArea.setRows(5);
        jScrollPane2.setViewportView(chatTextArea);

        jLabel1.setText("Username:");
              
        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        disconnectButton.setText("Disconnect");
        disconnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectButtonActionPerformed(evt);
            }
        });

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });
	

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Online Users");
        usersList = new javax.swing.JTextArea();
        usersList.setEditable(false);
        usersList.setColumns(20);
        usersList.setRows(5);
        jScrollPane3.setViewportView(usersList);
        
        
        /*
        usersList.addMouseListener(new MouseAdapter() 
        {
            public void mouseClicked(MouseEvent evt) 
            {
            	System.out.println(evt.getClickCount());
                if (evt.getClickCount() == 1) 
                {
                    JFrame wind = new JFrame();
                    wind.setBounds(100, 300, 500, 500);
                    wind.setVisible(true);
                } 
            }
        });
		*/
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addGap(4)
        					.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 336, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(sendButton, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE))
        				.addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 419, GroupLayout.PREFERRED_SIZE)
        				.addGroup(layout.createSequentialGroup()
        					.addGap(47)
        					.addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(usernameField, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(connectButton)
        					.addGap(18)
        					.addComponent(disconnectButton)))
        			.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
        				.addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addGroup(layout.createSequentialGroup()
        					.addGap(12)
        					.addComponent(usersList, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap())
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(disconnectButton)
        				.addComponent(connectButton)
        				.addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        				.addComponent(jLabel2))
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 261, GroupLayout.PREFERRED_SIZE)
        					.addGap(108)
        					.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        						.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addGroup(layout.createSequentialGroup()
        							.addComponent(sendButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
        							.addGap(22))))
        				.addGroup(layout.createSequentialGroup()
        					.addGap(4)
        					.addComponent(usersList, GroupLayout.PREFERRED_SIZE, 253, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap())
        );
        pack();
	}
	
	private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) 
	{
        	String welcomeclient = "";
        	String clientId = "";
        	//byte[] sendData = new byte[65508];       
            //byte[] receiveData = new byte[65508];
            
        
            if (isConnected == false)
            {
            	username = usernameField.getText();
            	usernameField.setEditable(false);
            try 
            {
                String clientUsername = username;
            	clientSocket = new DatagramSocket();
                InetAddress IPAddress = InetAddress.getByName("localhost");       
             
                
                welcomeclient = clientUsername+":Connect";
                DatagramPacket sendPacket = new DatagramPacket(welcomeclient.getBytes(), welcomeclient.getBytes().length, IPAddress, 10007);       
                clientSocket.send(sendPacket);     
                
                DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);       
                clientSocket.receive(receivePacket);       
                
                clientId = new String(receivePacket.getData(),0,receivePacket.getLength()); 
                 
	            isConnected = true;
	            
            } 
            catch (Exception ex) 
            {
                chatTextArea.append("Cannot Connect! Try Again. \n");
                usernameField.setEditable(true);
            }
            ListenThread(clientId,receivedData);
        } else if (isConnected == true) {
            chatTextArea.append("You are already connected. \n");
        }
    }                                             

    private void disconnectButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        // TODO add your handling code here:
        Disconnect();
    }                                                

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) 
    {                                           
        // TODO add your handling code here:
        String nothing = "";
        if ((inputTextArea.getText()).equals(nothing)) 
        {
            inputTextArea.setText("");
            inputTextArea.requestFocus();
        } 
        else 
        {
        	/*
        	try
        	{
            	System.out.print("Me: ");
                String clientSentence = clientUsername+":"+inFromUser.readLine();
                sendData = clientSentence.getBytes();
                    
                DatagramPacket dataPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 10007);       
                clientSocket.send(dataPacket);         
            } 
        	catch (Exception ex) 
        	{
                chatTextArea.append("Message was not sent. \n");
            }
        	*/
            inputTextArea.setText("");
            inputTextArea.requestFocus();
        }

        inputTextArea.setText("");
        inputTextArea.requestFocus();
    }                              
	
    public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					new ClientWindow().setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
    
    public static class SendReceiveClient implements Runnable
    {
    	DatagramSocket clientSocket = null;
    	byte[] received = new byte[65508];
    	DatagramPacket receivethreadpacket = null;
    	
    	SendReceiveClient(DatagramSocket clientSocket, byte[] receiveData)
    	{
    		this.clientSocket = clientSocket;
    		this.received = receiveData;
    	}
		
		public void run()
		{
			try 
            {
				receivethreadpacket = new DatagramPacket(received, received.length);   
				clientSocket.receive(receivethreadpacket);
				System.out.println("my port number is "+receivethreadpacket.getPort());
				String Sentence = new String(receivethreadpacket.getData(),0,receivethreadpacket.getLength()); 
	            System.out.println(Sentence);
				 
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
		}
    	
    	
    }
    
    private JTextArea chatTextArea;
	private JButton connectButton;
	private JButton disconnectButton;
	private JTextArea inputTextArea;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JMenu jMenu1;
	private JMenuBar jMenuBar1;
	private JMenuItem jMenuItem1;
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane2;
	private JScrollPane jScrollPane3;
	private JButton sendButton;
	private JTextField usernameField;
	private JTextArea usersList;
	// End of variables declaration 
   
}



                  

