package com.juridico;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getEngineTimer;

public class InimigoMovimentacao extends Component {

    private final double ALTURA_CHAO = 450;
    private final double passoDescida = 30;

    private double velocidadeX = 35;
    private int direcao = 1;
    private int direcaoVertical = 1;
    private double yInicial;

    @Override
    public void onAdded() {
        yInicial = getEntity().getY();
    }

    @Override
    public void onUpdate(double tpf) {
        Entity inimigo = getEntity();

        inimigo.translateX(direcao * velocidadeX * tpf);

        double largura = inimigo.getWidth();
        double larguraTela = getAppWidth();

        if (inimigo.getX() <= 0) {
            inimigo.setX(0);
            virar();
        } else if (inimigo.getX() + largura >= larguraTela) {
            inimigo.setX(larguraTela - largura);
            virar();
        }
    }

    private void virar() {
        direcao = -direcao;
        getEntity().translateY(direcaoVertical * passoDescida);

        double base = getEntity().getY() + getEntity().getHeight();

        if (base >= ALTURA_CHAO) {
            direcaoVertical = -1;
        } else if (getEntity().getY() <= yInicial) {
            direcaoVertical = 1;
        }
    }

    public void aumentarVelocidade() {
        velocidadeX += 10;

        if (velocidadeX > 300) {
            velocidadeX = 300;
        }
    }
}