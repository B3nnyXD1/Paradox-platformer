package com.myproject.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.myproject.game.MainGame;
import com.myproject.game.Screens.PlayScreen;
import com.myproject.game.Tools.Factories.AnimationCreator;

/**
 * Created by usuario on 01/02/2017.
 */

public class Zombie extends Actor {
    private Animation<TextureRegion> walkAnimation;

    public Zombie(PlayScreen screen, float x, float y){
        super(screen,x,y);
        this.walkAnimation = new AnimationCreator(screen).createWalkAnimation();
        setBounds(getX(), getY(), 140 / MainGame.PPM, 140 / MainGame.PPM);
    }

    public void update(float dt){
        stateTime += dt;
        setPosition(getBody().getPosition().x - getWidth() / 2, getBody().getPosition().y - getHeight() / 2);
        setRegion(walkAnimation.getKeyFrame(stateTime, true));
    }

    @Override
    protected Body getBodyDefinition() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(30 / MainGame.PPM, 60 / MainGame.PPM);
        fixtureDef.filter.categoryBits = MainGame.ENEMY_BIT;
        fixtureDef.filter.maskBits =MainGame.GROUND_BIT |
                MainGame.PLAYER_BIT |
                MainGame.BRICK_BIT |
                MainGame.COIN_BIT |
                MainGame.ENEMY_BIT |
                MainGame.OBJECT_BIT;

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

        // Hitbox cabeza
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1 / MainGame.PPM);
        vertice[1] = new Vector2(5, 8).scl(1 / MainGame.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / MainGame.PPM);
        vertice[3] = new Vector2(3, 3).scl(1 / MainGame.PPM);
        head.set(vertice);

        fixtureDef.shape = head;
        fixtureDef.restitution = 0.5f;
        fixtureDef.filter.categoryBits = MainGame.ENEMY_HEAD_BIT;
        body.createFixture(fixtureDef).setUserData(this);
        return body;
    }

    public void moveRight() {
        flip(false, false);
        if (isHorizontalVelocityLessThan(4)) {
            moveBody(0.2f, 0);
        }
    }

    public void moveLeft() {
        flip(true, false);
        if (isHorizontalVelocityGreaterThan(-4)) {
            moveBody(-0.2f, 0);
        }
    }
}