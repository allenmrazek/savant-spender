package com.savantspender.db.entity;

public interface Tag {
    void setSelected(boolean tf);
    boolean isSelected();

    String getName();
    int getId();
}
