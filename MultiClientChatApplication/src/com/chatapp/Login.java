package com.chatapp;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame
{
	private JPanel contentPane;
	private JTextField txtName;
	private JLabel lblErrlabel;
	
	public String host = "localhost";
	public int port = 8192;
	
	
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					Login frame = new Login();
					frame.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() 
	{
		try 
		{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		setResizable(false);
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(344, 183);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtName = new JTextField();
		txtName.setBounds(111, 29, 145, 19);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(23, 31, 70, 15);
		contentPane.add(lblName);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				String name = txtName.getText();
				if(name.equals(""))
				{
					lblErrlabel.setText("Name is required");
				}
				else
				{
					dispose();
					new ClientGui(name,host,port);
				}
			}
		});
		btnConnect.setBounds(111, 87, 117, 25);
		contentPane.add(btnConnect);
		
		lblErrlabel = new JLabel("");
		lblErrlabel.setBounds(111, 143, 155, 15);
		contentPane.add(lblErrlabel);
	}
}
