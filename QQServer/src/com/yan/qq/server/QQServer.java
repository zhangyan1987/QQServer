package com.yan.qq.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import com.yan.qq.common.Message;
import com.yan.qq.common.User;

public class QQServer {
	
	static int port = 8888;
	private static ServerSocket ssocket = null;
	
	public static HashMap<String,MsgDispatcherThread> threadMap = new HashMap<String,MsgDispatcherThread>();
	public static void main(String[] args) {

		
		System.out.println("QQ Server starting..........");
		try {
			ssocket = new ServerSocket(port);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (true) {
			try {
				Socket socket = ssocket.accept();
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				User u = (User)ois.readObject(); 
				if(u.getPasssword().equals("123")) {
					Message msg = new Message();
					msg.setMessageType(1);
					oos.writeObject(msg);
					MsgDispatcherThread msgDispatcherThread = new MsgDispatcherThread(socket);
					
					threadMap.put(u.getQQnumber(), msgDispatcherThread);
					msgDispatcherThread.start();
				}
					
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
}
