package edu.example.dmitry.videoeditor.Holders;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import edu.example.dmitry.videoeditor.ImageEditor;
import edu.example.dmitry.videoeditor.Items.BaseItem;
import edu.example.dmitry.videoeditor.Items.ImageEditorQueue;

import java.util.Stack;

/**
 * Created by dmitry on 02.11.17.
 */

public class HistoryHolder {
    public int lastEffect = 0;
    public Rect lastKrop = null;
    private Stack<Action> actions = new Stack<>();
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

    public boolean back() {
        if(actions.size() > 0) {
            Action action = actions.pop();
            action.back();
            return true;
        }
        return false;
    }
    public static abstract class Action {
        public abstract void back();
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
            ImageHolder.getInstance().setKropedBitmap(ImageEditor.krop(ImageHolder.getInstance().getDefaultBitmap(),
                    begin.left, begin.top, begin.right, begin.bottom));
            HistoryHolder.getInstance().lastKrop = begin;
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
            Log.d("0209", "" + begin + " " + end);
            Bitmap kropedBitmap = ImageHolder.getInstance().getKropedBitmap();
            if(kropedBitmap == null) {
                kropedBitmap = ImageHolder.getInstance().getDefaultBitmap();
                ImageHolder.getInstance().setKropedBitmap(kropedBitmap);
            }
            HistoryHolder.getInstance().lastEffect = begin;
            ImageHolder.getInstance().setFreshBitmap(ImageEditor.getEffectByNum(begin, kropedBitmap));
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
    }

    public static class RotateAndScale extends Action {
        BaseItem item;
        float alph;
        float scale;
        RotateAndScale(BaseItem item, float alph, float scale) {
            this.item = item;
            this.alph = alph;
            this.scale = scale;
        }
        @Override
        public void back() {
            item.setAlpha(- alph);
            item.scale(1 / scale);
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
