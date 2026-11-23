package com.juridico;

import static com.almasb.fxgl.dsl.FXGL.*;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.util.Duration;
import com.juridico.Entidades.Type;
import javafx.scene.paint.Color;

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
        settings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newMainMenu() {
                return new NovoMenu(MenuType.MAIN_MENU);
            }

            @Override
            public FXGLMenu newGameMenu() {
                return new NovoMenu(MenuType.GAME_MENU);
            }
        });
        settings.setGameMenuEnabled(true);
        settings.setMenuKey(KeyCode.P);
    }

    @Override
    protected void initInput() {
        FXGL.onKey(KeyCode.A, () ->{
            int andar = getb("veloJ") ? -1 : -3;
            moverJogador(andar);
        });
        FXGL.onKey(KeyCode.D, () -> {
            int andar = getb("veloJ") ? 1 : 3;
            moverJogador(andar);
        });
        FXGL.onKey(KeyCode.LEFT, () -> {
            int andar = getb("veloJ") ? -1 : -3;
            moverJogador(andar);
        });
        FXGL.onKey(KeyCode.RIGHT, () -> {
            int andar = getb("veloJ") ? 1 : 3;
            moverJogador(andar);
        });

        FXGL.onKeyDown(KeyCode.SPACE, () -> {
            Entity jogador = getGameWorld().getSingleton(Type.JOGADOR);
            Point2D centro = jogador.getCenter();
            String tipoTiro = getb("tirosTipo") ? "tiroMega" : "tiroJogador";
            spawn(tipoTiro, new SpawnData(centro.getX(), centro.getY())
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
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("vidas", 3);
        vars.put("fim", false);
        vars.put("pontos", 0);
        vars.put("tirosTipo", false);
        vars.put("veloJ", false);
    }

    @Override
    protected void initGame() {
        getGameScene().setBackgroundColor(Color.web("#8e4800"));
        entityBuilder()
                .at(0, 0)
                .view(texture("FundoJava.png", getAppWidth(), getAppHeight()))
                .zIndex(-500)
                .buildAndAttach();

        getGameWorld().addEntityFactory(new Entidades());

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 5; y++) {
                spawn("inimigo", new SpawnData(25 + x * 50, 60 + y * 45));
            }
        }

        spawn("jogador", new SpawnData(getAppWidth() / 2.0 - 22, getAppHeight() - 80));

        // A cada 0,5\ segundo, um inimigo vivo escolhido ao acaso atira
        getGameTimer().runAtInterval(() -> {
            List<Entity> inimigos = getGameWorld().getEntitiesByType(Type.INIMIGO);
            if (inimigos.isEmpty())
                return;

            Entity atirador = inimigos.get(random(0, inimigos.size() - 1));
            Point2D centro = atirador.getCenter();
            spawn("tiroInimigo", new SpawnData(centro.getX(), centro.getY())
                    .put("dir", new Point2D(0, 1)));
        }, Duration.seconds(0.5));

        getGameTimer().runAtInterval(() ->{
            spawn("estrela", new SpawnData(-100, 0));

        }, Duration.seconds(FXGLMath.random(10,15)));
    }

    private void fimJogo (String result){

        getDialogService().showConfirmationBox(result +"\n\nJogar de Novo?", answer -> {
            if (answer) {
                getGameController().startNewGame();
            } else {
                getGameController().exit();
            }
        });
    }

    @Override
    protected void initPhysics() {
        // Tiro do jogador atinge inimigo
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(Type.TIRO_BOM, Type.INIMIGO) {
            @Override
            protected void onCollisionBegin(Entity tiro, Entity inimigo) {
                tiro.removeFromWorld();
                inimigo.removeFromWorld();

                inc("pontos", 1);

                for (Entity restoInimigos : getGameWorld().getEntitiesByType(Type.INIMIGO)) {
                    restoInimigos.getComponent(InimigoMovimentacao.class).aumentarVelocidade();
                }

                if (!getb("fim") && getGameWorld().getEntitiesByType(Type.INIMIGO).isEmpty()) {
                    set("fim", true);
                    fimJogo("Você Ganhou!");
                }
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(Type.TIRO_MEGA, Type.INIMIGO) {
            @Override
            protected void onCollisionBegin(Entity tiro, Entity inimigo) {
                inimigo.removeFromWorld();

                inc("pontos", 1);

                for (Entity restoInimigos : getGameWorld().getEntitiesByType(Type.INIMIGO)) {
                    restoInimigos.getComponent(InimigoMovimentacao.class).aumentarVelocidade();
                }

                if (!getb("fim") && getGameWorld().getEntitiesByType(Type.INIMIGO).isEmpty()) {
                    set("fim", true);
                    fimJogo("Você Ganhou!");
                }
            }
        });

        // Tiro do inimigo atinge jogador
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(Type.TIRO_RUIM, Type.JOGADOR) {
            @Override
            protected void onCollisionBegin(Entity tiro, Entity inimigo) {

                tiro.removeFromWorld();

                if (getb("fim"))
                    return;

                inc("vidas", -1);
                if (geti("vidas") <= 0) {
                    set("fim", true);
                    fimJogo("Você Perdeu ):");
                }

            }
        });

        //Tiro jogador com entrela
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(Type.ESTRELA, Type.TIRO_BOM) {

            @Override
            protected void onCollisionBegin(Entity estrela, Entity tiro){

                tiro.removeFromWorld();

                Point2D centro = estrela.getCenter();
                spawn("estrelaCadente", new SpawnData(centro.getX(), centro.getY())
                        .put("dir", new Point2D(0, 1)));

                estrela.removeFromWorld();
            }

        });

        //Usuario pega estrela cadente
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(Type.ESTRELA_CADENTE, Type.JOGADOR) {

            @Override
            protected void onCollisionBegin(Entity EC, Entity jogador){
                EC.removeFromWorld();
                set("tirosTipo", true);
                runOnce(()-> set("tirosTipo", false), Duration.seconds(5));
                set("veloJ", true);
                runOnce(()-> set("veloJ", false), Duration.seconds(5));
            }
        });
    }

    @Override
    protected void initUI() {
        Text textoVidas = getUIFactoryService().newText("", 24);
        textoVidas.textProperty().bind(getip("vidas").asString("Vidas: %d"));
        addUINode(textoVidas, 20, 40);

        Text textoPontos = getUIFactoryService().newText("", 24);
        textoPontos.textProperty().bind(getip("pontos").asString("Pontuação: %d"));
        addUINode(textoPontos, 420, 40);
    }

    public static void main(String[] args) {
        launch(args);
    }
}