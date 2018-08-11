package deaddream.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.dd.Constants;

import deaddream.players.UnitSelection;
import deaddream.units.Unit;

public class SelectionRenderer {
	
	public static void render(UnitSelection selection, ShapeRenderer shapeRenderer) {
		for (Unit unit : selection.getSelected()) {
			renderSelectedUnit(unit, shapeRenderer);
		}
	}
	
	private static void renderSelectedUnit(Unit unit, ShapeRenderer shapeRenderer) {
		float diameter = unit.getWidth();
		if (diameter < unit.getHeight()) {
			diameter = unit.getHeight();
		}
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.circle(
				unit.getBody().getPosition().x * Constants.PPM,
				unit.getBody().getPosition().y * Constants.PPM,
				diameter / 2
			);
	}
}
