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

public class MsgDispatcherThread  {

	private Socket asocket;
	
	//<fromQQNumber,<toQQNumber,msg> >
	private static Map<String ,Map<String,String>> msgMap = new HashMap<String ,Map<String,String>>(); 
	
	//<toQQNumber,<fromQQNumber,msg> >
	private static Map<String ,Map<String,String>> msgMap2 = new HashMap<String ,Map<String,String>>();
	
	public MsgDispatcherThread(Socket asocket) {
		this.asocket =  asocket;
	}
	
	public void run() {
		InputStream is = null;
		OutputStream os = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		
		try {
			is = asocket.getInputStream();
			os = asocket.getOutputStream();
			
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			osw = new OutputStreamWriter(os);//should not use
			bw = new BufferedWriter(osw);//shoulde not use
			
			String line = null;
			line = br.readLine();//receive useinfo
			String[] qqNumArr = line.split("::");
			
			String fromNum = qqNumArr[0];
			String toNum = qqNumArr[1];
			StringBuilder msg = new StringBuilder();
			while((line = br.readLine()) != null) {
				msg.append(line);
			}
			//br.close();
			
			
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
			if(sendMap != null) {
				StringBuilder sendMsg = new StringBuilder();
				Iterator<String> it = sendMap.keySet().iterator();
				while(it.hasNext()) {
					String from = it.next();
					sendMsg.append(from).append("::").append(fromNum).append("\n");
					sendMsg.append(sendMap.get(from));
				}
	 			//bw.write(sendMsg.toString());
				
			}
			bw.write("welcone to QQ");//should not use
 			bw.flush();//should not use
 			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			
			/*try {
				if(br != null) {
					br.close();
					br = null;
				}
					
				
				if(isr != null) {
					isr.close();
					isr = null;
				}
					
				
				
				if(is != null) {
					is.close();
					is =null;
				}
					
				
				if(bw != null) {
					bw.close();
					bw = null;
				}
					
				if(osw != null) {
					osw.close();
					osw = null;
				}
				*/	

				//asocket.close();
		}
			
		
		
	}

}
