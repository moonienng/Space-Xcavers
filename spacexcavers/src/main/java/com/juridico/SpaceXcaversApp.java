package com.juridico;

import static com.almasb.fxgl.dsl.FXGL.*;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.util.Duration;
import com.juridico.Entidades.Type;

import java.util.List;
import java.util.Map;

public class SpaceXcaversApp extends GameApplication {

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
    protected void initInput() {
        FXGL.onKey(KeyCode.A, () -> moverJogador(-3));
        FXGL.onKey(KeyCode.D, () -> moverJogador(3));
        FXGL.onKey(KeyCode.LEFT, () -> moverJogador(-3));
        FXGL.onKey(KeyCode.RIGHT, () -> moverJogador(3));

        FXGL.onKeyDown(KeyCode.SPACE, () -> {
            Entity jogador = getGameWorld().getSingleton(Type.JOGADOR);
            Point2D centro = jogador.getCenter();
            spawn("tiroJogador", new SpawnData(centro.getX(), centro.getY())
                    .put("dir", new Point2D(0, -1)));
        });
    }

    private void moverJogador(double dx) {
        Entity jogador = getGameWorld().getSingleton(Type.JOGADOR);
        double maxX = getAppWidth() - jogador.getWidth();
        double novoX = Math.max(0, Math.min(jogador.getX() + dx, maxX));
        jogador.setX(novoX);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) 
    {
        vars.put("vidas", 3);
        vars.put("fim", false);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new Entidades());

        for (int x = 0; x < 11; x++) {
    for (int y = 0; y < 5; y++) {
        spawn("inimigo", new SpawnData(25 + x * 50, 60 + y * 45));
    }
}

        spawn("jogador", new SpawnData(getAppWidth() / 2.0 - 22, getAppHeight() - 80));

        // A cada 1 segundo, um inimigo vivo escolhido ao acaso atira
        getGameTimer().runAtInterval(() -> {
            List<Entity> inimigos = getGameWorld().getEntitiesByType(Type.INIMIGO);
            if (inimigos.isEmpty()) return;

            Entity atirador = inimigos.get(random(0, inimigos.size() - 1));
            Point2D centro = atirador.getCenter();
            spawn("tiroInimigo", new SpawnData(centro.getX(), centro.getY())
                    .put("dir", new Point2D(0, 1)));
        }, Duration.seconds(1));
    }

    @Override
    protected void initPhysics() {
        // Tiro do jogador atinge inimigo
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(Type.TIRO_BOM, Type.INIMIGO) {
            @Override
            protected void onCollisionBegin(Entity tiro, Entity inimigo) {
                tiro.removeFromWorld();
                inimigo.removeFromWorld();
            }
        });

        // Tiro do inimigo atinge jogador
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(Type.TIRO_BOM, Type.INIMIGO) {
    @Override
    protected void onCollisionBegin(Entity tiro, Entity inimigo) {
        tiro.removeFromWorld();
        inimigo.removeFromWorld();

        if (!getb("fim") && getGameWorld().getEntitiesByType(Type.INIMIGO).isEmpty()) {
            set("fim", true);
            getDialogService().showMessageBox("Você venceu!", () -> getGameController().startNewGame());
        }
    }
});
    }

    @Override
    protected void initUI() {
    Text textoVidas = getUIFactoryService().newText("", 24);
    textoVidas.textProperty().bind(getip("vidas").asString("Vidas: %d"));
    addUINode(textoVidas, 20, 40);
}

    public static void main(String[] args) {
        launch(args);
    }
}