package com.juridico.spacexcavers;

import javafx.fxml.FXML;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class HelloController {
    @FXML
    private Pane gamePane;

    private final Player player = new Player(376, 430);
    private boolean movingLeft;
    private boolean movingRight;
    private long lastFrame;

    @FXML
    private void initialize() {
        gamePane.getChildren().add(player.getView());
        gamePane.setFocusTraversable(true);
        gamePane.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
        gamePane.addEventHandler(KeyEvent.KEY_RELEASED, this::handleKeyReleased);

        AnimationTimer movementTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastFrame == 0) {
                    lastFrame = now;
                    return;
                }

                double elapsedSeconds = (now - lastFrame) / 1_000_000_000.0;
                lastFrame = now;
                double direction = (movingRight ? 1 : 0) - (movingLeft ? 1 : 0);
                player.moveHorizontally(direction * 260 * elapsedSeconds,
                        0, gamePane.getWidth() - player.getWidth());
            }
        };
        movementTimer.start();
        gamePane.requestFocus();
    }

    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.A) {
            movingLeft = true;
            event.consume();
        } else if (event.getCode() == KeyCode.D) {
            movingRight = true;
            event.consume();
        }
    }

    private void handleKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.A) {
            movingLeft = false;
            event.consume();
        } else if (event.getCode() == KeyCode.D) {
            movingRight = false;
            event.consume();
        }
    }
}
