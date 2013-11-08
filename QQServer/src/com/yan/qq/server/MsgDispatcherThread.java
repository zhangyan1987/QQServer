package com.yan.qq.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MsgDispatcherThread implements Runnable {

	private Socket asocket;
	
	//<fromQQNumber,<toQQNumber,msg> >
	private static Map<String ,Map<String,String>> msgMap = new HashMap<String ,Map<String,String>>(); 
	
	//<toQQNumber,<fromQQNumber,msg> >
	private static Map<String ,Map<String,String>> msgMap2 = new HashMap<String ,Map<String,String>>();
	
	public MsgDispatcherThread(Socket asocket) {
		this.asocket =  asocket;
	}
	
	@Override
	public void run() {
		InputStream is;
		OutputStream os;
		try {
			is = asocket.getInputStream();
			os = asocket.getOutputStream();
			
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);
			
			String line = null;
			line = br.readLine();//receive useinfo
			String[] qqNumArr = line.split("::");
			
			String fromNum = qqNumArr[0];
			String toNum = qqNumArr[1];
			StringBuilder msg = new StringBuilder();
			while((line = br.readLine()) != null) {
				msg.append(line);
			}
			br.close();
			isr.close();
			is.close();
			
			//receive from client
			Map<String,String>  toMsgMap = msgMap.get(fromNum);
			if(toMsgMap == null) {
				toMsgMap = new HashMap<String,String>();
				msgMap.put(fromNum, toMsgMap);	
			}
			toMsgMap.put(toNum, msg.toString());
			
			Map<String,String>  fromMsgMap = msgMap2.get(toNum);
			if(fromMsgMap == null) {
				fromMsgMap = new HashMap<String,String>();
				msgMap2.put(toNum, fromMsgMap);
			}
			fromMsgMap.put(fromNum, msg.toString());
			
			
			//response to client
			Map<String,String>  sendMap = msgMap2.get(fromNum);
			StringBuilder sendMsg = new StringBuilder();
			Iterator<String> it = sendMap.keySet().iterator();
			while(it.hasNext()) {
				String from = it.next();
				sendMsg.append(from).append("::").append(fromNum).append("\n");
				sendMsg.append(sendMap.get(from));
			}
 			bw.write(sendMsg.toString());
			bw.close();
			osw.close();
			asocket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
