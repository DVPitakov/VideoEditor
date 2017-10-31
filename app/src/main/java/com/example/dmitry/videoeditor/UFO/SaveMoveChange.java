package com.example.dmitry.videoeditor.UFO;


/**
 * Created by dmitry on 31.10.17.
 */

public class SaveMoveChange extends Change {

    public SaveMoveChange() {}

    @Override
    public void run(Object targetElement) {
        ImageChangesHolder.MoveAble moveAble = (ImageChangesHolder.MoveAble)targetElement;
        moveAble.saveMovePoin();
    }
}
