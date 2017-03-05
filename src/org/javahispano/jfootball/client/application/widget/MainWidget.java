/**
 * 
 */
package org.javahispano.jfootball.client.application.widget;

import com.akjava.gwt.three.client.java.ui.CameraMoveWidget;
import com.akjava.gwt.three.client.java.ui.CameraRotationWidget;
import com.akjava.gwt.three.client.js.THREE;
import com.akjava.gwt.three.client.js.renderers.WebGLRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author alfonso
 *
 */
public class MainWidget extends Composite {

	public static CameraMoveWidget cameraMove;
	public static CameraRotationWidget cameraRotation;
	private static MainWidgetUiBinder uiBinder = GWT.create(MainWidgetUiBinder.class);

	interface MainWidgetUiBinder extends UiBinder<Widget, MainWidget> {
	}

	int width = 500, height = 500;
	private int rendererType;
	public static final int RENDERER_WEBGL = 0;
	public static final int RENDERER_CANVAS = 1;
	public static final int RENDERER_CSS3D = 2;

	@UiField
	VerticalPanel main;
	@UiField
	VerticalPanel side;
	@UiField
	VerticalPanel controler;

	private WebGLRenderer renderer;

	private FocusPanel focusPanel;
	private ScrollPanel howToPanel;
	private VerticalPanel controlPanel;

	public MainWidget() {
		renderer = THREE.WebGLRenderer();
		rendererType = RENDERER_WEBGL;
		if (renderer != null) {
			renderer.gwtSetType("webgl");
		}
		
		CameraMoveWidget cameraMove = new CameraMoveWidget();
		cameraMove.setVisible(false);// useless
		controler.add(cameraMove);

		MainWidget.cameraMove = cameraMove;

		CameraRotationWidget cameraRotation = new CameraRotationWidget();
		cameraRotation.setVisible(false);// useless
		controler.add(cameraRotation);

		MainWidget.cameraRotation = cameraRotation;

		if (renderer != null) {
			HTMLPanel div = new HTMLPanel("");
			div.getElement().appendChild(renderer.getDomElement());
			focusPanel = new FocusPanel();
			focusPanel.add(div);
		}

		getMain().add(focusPanel);
	}

	public VerticalPanel getMain() {
		return main;
	}
	
	public WebGLRenderer getRenderer() {
		return renderer;
	}
	
	public FocusPanel getFocusPanel() {
		return focusPanel;
	}
}
