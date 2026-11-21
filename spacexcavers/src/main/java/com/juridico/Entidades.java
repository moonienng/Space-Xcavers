package com.juridico;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.*;
public class Entidades implements EntityFactory
{
    public enum Type {
        JOGADOR, INIMIGO, TIRO_BOM, TIRO_RUIM;
    }    

    @Spawns("jogador")
    public Entity spawnJogador(SpawnData data) {
        Texture texture = getAssetLoader().loadTexture("PinguimMC.gif");
        texture.setFitWidth(50);
        texture.setFitHeight(50);

        return entityBuilder(data)
                .type(Type.JOGADOR)
                .viewWithBBox(texture)
                .collidable()
                .build();
    }

    @Spawns("inimigo")
    public Entity spawnInimigo(SpawnData data) {
        Texture texture = getAssetLoader().loadTexture("estrela.gif");
        texture.setFitWidth(36);
        texture.setFitHeight(36);

        return entityBuilder(data)
                .type(Type.INIMIGO)
                .viewWithBBox(texture)
                .collidable()
                .with(new InimigoMovimentacao())
                .build();
    }

    @Spawns("tiroJogador")
    public Entity spawnTiroJogador(SpawnData data) {
        return criarTiro(data, Type.TIRO_BOM);
    }

    @Spawns("tiroInimigo")
    public Entity spawnTiroInimigo(SpawnData data) {
        return criarTiro(data, Type.TIRO_RUIM);
    }

    private Entity criarTiro(SpawnData data, Type Type) {
        Texture texture = getAssetLoader().loadTexture("Pikareta5FPS.gif");
        texture.setFitWidth(10);
        texture.setFitHeight(24);
        Point2D direcao = data.get("dir");

        return entityBuilder(data)
                .type(Type)
                .viewWithBBox(texture)
                .with(new ProjectileComponent(direcao, 300)) // px/s; 50 era muito lento
                .with(new OffscreenCleanComponent())
                .collidable()
                .build();
    }
}
