/**
 * 
 */
package org.javahispano.jfootball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * @author alfonso
 *
 */
public class Assets {
    public static Skin skin;
    public static AssetManager assetManager;

    public Assets() {
        skin = new Skin();
        FileHandle fileHandle = Gdx.files.internal("data/uiskin.json");
        FileHandle atlasFile = fileHandle.sibling("uiskin.atlas");
        if (atlasFile.exists()) {
            skin.addRegions(new TextureAtlas(atlasFile));
        }
        skin.load(fileHandle);

        assetManager = new AssetManager();
    }

    public static void dispose() {
        skin.dispose();
        assetManager.dispose();
    }
}
