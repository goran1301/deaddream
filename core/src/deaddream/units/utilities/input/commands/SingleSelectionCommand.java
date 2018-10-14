package deaddream.units.utilities.input.commands;

import deaddream.players.Player;

public class SingleSelectionCommand implements BaseCommandInterface {
    
	private int id;
	
	private Player player;
	
	private int index;
	
	private float delta;
	
	private int code;
	
	public SingleSelectionCommand(int id, float delta, Player player, int index) {
		this.id = id;
		this.delta = delta;
		this.player = player;
		this.index = index;
	}
	
	
	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public int getCode() {
		return 2;
	}
	
	public int getIndex() {
		return index;
	}
	

	@Override
	public int getFrameId() {
		return id;
	}


	@Override
	public float getDelta() {
		return delta;
	}


	@Override
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

}
