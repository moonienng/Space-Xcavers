package com.juridico;
import static com.almasb.fxgl.dsl.FXGL.*;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.texture.Texture;

import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.util.Map;


public class SpaceXcaversApp extends GameApplication {

    public enum Type
    {
        JOGADOR, INIMIGO, TIRO
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
       FXGL.onKey(KeyCode.A, () -> jogador.translateX(-3));
       FXGL.onKey(KeyCode.D, () -> jogador.translateX(3));
       FXGL.onKey(KeyCode.LEFT, () -> jogador.translateX(-3));
       FXGL.onKey(KeyCode.RIGHT, () -> jogador.translateX(3));

       FXGL.onKeyDown(KeyCode.SPACE, () -> {
        Point2D playerCenter = jogador.getCenter();
        
        FXGL.spawn("tiro", new SpawnData(playerCenter.getX(), playerCenter.getY())
                .put("dir", new Point2D(0, -1))); // (1, 0) = direita; (0, -1) = cima
    });
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) 
    {
        vars.put("pixelsMoved", 0);
    }


    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new GameFactory());
        for(int x = 0; x<11 ; x++)
        {
            for(int y = 0; y<5; y++)
            {
                    spawnInimigo(x * 16,y * 8);
            }
        }
        spawnJogador(89.5,195);
    }

    @Override
    protected void initUI() {
        
    }
    private void spawnInimigo(double x, double y) {
    Texture texture = getAssetLoader().loadTexture("estrela.gif");
    texture.setFitWidth(15);
    texture.setFitHeight(17);

    entityBuilder()
            .type(Type.INIMIGO)
            .at(x, y)
            .view(texture)
            .with(new CollidableComponent(true))
            .with(new InimigoMovimentacao())
            .buildAndAttach();
}
    private Entity jogador;

    private void spawnJogador(double x, double y)
    {
        Texture texture = getAssetLoader().loadTexture("PinguimMC.gif");
    texture.setFitWidth(45);
    texture.setFitHeight(51);

    jogador = entityBuilder()
            .type(Type.JOGADOR)
            .at(x, y)
            .view(texture)
            .with(new CollidableComponent(true))
            .buildAndAttach();
    }

    public class GameFactory implements EntityFactory
    {
        @Spawns("tiro")
        public Entity spawnTiro(SpawnData data)
        {
            Texture texture = getAssetLoader().loadTexture("Pikareta5FPS.gif");
            texture.setFitHeight(25);
            texture.setFitWidth(25);
            Point2D direction = data.get("dir");

        return FXGL.entityBuilder(data)
                .type(Type.TIRO)
                .viewWithBBox(texture) // Visual + Hitbox automática
                .with(new ProjectileComponent(direction, 50))    // Direção e velocidade (px/s)
                .with(new OffscreenCleanComponent())              // Destrói ao sair da tela
                .collidable()                                     // Habilita colisões
                .build();
    }
    }
    public static void main(String[] args) {
        launch(args);
    }
}