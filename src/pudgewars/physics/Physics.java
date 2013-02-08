package pudgewars.physics;

import pudgewars.entities.HookEntity;
import pudgewars.entities.HookPieceEntity;
import pudgewars.entities.PudgeEntity;
import pudgewars.interfaces.BBOwner;
import pudgewars.level.Tile;

public class Physics {
	public final static boolean blocks(Object b1, Object b2) {
		// Hook Pieces intersect with nothing
		if (b1 instanceof HookPieceEntity) return false;
		// Hook Collisions
		if (b1 instanceof HookEntity) {
			if (b2 instanceof HookEntity) return false;
			if (b2 instanceof PudgeEntity) return true;
			if (b2 instanceof Tile) {
				if (((Tile) b2).isHookable()) return true;
				if (((Tile) b2).isHookSolid()) return true;
				if (((Tile) b2).isPudgeSolid()) return false;
			}
		}
		// Pudge Collisions
		if (b1 instanceof PudgeEntity) {
			if (b2 instanceof HookEntity) return true;
			if (b2 instanceof PudgeEntity) return true;
			if (b2 instanceof Tile) {
				if (((Tile) b2).isHookable()) return false;
				if (((Tile) b2).isHookSolid()) return false;
				if (((Tile) b2).isPudgeSolid()) return true;
			}
		}

		return false;
	}
}
