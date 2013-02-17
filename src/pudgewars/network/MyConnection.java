package pudgewars.network;

import java.net.Socket;
import java.net.InetAddress;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MyConnection {
	
	Socket socket;
	
	InputStream inputStream;
	InputStreamReader inputStreamReader;
	BufferedReader bufferedReader;
	
	OutputStream outputStream;
	OutputStreamWriter outputStreamWriter;
	PrintWriter printWriter;
	
	public MyConnection(Socket s){
		try{
			this.socket = s;
			
			inputStream = this.socket.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			
			outputStream = socket.getOutputStream();
			outputStreamWriter = new OutputStreamWriter(outputStream);
			printWriter = new PrintWriter(outputStreamWriter);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public InetAddress getInetAddress(){
		return this.socket.getInetAddress();
	}
	
	public boolean sendMessage(String msg){
		try{
			printWriter.println(msg);
			printWriter.flush();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public String getMessage(){
		try{
			String msg = bufferedReader.readLine();
			return msg;
		}
		catch(Exception e){
			return e.toString();
		}
	}
}