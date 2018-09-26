package deaddream.units.utilities.input.commandhandlers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import deaddream.maps.MapManager;
import deaddream.units.Unit;
import deaddream.units.utilities.input.commands.BaseCommandInterface;
import deaddream.units.utilities.input.commands.MoveCommand;

public class MoveCommandHandler extends CommandHandler<MoveCommand>{

	private MapManager mapManager;
	
	public MoveCommandHandler(MapManager mapManager) {
		this.mapManager = mapManager;
	}
	
	@Override
	public boolean checkCommandType(BaseCommandInterface command) {
		return (command instanceof MoveCommand);
	}

	@Override
	protected void handleProcess(MoveCommand command) {
		for(Unit unit : command.getPlayer().getSelection().getSelected()) {
			if (unit.getPlayer() == command.getPlayer()) {
				Array<Vector2> path = mapManager.pathFinder.getPath(unit, command.getCursorPosition());
				if (path != null) {
					unit.moveTo(path);
				}
			}
		}		
	}

}
