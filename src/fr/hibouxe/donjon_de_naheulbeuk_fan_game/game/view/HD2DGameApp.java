package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

/**
 * Moteur de Rendu Graphique HD-2D basé sur LibGDX et OpenGL.
 * Gère la boucle de rendu 3D (Game Loop 60 FPS), la caméra perspective et les Shaders.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class HD2DGameApp extends ApplicationAdapter {
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;

    @Override
    public void create() {
        // Initialisation de la Caméra 3D HD-2D (FOV 45 degrés, inclinaison plongée)
        camera = new PerspectiveCamera(45, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 10f, 8f);
        camera.lookAt(0f, 0f, 0f);
        camera.near = 0.1f;
        camera.far = 300f;
        camera.update();

        modelBatch = new ModelBatch();
    }

    @Override
    public void render() {
        // Effacement du tampon de couleur et de profondeur OpenGL (Bleu nuit donjon)
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.08f, 0.08f, 0.12f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();
    }

    @Override
    public void dispose() {
        if (modelBatch != null) modelBatch.dispose();
    }
}
