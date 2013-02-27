package pudgewars.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import pudgewars.ServerGame;

public class Server {
	// Server State Constants
	private final static int WAITING_STATE = 0;
	private final static int FULL_STATE = 1;
	private final static int INGAME_STATE = 2;

	// Server State Variables
	private static int serverState = 0;
	private static int playerCounter = 0;
	private static int ready = 0;

	private static List<ClientNode> clients = new ArrayList<ClientNode>();

	private static class HandleClient implements Runnable {
		Socket socket;

		public HandleClient(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			MyConnection conn = new MyConnection(socket);

			// Automatic Team Assignment
			// -alternate teams based on who joins next
			int team = 0;
			if (teamCount(0) < 4 && teamCount(1) < 4) team = playerCounter % 2;
			else if (teamCount(0) < 4) team = 0;
			else if (teamCount(1) < 4) team = 1;
			else serverState = FULL_STATE;

			// Check if server is full or there is an ongoing game
			if (serverState == FULL_STATE || serverState == INGAME_STATE) {
				conn.sendMessage("SERVERERROR\nEOM");
				return;
			}

			// Add client to clients list
			ClientNode client = new ClientNode(conn, "Client" + playerCounter++, "", team);
			clients.add(client);
			conn.sendMessage("NAME\n" + client.getName() + "\nEOM");
			sendToAll("Server Message: " + client.getName() + " has joined.\n");
			updateClientsInfo();

			// Wait for all players to get ready, chat system
			while (serverState != INGAME_STATE) {
				String line = conn.getMessage();

				if (line.equals("STOP")) return;

				if (line.equals("/q")) {
					System.out.println("Server Message: " + client.getName() + " has left.\n");
					sendToAll("Server Message: " + client.getName() + " has left.\n");
					clients.remove(client);
					if (!client.getStatus().equals("")) ready--;
					updateClientsInfo();
					serverState = WAITING_STATE;
					break;
				}

				do {
					if (line.length() >= 4 && line.substring(0, 3).equals("/cn")) {
						line = line.replaceAll("\\s+", " ");
						String newName = line.split(" ")[1];
						conn.sendMessage("NAME\n" + newName + "\nEOM");
						sendToAll("Server Message: " + client.getName() + " has changed name to " + newName + ".\n");
						client.setName(newName);
						updateClientsInfo();
					} else if (line.length() >= 4 && line.substring(0, 3).equals("/cs")) {
						line = line.replaceAll("\\s+", " ");
						if (!line.substring(4).equals("")) {
							client.setStatus(" - " + line.substring(4));
							ready++;
						} else {
							client.setStatus("");
							ready--;
						}
						updateClientsInfo();
					} else if (line.equals("/ct")) {
						if (client.getTeam() == 0 && teamCount(1) < 4) client.setTeam(1);
						else if (client.getTeam() == 1 && teamCount(0) < 4) client.setTeam(0);
						else conn.sendMessage("FULLERROR\nEOM");
						updateClientsInfo();
					} else {
						sendToAll(client.getName() + ": " + line + "\n");
					}

					line = conn.getMessage();
				} while (!line.equals("EOM"));
				if (ready == clients.size()) {
					if (teamCount(1) > 0 && teamCount(0) > 0) serverState = INGAME_STATE;
					else {
						sendToAll("TEAMERROR\n");
						ready = 0;
						for (int x = 0; x < clients.size(); x++)
							clients.get(x).setStatus("");
						updateClientsInfo();
					}
				}
			}

			// start game!
			if (serverState == INGAME_STATE) {
				try {
					new Socket("127.0.0.1", 8888);
				} catch (Exception e) {
					e.printStackTrace();
				}
				sendToAll("START\nEOM");
				conn.getMessage();
				return;
			}
		}

		private int teamCount(int team) {
			int count = 0;
			for (int x = 0; x < clients.size(); x++)
				if (clients.get(x).getTeam() == team) count++;
			return count;
		}

		public void sendToAll(String msg) {
			for (int x = 0; x < clients.size(); x++)
				clients.get(x).getConnection().sendMessage(msg + "EOM");
		}

		public void updateClientsInfo() {
			String msg = "ClientsInfo1\n";
			for (int x = 0; x < clients.size(); x++)
				if (clients.get(x).getTeam() == 0) msg = msg + clients.get(x).getName() + clients.get(x).getStatus() + "\n";

			msg = msg + "ClientsInfo2\n";
			for (int x = 0; x < clients.size(); x++)
				if (clients.get(x).getTeam() == 1) msg = msg + clients.get(x).getName() + clients.get(x).getStatus() + "\n";
			sendToAll(msg);
		}
	}

	public static void main(String args[]) {
		try {
			ServerSocket ssocket = new ServerSocket(8888);
			List<Thread> threads = new ArrayList<Thread>();
			while (true) {
				Socket socket = ssocket.accept();
				if (serverState == INGAME_STATE) break;
				Thread t = new Thread(new HandleClient(socket));
				t.start();
				threads.add(t);
			}
			for (int i = 0; i < threads.size(); i++)
				threads.get(i).join();

			// Join all threads, no more threads
			ServerGame g = new ServerGame(clients);
			g.init();
			g.gameLoop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
