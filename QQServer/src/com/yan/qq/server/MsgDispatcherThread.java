package com.yan.qq.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.yan.qq.common.Message;

public class MsgDispatcherThread  extends Thread{

	public  Socket asocket;
	
	
	public MsgDispatcherThread(Socket asocket) {
		this.asocket =  asocket;
	}
	
	@Override
	public void run() {
		while(true) {
			
			try {
				ObjectInputStream ois = new ObjectInputStream(asocket.getInputStream());
				
				Message msg = (Message)ois.readObject();
				System.out.println(msg.getSender()+" ธ๘ "+msg.getRecevier()+" หต:"+msg.getMsg());
				ObjectOutputStream oos = new ObjectOutputStream(QQServer.threadMap.get(msg.getRecevier()).asocket.getOutputStream());
				oos.writeObject(msg);
				
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
