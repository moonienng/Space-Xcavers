package com.juridico;
import static com.almasb.fxgl.dsl.FXGL.*;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.texture.Texture;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.util.Map;


public class SpaceXcaversApp extends GameApplication {

    public enum Type
    {
        PLAYER, INIMIGO, BULLET
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(600);
        settings.setHeight(600);
        settings.setTitle("SpaceXcavers");
        settings.setVersion("0.1");
        settings.setIntroEnabled(false);
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setMenuKey(KeyCode.P);
    }

    @Override
    protected void initInput() 
    {
       
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) 
    {
        vars.put("pixelsMoved", 0);
    }


    @Override
    protected void initGame() {
        for(int x = 0; x<5 ; x++)
        {
            for(int y = 0; y<8; y++)
            {
                    spawnInimigo(x * 40 + 10,y * 40 + 10);
            }
        }
    }

    @Override
    protected void initUI() {
        
    }
    private void spawnInimigo(double x, double y) {
    Texture texture = getAssetLoader().loadTexture("estrela.gif");
    texture.setFitWidth(40);
    texture.setFitHeight(40);

    entityBuilder()
            .type(Type.INIMIGO)
            .at(x, y)
            .view(texture)
            .with(new CollidableComponent(true))
            .buildAndAttach();
}
    public static void main(String[] args) {
        launch(args);
    }
}