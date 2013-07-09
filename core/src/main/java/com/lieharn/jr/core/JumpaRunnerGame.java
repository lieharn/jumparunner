package com.lieharn.jr.core;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Game;
import playn.core.Image;
import playn.core.ImageLayer;
import static playn.core.PlayN.*;

public class JumpaRunnerGame extends Game.Default {

    public JumpaRunnerGame() {
        super(33); // call update every 33ms (30 times per second)
    }

    @Override
    public void init() {
        // create and add background image layer
//    Image bgImage = assets().getImage("images/bg.png");
//    ImageLayer bgLayer = graphics().createImageLayer(bgImage);
//    graphics().rootLayer().add(bgLayer);
        int width = 640;
        int height = 480;
        CanvasImage bgImage = graphics().createImage(width, height);
        Canvas canvas = bgImage.canvas();
        canvas.setFillColor(0xff87ceeb);
        canvas.fillRect(0, 0, width, height);
        ImageLayer bg = graphics().createImageLayer(bgImage);
        graphics().rootLayer().add(bg);

        Image cloudImage = assets().getImage("images/cloud.jpg");
        ImageLayer cloud = graphics().createImageLayer(cloudImage);
        graphics().rootLayer().add(cloud);
        float x = 24.0f;
        float y = 3.0f;
        cloud.setTranslation(x, y);
    }

    @Override
    public void update(int delta) {
    }

    @Override
    public void paint(float alpha) {
        // the background automatically paints itself, so no need to do anything here!
    }
}
