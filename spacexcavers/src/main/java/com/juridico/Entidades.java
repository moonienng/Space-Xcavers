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
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;

public class Entidades implements EntityFactory
{
    public enum Type {
        JOGADOR, INIMIGO, TIRO_BOM, TIRO_RUIM, ESTRELA, ESTRELA_CADENTE, TIRO_MEGA, EXPLOSAO
    }    

    @Spawns("jogador")
    public Entity spawnJogador(SpawnData data) {
        Texture texture = getAssetLoader().loadTexture("PinguimMC.gif");
        texture.setFitWidth(100);
        texture.setFitHeight(100);

        HitBox hitbox = new HitBox(new Point2D(30, 25), BoundingShape.box(40, 55));

        return entityBuilder(data)
                .type(Type.JOGADOR)
                .view(texture)
                .bbox(hitbox)
                .collidable()
                .build();
    }

    @Spawns("inimigo")
    public Entity spawnInimigo(SpawnData data) {
        Texture texture = getAssetLoader().loadTexture("drones2.gif");
        texture.setFitWidth(65);
        texture.setFitHeight(65);

        return entityBuilder(data)
                .type(Type.INIMIGO)
                .viewWithBBox(texture)
                .collidable()
                .with(new InimigoMovimentacao())
                .build();
    }

    @Spawns("estrela")
    public Entity spawnEstrela(SpawnData data){
        Texture texture = getAssetLoader().loadTexture("estrela.gif");
        texture.setFitWidth(65);
        texture.setFitHeight(65);

        return entityBuilder(data)
                .type(Type.ESTRELA)
                .viewWithBBox(texture)
                .collidable()
                .with(new EstrelaMovimenta())
                .build();
    }

    @Spawns("estrelaCadente")
    public Entity spawnEstrelaC(SpawnData data){
        Texture texture = getAssetLoader().loadTexture("estrela.gif");
        texture.setFitWidth(40);
        texture.setFitHeight(40);

        Point2D direcao = data.get("dir");
        return entityBuilder(data)
                .type(Type.ESTRELA_CADENTE)
                .viewWithBBox(texture)
                .with(new ProjectileComponent(direcao, 150)) // px/s; 50 era muito lento
                .with(new OffscreenCleanComponent())
                .collidable()
                .build();
    }

    @Spawns("explosao")
    public Entity spawnExplosao(SpawnData data){
        Texture texture = getAssetLoader().loadTexture("explosão.png");
        texture.setFitHeight(55);
        texture.setFitWidth(55);
        texture.setRotate(-180);
        return entityBuilder(data)
            .type(Type.EXPLOSAO)
            .viewWithBBox(texture)
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

    @Spawns("tiroMega")
    public Entity spawnsTiroMega(SpawnData data){return criarTiro(data, Type.TIRO_MEGA);}

    private Entity criarTiro(SpawnData data, Type Type) {
        String imagem = switch (Type) {
            case TIRO_BOM -> "Pikareta10FPS.gif";
            case TIRO_RUIM -> "TiroInimigo.gif";
            case TIRO_MEGA -> "Pikareta5FPS.gif";
            default -> throw new IllegalArgumentException("Tipo sem textura: " + Type);
        };

        Texture texture = getAssetLoader().loadTexture(imagem);
        Point2D direcao = data.get("dir");

        if (Type == Type.TIRO_MEGA){
            texture.setFitWidth(70);
            texture.setFitHeight(90);
        }else{
            texture.setFitWidth(35);
            texture.setFitHeight(55);

        }

        if (Type == Type.TIRO_RUIM) {
            texture.setRotate(-90);
        }

        double hitL = (Type == Type.TIRO_RUIM) ? 8 : 20;
        double hitA = (Type == Type.TIRO_RUIM) ? 14 : 30;

        HitBox hitbox = new HitBox(
                new Point2D((45 - hitL) / 2, (55 - hitA) / 2),   // centralizando na imagem
                BoundingShape.box(hitL, hitA));

        return entityBuilder(data)
                .type(Type)
                .viewWithBBox(texture)
                .with(new ProjectileComponent(direcao, 300)) // px/s; 50 era muito lento
                .with(new OffscreenCleanComponent())
                .collidable()
                .build();
    }
}
