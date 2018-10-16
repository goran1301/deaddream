package deaddream.players;

import com.badlogic.gdx.math.Vector3;

import deaddream.units.utilities.input.CommanderInterface;
import deaddream.units.utilities.input.InputManager;

public class LocalPlayer extends Player {

	protected CommanderInterface<Vector3> controller;
	
	public LocalPlayer(int id, int type, String color) {
		super(id, type, color);
		controller = new InputManager(this);
	}

	@Override
	public CommanderInterface<Vector3> getController() {
		return controller;
	}

}
