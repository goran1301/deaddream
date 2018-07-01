package deaddream.players;

import com.badlogic.gdx.utils.Array;

import deaddream.units.Unit;

public class UnitSelection {
	
	protected final Array<Unit> selected = new Array<Unit>();
	
	public Array<Unit> getSelected() {
		return selected;
	}
	
	public void add(Unit unit) {
		selected.add(unit);
	}
	
	public void add(Array<Unit> units) {
		selected.addAll(units);
	}
	
	public void select(Unit unit) {
		selected.clear();
		selected.add(unit);
	}
	
	public void select(Array<Unit> units) {
		selected.clear();
		selected.addAll(units);
	}
	
	public void remove(Unit unit) {
		selected.removeValue(unit, true);
	}
}
