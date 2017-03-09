package org.javahispano.jfootball.client;

import org.javahispano.jfootball.client.application.animations.AnimationDemo;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Jfootball implements EntryPoint {
	private DockLayoutPanel center;
	private AnimationDemo animationDemo;

	@Override
	public void onModuleLoad() {
		DockLayoutPanel root = new DockLayoutPanel(Unit.PX);

		RootLayoutPanel.get().add(root);

		center = new DockLayoutPanel(Unit.PX);

		// left-side
		VerticalPanel side = new VerticalPanel();
		side.setWidth("100%");
		side.setStylePrimaryName("side");
		side.setSpacing(4);

		Label title = new Label("GWT-three.js Examples");
		title.setStylePrimaryName("header");

		side.add(title);

		Label webgl = new Label("webgl");
		webgl.setStylePrimaryName("subheader");
		webgl.setWidth("100%");
		side.add(webgl);
		root.addWest(side, 300);
		root.add(center);

		Label links=new Label("Links");
		links.setStylePrimaryName("subheader");
		side.add(links);
		side.add(new Anchor("Three.js(github)", "https://github.com/mrdoob/three.js/"));
		side.add(new Anchor("Three.js origin examples", "http://threejs.org/examples/"));
		side.add(new Anchor("GWT-Three.js(github)", "https://github.com/akjava/gwt-three.js-test"));
		side.add(new Anchor("GWT-Three.js old examples", "http://akjava.github.io/gwt-three.js-test/ThreeTest.html"));
		side.add(new Anchor("GWT", "http://www.gwtproject.org/"));
		
		animationDemo = new AnimationDemo();
		animationDemo.start(getPanel());
	}
	
	public Panel getPanel() {
		return center;
	}

}
