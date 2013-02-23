package pudgewars.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
	private static int WAITING_STATE = 0;
	private static int FULL_STATE = 1;
	private static int INGAME_STATE = 2;
	private static int SERVER_STATE = 0;
	private static int COUNTER = 0;
	private static int READY = 0;
	
	private static Vector<ClientNode> clients = new Vector<ClientNode>();
	
	private static class handleClient implements Runnable{
		Socket socket;
		
		public handleClient(Socket socket){
			this.socket = socket;
		}
		
		public void run(){
			MyConnection conn = new MyConnection(socket);

			//automatic team assignment
			int team = 0;
			if(teamCount(0) < 4 && teamCount(1) < 4) team = COUNTER % 2;
			else if(teamCount(0) < 4) team = 0;
			else if(teamCount(1) < 4) team = 1;
			else SERVER_STATE = FULL_STATE; 
			
			//check if server is full or there is an ongoing game
			if(SERVER_STATE == FULL_STATE || SERVER_STATE == INGAME_STATE){
				conn.sendMessage("SERVERERROR\nEOM");
				return;
			}
			
			//add client to clients vector
			ClientNode client = new ClientNode(conn, "Client" + COUNTER++, "", team);
			clients.add(client);
			conn.sendMessage("NAME\n" + client.getName() + "\nEOM");
			sendToAll("Server Message: " + client.getName() + " has joined.\n");
			updateClientsInfo();
			
			//waiting for all players to get ready, chat system
			while(SERVER_STATE != INGAME_STATE){
				String line = conn.getMessage();
				
				if(line.equals("/q")){
					System.out.println("Server Message: " + client.getName() + " has left.\n");
					sendToAll("Server Message: " + client.getName() + " has left.\n");
					clients.remove(client);
					if(!client.getStatus().equals("")) READY--;
					updateClientsInfo();
					SERVER_STATE = WAITING_STATE;
					break;
				}
				
				do{
					if(line.length() >= 4 && line.substring(0, 3).equals("/cn")){
						line = line.replaceAll("\\s+", " ");
						String newName = line.split(" ")[1];
						conn.sendMessage("NAME\n" + newName + "\nEOM");
						sendToAll("Server Message: " + client.getName() + " has changed name to " + newName + ".\n");
						client.setName(newName);
						updateClientsInfo();
					}else if(line.length() >= 4 && line.substring(0, 3).equals("/cs")){
						line = line.replaceAll("\\s+", " ");
						if(!line.substring(4).equals("")){
							client.setStatus(" - " + line.substring(4));
							READY++;
						}
						else{
							client.setStatus("");
							READY--;
						}
						updateClientsInfo();
					}else if(line.equals("/ct")){
						if(client.getTeam() == 0 && teamCount(1) < 4) client.setTeam(1);
						else if(client.getTeam() == 1 && teamCount(0) < 4) client.setTeam(0);
						else conn.sendMessage("FULLERROR\nEOM");
						updateClientsInfo();
					}else{
						sendToAll(client.getName() + ": " + line + "\n");
					}
					
					line = conn.getMessage();
				}while(!line.equals("EOM"));
				if(READY == clients.size()){
					if(teamCount(1) > 0 && teamCount(0) > 0) SERVER_STATE = INGAME_STATE;
					else{
						sendToAll("TEAMERROR\n");
						READY = 0;
						for(int x = 0;x<clients.size();x++) clients.get(x).setStatus("");
						updateClientsInfo();
					}
				}
			}
			
			if(SERVER_STATE == INGAME_STATE){
				sendToAll("START\nEOM");
			}
		}
		
		private int teamCount(int team){
			int count = 0;
			for(int x = 0; x<clients.size(); x++){
				if(clients.get(x).getTeam() == team) count++;
			}
			return count;
		}
		
		public void sendToAll(String msg){
			for(int x = 0; x<clients.size(); x++){
				clients.get(x).getConnection().sendMessage(msg + "EOM");
			}
		}
		
		public void updateClientsInfo(){
			String msg = "ClientsInfo1\n";
			for(int x = 0; x<clients.size(); x++){
				if(clients.get(x).getTeam() == 0) msg = msg + clients.get(x).getName() + clients.get(x).getStatus() + "\n";
			}
			msg = msg + "ClientsInfo2\n";
			for(int x = 0; x<clients.size(); x++){
				if(clients.get(x).getTeam() == 1) msg = msg + clients.get(x).getName() + clients.get(x).getStatus() + "\n";
			}
			sendToAll(msg);
		}
	}
		
	public static void main(String args[]){
		try {
			ServerSocket ssocket = new ServerSocket(8888);
			while (true) {
				Socket socket = ssocket.accept();

				Thread t = new Thread(new handleClient(socket));
				t.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
