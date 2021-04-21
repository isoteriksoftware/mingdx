package com.isoterik.mgdx;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorTransform extends Transform {
    public final Actor actor;

    public ActorTransform() {
        actor = new Actor();
    }

    @Override
    public void setOrigin(float originX, float originY) {
        super.setOrigin(originX, originY);
        actor.setOrigin(originX, originY);
    }

    @Override
    public void setOrigin(float originX, float originY, float originZ) {
        super.setOrigin(originX, originY, originZ);
        actor.setOrigin(originX, originY);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        actor.setPosition(x, y);
    }

    @Override
    public void setPosition(float x, float y, float z) {
        super.setPosition(x, y, z);
        actor.setPosition(x, y);
    }

    @Override
    public void setRotation(float rotation) {
        super.setRotation(rotation);
        actor.setRotation(rotation);
    }

    @Override
    public void setRotation(float rotationX, float rotationY, float rotationZ) {
        super.setRotation(rotationX, rotationY, rotationZ);
        actor.setRotation(rotationY);
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
        super.setScale(scaleX, scaleY);
        actor.setScale(scaleX, scaleY);
    }

    @Override
    public void setScale(float scaleX, float scaleY, float scaleZ) {
        super.setScale(scaleX, scaleY, scaleZ);
        actor.setScale(scaleX, scaleY);
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        actor.setSize(width, height);
    }

    @Override
    public void setSize(float width, float height, float depth) {
        super.setSize(width, height, depth);
        actor.setSize(width, height);
    }

    @Override
    public float getHeight() {
        return actor.getHeight();
    }

    @Override
    public float getWidth() {
        return actor.getWidth();
    }

    @Override
    public float getOriginX() {
        return actor.getOriginX();
    }

    @Override
    public float getOriginY() {
        return actor.getOriginY();
    }

    @Override
    public float getRotation() {
        return actor.getRotation();
    }

    @Override
    public float getScaleX() {
        return actor.getScaleX();
    }

    @Override
    public float getScaleY() {
        return actor.getScaleY();
    }

    @Override
    public float getX() {
        return actor.getX();
    }

    @Override
    public float getY() {
        return actor.getY();
    }

    public Actor getActor() {
        return actor;
    }

    @Override
    public void update(float deltaTime) {
        scale.set(actor.getScaleX(), actor.getScaleY(), 1);
        size.set(actor.getWidth(), actor.getHeight(), 0);
        rotation.set(0, actor.getRotation(), 0);
        origin.set(actor.getOriginX(), actor.getOriginY(), 0);
        position.set(actor.getX(), actor.getY(), 0);
    }
}
