package pudgewars.network;

import pudgewars.Game;

public class ClientNetwork extends Network {
	private MyConnection clientConn;

	public ClientNetwork(MyConnection conn) {
		super();
		this.clientConn = conn;
	}

	public void sendEntityData(String msg){
		//System.out.println(msg);
		clientConn.sendMessage(msg);
	}
	
	public void getEntityData(){
		String msg;
		while(!(msg = clientConn.getMessage()).equals("EOM")){
			String t[] = msg.split(":");
			int index = Integer.parseInt(t[0]);
			if(Game.entities.entities.size()-1 < index) Game.entities.addClientEntity(msg);
			else Game.entities.entities.get(index).setNetworkString(t[1] + ":" + t[2] + ":" + t[3]);
		}
	}
}
