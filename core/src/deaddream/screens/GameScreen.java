package deaddream.screens;

import java.text.DecimalFormat;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.pfa.PathSmoother;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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

import deaddream.backgrounds.BackgroundInterface;
import deaddream.backgrounds.DarkSpaceBackground;
import deaddream.backgrounds.WorldBackground;
import deaddream.logic.pathfinding.BaseIndexedGraph;
import deaddream.logic.pathfinding.MapBaseIndexedGraphFactory;
import deaddream.logic.pathfinding.PathCoordinator;
import deaddream.logic.pathfinding.TiledManhattanDistance;
import deaddream.logic.pathfinding.TiledNode;
import deaddream.logic.pathfinding.TiledSmoothableGraphPath;
import deaddream.maps.TiledObjectUtil;
import deaddream.units.Protector;
import deaddream.units.Stone;
import deaddream.units.Unit;
import deaddream.units.UCMothership;
import deaddream.units.utilities.map.BaseGraphDebugRenderer;
import deaddream.units.utilities.map.MothershipDebugRenderer;
import deaddream.units.UCMothership;

public class GameScreen implements Screen {
	
	private DeadDream game;
	
	private World world;
	
	private Unit unit00, unit01, stone, UCMothership;
	
	private Box2DDebugRenderer b2ddr;
	
	private Matrix4 debugMatrix;
	
	private Texture background;
	
	public OrthogonalTiledMapRenderer tmr;
	
	public TiledMap map;
	
	private IndexedAStarPathFinder<TiledNode> pathFinder;
	
	private BaseIndexedGraph<TiledNode> graph;
	
	private PathSmoother<TiledNode, Vector2> pathSmoother;
	
	private Stage stage;
	
	private Unit selectedUnit;
	
	private BaseGraphDebugRenderer graphDebugRenderer;
	
	private MothershipDebugRenderer msDebugRenderer;
	
	TiledManhattanDistance<TiledNode> heuristic = new TiledManhattanDistance<TiledNode>();
	
	TiledSmoothableGraphPath<TiledNode> path = new TiledSmoothableGraphPath<TiledNode>();
	

	boolean flipY = true;
	
	Matrix4 transform = new Matrix4();


	// position of our light
	final Vector3 DEFAULT_LIGHT_POS = new Vector3(50f, 50f, 0.07f);
	// the color of our light
	final Vector3 DEFAULT_LIGHT_COLOR = new Vector3(3f, 3f, 3f);
	// the ambient color (color to use when unlit)
	final Vector3 DEFAULT_AMBIENT_COLOR = new Vector3(0.7f, 0.7f, 0.7f);
	// the attenuation factor: x=constant, y=linear, z=quadratic
	final Vector3 DEFAULT_ATTENUATION = new Vector3(0.4f, 0.4f, 0.1f);
	// the ambient intensity (brightness to use when unlit)
	final float DEFAULT_AMBIENT_INTENSITY = 1f;
	final float DEFAULT_STRENGTH = 1f;
	
	final Color NORMAL_VCOLOR = new Color(1f,1f,1f,DEFAULT_STRENGTH);
	
	// the position of our light in 3D space
	Vector3 lightPos = new Vector3(DEFAULT_LIGHT_POS);
	// the resolution of our game/graphics
	Vector2 resolution = new Vector2();
	// the current attenuation
	Vector3 attenuation = new Vector3(DEFAULT_ATTENUATION);
	// the current ambient intensity
	float ambientIntensity = DEFAULT_AMBIENT_INTENSITY;
	float strength = DEFAULT_STRENGTH;
	
	// whether to use attenuation/shadows
	boolean useShadow = true;

	// whether to use lambert shading (with our normal map)
	boolean useNormals = true;
	
	ShaderProgram program;
	
	SpriteBatch fxBatch;
	
	BackgroundInterface bg;
	
	
	public GameScreen(final DeadDream game) {
		this.game = game;
		this.world = new World(new Vector2(0, 0), false);
		this.b2ddr = new Box2DDebugRenderer();
		this.debugMatrix = this.game.camera.combined.cpy();//new Matrix4(this.game.camera.combined.cpy());
		this.debugMatrix.scale(Constants.PPM, Constants.PPM, 0.0f);
		this.stage = new Stage(new StretchViewport(this.game.V_WIDTH, this.game.V_HEIGHT, this.game.camera), this.game.batch);
		this.create();
	}	

	@Override
	public void show() {
		System.out.println("Game");
		Gdx.input.setInputProcessor(this.stage);
		game.shapeRenderer.setProjectionMatrix(game.camera.combined);
		map = new TmxMapLoader().load("maps/test2.tmx");
		tmr = new OrthogonalTiledMapRenderer(map);
		this.loadTextures();
		Texture protecterTexture = game.assets.get("skins/units/protector.png", Texture.class);
		protecterTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Texture protecterNormalTexture = game.assets.get("skins/units/protectorNormal.png", Texture.class);
		protecterNormalTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Texture stoneTexture = game.assets.get("skins/units/stone.png", Texture.class);
		stoneTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Texture stoneNormalTexture = game.assets.get("skins/units/stoneNormal.png", Texture.class);
		stoneNormalTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Texture UCMothershipTexture = game.assets.get("skins/units/materinskiy.png", Texture.class);
		UCMothershipTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		Texture UCMothershipNormalTexture = game.assets.get("skins/units/materinskiyNormal.png", Texture.class);
		UCMothershipNormalTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.unit00 = new Protector(this.world, new Sprite(protecterTexture), new Sprite(protecterNormalTexture), 23f, 23f, 0.0f);
		this.unit01 = new Protector(this.world, new Sprite(protecterTexture), new Sprite(protecterNormalTexture), 35f, 40f, 1f);
		this.stone = new Stone(this.world, new Sprite(stoneTexture), new Sprite(stoneNormalTexture), 25f, 25f, 1f);
		UCMothership ms = new UCMothership(world, new Sprite(UCMothershipTexture), new Sprite(UCMothershipNormalTexture), 40f, 40f, 1f);
		this.UCMothership = ms;
		msDebugRenderer = new MothershipDebugRenderer(ms);

		MapObjects objects =  map.getLayers().get("collision-layer").getObjects();
		TiledObjectUtil.parseTiledObjectLayer(world, objects);
		graph = MapBaseIndexedGraphFactory.create(map);
		graphDebugRenderer = new BaseGraphDebugRenderer(graph, game.batch);
		
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
			public void clicked(rendererInputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});*/
        pathFinder = new IndexedAStarPathFinder<TiledNode>(graph, true);
        pathSmoother = new PathSmoother<TiledNode, Vector2>(new deaddream.logic.pathfinding.TiledRaycastCollisionDetector<TiledNode>(graph));
        
        game.font.getData().setScale(1);

	}
	
	private void selectUnit(Unit unit) {
		selectedUnit = unit;
	}
	
	private void loadTextures() {
		this.background = game.assets.get("backgrounds/world_background/stars.png", Texture.class);
		this.background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bg = new WorldBackground(
				game.assets.get("backgrounds/world_background/stars.png", Texture.class),
				game.assets.get("backgrounds/world_background/middle_layer.png", Texture.class),
				game.batch
			);
		bg.setResolution(game.camera.viewportWidth, game.camera.viewportHeight);
		//this.testUnitSkin = game.assets.get("skins/units/protector.png", Texture.class);
		//this.background = new Image(background);
	}

	@Override
	public void render(float delta) {
		update(delta);
		
		game.batch.begin();
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.end();
		// start our FX batch, which will bind our shader program
				fxBatch.begin();
				
				// get y-down light position based on mouse/touch
				lightPos.x = 100;//Gdx.input.getX();
				lightPos.y = 100;//Gdx.graphics.getHeight() - Gdx.input.getY();
				
				
				// update our uniforms
				program.setUniformf("ambientIntensity", ambientIntensity);
				program.setUniformf("attenuation", attenuation);
				program.setUniformf("light", lightPos);
				program.setUniformi("useNormals", useNormals ? 1 : 0);
				program.setUniformi("useShadow", useShadow ? 1 : 0);
				program.setUniformf("strength", strength);
				fxBatch.end();
		bg.render();
		tmr.render();
		this.UCMothership.setShaderProgram(program);
		this.unit00.setShaderProgram(program);
		this.unit01.setShaderProgram(program);
		this.stone.setShaderProgram(program);
		stage.draw();
		
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			game.font.setColor(Color.WHITE);
			graphDebugRenderer.render(game.shapeRenderer, game.font);
			if (selectedUnit != null) {
				game.shapeRenderer.begin(ShapeType.Line);
				TiledNode node = graph.getNodeByCoordinates(selectedUnit.getBody().getPosition().x * Constants.PPM,
						selectedUnit.getBody().getPosition().y * Constants.PPM);
				graphDebugRenderer.renderNode(node, game.shapeRenderer, Color.BLUE, game.font);
				game.shapeRenderer.end();
			}

		
			graphDebugRenderer.renderPath(path, game.shapeRenderer, game.font);
			msDebugRenderer.render(delta, game.shapeRenderer);

		}
		
	}
	
	public void update(float delta) {
		
		game.shapeRenderer.setProjectionMatrix(game.camera.combined);
		Vector3 tmp = this.game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
	    	
				
		world.step(1/60f, 6, 2);
		stage.act(delta);
		this.game.batch.setProjectionMatrix(this.game.camera.combined);
		
		this.updateInput();
		bg.updateCameraPosition(game.camera.position.x, game.camera.position.y);
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
		
		tmr.setView(game.camera);
		
	}
	
	
	
	private void updateInput() {
		if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT))
		{
			
		    Vector3 tmp = this.game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		    if (selectedUnit != null) {
		    	path.clear();
				graph.startNode = graph.getNodeByCoordinates(
						selectedUnit.getBody().getPosition().x * Constants.PPM,
						selectedUnit.getBody().getPosition().y * Constants.PPM
					);
				graph.unitSize.x = selectedUnit.getWidth();
				graph.unitSize.y = selectedUnit.getHeight();
				
				TiledNode endNode = graph.getNodeByCoordinates(tmp.x, tmp.y);
				if (endNode != null) {
					graph.setBodyWeight((int) Math.ceil(selectedUnit.getLargestSize() / Constants.PPM));
					System.out.println("Unit WEIGHT" + String.valueOf(graph.getBodyWeight()));
					pathFinder.searchNodePath(graph.startNode, endNode, heuristic, path);
					pathSmoother.smoothPath(path);
					selectedUnit.moveTo(PathCoordinator.getCoordinatesPath(path, tmp.x, tmp.y, graph.getPixelNodeSizeX(), graph.getPixelNodeSizeY(), graph.getBodyWeight() % 2 == 0));
				}
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
	public void create() {
		// create our shader program...
				program = createShader();

				// now we create our sprite batch for our shader
				fxBatch = new SpriteBatch(100, program);
				// setShader is needed; perhaps this is a LibGDX bug?
				fxBatch.setShader(program);
	}
	
	private ShaderProgram createShader() {
		// see the code here: http://pastebin.com/7fkh1ax8
		// simple illumination model using ambient, diffuse (lambert) and attenuation
		// see here: http://nccastaff.bournemouth.ac.uk/jmacey/CGF/slides/IlluminationModels4up.pdf
		String vert = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
				+ "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
				+ "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
				+ "uniform mat4 u_proj;\n" //
				+ "uniform mat4 u_trans;\n" //
				+ "uniform mat4 u_projTrans;\n" //
				+ "varying vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "\n" //
				+ "void main()\n" //
				+ "{\n" //
				+ "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
				+ "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
				+ "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
				+ "}\n";
		
		String frag = "#ifdef GL_ES\n" +
				"precision mediump float;\n" +
				"#endif\n" +
				"varying vec4 v_color;\n" +
				"varying vec2 v_texCoords;\n" +
				
				"uniform sampler2D u_texture;\n" +
				"uniform sampler2D u_normals;\n" +
				"uniform vec3 light;\n" + 
				"uniform vec3 ambientColor;\n" + 
				"uniform float ambientIntensity; \n" + 
				"uniform vec2 resolution;\n" + 
				"uniform vec3 lightColor;\n" + 
				"uniform bool useNormals;\n" + 
				"uniform bool useShadow;\n" + 
				"uniform vec3 attenuation;\n" + 
				"uniform float strength;\n" +
				"uniform bool yInvert;\n"+ 
				"\n" + 
				"void main() {\n" +
				"	//sample color & normals from our textures\n" +
				"	vec4 color = texture2D(u_texture, v_texCoords.st);\n" +
				"	vec3 nColor = texture2D(u_normals, v_texCoords.st).rgb;\n\n" +
				"	//some bump map programs will need the Y value flipped..\n" +
				"	nColor.g = yInvert ? 1.0 - nColor.g : nColor.g;\n\n" +
				"	//this is for debugging purposes, allowing us to lower the intensity of our bump map\n" +
				"	vec3 nBase = vec3(0.5, 0.5, 1.0);\n" +
				"	nColor = mix(nBase, nColor, strength);\n\n" +
				"	//normals need to be converted to [-1.0, 1.0] range and normalized\n" +
				"	vec3 normal = normalize(nColor * 2.0 - 1.0);\n\n" +
				"	//here we do a simple distance calculation\n" +
				"	vec3 deltaPos = vec3( (light.xy - gl_FragCoord.xy) / resolution.xy, light.z );\n\n" +
				"	vec3 lightDir = normalize(deltaPos);\n" + 
				"	float lambert = useNormals ? clamp(dot(normal, lightDir), 0.0, 1.0) : 1.0;\n" + 
				"	\n" + 
				"	//now let's get a nice little falloff\n" + 
				"	float d = sqrt(dot(deltaPos, deltaPos));"+ 
				"	\n" + 
				"	float att = useShadow ? 1.0 / ( attenuation.x + (attenuation.y*d) + (attenuation.z*d*d) ) : 1.0;\n" + 
				"	\n" + 
				"	vec3 result = (ambientColor * ambientIntensity) + (lightColor.rgb * lambert) * att;\n" + 
				"	result *= color.rgb;\n" + 
				"	\n" + 
				"	gl_FragColor = v_color * vec4(result, color.a);\n" + 
				"}";
		System.out.println("VERTEX PROGRAM:\n------------\n\n"+vert);
		System.out.println("FRAGMENT PROGRAM:\n------------\n\n"+frag);
		ShaderProgram program = new ShaderProgram(vert, frag);
		// u_proj and u_trans will not be active but SpriteBatch will still try to set them...
		program.pedantic = false;
		if (program.isCompiled() == false)
			throw new IllegalArgumentException("couldn't compile shader: "
					+ program.getLog());

		// set resolution vector
		resolution.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// we are only using this many uniforms for testing purposes...!!
		program.begin();
		program.setUniformi("u_texture", 0);
		program.setUniformi("u_normals", 1);
		program.setUniformf("light", lightPos);
		program.setUniformf("strength", strength);
		program.setUniformf("ambientIntensity", ambientIntensity);
		program.setUniformf("ambientColor", DEFAULT_AMBIENT_COLOR);
		program.setUniformf("resolution", resolution);
		program.setUniformf("lightColor", DEFAULT_LIGHT_COLOR);
		program.setUniformf("attenuation", attenuation);
		program.setUniformi("useShadow", useShadow ? 1 : 0);
		program.setUniformi("useNormals", useNormals ? 1 : 0);
		program.setUniformi("yInvert", flipY ? 1 : 0);
		program.end();

		return program;
	}

	@Override
	public void dispose() {
		this.background.dispose();
		this.stage.dispose();
		world.dispose();
		b2ddr.dispose();
		map.dispose();
		tmr.dispose();
		fxBatch.dispose();
		// TODO Auto-generated method stub
		
	}

}
