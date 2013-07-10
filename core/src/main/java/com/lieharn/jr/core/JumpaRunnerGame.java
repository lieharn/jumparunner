package com.lieharn.jr.core;

import java.util.ArrayList;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Game;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import static playn.core.PlayN.*;
import playn.core.Pointer;

public class JumpaRunnerGame extends Game.Default {

    CanvasImage bgImage;
    ImageLayer bgLayer;
    Image cloudImage;
    ImageLayer cloudLayer;
    float x;
    float y;
    Image ballImage;
    GroupLayer ballLayer;
    World world;

    public JumpaRunnerGame() {
        super(25); // call update every 33ms (30 times per second)
    }

    class Ball {

        public ImageLayer layer;
        float angle = 0.0f;
        Body body;

        public Ball(Image image, float x, float y) {
            float radius = 0.5f;
            layer = graphics().createImageLayer(image);
            layer.setOrigin(image.width() / 2f, image.height() / 2f);
            layer.setScale((radius * 2) / image.width(), (radius * 2) / image.height());
            layer.setTranslation(x, y);

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.DYNAMIC;
            bodyDef.position = new Vec2(x, y);
            body = world.createBody(bodyDef);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = new CircleShape();
            fixtureDef.shape.m_radius = radius;
            fixtureDef.density = 1.0f;
            fixtureDef.friction = 0.3f;
            body.createFixture(fixtureDef);
            body.setTransform(new Vec2(x, y), angle);
        }

        public void paint(float delta) {
            layer.setTranslation(body.getPosition().x, body.getPosition().y);
            angle += delta * 0.1f;
            layer.setRotation(angle);
        }
    }
    ArrayList<Ball> balls = new ArrayList<Ball>();
    float physUnitPerScreenUnit = 1 / 26.666667f;
    int width = 640;
    int height = 480;

    @Override
    public void init() {
        bgImage = graphics().createImage(width, height);
        Canvas canvas = bgImage.canvas();
        canvas.setFillColor(0xff87ceeb);
        canvas.fillRect(0, 0, width, height);
        bgLayer = graphics().createImageLayer(bgImage);
        graphics().rootLayer().add(bgLayer);

        cloudImage = assets().getImage("images/cloud.png");
        cloudLayer = graphics().createImageLayer(cloudImage);
        graphics().rootLayer().add(cloudLayer);
        x = 24.0f;
        y = 3.0f;
        ballImage = assets().getImage("images/ball.png");
        ballLayer = graphics().createGroupLayer();
        ballLayer.setScale(1f / physUnitPerScreenUnit);
        graphics().rootLayer().add(ballLayer);
        pointer().setListener(new Pointer.Adapter() {
            @Override
            public void onPointerEnd(Pointer.Event event) {
                Ball ball = new Ball(ballImage, physUnitPerScreenUnit * event.x(), physUnitPerScreenUnit * event.y());
                balls.add(ball);
                ballLayer.add(ball.layer);
            }
        });
        initPhysics();
    }

    public void initPhysics() {
        Vec2 gravity = new Vec2(0.0f, 10.0f);
        world = new World(gravity);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);
    }

    @Override
    public void update(int delta) {
        // the background automatically paints itself, so no need to do anything here!
        world.step(0.033f, 10, 10);
    }

    @Override
    public void paint(float delta) {

        x += delta;
        if (x > bgImage.width() + cloudImage.width()) {
            x = -cloudImage.width();
        }
        cloudLayer.setTranslation(x, y);

        for (Ball ball : balls) {
            ball.paint(delta);
        }
    }
}
