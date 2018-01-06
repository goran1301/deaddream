package deaddream.renderers;

import java.util.ArrayList;

/**
 * For rendering main menu
 * 
 * @author goran
 *
 */
public class MenuRenderer implements RenderInterface {
    
	private ArrayList<RenderInterface> objectsToRender;
	
	public MenuRenderer(ArrayList<RenderInterface> objectsToRender) {
		this.objectsToRender = objectsToRender;
	}
	
	public MenuRenderer() {
		this.objectsToRender = new ArrayList<RenderInterface>();
	}
	
	public void render() {
		for (int i = 0; i < objectsToRender.size(); i++) {
			objectsToRender.get(i).render();
		}
	}
	
}
