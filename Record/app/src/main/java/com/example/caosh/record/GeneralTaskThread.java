package com.example.caosh.record;

/**
 * Created by caosh on 4/9/15.
 */
abstract class GeneralTaskThread extends Thread {

    private StateListener stateListener;

    public void setStateListener(StateListener stateListener) {
        this.stateListener = stateListener;
    }

    protected abstract void onRun();

    @Override
    public final void run() {
        if (stateListener != null) {
            stateListener.onStart();
        }
        onRun();
        if (stateListener != null) {
            stateListener.onEnd();
        }
    }
}
