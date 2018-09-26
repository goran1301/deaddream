package deaddream.units.utilities.input.commandhandlers;

import com.mygdx.dd.Constants;

import deaddream.units.Unit;
import deaddream.units.utilities.input.SelectField;
import deaddream.units.utilities.input.commands.BaseCommandInterface;
import deaddream.units.utilities.input.commands.GroupSelectionCommand;

public class GroupSelectionCommandHandler extends CommandHandler<GroupSelectionCommand> {

	@Override
	public boolean checkCommandType(BaseCommandInterface command) {
		if (command instanceof GroupSelectionCommand) {
			return true;
			
		}
		return false;
	}

	@Override
	protected void handleProcess(GroupSelectionCommand command) {
		command.getPlayer().getSelection().drop();
		for (Unit unit : command.getPlayer().getUnits()) {
			if (SelectField.isInSelectField(
					unit.getBody().getPosition().x * Constants.PPM,
					unit.getBody().getPosition().y * Constants.PPM,
					command.getX1(),
					command.getX2(),
					command.getY1(),
					command.getY2()
				)
			) {
				command.getPlayer().getSelection().add(unit);
			}
		}
	}
	

}
