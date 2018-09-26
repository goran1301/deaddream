package deaddream.players;


import com.badlogic.gdx.utils.Array;
import deaddream.units.Unit;
import deaddream.units.utilities.input.CommanderInterface;

abstract public class Player {
	
	protected int id;
	protected Array<Unit> units = new Array<Unit>();
	protected int type;
	protected int status;
	protected final UnitSelection selection = new UnitSelection();
	
	public final static int notReadyStatus = 0;
	
	public final static int readyStatus = 1;
	
	public final static int lostStatus = 2;
	
	public final static int winnerStatus = 3;
	
	public final static int inGameStatus = 4;
	
	public Player(int id, int type) {
		this.id = id;
		this.type = type;
	}
	
	public int getId() {
		return id;
	}
	
	abstract public CommanderInterface<?> getController();
	
	public boolean isReady() {
		return status == readyStatus;
	}
	
	public boolean isWinner() {
		return status == winnerStatus;
	}
	
	public boolean isInGame() {
		return status == inGameStatus;
	}
	
	public void ready() {
		status = readyStatus;
	}
	
	public void playGame() {
		status = inGameStatus;
	}
	
	public void notReady() {
		status = notReadyStatus;
	}
	
	public void winTheGame() {
		status = winnerStatus;
	}
	
	public void lose() {
		status = lostStatus;
	}
	
	public void addUnit(Unit unit) {
		unit.setPlayer(this);
		units.add(unit);
	}
	
	public Unit getUnit(int index) {
		return units.get(index);
	}
	
	public void loseUnit(Unit unit) {
		units.removeValue(unit, true);
	}
	
	public UnitSelection getSelection() {
		return selection;
	}
	
	public Array<Unit> getUnits() {
		return units;
	}
}
