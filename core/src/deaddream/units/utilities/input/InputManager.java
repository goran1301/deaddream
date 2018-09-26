package deaddream.units.utilities.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import deaddream.players.LocalPlayer;
import deaddream.units.utilities.input.commands.BaseCommandInterface;
import deaddream.units.utilities.input.commands.GroupSelectionCommand;
import deaddream.units.utilities.input.commands.MoveCommand;

public class InputManager implements CommanderInterface<Vector3>{
	
	private Vector3 cursorPosition;
	
	public SelectField selectField = new SelectField();
	
	private boolean rightPressed = false;
	
	private boolean leftPressed = false;
	
	private LocalPlayer player;
	
	public InputManager(LocalPlayer player) {
		this.player = player;
	}
	
	private void updatePosition(Vector3 position) {
		cursorPosition = position;
	}
	
	public Vector3 getCursorPosition() {
		return cursorPosition;
	}
	
	public void updateSelection() {
		if (leftPressed) {
			selectField.update(cursorPosition.x, cursorPosition.y);
		} 
	}
	
	public void update(Vector3 position) {
		gameClickEventCheck();
		updatePosition(position);
		updateSelection();
	}
	
	private void gameClickEventCheck() {
		leftPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
		rightPressed = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
	}
	
	public boolean rightPressed() {
		return rightPressed;
	}
	
	public boolean leftPressed(){
		return leftPressed;
	}
	
	public void render(ShapeRenderer shapeRenderer) {
		if (selectField.getIsReady()) {
			shapeRenderer.setColor(Color.GREEN);
			float startX = selectField.getStartX() < selectField.getEndX() ? selectField.getStartX() : selectField.getEndX();
			float startY = selectField.getStartY() < selectField.getEndY() ? selectField.getStartY() : selectField.getEndY();			
			float endX = selectField.getStartX() > selectField.getEndX() ? selectField.getStartX() : selectField.getEndX();
			float endY = selectField.getStartY() > selectField.getEndY() ? selectField.getStartY() : selectField.getEndY();
			shapeRenderer.rect(
					startX,
					startY,
					Math.abs(endX -startX),
					Math.abs(endY -startY)
				);
		}
	}

	@Override
	public BaseCommandInterface getCommand() {
		
		if (selectField.getIsReady() && !leftPressed) {
			GroupSelectionCommand command = new GroupSelectionCommand(
				player,
				selectField.getStartX(),
				selectField.getEndX(),
				selectField.getStartY(),
				selectField.getEndY()
			);
			selectField.drop();
			return command;
		}
		
		if (rightPressed) {
			return new MoveCommand(player, cursorPosition);
		}
		
		return null;
	}
}
