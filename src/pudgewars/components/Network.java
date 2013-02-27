package pudgewars.components;

import pudgewars.network.MyConnection;
import pudgewars.util.Vector2;
import pudgewars.entities.Team;

public class Network {
	private MyConnection conn;
	
	public Network(MyConnection conn){
		this.conn = conn;
	}
	
	public void sendServerPudgeEntity(Vector2 position, Team team, boolean controllable){
		conn.sendMessage("GENERATE " + position.x + " " + position.y + " " + team + " " + controllable);
	}
	
	public void sendMessage(String msg){
		conn.sendMessage(msg);
	}
	
	public String getMessage(){
		return conn.getMessage();
	}
}
