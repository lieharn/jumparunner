package com.lieharn.jr.core;

import java.util.ArrayList;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Game;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import static playn.core.PlayN.*;
import playn.core.Pointer;

public class JumpaRunnerGame extends Game.Default {

    private float x = 24.0f;
    private float y = 3.0f;
    private CanvasImage bgImage;
    private Image cloudImage;
    private ImageLayer cloud;
    Image ballImage;
    GroupLayer ballsLayer;

    public JumpaRunnerGame() {
        super(25); // call update every 33ms (30 times per second)
    }

    class Ball {

        public ImageLayer layer;
        float angle = 0.0f;

        public Ball(Image image, float x, float y) {
            layer = graphics().createImageLayer(image);
            layer.setOrigin(ballImage.width() / 2f, ballImage.height() / 2f);
            layer.setTranslation(x, y);
        }

        public void update(float delta) {
            layer.setRotation(angle);
            angle += delta;
        }
    }
    ArrayList<Ball> balls = new ArrayList<Ball>();

    @Override
    public void init() {
        // create and add background image layer
//    Image bgImage = assets().getImage("images/bg.png");
//    ImageLayer bgLayer = graphics().createImageLayer(bgImage);
//    graphics().rootLayer().add(bgLayer);
        int width = 640;
        int height = 480;
        bgImage = graphics().createImage(width, height);

        Canvas canvas = bgImage.canvas();
        canvas.setFillColor(0xff87ceeb);
        canvas.fillRect(0, 0, width, height);

        ImageLayer bg = graphics().createImageLayer(bgImage);
        graphics().rootLayer().add(bg);

        cloudImage = assets().getImage("images/cloud.png");
        cloud = graphics().createImageLayer(cloudImage);
        graphics().rootLayer().add(cloud);

        cloud.setTranslation(x, y);


        ballImage = assets().getImage("images/ball.png");
        ballsLayer = graphics().createGroupLayer();
        graphics().rootLayer().add(ballsLayer);
        pointer().setListener(new Pointer.Adapter() {
            @Override
            public void onPointerEnd(Pointer.Event event) {
                Ball ball = new Ball(ballImage, event.x(), event.y());
                balls.add(ball);
                ballsLayer.add(ball.layer);

//                ImageLayer ball = graphics().createImageLayer(ballImage);
//                ball.setTranslation(event.x(), event.y());
//                ballsLayer.add(ball);
            }
        });
    }

    @Override
    public void update(int delta) {
        // the background automatically paints itself, so no need to do anything here!

        x += 0.1f * delta;
        if (x > bgImage.width() + cloudImage.width()) {
            x = -cloudImage.width();
        }
        cloud.setTranslation(x, y);

        for (Ball ball : balls) {
            ball.update(delta);
        }
    }

    @Override
    public void paint(float delta) {
    }
}
