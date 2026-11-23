package com.juridico;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGL.*;

public class NovoMenu extends FXGLMenu {

    public NovoMenu(MenuType type){
        super(type);

        Rectangle base = new Rectangle(-300, -300, getAppWidth() + 600, getAppHeight() + 600);
        base.setFill(Color.web("#8e4800"));
        getContentRoot().getChildren().add(0, base);

        ImageView fundo = new ImageView(image("FundoJava.png", getAppWidth(), getAppHeight()));
        getContentRoot().getChildren().add(1, fundo);

        Texture drone = getAssetLoader().loadTexture("drones2.gif");
        drone.setFitWidth(140);
        drone.setFitHeight(140);

        Text titulo = getUIFactoryService().newText("SPACEXCAVERS", Color.GOLD, 48);

        Texture pinguim = getAssetLoader().loadTexture("PinguimMC.gif");
        pinguim.setFitWidth(150);
        pinguim.setFitHeight(150);


        Button botaoPrincipal = new Button(type == MenuType.MAIN_MENU ? "Jogar" : "Continuar");
        botaoPrincipal.setOnAction(e -> {
            if (type == MenuType.MAIN_MENU) fireNewGame();
            else fireResume();
        });

        Button botaoSair = new Button("Sair");
        botaoSair.setOnAction(e -> fireExit());

        VBox caixa = new VBox(20, drone, titulo, pinguim, botaoPrincipal, botaoSair);
        caixa.setAlignment(Pos.CENTER);
        caixa.setPrefWidth(getAppWidth());
        caixa.setTranslateY(45);

        getContentRoot().getChildren().add(caixa);
    }
}
