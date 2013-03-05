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
		System.out.println("ACTION COMMITTED >> " + msg + " sent to server.");
		clientConn.sendMessage(msg);
	}
	
	public void getEntityData(){
		//format for PUDGE >> index:controllable:PUDGE:position:velocity:moveTarget:team:hookTarget:isGrapple:life
		//format for HOOKS >> index:controllable:HOOKNAME:ownerIndex:target:position:velocity
		String msg;
		
		while(!(msg = clientConn.getMessage()).equals("EOM")){
			String t[] = msg.split(":");
			int index = Integer.parseInt(t[0]);
			
			if(t[2].equals("PUDGE")){
				if(Game.entities.entities.size()-1 < index) 
					Game.entities.addPudgeEntity(msg);
				else 
					Game.entities.entities.get(index).setNetworkString(msg.substring(msg.indexOf(':',msg.indexOf(':')+1)+1));
			}else if(t[2].equals("NORMALHOOK") || t[2].equals("GRAPPLEHOOK") ){
				int hookIndex = Game.entities.containsHook(Integer.parseInt(t[3]), t[4]);
				if(hookIndex == -1)
					Game.entities.addHookEntity(msg);
				else 
					Game.entities.entities.get(hookIndex).setNetworkString(t[5] + ":" + t[6]);
			}
		}
	}
	
	public void clearBuffer(){
		String msg;
		while(!(msg = clientConn.getMessage()).equals("EOM"));
	}
}
