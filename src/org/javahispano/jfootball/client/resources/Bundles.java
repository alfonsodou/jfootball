package org.javahispano.jfootball.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface Bundles extends ClientBundle {
public static Bundles INSTANCE=GWT.create(Bundles.class);

	@Source("standard_cmu.txt")
	TextResource standard_cmu();
	
	@Source("small_cmu.txt")
	TextResource small_cmu();
}
