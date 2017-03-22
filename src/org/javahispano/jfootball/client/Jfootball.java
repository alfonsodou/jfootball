package org.javahispano.jfootball.client;

import java.util.HashMap;
import java.util.Map;

import org.javahispano.jfootball.client.application.animations.AnimationDemo;

import com.akjava.gwt.bvh.client.BoxData;
import com.akjava.gwt.three.client.js.core.Object3D;
import com.akjava.gwt.three.client.js.math.Vector3;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
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
	private DockLayoutPanel root;
	private AnimationDemo animationDemo;
	
	private Map<String, Vector3> boneSizeMap = new HashMap<String, Vector3>();
	private Object3D rootGroup, boneContainer, backgroundContainer;
	private Map<String, BoxData> boxDatas;


	@Override
	public void onModuleLoad() {
		root = new DockLayoutPanel(Unit.PX);
		
		RootLayoutPanel.get().add(root);

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {

				animationDemo = new AnimationDemo();
				animationDemo.start(getPanel());
			}
		});
	}

	public Panel getPanel() {
		return root;
	}

}
