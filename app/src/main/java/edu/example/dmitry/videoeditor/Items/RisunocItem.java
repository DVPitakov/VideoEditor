package edu.example.dmitry.videoeditor.Items;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;

import edu.example.dmitry.videoeditor.Tools;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dmitry on 21.10.17.
 */

public class RisunocItem extends BaseItem {
    private int imageSize = 60;
    private int imageSizeD = 60;
    private int inheritedColor = 255 << 16 & 255 << 8;
    private int inheritedSize = 15;
    private boolean ready = false;
    private float oldScale = 1;
    private float newScale = 1;


    View view;

    private ArrayList<Point> arrayList = new ArrayList<>();
    private HashMap<Integer, Integer> colors = new HashMap<>();
    private HashMap<Integer, Integer> sizes2 = new HashMap<>();

    public RisunocItem(View view, int left, int top) {
        super(view);
        this.view = view;
        rect = new Rect();
        rect.set(left, top, left, top);

    }

    public boolean isReady() {
        return ready;

    }

    public void setReady(boolean ready) {
        this.ready = ready;

    }

    public void beginNewLineGroop(Integer color, Integer size2) {
        if (size2 != null) {
            Log.d("fucking", "" + size2);
            Log.d("fucking", "addSize " + arrayList.size());
            inheritedSize = size2;
            sizes2.put(arrayList.size(), inheritedSize);
        }
        if (color != null) {
            inheritedColor = color;
            colors.put(arrayList.size(), inheritedColor);
        }
    }

    public void newLine() {
        colors.put(arrayList.size(), inheritedColor);
    }

    public void setPos(int left, int top) {
        rect.set(left, top, left + imageSizeD, top +  imageSizeD);

    }

    public void addDrawingPoint(int x, int y) {
        Point cur;
        if (arrayList.size() > 0) {
            cur = arrayList.get(arrayList.size() - 1);
            if (Math.abs(cur.x - x) > 4 || Math.abs(cur.y - y) > 4) {
                arrayList.add(new Point(x,y));
            }
            if (cur.x < rect.left) {
                rect.left = cur.x;
            }
            if (cur.x > rect.right) {
                rect.right = cur.x;
            }
            if (cur.y < rect.top) {
                rect.top = cur.y;
            }
            if(cur.y > rect.bottom) {
                rect.bottom = cur.y;
            }
        }
        else {
            cur = new Point();
            cur.set(x, y);
            arrayList.add(cur);
            rect.bottom = cur.y;
            rect.top = cur.y;
            rect.left = cur.x;
            rect.right = cur.x;
        }


    }

    @Override
    public void scale(float scale) {

    }


    public void draw(Canvas canvas) {
        float senterX = Tools.getCenter(rect.right + x, rect.left + x);
        float senterY = Tools.getCenter(rect.top + y , rect.bottom + y);
        matrix.reset();
        matrix.setTranslate(rect.left + x, rect.top + y);
        matrix.postRotate(alpha + oldAlpha, senterX, senterY);

        inverseMatrix.reset();
        inverseMatrix.setRotate(-alpha - oldAlpha, senterX, senterY);
        inverseMatrix.postTranslate(-rect.left - x, - rect.top  - y);
        canvas.setMatrix(matrix);
        Paint fontPaint = new Paint();
        fontPaint.setColor(inheritedColor);
        fontPaint.setStrokeWidth((inheritedSize + 1) * 15);
        if (arrayList.size() > 0) {
            Point start = arrayList.get(0);
            Point stop;
            for (int i = 0; i < arrayList.size(); i++) {
                stop = arrayList.get(i);


                if (sizes2.get(i) != null) {
                    Log.d("fucking_err", "here2");
                    Log.d("fucking_err", "i: " + i + " size: " + sizes2.get(i));
                    fontPaint.setStrokeWidth((sizes2.get(i) + 1) * 15);
                }
                if (colors.get(i) != null) {
                    fontPaint.setColor(colors.get(i));
                }
                if (sizes2.get(i) != null || colors.get(i) != null) {
                    start = stop;
                    continue;
                }
                canvas.drawCircle(start.x - rect.left, start.y - rect.top, fontPaint.getStrokeWidth() / 2, fontPaint);
                canvas.drawLine(start.x  - rect.left, start.y  - rect.top, stop.x  - rect.left, stop.y  - rect.top, fontPaint);
                start = stop;

            }
        }

        if(focused && isReady()) {
            drawFrame(canvas);
        }
    }

    @Override
    public void saveLeft() {
        rect.set(rect.left + x, rect.top, rect.right + x, rect.bottom);
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).x += x;
        }
        x = 0;


    }


    @Override
    public void saveTop() {
        rect.set(rect.left, rect.top + y, rect.right, rect.bottom + y);
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).y += y;
        }
        y = 0;

    }
}
