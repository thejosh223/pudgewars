package pudgewars.components;

import java.util.ArrayList;
import java.util.List;

import pudgewars.util.Vector2;

public class Network {
	public List<Vector2> moveTargets;
	public List<Vector2> hookTargets;
	public List<Boolean> isSpecialHook;
	
	public Network(){
		moveTargets = new ArrayList<Vector2>();
		hookTargets = new ArrayList<Vector2>();
		isSpecialHook = new ArrayList<Boolean>();
	}
}

