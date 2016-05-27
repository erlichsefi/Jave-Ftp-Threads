package Client_Threads;

import java.io.BufferedOutputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import Communication.ClientCommunicationTools;



public class ReceiveFile extends Thread {
	private String Ip;
	private int port;
	private String path_to_save;
private ClientCommunicationTools communication;
	public ReceiveFile(int _port,String _path,String path_Save, ClientCommunicationTools _communication){
		this.port=_port;
		this.Ip=_path;
		this.path_to_save=path_Save;
		communication=_communication;
	}

	public void run(){
		try {
			
			Socket sock = new Socket(Ip, port);
			byte[] mybytearray = new byte[1024];
			InputStream is = sock.getInputStream();
			FileOutputStream fos = new FileOutputStream(path_to_save);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			int bytesRead = is.read(mybytearray);
			while (bytesRead > 0){
			bos.write(mybytearray, 0, bytesRead);
			bytesRead = is.read(mybytearray);
			}
			bos.close();
			sock.close();
			communication.addConsole("got all of file: "+path_to_save);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}