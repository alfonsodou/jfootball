/**
 * 
 */
package org.javahispano.jfootball.client.application.animations;

import com.akjava.gwt.stats.client.Stats;
import com.akjava.gwt.three.client.gwt.materials.MeshLambertMaterialParameter;
import com.akjava.gwt.three.client.js.THREE;
import com.akjava.gwt.three.client.js.cameras.PerspectiveCamera;
import com.akjava.gwt.three.client.js.core.Clock;
import com.akjava.gwt.three.client.js.core.Object3D;
import com.akjava.gwt.three.client.js.lights.DirectionalLight;
import com.akjava.gwt.three.client.js.lights.Light;
import com.akjava.gwt.three.client.js.objects.Mesh;
import com.akjava.gwt.three.client.js.renderers.WebGLRenderer;
import com.akjava.gwt.three.client.js.scenes.Scene;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author alfonso
 *
 */
public class AnimationDemo extends AbstractAnimation {
	@Override
	public String getName() {
		return "geometries";
	}

	@Override
	public void animate(double timestamp) {
		renderer.render(scene, camera);
	}

	private WebGLRenderer renderer;
	private Scene scene;
	private PerspectiveCamera camera;
	private Stats stats;

	double SCREEN_WIDTH;
	double SCREEN_HEIGHT;

	

	Clock clock;
	private DirectionalLight light;
	private Object3D object;
	@Override
	public void init() {
	
		//setup panels
		//need focusPanel to get events
		FocusPanel focusPanel = new FocusPanel();
		getParent().add(focusPanel);
		
		/*
		 * no need make dummy html panel?
		HTMLPanel div=new HTMLPanel(""); 
		div.getElement().appendChild(renderer.getDomElement());
		focusPanel.add(div);
		*/
		
		int width=getParent().getOffsetWidth();
		int height=getParent().getOffsetHeight();
		
		
		//setup renderer
		renderer = THREE.WebGLRenderer();
		
		focusPanel.getElement().appendChild(renderer.getDomElement());
		
		renderer.setSize(width, height);
		renderer.setClearColor(0xffffff, 1);
		
		
		
		
		scene = THREE.Scene();
		camera = THREE.PerspectiveCamera(35,(double)width/height,.1,10000);
		scene.add(camera);
		
		camera.getPosition().setZ(20);
		camera.getPosition().setX(5);
		camera.getPosition().setY(5);
		
		
		final Mesh mesh=THREE.Mesh(THREE.BoxGeometry(5, 5, 5), 
				THREE.MeshLambertMaterial(MeshLambertMaterialParameter.create().color(0xff0000)));
		scene.add(mesh);
		
		final Light light=THREE.PointLight(0xffffff);
		light.setPosition(10, 0, 10);
		scene.add(light);
		
	}
	
	public void onWindowResize() {
		SCREEN_WIDTH = getWindowInnerWidth();
		SCREEN_HEIGHT = getWindowInnerHeight();
	
		//re read because of double
		camera.setAspect(SCREEN_WIDTH / SCREEN_HEIGHT);
		camera.updateProjectionMatrix();

		renderer.setSize( SCREEN_WIDTH , SCREEN_HEIGHT );
	
	}
	


}
