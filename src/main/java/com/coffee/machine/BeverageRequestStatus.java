package com.coffee.machine;

public class BeverageRequestStatus {
    private String beverageName;
    private String message;
    private boolean prepared;

    public BeverageRequestStatus(String beverageName, String message, boolean prepared) {
        this.beverageName = beverageName;
        this.message = message;
        this.prepared = prepared;
    }

    public String getBeverageName() {
        return beverageName;
    }

    public String getMessage() {
        return message;
    }

    public boolean isPrepared() {
        return prepared;
    }

    public void setBeverageName(String beverageName) {
        this.beverageName = beverageName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPrepared(boolean prepared) {
        this.prepared = prepared;
    }
}
