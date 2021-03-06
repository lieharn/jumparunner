package com.lieharn.jr.core;

import java.util.ArrayList;
import org.jbox2d.collision.shapes.*;
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
import playn.core.Key;
import playn.core.Keyboard;
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
    GroupLayer levelLayer;
    World world;
    Image blockImage;
    ImageLayer blockLayer;
    float curserX = 320.0f;
    float curserY = 240.0f;
    float curserJumpStartY;
    float curserJumpGoalY;
    boolean curserJumpGoalYReached = false;

    public JumpaRunnerGame() {
        super(25);
    }

    private void updateCurser() {
        if (curserUp) {
            if (curserY >= curserJumpGoalY && !curserJumpGoalYReached) {
                curserY -= curserTranslation * 3;
                blockLayer.setTranslation(curserX, curserY);
                if (curserJumpGoalY >= curserY) {
                    curserJumpGoalYReached = true;
                }
            }
        }
        if (curserJumpGoalYReached) {
            curserY += curserTranslation * 2;
            blockLayer.setTranslation(curserX, curserY);
            if (curserJumpStartY <= curserY) {
                curserJumpGoalYReached = false;
            }
        }
        if (curserDown) {
            curserY += curserTranslation;
            blockLayer.setTranslation(curserX, curserY);
        }
        if (curserRight) {
            curserX += curserTranslation;
            blockLayer.setTranslation(curserX, curserY);
        }
        if (curserLeft) {
            curserX -= curserTranslation;
            blockLayer.setTranslation(curserX, curserY);
        }
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
    int width = 800;
    int height = 600;
    Canvas canvas;
    boolean curserUp = false;
    boolean curserRight = false;
    boolean curserLeft = false;
    boolean curserDown = false;
    float curserTranslation = 3.5f;

    @Override
    public void init() {
        bgImage = graphics().createImage(width, height);
        canvas = bgImage.canvas();
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

        blockImage = assets().getImage("images/block.png");
        blockLayer = graphics().createImageLayer(blockImage);
        blockLayer.setTranslation(curserX, curserY);
        graphics().rootLayer().add(blockLayer);


        pointer().setListener(new Pointer.Adapter() {
            @Override
            public void onPointerEnd(Pointer.Event event) {
                Ball ball = new Ball(ballImage, physUnitPerScreenUnit * event.x(), physUnitPerScreenUnit * event.y());
                balls.add(ball);
                ballLayer.add(ball.layer);
            }
        });


        keyboard().setListener(new Keyboard.Adapter() {
            @Override
            public void onKeyDown(Keyboard.Event event) {
                if (event.key() == Key.UP) {
                    curserJumpStartY = curserY;
                    curserJumpGoalY = curserJumpStartY - 150.0f;
                    curserUp = true;
                }
                if (event.key() == Key.DOWN) {
                    curserDown = true;
                }
                if (event.key() == Key.RIGHT) {
                    curserRight = true;
                }
                if (event.key() == Key.LEFT) {
                    curserLeft = true;
                }
            }

            @Override
            public void onKeyUp(Keyboard.Event event) {
                if (event.key() == Key.UP) {
                    curserUp = false;
                    curserJumpGoalYReached = true;
                }
                if (event.key() == Key.DOWN) {
                    curserDown = false;
                }
                if (event.key() == Key.RIGHT) {
                    curserRight = false;
                }
                if (event.key() == Key.LEFT) {
                    curserLeft = false;
                }
            }
        });


        initPhysics();
    }

    public void initPhysics() {
        Vec2 gravity = new Vec2(0.0f, 10.0f);
        world = new World(gravity);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);


        float pWidth = physUnitPerScreenUnit * bgImage.width();
        float pHeight = physUnitPerScreenUnit * bgImage.height();
        Body ground = world.createBody(new BodyDef());

        EdgeShape bottom = new EdgeShape();
        bottom.set(new Vec2(0, pHeight), new Vec2(pWidth, pHeight));
        ground.createFixture(bottom, 0.0f);

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

    @Override
    public void update(int delta) {
        world.step(0.033f, 10, 10);

        updateCurser();


    }
}
