package deaddream.units.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.dd.DeadDream;

import deaddream.players.Player;
import deaddream.units.Protector;
import deaddream.units.UCMothership;
import deaddream.units.Unit;

public class UnitFactory {
	
	private DeadDream game;
	
	public UnitFactory(final DeadDream game) {
		this.game = game;
	}
	
	public Unit createProtector(World world, float xPos, float yPos, Group group, Player player) {
		Texture protecterTexture = game.assets.get("skins/units/protector.png", Texture.class);
		protecterTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Texture protecterNormalTexture = game.assets.get("skins/units/protectorNormal.png", Texture.class);
		protecterNormalTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		final Protector protector = new Protector(world, new Sprite(protecterTexture), new Sprite(protecterNormalTexture), xPos, yPos, 0.0f);
		player.addUnit(protector);
		group.addActor(protector);
		protector.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				protector.getPlayer().getSelection().select(protector);
				//System.out.println("Unit00 selected" + String.valueOf(protector.getZIndex()));
				//selectUnit(unit00);
			}
		});
		return protector;
	}
	
	public Unit createUCMothership(World world, float xPos, float yPos, Group group, Player player) {
		Texture mothershipTexture = game.assets.get("skins/units/materinskiy.png", Texture.class);
		mothershipTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Texture mothershipNormalTexture = game.assets.get("skins/units/materinskiyNormal.png", Texture.class);
		mothershipNormalTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		final UCMothership mothership = new UCMothership(world, new Sprite(mothershipTexture), new Sprite(mothershipNormalTexture), xPos, yPos, 0.0f);
		player.addUnit(mothership);
		group.addActor(mothership);
		mothership.toBack();
		mothership.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				mothership.getPlayer().getSelection().select(mothership);
				//System.out.println("Unit00 selected" + String.valueOf(protector.getZIndex()));
				//selectUnit(unit00);
			}
		});
		return mothership;
	}
}
