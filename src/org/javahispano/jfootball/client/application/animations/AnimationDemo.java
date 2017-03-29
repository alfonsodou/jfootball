/**
 * 
 */
package org.javahispano.jfootball.client.application.animations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akjava.bvh.client.BVH;
import com.akjava.bvh.client.BVHNode;
import com.akjava.bvh.client.BVHParser;
import com.akjava.bvh.client.BVHParser.ParserListener;
import com.akjava.bvh.client.Vec3;
import com.akjava.gwt.bvh.client.BoxData;
import com.akjava.gwt.html5.client.InputRangeWidget;
import com.akjava.gwt.lib.client.IStorageControler;
import com.akjava.gwt.lib.client.LogUtils;
import com.akjava.gwt.lib.client.StorageControler;
import com.akjava.gwt.stats.client.Stats;
import com.akjava.gwt.three.client.gwt.GWTParamUtils;
import com.akjava.gwt.three.client.gwt.materials.LineBasicMaterialParameter;
import com.akjava.gwt.three.client.gwt.materials.MeshBasicMaterialParameter;
import com.akjava.gwt.three.client.gwt.materials.MeshLambertMaterialParameter;
import com.akjava.gwt.three.client.java.JClock;
import com.akjava.gwt.three.client.java.utils.GWTThreeUtils;
import com.akjava.gwt.three.client.js.THREE;
import com.akjava.gwt.three.client.js.cameras.PerspectiveCamera;
import com.akjava.gwt.three.client.js.core.Geometry;
import com.akjava.gwt.three.client.js.core.Object3D;
import com.akjava.gwt.three.client.js.extras.geometries.BoxGeometry;
import com.akjava.gwt.three.client.js.extras.geometries.SphereGeometry;
import com.akjava.gwt.three.client.js.lights.DirectionalLight;
import com.akjava.gwt.three.client.js.materials.MeshPhongMaterial;
import com.akjava.gwt.three.client.js.math.Vector3;
import com.akjava.gwt.three.client.js.objects.Line;
import com.akjava.gwt.three.client.js.objects.Mesh;
import com.akjava.gwt.three.client.js.renderers.WebGLRenderer;
import com.akjava.gwt.three.client.js.scenes.Scene;
import com.akjava.gwt.three.client.js.textures.Texture;
import com.akjava.lib.common.utils.Benchmark;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
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
		// renderer.render(scene, camera);
		render(timestamp);
	}

	public void render(double now) {
		camera.lookAt(scene.getPosition());

		renderer.render(scene, camera);
	}

	private WebGLRenderer renderer;
	private Scene scene;
	private PerspectiveCamera camera;
	private Stats stats;
	private Mesh sphere;

	double SCREEN_WIDTH;
	double SCREEN_HEIGHT;

	JClock clock = new JClock();
	private DirectionalLight light;
	private Object3D object;

	/* BVH */
	private Map<String, Vector3> boneSizeMap = new HashMap<String, Vector3>();
	private Object3D rootGroup, boneContainer, backgroundContainer;
	private Map<String, BoxData> boxDatas;
	private IStorageControler storageControler;
	private Map<String, Object3D> jointMap;
	Object3D boneRoot;
	private BVH bvh;
	private InputRangeWidget currentFrameRange;
	private List<Object3D> bodyMeshs = new ArrayList<Object3D>();
	double baseBoneSize = 0.4;
	String tmp = "";


	@Override
	public void init() {
		addResizedHandler();

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

		scene.setFog(THREE.Fog(0xcce0ff, 500, 10000));

		// camera

		camera = THREE.PerspectiveCamera(30, getWindowInnerWidth() / getWindowInnerHeight(), 1, 10000);
		camera.getPosition().setY(50);
		camera.getPosition().setZ(1500);
		scene.add(camera);

		// lights

		DirectionalLight light;
		// materials;

		scene.add(THREE.AmbientLight(0x666666));

		light = THREE.DirectionalLight(0xdfebff, 1.75);
		light.getPosition().set(50, 200, 100);
		light.getPosition().multiplyScalar(1.3);

		light.setCastShadow(true);
		// light.shadowCameraVisible = true;

		light.setShadowMapWidth(1024);
		// light.getShadow().getMapSize().setWidth(1024);
		light.setShadowMapHeight(1024);
		// light.getShadow().getMapSize().setHeight(1024);
		double d = 300;

		// DirectionalLight return use OrthographicCamera;

		/*
		 * light.getShadow().getCamera().gwtCastOrthographicCamera().setLeft(-d)
		 * ;
		 * light.getShadow().getCamera().gwtCastOrthographicCamera().setRight(d)
		 * ;
		 * light.getShadow().getCamera().gwtCastOrthographicCamera().setTop(d);
		 * light.getShadow().getCamera().gwtCastOrthographicCamera().setBottom(-
		 * d);
		 * 
		 * light.getShadow().getCamera().gwtCastOrthographicCamera().setFar(1000
		 * );
		 */

		scene.add(light);

		// sphere

		SphereGeometry ballGeo = THREE.SphereGeometry(60, 20, 20);// var ballGeo
																	// = new
																	// THREE.SphereGeometry(
																	// ballSize,
																	// 20, 20 );
		MeshPhongMaterial ballMaterial = THREE.MeshPhongMaterial(GWTParamUtils.MeshPhongMaterial().color(0xffffff));// var
																													// ballMaterial
																													// =
																													// new
																													// THREE.MeshPhongMaterial(
																													// {
																													// color:
																													// 0xffffff
																													// }
																													// );

		sphere = THREE.Mesh(ballGeo, ballMaterial);// sphere = new THREE.Mesh(
													// ballGeo, ballMaterial );
		sphere.setCastShadow(true);// sphere.castShadow = true;
		sphere.setReceiveShadow(true);// sphere.receiveShadow = true;
		scene.add(sphere);

		// ground

		Texture groundTexture = THREE.TextureLoader().load("textures/terrain/grasslight-big.jpg");// var
																									// groundTexture
																									// =
																									// THREE.ImageUtils.loadTexture(
																									// "textures/terrain/grasslight-big.jpg"
																									// );
		groundTexture.setWrapS(THREE.RepeatWrapping);// groundTexture.wrapS =
														// groundTexture.wrapT =
														// THREE.RepeatWrapping;
		groundTexture.setWrapT(THREE.RepeatWrapping);
		groundTexture.getRepeat().set(25, 25);// groundTexture.repeat.set( 25,
												// 25 );
		groundTexture.setAnisotropy(16);// groundTexture.anisotropy = 16;

		MeshPhongMaterial groundMaterial = THREE.MeshPhongMaterial(
				GWTParamUtils.MeshPhongMaterial().color(0xffffff).specular(0x111111).map(groundTexture));// var
																											// groundMaterial
																											// =
																											// new
																											// THREE.MeshPhongMaterial(
																											// {
																											// color:
																											// 0xffffff,
																											// specular:
																											// 0x111111,
																											// map:
																											// groundTexture
																											// }
																											// );

		Mesh mesh = THREE.Mesh(THREE.PlaneBufferGeometry(20000, 20000), groundMaterial);// var
																						// mesh
																						// =
																						// new
																						// THREE.Mesh(
																						// new
																						// THREE.PlaneBufferGeometry(
																						// 20000,
																						// 20000
																						// ),
																						// groundMaterial
																						// );
		mesh.getPosition().setY(-250);// mesh.position.y = -250;
		mesh.getRotation().setX(-Math.PI / 2);// mesh.rotation.x = - Math.PI /
												// 2;
		mesh.setReceiveShadow(true);// mesh.receiveShadow = true;
		scene.add(mesh);

		// poles

		BoxGeometry poleGeo = THREE.BoxGeometry(5, 375, 5);// var poleGeo = new
															// THREE.BoxGeometry(
															// 5, 375, 5 );
		MeshPhongMaterial poleMat = THREE
				.MeshPhongMaterial(GWTParamUtils.MeshPhongMaterial().color(0xffffff).specular(0x111111).shininess(100));// var
																														// poleMat
																														// =
																														// new
																														// THREE.MeshPhongMaterial(
																														// {
																														// color:
																														// 0xffffff,
																														// specular:
																														// 0x111111,
																														// shiness:
																														// 100
																														// }
																														// );

		mesh = THREE.Mesh(poleGeo, poleMat);// var mesh = new THREE.Mesh(
											// poleGeo, poleMat );
		mesh.getPosition().setX(-125);// mesh.position.x = -125;
		mesh.getPosition().setY(-62);// mesh.position.y = -62;
		mesh.setReceiveShadow(true);// mesh.receiveShadow = true;
		mesh.setCastShadow(true);// mesh.castShadow = true;
		scene.add(mesh);

		mesh = THREE.Mesh(poleGeo, poleMat);// var mesh = new THREE.Mesh(
											// poleGeo, poleMat );
		mesh.getPosition().setX(125);// mesh.position.x = 125;
		mesh.getPosition().setY(-62);// mesh.position.y = -62;
		mesh.setReceiveShadow(true);// mesh.receiveShadow = true;
		mesh.setCastShadow(true);// mesh.castShadow = true;
		scene.add(mesh);

		mesh = THREE.Mesh(THREE.BoxGeometry(255, 5, 5), poleMat);// var mesh =
																	// new
																	// THREE.Mesh(
																	// new
																	// THREE.BoxGeometry(
																	// 255, 5, 5
																	// ),
																	// poleMat
																	// );
		mesh.getPosition().setY(-250 + 750 / 2);// mesh.position.y = -250 +
												// 750/2;
		mesh.getPosition().setX(0);// mesh.position.x = 0;
		mesh.setReceiveShadow(true);// mesh.receiveShadow = true;
		mesh.setCastShadow(true);// mesh.castShadow = true;
		scene.add(mesh);

		BoxGeometry gg = THREE.BoxGeometry(10, 10, 10);// var gg = new
														// THREE.BoxGeometry(
														// 10, 10, 10 );
		mesh = THREE.Mesh(gg, poleMat);// var mesh = new THREE.Mesh( gg, poleMat
										// );
		mesh.getPosition().setY(-250);// mesh.position.y = -250;
		mesh.getPosition().setX(125);// mesh.position.x = 125;
		mesh.setReceiveShadow(true);// mesh.receiveShadow = true;
		mesh.setCastShadow(true);// mesh.castShadow = true;
		scene.add(mesh);

		mesh = THREE.Mesh(gg, poleMat);// var mesh = new THREE.Mesh( gg, poleMat
										// );
		mesh.getPosition().setY(-250);// mesh.position.y = -250;
		mesh.getPosition().setX(-125);// mesh.position.x = -125;
		mesh.setReceiveShadow(true);// mesh.receiveShadow = true;
		mesh.setCastShadow(true);// mesh.castShadow = true;
		scene.add(mesh);

		//
		renderer = THREE.WebGLRenderer(GWTParamUtils.WebGLRenderer().antialias(true));// renderer
																						// =
																						// new
																						// THREE.WebGLRenderer(
																						// {
																						// antialias:
																						// true
																						// }
																						// );
		renderer.setPixelRatio(GWTThreeUtils.getWindowDevicePixelRatio());
		renderer.setSize((int) getWindowInnerWidth(), (int) getWindowInnerHeight());
		renderer.setClearColor(scene.getFog().getColor());// renderer.setClearColor(
															// scene.fog.color
															// );

		focusPanel.getElement().appendChild(renderer.getDomElement());

		renderer.setGammaInput(true);// renderer.gammaInput = true;
		renderer.setGammaOutput(true);// renderer.gammaOutput = true;

		// renderer.setShadowMapEnabled(true);//renderer.shadowMapEnabled =
		// true;
		renderer.getShadowMap().setEnabled(true);
		//

		// Stats
		/*
		 * stats = Stats.create(); stats.setPosition(0, 0);
		 * focusPanel.getElement().appendChild(stats.domElement());
		 */

		// window.addEventListener( 'resize', onWindowResize, false );

		// sphere.setVisible(false);

		storageControler = new StorageControler();

		boxDatas = new HashMap<String, BoxData>();

		doLoad("14_08");
	}

	protected void doLoad(String itemText) {
		String[] g_n = itemText.split("_");
		loadBVH("bvhs/" + g_n[0] + "/" + itemText + ".bvh");
	}

	private void loadBVH(String path) {
		Benchmark.start("load");
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(path));
		try {
			builder.sendRequest(null, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {

					String bvhText = response.getText();
					// log("loaded:"+Benchmark.end("load"));
					// useless spend allmost time with request and spliting.
					parseBVH(bvhText);
				}

				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("load faild:");
				}
			});
		} catch (RequestException e) {
			LogUtils.log(e.getMessage());
			e.printStackTrace();
		}
	}

	private void parseBVH(String bvhText) {
		final BVHParser parser = new BVHParser();
		jointMap = new HashMap<String, Object3D>();

		parser.parseAsync(bvhText, new ParserListener() {

			@Override
			public void onSuccess(BVH bv) {
				setBVH(bv);
			}

			@Override
			public void onFaild(String message) {
				LogUtils.log(message);
			}
		});
	}

	private void setBVH(BVH bv) {
		bvh = bv;
		bvh.setSkips(skipFrames);

		BVHNode node = bvh.getHiearchy();

		if (boneRoot != null) {
			boneContainer.remove(boneRoot);
		}
		boneRoot = THREE.Object3D();
		boneContainer.add(boneRoot);

		// possible bone root is not 0
		boneRoot.setPosition(node.getOffset().getX(), node.getOffset().getY(), node.getOffset().getZ());

		doJoint(boneRoot, null, node);
		// GWT.log(tmp);
		int poseIndex = 0;
		GWT.log("f-size:" + bvh.getFrames());

		clock.update();
		updatePoseIndex(poseIndex);
		doPose(bvh, bvh.getFrameAt(poseIndex));
		currentFrameRange.setMax(bvh.getFrames() - 1);
	}

	public void doJoint(Object3D parent, BVHNode pNode, BVHNode node) {

		if (pNode != null) {
			LogUtils.log(pNode.getName() + "," + node.getName());
		} else {
			LogUtils.log(null + "," + node.getName());
		}

		GWT.log(node.getName() + "," + node.getOffset() + ",endsite:" + node.getEndSite());
		GWT.log(node.getChannels().toString());

		Object3D group = THREE.Object3D();
		Mesh mesh = THREE.Mesh(THREE.CubeGeometry(baseBoneSize, baseBoneSize, baseBoneSize),
				THREE.MeshLambertMaterial(MeshLambertMaterialParameter.create().color(0x00ff00)));
		group.add(mesh);
		mesh.setName(node.getName());
		group.setName(node.getName());
		// initial position
		group.setPosition(THREE.Vector3(node.getOffset().getX(), node.getOffset().getY(), node.getOffset().getZ()));
		jointMap.put(node.getName(), group);

		// create half
		Vector3 half = group.getPosition().clone();
		if (half.getX() != 0 || half.getY() != 0 || half.getY() != 0) {
			half.divideScalar(2);
			// Mesh hmesh=THREE.Mesh(THREE.CubeGeometry(.2,.2,.2),
			// THREE.MeshLambertMaterial().color(0xffffff).build());
			Mesh halfMesh = THREE.Mesh(THREE.CylinderGeometry(baseBoneSize / 4, baseBoneSize / 4, baseBoneSize / 2, 6),
					THREE.MeshLambertMaterial(MeshLambertMaterialParameter.create().color(0xffffff)));

			halfMesh.setPosition(half);

			if (pNode != null) {
				parent.add(halfMesh);
				bodyMeshs.add(halfMesh);

			}

			if (pNode != null) {
				tmp += pNode.getName() + ",1,1,1\n";

				// this is supported-bone structor.
				BoxData data = boxDatas.get(pNode.getName());//
				if (data != null) {
					halfMesh.setScale(data.getScaleX(), data.getScaleY(), data.getScaleZ());
					halfMesh.getRotation().setZ(Math.toRadians(data.getRotateZ()));
				}
			}

		}

		// line
		Line lineMesh = createLine(new Vec3(), node.getOffset());

		if (pNode != null) {
			parent.add(lineMesh);
		}

		if (node.getEndSite() != null) {
			Mesh end = THREE.Mesh(THREE.CubeGeometry(baseBoneSize / 4, baseBoneSize / 4, baseBoneSize / 4),
					THREE.MeshBasicMaterial(MeshBasicMaterialParameter.create().color(0x008800)));
			end.setPosition(
					THREE.Vector3(node.getEndSite().getX(), node.getEndSite().getY(), node.getEndSite().getZ()));
			group.add(end);
			Geometry lineG = THREE.Geometry();
			lineG.vertices().push(THREE.Vector3(0, 0, 0));
			lineG.vertices()
					.push(THREE.Vector3(node.getEndSite().getX(), node.getEndSite().getY(), node.getEndSite().getZ()));
			Line line = THREE.Line(lineG, THREE.LineBasicMaterial(LineBasicMaterialParameter.create().color(0)));
			group.add(line);

			Vector3 half2 = end.getPosition().clone();
			if (half2.getX() != 0 || half2.getY() != 0 || half2.getY() != 0) {
				half2.divideScalar(2);
				// Mesh hmesh=THREE.Mesh(THREE.CubeGeometry(.1,.1,.1),
				// THREE.MeshLambertMaterial().color(0xffffff).build());
				Mesh hmesh = THREE.Mesh(THREE.CylinderGeometry(baseBoneSize / 4, baseBoneSize / 4, baseBoneSize / 2, 6),
						THREE.MeshLambertMaterial(MeshLambertMaterialParameter.create().color(0xffffff)));

				hmesh.setPosition(half2);
				group.add(hmesh);

				tmp += node.getName() + ",1,1,1\n";

				// this is special treatment for bone.
				BoxData data = boxDatas.get(node.getName());
				if (data != null) {
					hmesh.setScale(data.getScaleX(), data.getScaleY(), data.getScaleZ());
				}
				if (node.getName().equals("Head")) {// why need this?
					// hmesh.getPosition().setZ(hmesh.getPosition().getZ()+0.5);
				}

				bodyMeshs.add(hmesh);

			}
		}
		parent.add(group);
		List<BVHNode> joints = node.getJoints();
		if (joints != null) {
			for (BVHNode joint : joints) {
				doJoint(group, node, joint);
			}
		}

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
