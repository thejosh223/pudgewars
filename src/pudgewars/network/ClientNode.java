package pudgewars.network;

public class ClientNode {
	private MyConnection conn;
	private String name;
	private String status;
	private int team;
	
	public ClientNode(MyConnection conn, String name, String status, int team){
		this.conn = conn;
		this.name = name;
		this.status = status;
		this.team = team;
	}
	
	public MyConnection getConnection(){
		return this.conn;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
		return;
	}
	
	public String getStatus(){
		return this.status;
	}
	
	public void setStatus(String status){
		this.status = status;
		return;
	}
	
	public int getTeam(){
		return this.team;
	}
	
	public void setTeam(int team){
		this.team = team;
		return;
	}
}