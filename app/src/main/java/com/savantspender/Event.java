package com.savantspender;

import androidx.annotation.Nullable;

public class Event<T> {
    private boolean mHandled = false;
    private T mParam;

    public Event(T param) {
        mParam = param;
    }

    public @Nullable T getContentIfNotHandled() {
        if (mHandled)
            return null;

        setHandled();

        return mParam;
    }

    public T peekContent() {
        return mParam;
    }

    public boolean isHandled() {
        return mHandled;
    }
    public void setHandled() {
        mHandled = true;
    }
}
