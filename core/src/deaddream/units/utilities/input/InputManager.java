package deaddream.units.utilities.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.dd.Constants;

import deaddream.players.Player;
import deaddream.units.Unit;

public class InputManager {
	
	private Vector3 cursorPosition;
	
	public SelectField selectField = new SelectField();
	
	private void updatePosition(Vector3 position) {
		cursorPosition = position;
	}
	
	public Vector3 getCursorPosition() {
		return cursorPosition;
	}
	
	public void updateSelection(Player currentPlayer) {
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			selectField.update(cursorPosition.x, cursorPosition.y);
		} else {
			if (selectField.getIsReady()) {
				currentPlayer.getSelection().drop();
				for (Unit unit : currentPlayer.getUnits()) {
					if (selectField.isInSelectField(
							unit.getBody().getPosition().x * Constants.PPM,
							unit.getBody().getPosition().y * Constants.PPM
						)
					) {
						currentPlayer.getSelection().add(unit);
					}
				}
			}
			selectField.drop();
		}
	}
	
	public void update(Vector3 position, Player currentPlayer) {
		updatePosition(position);
		updateSelection(currentPlayer);
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
}
