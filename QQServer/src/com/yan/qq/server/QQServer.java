package com.yan.qq.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class QQServer {

	
	public static void main(String[] args) {

		int port = 8888;
		ServerSocket ssocket = null;
		try {
			System.out.println("QQ Server starting..........");
			ssocket = new ServerSocket(port);
			Socket asocket = null;
			while ((asocket = ssocket.accept()) !=null) {
				System.out.println("client connected");
				(new MsgDispatcherThread(asocket)).run();
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
