package com.example.dmitry.videoeditor.UFO;


/**
 * Created by dmitry on 31.10.17.
 */

public class MoveChange extends Change {
    private float x;
    private float y;

    public MoveChange(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void run(Object targetElement) {
        ImageChangesHolder.MoveAble moveAble = (ImageChangesHolder.MoveAble)targetElement;
        moveAble.move(x, y);
    }
}