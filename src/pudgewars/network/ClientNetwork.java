package pudgewars.network;

import pudgewars.Game;
import pudgewars.entities.Entity;
import pudgewars.entities.PudgeEntity;
import pudgewars.entities.hooks.NormalHookEntity;
import pudgewars.entities.hooks.GrappleHookEntity;
import pudgewars.util.Vector2;

public class ClientNetwork extends Network {
	private MyConnection clientConn;

	public ClientNetwork(MyConnection conn) {
		super();
		this.clientConn = conn;
	}

	public void sendEntityData(String msg){
		// System.out.println(msg);
		clientConn.sendMessage(msg);
	}
	
	public void getEntityData(){
		//format index:position:velocity:moveTarget:team:hookTarget:isGrapple:controllable
		String msg;
		
		while(!(msg = clientConn.getMessage()).equals("EOM")){
			String t[] = msg.split(":");
			int index = Integer.parseInt(t[0]);
			
			if(t[1].equals("PUDGE")){
				if(Game.entities.entities.size()-1 < index) 
					Game.entities.addPudgeEntity(msg);
				else 
					Game.entities.entities.get(index).setNetworkString(t[1] + ":" + t[2] + ":" + t[3] + ":" + t[4] + ":" + t[5] + ":" + t[6]);
			}else if(t[1].equals("NORMALHOOK") || t[1].equals("GRAPPLEHOOK") ){
				int hookIndex = Game.entities.containsHook(Integer.parseInt(t[2]), t[3]);
				if(hookIndex == -1)
					Game.entities.addHookEntity(msg);
				else 
					Game.entities.entities.get(hookIndex).setNetworkString(t[4] + ":" + t[5]);
			}
		}
	}
	
	public void clearBuffer(){
		String msg;
		while(!(msg = clientConn.getMessage()).equals("EOM"));
	}
}
