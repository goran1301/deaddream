package deaddream.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.dd.Constants;
import com.mygdx.dd.DeadDream;
import deaddream.maps.TiledObjectUtil;
import deaddream.units.Protector;
import deaddream.units.Stone;
import deaddream.units.Unit;


public class GameScreen implements Screen {
	
	private DeadDream game;
	
	private World world;
	
	private Unit unit00, unit01, stone, UCMothership;
	
	private Box2DDebugRenderer b2ddr;
	
	private Matrix4 debugMatrix;
	
	private Texture background;
	
	public OrthogonalTiledMapRenderer tmr;
	
	public TiledMap map;
	
	private IndexedAStarPathFinder pathFinder;
	
	private Stage stage;
	
	private Unit selectedUnit;
	
	public GameScreen(final DeadDream game) {
		this.game = game;
		this.world = new World(new Vector2(0, 0), false);
		this.b2ddr = new Box2DDebugRenderer();
		this.debugMatrix = this.game.camera.combined.cpy();//new Matrix4(this.game.camera.combined.cpy());
		this.debugMatrix.scale(Constants.PPM, Constants.PPM, 0.0f);
		this.stage = new Stage(new StretchViewport(this.game.V_WIDTH, this.game.V_HEIGHT, this.game.camera));
	}	

	@Override
	public void show() {
		System.out.println("Game");
		Gdx.input.setInputProcessor(this.stage);
		map = new TmxMapLoader().load("maps/test.tmx");
		tmr = new OrthogonalTiledMapRenderer(map);
		this.loadTextures();
		this.unit00 = new Protector(this.world, game.assets.get("skins/units/protector.png", Texture.class), 23f, 23f, 0.0f);
		this.unit01 = new Protector(this.world, game.assets.get("skins/units/protector.png", Texture.class), 35f, 40f, 1f);
		this.stone = new Stone(this.world, game.assets.get("skins/units/stone.png", Texture.class), 25f, 25f, 1f);
		this.UCMothership = new deaddream.units.UCMothership(this.world, game.assets.get("skins/units/ucmothership.png", Texture.class), 40f, 40f, 1f);
		MapObjects objects =  map.getLayers().get("collision-layer").getObjects();
		TiledObjectUtil.parseTiledObjectLayer(world, objects);
		
		Group group = new Group();
		group.addActor(UCMothership);
		group.addActor(unit00);
		group.addActor(unit01);
		
		//stage.addActor(UCMothership);
		//stage.addActor(unit00);
		//stage.addActor(unit01);
		stage.addActor(group);
		stage.addActor(stone);
		
		unit00.setZIndex(1);
		unit01.setZIndex(2);
		UCMothership.setZIndex(0);
		
		UCMothership.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("UCMothership selected" + String.valueOf(UCMothership.getZIndex()));
				selectUnit(UCMothership);
			}
		});
		
		unit00.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Unit00 selected" + String.valueOf(unit00.getZIndex()));
				selectUnit(unit00);
			}
		});
		
		unit01.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Unit01 selected" + String.valueOf(unit01.getZIndex()));
				selectUnit(unit01);
			}
		});
		
		
		
		
		
		/*this.buttonExit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});*/
        //pathFinder = new IndexedAStarPathFinder(null, false);

	}
	
	private void selectUnit(Unit unit) {
		selectedUnit = unit;
	}
	
	private void loadTextures() {
		this.background = game.assets.get("backgrounds/bg1.jpg", Texture.class);
		//this.testUnitSkin = game.assets.get("skins/units/protector.png", Texture.class);
		//this.background = new Image(background);
	}

	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		beginBatch();
		renderBackground();
		this.game.batch.end();

		tmr.render();
		stage.draw();
		
		
		//b2ddr.render(world, debugMatrix);

	}
	
	private void beginBatch() {
		this.game.batch.begin();
		this.game.batch.enableBlending();
		
	}
	
	private void renderBackground() {
		
		this.game.batch.draw(this.background, 
				this.game.camera.position.x - this.game.V_WIDTH / 2, 
				this.game.camera.position.y - this.game.V_HEIGHT / 2,
                this.game.V_WIDTH, this.game.V_HEIGHT
                );
	}
	
	public void update(float delta) {
		Vector3 tmp = this.game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		
				
		world.step(1/60f, 6, 2);
		stage.act(delta);
		this.game.batch.setProjectionMatrix(this.game.camera.combined);
		tmr.setView(game.camera);
		this.updateInput();
		
		float cameraX = this.game.camera.position.x;
		float cameraY = this.game.camera.position.y;
		if (tmp.x >= cameraX + game.V_WIDTH / 2 - 5){
			cameraX += 10f;
		}
		if (tmp.x <= cameraX - game.V_WIDTH / 2 + 5){
			cameraX -= 10f;
		}
		if (tmp.y >= cameraY + game.V_HEIGHT / 2 - 5){
			cameraY += 10f;
		}
		if (tmp.y <= cameraY - game.V_HEIGHT / 2 + 5){
			cameraY -= 10f;
		}
		
		this.cameraUpdate(cameraX, cameraY);
		
	}
	
	
	
	private void updateInput() {
		if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT))
		{
		    Vector3 tmp = this.game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		    if (selectedUnit != null) {
		    	selectedUnit.moveTo(tmp.x / Constants.PPM, tmp.y / Constants.PPM);
		    }
		}
	}
	
	public void setCameraToMeters(float x, float y) {
		cameraUpdate(x * Constants.PPM, y * Constants.PPM);
	}
	
	public void cameraUpdate(float x, float y) {
		Vector3 position = this.game.camera.position;
		position.x = x;
		position.y = y;
		game.camera.position.set(position);
		this.debugMatrix = this.game.camera.combined.cpy();
		this.debugMatrix.scale(Constants.PPM, Constants.PPM, 0.0f);
		game.camera.update();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		this.background.dispose();
		this.unit00.dispose();
		this.unit01.dispose();
		world.dispose();
		b2ddr.dispose();
		map.dispose();
		tmr.dispose();
		// TODO Auto-generated method stub
		
	}

}
