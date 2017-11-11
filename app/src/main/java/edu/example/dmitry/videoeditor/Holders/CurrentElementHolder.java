package edu.example.dmitry.videoeditor.Holders;

import edu.example.dmitry.videoeditor.Items.BaseItem;

/**
 * Created by dmitry on 21.10.17.
 */

public class CurrentElementHolder {
    private static CurrentElementHolder instance;
    private CurrentElementHolder() {}
    private BaseItem baseItem;
    public static CurrentElementHolder getInstance() {
        if(instance == null) {
            instance = new CurrentElementHolder();
        }
        return instance;
    }

    public void setCurrentElement(BaseItem baseItem) {
        BaseItem buf = this.baseItem;
        if (buf != baseItem) {
            if (this.baseItem != null) {
                this.baseItem.focusLosed();
            }
            this.baseItem = baseItem;
            if (this.baseItem != null) {
                this.baseItem.setFocus();
            }
        }

    }

    public void removeCurrentElement() {
        this.baseItem = null;

    }

    public BaseItem getCurrentElement() {
        return baseItem;

    }

}
