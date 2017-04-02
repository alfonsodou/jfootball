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
import com.akjava.bvh.client.Channels;
import com.akjava.bvh.client.NameAndChannel;
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
import com.akjava.gwt.three.client.js.lights.SpotLight;
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
	private int skipFrames = 0;

	@Override
	public void init() {
		addResizedHandler();

		FocusPanel focusPanel = new FocusPanel();
		getParent().add(focusPanel);

		int width = getParent().getOffsetWidth();
		int height = getParent().getOffsetHeight();

		// scene
		scene = THREE.Scene();

		// camera

		camera = THREE.PerspectiveCamera(45, getWindowInnerWidth() / getWindowInnerHeight(), 0.1, 1000);
		camera.getPosition().setX(50);
		camera.getPosition().setY(60);
		camera.getPosition().setZ(80);
		camera.lookAt(scene.getPosition());
		scene.add(camera);

		// lights

		DirectionalLight light;
		// materials;

		scene.add(THREE.AmbientLight(0x0c0c0c));

		light = THREE.DirectionalLight(0xdfebff, 1.75);
		light.getPosition().set(-40, 60, -10);
		light.getPosition().multiplyScalar(1.3);

		light.setCastShadow(true);

		scene.add(light);

		// sphere

		SphereGeometry ballGeo = THREE.SphereGeometry(1, 20, 20);
		MeshPhongMaterial ballMaterial = THREE.MeshPhongMaterial(GWTParamUtils.MeshPhongMaterial().color(0xffffff));

		sphere = THREE.Mesh(ballGeo, ballMaterial);
		sphere.setCastShadow(true);
		sphere.setReceiveShadow(true);
		sphere.getPosition().setY(0.6);
		scene.add(sphere);

		// ground

		Texture groundTexture = THREE.TextureLoader().load("textures/terrain/grasslight-big.jpg");
		groundTexture.setWrapS(THREE.RepeatWrapping);
		groundTexture.setWrapT(THREE.RepeatWrapping);
		groundTexture.getRepeat().set(25, 25);
		groundTexture.setAnisotropy(16);

		MeshPhongMaterial groundMaterial = THREE.MeshPhongMaterial(
				GWTParamUtils.MeshPhongMaterial().color(0xffffff).specular(0x111111).map(groundTexture));
		Mesh mesh = THREE.Mesh(THREE.PlaneBufferGeometry(500, 500), groundMaterial);
		mesh.getRotation().setX(-Math.PI / 2);
		mesh.setReceiveShadow(true);
		scene.add(mesh);

		// poles

		BoxGeometry poleGeo = THREE.BoxGeometry(1, 35, 1);
		MeshPhongMaterial poleMat = THREE
				.MeshPhongMaterial(GWTParamUtils.MeshPhongMaterial().color(0xffffff).specular(0x111111).shininess(100));

		mesh = THREE.Mesh(poleGeo, poleMat);
		mesh.getPosition().setX(-25);
		mesh.getPosition().setY(17.5);
		mesh.setReceiveShadow(true);
		mesh.setCastShadow(true);
		scene.add(mesh);

		mesh = THREE.Mesh(poleGeo, poleMat);
		mesh.getPosition().setX(25);
		mesh.getPosition().setY(17.5);
		mesh.setReceiveShadow(true);
		mesh.setCastShadow(true);
		scene.add(mesh);

		mesh = THREE.Mesh(THREE.BoxGeometry(50, 1, 1), poleMat);
		mesh.getPosition().setY(35);
		mesh.getPosition().setX(0);
		mesh.setReceiveShadow(true);
		mesh.setCastShadow(true);
		scene.add(mesh);

		boneContainer = THREE.Object3D();
		scene.add(boneContainer);

		storageControler = new StorageControler();

		boxDatas = new HashMap<String, BoxData>();

		doLoad("14_08");

		boneContainer.getPosition().setY(17);

		renderer = THREE.WebGLRenderer(GWTParamUtils.WebGLRenderer().antialias(true));
		renderer.setPixelRatio(GWTThreeUtils.getWindowDevicePixelRatio());
		renderer.setSize((int) getWindowInnerWidth(), (int) getWindowInnerHeight());

		focusPanel.getElement().appendChild(renderer.getDomElement());

		renderer.setGammaInput(true);
		renderer.setGammaOutput(true);

		renderer.getShadowMap().setEnabled(true);
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
		int poseIndex = 1;
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
		Mesh mesh = THREE.Mesh(THREE.BoxGeometry(baseBoneSize, baseBoneSize, baseBoneSize),
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
			Mesh end = THREE.Mesh(THREE.BoxGeometry(baseBoneSize / 4, baseBoneSize / 4, baseBoneSize / 4),
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

	public Line createLine(Vec3 from, Vec3 to) {
		Geometry lineG = THREE.Geometry();
		lineG.vertices().push(THREE.Vector3(from.getX(), from.getY(), from.getY()));
		lineG.vertices().push(THREE.Vector3(to.getX(), to.getY(), to.getZ()));
		Line line = THREE.Line(lineG, THREE.LineBasicMaterial(LineBasicMaterialParameter.create().color(0)));
		return line;
	}

	private void updatePoseIndex(int index) {
		if (index >= bvh.getFrames()) {
			if (bvh.getFrames() != 1) {// 1 frame pose usually happen
				LogUtils.log("invalid frame at:" + index + " of " + bvh.getFrames());
			}
			return;
		}
		// poseIndex=index;
		currentFrameRange.setValue(index);
		doPose(bvh, bvh.getFrameAt(index));
	}

	private void doPose(BVH bvh, double[] vs) {
		Object3D oldTarget = null;
		String lastOrder = null;
		for (int i = 0; i < vs.length; i++) {
			NameAndChannel nchannel = bvh.getNameAndChannels().get(i);
			lastOrder = nchannel.getOrder();
			Object3D target = jointMap.get(nchannel.getName());

			switch (nchannel.getChannel()) {
			case Channels.XROTATION:
				target.getRotation().setX(Math.toRadians(vs[i]));

				break;
			case Channels.YROTATION:
				target.getRotation().setY(Math.toRadians(vs[i]));
				break;
			case Channels.ZROTATION:
				target.getRotation().setZ(Math.toRadians(vs[i]));
				break;
			case Channels.XPOSITION:
				// if (translatePosition.getValue()) {
				// target.getPosition().setX(vs[i]);
				// } else {
				target.getPosition().setX(0);
				// }
				break;
			case Channels.YPOSITION:
				// if (translatePosition.getValue()) {
				// target.getPosition().setY(vs[i]);
				// } else {
				target.getPosition().setY(0);
				// }
				break;
			case Channels.ZPOSITION:
				// if (translatePosition.getValue()) {
				// target.getPosition().setZ(vs[i]);
				// } else {
				target.getPosition().setZ(0);
				// }
				break;
			}

			if (oldTarget != null && oldTarget != target) {
				doRotation(oldTarget, lastOrder);
			}
			oldTarget = target;
		}
		doRotation(oldTarget, lastOrder);// do last one
	}

	private void doRotation(Object3D target, String lastOrder) {
		// log(target.getName()+",order="+lastOrder+"
		// "+ThreeLog.get(GWTThreeUtils.radiantToDegree(target.getRotation())));
		target.getRotation().setOrder(lastOrder);
		/*
		 * Matrix4 mx=THREE.Matrix4(); mx.setRotationFromEuler(vec, lastOrder);
		 * vec.setRotationFromMatrix(mx);//in this here,miss rotation because of
		 * over 90?
		 */

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
