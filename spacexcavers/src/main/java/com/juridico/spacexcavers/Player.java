package com.juridico.spacexcavers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player {
    private static final double WIDTH = 48;
    private static final double HEIGHT = 24;

    private final Rectangle view;

    public Player(double x, double y) {
        view = new Rectangle(WIDTH, HEIGHT, Color.LIMEGREEN);
        view.setArcWidth(8);
        view.setArcHeight(8);
        view.setTranslateX(x);
        view.setTranslateY(y);
    }

    public Rectangle getView() {
        return view;
    }

    public double getX() {
        return view.getTranslateX();
    }

    public void moveHorizontally(double distance, double minX, double maxX) {
        double nextX = getX() + distance;
        view.setTranslateX(Math.max(minX, Math.min(nextX, maxX)));
    }

    public double getWidth() {
        return WIDTH;
    }
}
