/**
 * 
 */
package org.javahispano.jfootball.client.application.animations;

import com.akjava.gwt.stats.client.Stats;
import com.akjava.gwt.three.client.gwt.GWTParamUtils;
import com.akjava.gwt.three.client.java.utils.GWTThreeUtils;
import com.akjava.gwt.three.client.js.THREE;
import com.akjava.gwt.three.client.js.cameras.PerspectiveCamera;
import com.akjava.gwt.three.client.js.core.Clock;
import com.akjava.gwt.three.client.js.core.Object3D;
import com.akjava.gwt.three.client.js.extras.geometries.BoxGeometry;
import com.akjava.gwt.three.client.js.extras.geometries.SphereGeometry;
import com.akjava.gwt.three.client.js.lights.DirectionalLight;
import com.akjava.gwt.three.client.js.materials.MeshPhongMaterial;
import com.akjava.gwt.three.client.js.objects.Mesh;
import com.akjava.gwt.three.client.js.renderers.WebGLRenderer;
import com.akjava.gwt.three.client.js.scenes.Scene;
import com.akjava.gwt.three.client.js.textures.Texture;
import com.google.gwt.user.client.ui.FocusPanel;

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
		//renderer.render(scene, camera);
		render(timestamp);
	}

	public void render(double now) {
		camera.lookAt( scene.getPosition() );

		renderer.render(scene, camera);
	}

	private WebGLRenderer renderer;
	private Scene scene;
	private PerspectiveCamera camera;
	private Stats stats;
	private Mesh sphere;

	double SCREEN_WIDTH;
	double SCREEN_HEIGHT;

	Clock clock;
	private DirectionalLight light;
	private Object3D object;

	@Override
	public void init() {
		FocusPanel focusPanel = new FocusPanel();
		getParent().add(focusPanel);

		/*
		 * no need make dummy html panel? HTMLPanel div=new HTMLPanel("");
		 * div.getElement().appendChild(renderer.getDomElement());
		 * focusPanel.add(div);
		 */

		int width = getParent().getOffsetWidth();
		int height = getParent().getOffsetHeight();

		// scene
		scene = THREE.Scene();

		scene.setFog(THREE.Fog( 0xcce0ff, 500, 10000 ));
		
		// camera

		camera = THREE.PerspectiveCamera( 30, getWindowInnerWidth() / getWindowInnerHeight(), 1, 10000 );
		camera.getPosition().setY(50) ;
		camera.getPosition().setZ(1500);
		scene.add( camera );
		
		// lights

		DirectionalLight light;
		//materials;

		scene.add(THREE.AmbientLight( 0x666666 ) );
		
		light = THREE.DirectionalLight( 0xdfebff, 1.75 );
		light.getPosition().set( 50, 200, 100 );
		light.getPosition().multiplyScalar( 1.3 );

		light.setCastShadow(true);
		//light.shadowCameraVisible = true;

		
		light.setShadowMapWidth(1024);
		//light.getShadow().getMapSize().setWidth(1024);
		light.setShadowMapHeight(1024);
		//light.getShadow().getMapSize().setHeight(1024);
		double d = 300;

		
		//DirectionalLight return use OrthographicCamera;
		
		/*light.getShadow().getCamera().gwtCastOrthographicCamera().setLeft(-d);
		light.getShadow().getCamera().gwtCastOrthographicCamera().setRight(d);
		light.getShadow().getCamera().gwtCastOrthographicCamera().setTop(d);
		light.getShadow().getCamera().gwtCastOrthographicCamera().setBottom(-d);
		
		light.getShadow().getCamera().gwtCastOrthographicCamera().setFar(1000);
		*/
		

		scene.add( light );
		
		// sphere

		SphereGeometry ballGeo = THREE.SphereGeometry( 60, 20, 20 );//var ballGeo = new THREE.SphereGeometry( ballSize, 20, 20 );
		MeshPhongMaterial ballMaterial = THREE.MeshPhongMaterial( GWTParamUtils.MeshPhongMaterial().color(0xffffff));//		var ballMaterial = new THREE.MeshPhongMaterial( { color: 0xffffff } );

		sphere = THREE.Mesh( ballGeo, ballMaterial );//		sphere = new THREE.Mesh( ballGeo, ballMaterial );
		sphere.setCastShadow(true);//		sphere.castShadow = true;
		sphere.setReceiveShadow(true);//		sphere.receiveShadow = true;
		scene.add( sphere );

		
		// ground

		Texture groundTexture = THREE.TextureLoader().load( "textures/terrain/grasslight-big.jpg" );//var groundTexture = THREE.ImageUtils.loadTexture( "textures/terrain/grasslight-big.jpg" );
		groundTexture.setWrapS(THREE.RepeatWrapping);//groundTexture.wrapS = groundTexture.wrapT = THREE.RepeatWrapping;
		groundTexture.setWrapT(THREE.RepeatWrapping);
		groundTexture.getRepeat().set( 25, 25 );//groundTexture.repeat.set( 25, 25 );
		groundTexture.setAnisotropy(16);//groundTexture.anisotropy = 16;
		
		MeshPhongMaterial groundMaterial = THREE.MeshPhongMaterial( GWTParamUtils.MeshPhongMaterial().color(0xffffff).specular(0x111111).map(groundTexture) );//var groundMaterial = new THREE.MeshPhongMaterial( { color: 0xffffff, specular: 0x111111, map: groundTexture } );

		Mesh mesh = THREE.Mesh( THREE.PlaneBufferGeometry( 20000, 20000 ), groundMaterial );//var mesh = new THREE.Mesh( new THREE.PlaneBufferGeometry( 20000, 20000 ), groundMaterial );
		mesh.getPosition().setY(-250);//mesh.position.y = -250;
		mesh.getRotation().setX(- Math.PI / 2);//mesh.rotation.x = - Math.PI / 2;
		mesh.setReceiveShadow(true);//mesh.receiveShadow = true;
		scene.add( mesh );

		// poles

		BoxGeometry poleGeo = THREE.BoxGeometry( 5, 375, 5 );//var poleGeo = new THREE.BoxGeometry( 5, 375, 5 );
		MeshPhongMaterial poleMat = THREE.MeshPhongMaterial( GWTParamUtils.MeshPhongMaterial().color(0xffffff).specular(0x111111).shininess(100) );//var poleMat = new THREE.MeshPhongMaterial( { color: 0xffffff, specular: 0x111111, shiness: 100 } );

		mesh = THREE.Mesh( poleGeo, poleMat );//var mesh = new THREE.Mesh( poleGeo, poleMat );
		mesh.getPosition().setX(-125);//mesh.position.x = -125;
		mesh.getPosition().setY(-62);//mesh.position.y = -62;
		mesh.setReceiveShadow(true);//mesh.receiveShadow = true;
		mesh.setCastShadow(true);//mesh.castShadow = true;
		scene.add( mesh );

		mesh = THREE.Mesh( poleGeo, poleMat );//var mesh = new THREE.Mesh( poleGeo, poleMat );
		mesh.getPosition().setX(125);//mesh.position.x = 125;
		mesh.getPosition().setY(-62);//mesh.position.y = -62;
		mesh.setReceiveShadow(true);//mesh.receiveShadow = true;
		mesh.setCastShadow(true);//mesh.castShadow = true;
		scene.add( mesh );

		mesh = THREE.Mesh( THREE.BoxGeometry( 255, 5, 5 ), poleMat );//var mesh = new THREE.Mesh( new THREE.BoxGeometry( 255, 5, 5 ), poleMat );
		mesh.getPosition().setY(-250 + 750/2);//mesh.position.y = -250 + 750/2;
		mesh.getPosition().setX(0);//mesh.position.x = 0;
		mesh.setReceiveShadow(true);//mesh.receiveShadow = true;
		mesh.setCastShadow(true);//mesh.castShadow = true;
		scene.add( mesh );

		BoxGeometry gg = THREE.BoxGeometry( 10, 10, 10 );//var gg = new THREE.BoxGeometry( 10, 10, 10 );
		mesh = THREE.Mesh( gg, poleMat );//var mesh = new THREE.Mesh( gg, poleMat );
		mesh.getPosition().setY(-250);//mesh.position.y = -250;
		mesh.getPosition().setX(125);//mesh.position.x = 125;
		mesh.setReceiveShadow(true);//mesh.receiveShadow = true;
		mesh.setCastShadow(true);//mesh.castShadow = true;
		scene.add( mesh );

		mesh = THREE.Mesh( gg, poleMat );//var mesh = new THREE.Mesh( gg, poleMat );
		mesh.getPosition().setY(-250);//mesh.position.y = -250;
		mesh.getPosition().setX(-125);//mesh.position.x = -125;
		mesh.setReceiveShadow(true);//mesh.receiveShadow = true;
		mesh.setCastShadow(true);//mesh.castShadow = true;
		scene.add( mesh );

		//
		renderer = THREE.WebGLRenderer( GWTParamUtils.WebGLRenderer().antialias(true) );//renderer = new THREE.WebGLRenderer( { antialias: true } );
		renderer.setPixelRatio( GWTThreeUtils.getWindowDevicePixelRatio() );
		renderer.setSize((int)getWindowInnerWidth()  , (int)getWindowInnerHeight());
		renderer.setClearColor( scene.getFog().getColor() );//renderer.setClearColor( scene.fog.color );

		
		focusPanel.getElement().appendChild(renderer.getDomElement());

		renderer.setGammaInput(true);//renderer.gammaInput = true;
		renderer.setGammaOutput(true);//renderer.gammaOutput = true;

		//renderer.setShadowMapEnabled(true);//renderer.shadowMapEnabled = true;
		renderer.getShadowMap().setEnabled(true);
		//

		stats = Stats.create();
		stats.setPosition(0, 0);
		focusPanel.getElement().appendChild(stats.domElement());


		//window.addEventListener( 'resize', onWindowResize, false );

		//sphere.setVisible(false);
	}

	public void onWindowResize() {
		SCREEN_WIDTH = getWindowInnerWidth();
		SCREEN_HEIGHT = getWindowInnerHeight();

		// re read because of double
		camera.setAspect(SCREEN_WIDTH / SCREEN_HEIGHT);
		camera.updateProjectionMatrix();

		renderer.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

	}

}
