package org.javahispano.jfootball.client;

import com.akjava.gwt.stats.client.Stats;
import com.akjava.gwt.three.client.gwt.renderers.WebGLRendererParameter;
import com.akjava.gwt.three.client.js.THREE;
import com.akjava.gwt.three.client.js.cameras.Camera;
import com.akjava.gwt.three.client.js.renderers.WebGLRenderer;
import com.akjava.gwt.three.client.js.renderers.WebGLRenderer.WebGLCanvas;
import com.akjava.gwt.three.client.js.scenes.Scene;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Jfootball implements EntryPoint {

	private WebGLRenderer renderer;

	protected Timer timer;
	protected Stats stats;

	protected WebGLCanvas canvas;

	private VerticalPanel main;

	protected int canvasWidth, canvasHeight;

	protected Camera camera;
	protected int cameraX, cameraY, cameraZ;
	protected int screenWidth, screenHeight;
	protected long mouseLast;
	protected int tmpZoom;
	protected Scene scene;
	protected int defaultZoom = 2;
	protected int minCamera = 5;

	public WebGLCanvas getCanvas() {
		return canvas;
	}

	@Override
	public void onModuleLoad() {
		int width = Window.getClientWidth();
		int height = Window.getClientHeight();

		renderer = THREE.WebGLRenderer(WebGLRendererParameter.create().preserveDrawingBuffer(true));
		renderer.setSize(width, height);

		canvas = new WebGLCanvas(renderer);
		canvas.setClearColorHex(0);

		canvas.setWidth("100%");
		canvas.setHeight("100%");
		RootLayoutPanel.get().add(canvas);

		canvasWidth = width;
		canvasHeight = height;
		initialize(renderer, width, height);

		stats = Stats.insertStatsToRootPanel();
		timer = new Timer() {
			public void run() {
				update(renderer);
				stats.update();
			}
		};

		if (!GWT.isScript()) {
			timer.scheduleRepeating(100);
		} else {
			timer.scheduleRepeating(1000 / 60);
		}

		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				int w = canvas.getOffsetWidth();
				int h = canvas.getOffsetHeight();
				canvasWidth = w;
				canvasHeight = h;
				resized(w, h);
				renderer.setSize(w, h);
			}
		});
	}

	public void initialize(WebGLRenderer renderer, int width, int height) {
		cameraZ = 100;
		screenWidth = width;
		screenHeight = height;
		// renderer.setClearColorHex(0x333333, 1);
		canvas.setClearColorHex(0x333333);
		scene = THREE.Scene();
		createCamera(scene, width, height);

		initializeOthers(renderer);
	}

	public void update(WebGLRenderer renderer) {
		beforeUpdate(renderer);
		camera.getPosition().set(cameraX, cameraY, cameraZ);
		renderer.render(scene, camera);
	}

	private void createCamera(Scene scene, int width, int height) {
		if (camera != null) {
			// TODO find update way.
			scene.remove(camera);
		}
		camera = THREE.PerspectiveCamera(35, (double) width / height, 1, 6000);
		// camera.getPosition().set(0, 0, cameraZ);
		scene.add(camera);
	}

	public void resized(int width, int height) {
		if (width == 0 || height == 0) {// ignore 0 resize.
			return;
		}
		screenWidth = width;
		screenHeight = height;
		createCamera(scene, width, height);
	}

}
