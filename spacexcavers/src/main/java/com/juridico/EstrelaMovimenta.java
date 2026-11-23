package com.juridico;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

public class EstrelaMovimenta extends Component{

    private double velocidade = 35;

    @Override
    public void onUpdate(double tpf) {
        Entity estrela = getEntity();
        estrela.translateX(velocidade * tpf);
    }
}
