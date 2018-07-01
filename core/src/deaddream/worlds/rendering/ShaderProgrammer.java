package deaddream.worlds.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import deaddream.players.Player;
import deaddream.units.Unit;

public class ShaderProgrammer {
    
    boolean flipY = true;
	
	Matrix4 transform = new Matrix4();


	// position of our light
	final Vector3 DEFAULT_LIGHT_POS = new Vector3(50f, 50f, 0.07f);
	// the color of our light
	final Vector3 DEFAULT_LIGHT_COLOR = new Vector3(3f, 3f, 3f);
	// the ambient color (color to use when unlit)
	final Vector3 DEFAULT_AMBIENT_COLOR = new Vector3(0.7f, 0.7f, 0.7f);
	// the attenuation factor: x=constant, y=linear, z=quadratic
	final Vector3 DEFAULT_ATTENUATION = new Vector3(0.4f, 0.4f, 0.3f);
	// the ambient intensity (brightness to use when unlit)
	final float DEFAULT_AMBIENT_INTENSITY = 0.8f;
	final float DEFAULT_STRENGTH = 0.1f;
	
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
	
	public ShaderProgrammer() {
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
	
	public void update(Array<Player> players) {
		fxBatch.begin();
		
		// get y-down light position based on mouse/touch
		lightPos.x = -50;//Gdx.input.getX();
		lightPos.y = -50;//Gdx.graphics.getHeight() - Gdx.input.getY();
		
		
		// update our uniforms
		program.setUniformf("ambientIntensity", ambientIntensity);
		program.setUniformf("attenuation", attenuation);
		program.setUniformf("light", lightPos);
		program.setUniformi("useNormals", useNormals ? 1 : 0);
		program.setUniformi("useShadow", useShadow ? 1 : 0);
		program.setUniformf("strength", strength);
		fxBatch.end();
		setShadersToPlayers(players);
	}
	
	public void setShadersToUnits(Unit[] units) {
		for (Unit unit : units) {
			unit.setShaderProgram(program);
		}
	}
	
	public void setShadersToPlayers(Array<Player> players) {
		for (Player player : players) {
			//setShadersToUnits(players.items[i].getUnits());
			//players.items[i].isReady();
			Array<Unit> units = player.getUnits();//s.items[i].getUnits();
			for (Unit unit : units) {
				unit.setShaderProgram(program);
			}
		}
	}
	
	public ShaderProgram getShaderProgram() {
		return program;
	}
	
}
