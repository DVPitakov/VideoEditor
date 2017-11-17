package edu.example.dmitry.videoeditor.holders;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import edu.example.dmitry.videoeditor.ImageEditor;
import edu.example.dmitry.videoeditor.items.BaseItem;
import edu.example.dmitry.videoeditor.items.ImageEditorQueue;

import java.util.Stack;

/**
 * Created by dmitry on 02.11.17.
 */

public class HistoryHolder {
    public int lastEffect = 0;
    public int futureEffect = 0;
    public Rect lastKrop = null;
    public Rect futureKrop = null;
    private Stack<Action> actions = new Stack<>();
    private Stack<Action> futureActions = new Stack<>();
    private static HistoryHolder instance;
    private HistoryHolder() {};
    public static HistoryHolder getInstance() {
        if(instance == null) {
            instance = new HistoryHolder();
        }
        return  instance;
    }

    public void addAction(Action action) {
        actions.push(action);
    }


    public boolean clear() {
        actions = new Stack<>();
        futureActions = new Stack<>();
        lastEffect = 0;
        return false;
    }

    public boolean future() {
        if(futureActions.size() > 0) {
            Action action = futureActions.pop();
            actions.add(action);
            action.forward();
            return true;
        }
        return false;
    }

    public boolean back() {
        if(actions.size() > 0) {
            Action action = actions.pop();
            Log.d("action_class", action.getClass().toString());
            futureActions.add(action);
            action.back();
            return true;
        }
        return false;
    }
    public static abstract class Action {
        public abstract void back();
        public void forward(){}
    }

    public static class Krop extends Action{
        private Rect begin;
        private Rect end;
        public Krop(Rect begin, Rect end) {
            this.begin = begin;
            this.end = end;
        }
        @Override
        public void back() {
            ImageEditorQueue.getInstance().moveAll( end.left - begin.left, end.top - begin.top);
            ImageHolder.getInstance().setKropedBitmap(ImageEditor.krop(ImageHolder.getInstance().getDefaultBitmap(),
                    begin.left, begin.top, begin.right, begin.bottom));
            HistoryHolder.getInstance().lastKrop = begin;
        }

        @Override
        public void forward() {
            ImageEditorQueue.getInstance().moveAll(begin.left - end.left, begin.top - end.top);
            ImageHolder.getInstance().setKropedBitmap(ImageEditor.krop(ImageHolder.getInstance().getDefaultBitmap(),
                    end.left, end.top, end.right, end.bottom));
            HistoryHolder.getInstance().futureKrop = end;
        }
    }

    public static class Effect extends Action{
        private int begin;
        private int end;
        public Effect(int effectBefore, int newEffect) {
            this.begin = effectBefore;
            this.end = newEffect;
        }

        @Override
        public void back() {
            Bitmap kropedBitmap = ImageHolder.getInstance().getKropedBitmap();
            if(kropedBitmap == null) {
                kropedBitmap = ImageHolder.getInstance().getDefaultBitmap();
                ImageHolder.getInstance().setKropedBitmap(kropedBitmap);
            }
            HistoryHolder.getInstance().lastEffect = begin;
            ImageHolder.getInstance().setFreshBitmap(ImageEditor.getEffectByNum(begin, kropedBitmap));
        }

        @Override
        public void forward() {
            Bitmap kropedBitmap = ImageHolder.getInstance().getKropedBitmap();
            if(kropedBitmap == null) {
                kropedBitmap = ImageHolder.getInstance().getDefaultBitmap();
                ImageHolder.getInstance().setKropedBitmap(kropedBitmap);
            }
            HistoryHolder.getInstance().futureEffect = end;
            ImageHolder.getInstance().setFreshBitmap(ImageEditor.getEffectByNum(end, kropedBitmap));
        }

    }

    public static class AddItem extends Action {
        public AddItem(BaseItem item) {
            this.item = item;
        }
        BaseItem item;
        @Override
        public void back() {
            ImageEditorQueue.getInstance().deleteElement(item);
            ImageHolder.getInstance().setBitmapWithElements(null);
        }
        @Override
        public void forward() {
            ImageEditorQueue.getInstance().addElement(item);
            ImageHolder.getInstance().setBitmapWithElements(null);
        }
    }

    public static class DeleteItem extends Action {
        public DeleteItem(BaseItem item) {
            this.item = item;
        }
        BaseItem item;
        @Override
        public void back() {
            ImageEditorQueue.getInstance().addElement(item);
            ImageHolder.getInstance().setBitmapWithElements(null);
        }
        @Override
        public void forward() {
            ImageEditorQueue.getInstance().deleteElement(item);
            ImageHolder.getInstance().setBitmapWithElements(null);
        }
    }

    public static class RotateAndScale extends Action {
        BaseItem item;
        float alph;
        float scale;
        public RotateAndScale(BaseItem item, float alph, float scale) {
            this.item = item;
            this.alph = alph;
            this.scale = scale;
        }
        @Override
        public void back() {
            item.setAlpha(- alph);
            item.scale(1f / scale);
            item.moveEnd();
            ImageHolder.getInstance().setBitmapWithElements(null);
        }
        @Override
        public void forward() {
            item.setAlpha(alph);
            item.scale(scale);
            item.moveEnd();
            ImageHolder.getInstance().setBitmapWithElements(null);
        }
    }

    public static class Move extends Action {
        BaseItem item;
        int dx;
        int dy;
        public Move(BaseItem item, int dx, int dy) {
            this.item = item;
            this.dx = dx;
            this.dy = dy;
        }
        @Override
        public void back() {
            item.__force_set_action_1();
            item.move(-dx, -dy);
            item.moveEnd();
            ImageHolder.getInstance().setBitmapWithElements(null);
        }
    }
}
