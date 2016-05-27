package Server_Threads;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import Communication.ServerCommunicationTools;
import Tools.message;





public class SendFile extends Thread {
	private int port;
	private String filePath;
	private  ServerCommunicationTools Communication;
	private String ToClient;
	private int Sent=0;
	private boolean send=true;



	public SendFile(String client,int _port,String _path,ServerCommunicationTools _Communication){
		this.port=_port;
		this.filePath=_path;
		Communication=_Communication;
		ToClient=client;
	}


	public void run(){
		ServerSocket servsock = null;
		try {
			servsock = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		File myFile = new File(filePath);
		try {
			boolean sentfalg=true;
			byte[] mybytearray = new byte[(int) myFile.length()];
			Communication.putExit(new message("server",ToClient,Tools.MessageType.GET_FILE,mybytearray.length));
			Socket sock = servsock.accept();
			Communication.AddLog("sending file with : "+mybytearray.length+" bites");
			int half=mybytearray.length/2;
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
			OutputStream os = sock.getOutputStream();

			int count;
			while (((count = bis.read(mybytearray)) > 0 && send)) {
				os.write(mybytearray, 0, count);
				os.flush();
				Communication.AddLog("sending : "+Sent+" to "+(Sent+count));
				Sent+=count+1;
				if (Sent>half){
					if (sentfalg){
						Communication.putExit(new message("server",ToClient,Tools.MessageType.TAKE_HALF,null));
						sentfalg=false;
					}
				}
			}
			if (send){
				Communication.putExit(new message("server",ToClient,Tools.MessageType.TAKE_FULL,null));
				Communication.AddLog("Done");
			}else{
				Communication.putExit(new message("server",ToClient,Tools.MessageType.U_HAVE_STOP,null));
				Communication.AddLog("as been stoped");
			}
			sock.close();
			bis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//servsock.close();


	public void StopSending(){
		send=false;
	}

}
