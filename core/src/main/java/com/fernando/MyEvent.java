package com.fernando;

import java.io.Serializable;

public abstract class MyEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public MyEvent() {
    }
}
