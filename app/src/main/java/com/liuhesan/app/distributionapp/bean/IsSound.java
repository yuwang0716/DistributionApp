package com.liuhesan.app.distributionapp.bean;

/**
 * Created by Tao on 2017/1/10.
 */

public class IsSound {
    private   boolean isSound;
    private boolean isShake;

    public boolean isSound() {
        return isSound;
    }

    public void setSound(boolean sound) {
        isSound = sound;
    }

    public boolean isShake() {
        return isShake;
    }

    public void setShake(boolean shake) {
        isShake = shake;
    }
}
