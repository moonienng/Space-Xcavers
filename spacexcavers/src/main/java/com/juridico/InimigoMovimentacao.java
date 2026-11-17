package com.juridico;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

public class InimigoMovimentacao extends Component {

    private final double velocidadeX = 35;   
    private final double passoDescida = 8;   
    private int direcao = 1;    

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
        getEntity().translateY(passoDescida);
    }
}