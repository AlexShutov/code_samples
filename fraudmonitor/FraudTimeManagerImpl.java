package com.lodoss.examples.fraudmonitor;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Date;

/**
 * {@inheritDoc}
 */
public class FraudTimeManagerImpl {
    private static final String LOG_TAG = FraudTimeManagerImpl.class.getSimpleName().toString();
    private static final String PREFS_NAME = "FRAUD_MANAGER_VALUES";
    private static final String KEY_MAX_NUMBER_OF_ERRORS = "KEY_MAX_NUMBER_OF_ERRORS";
    private static final String KEY_CURR_ERROR_CNT = "KEY_CURR_ERROR_CNT";
    private static final String KEY_INTERVAL_DURATION = "KEY_INTERVAL_DURATION";
    private static final String KEY_ELAPSED_TIME = "KEY_ELAPSED_TIME";


    private ContextWrapper contextWrapper;
    private long intervalDuration;
    private int maxNumberOfErrors;

    private long currIntervalBeginTime;
    private long currIntervalEndTime;
    private int  currNumberOfErrors;

    public FraudTimeManagerImpl(ContextWrapper contextWrapper){
        this.contextWrapper = contextWrapper;
        clear();
    }

    @Override
    public int getNumberOfWrongTries() {
        return currNumberOfErrors;
    }

    @Override
    public void updateTimeInterval(int nMinutes) {
        checkInterval();
        // mSecs
        intervalDuration = nMinutes * 60l * 1000l;
        currIntervalEndTime = currIntervalBeginTime + nMinutes;
    }

    @Override
    public void setMaxErrorCount(int numberOfTimes) {
        maxNumberOfErrors = numberOfTimes;
        checkInterval();
    }

    @Override
    public void start() {
        restorePreviousState();
    }

    @Override
    public void stop() {
        checkInterval();
        saveValues();
    }

    @Override
    public void reset() {
        if (isErrorThresholdExceeded()){
            Log.i(LOG_TAG, "Fraud detected, resetting error counter");
        }
        currNumberOfErrors = 0;
        checkInterval();
    }

    @Override
    public void notifyAboutFraud() {
        if (!isErrorThresholdExceeded()) {
            checkInterval();
        }
        currNumberOfErrors++;
    }

    @Override
    public boolean isErrorThresholdExceeded() {
        return currNumberOfErrors >= maxNumberOfErrors;
    }

    @Override
    public long getElapsedTime() {
        long now = new Date().getTime();
        if (now >= currIntervalEndTime && !isErrorThresholdExceeded()){
            checkInterval();
            return intervalDuration;
        }
        return currIntervalEndTime - now;
    }

    private SharedPreferences getSharedPreferences(){
        SharedPreferences prefs = contextWrapper.getContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs;
    }

    private void restorePreviousState(){
        SharedPreferences prefs = getSharedPreferences();
        intervalDuration = prefs.getLong(KEY_INTERVAL_DURATION, 0);
        maxNumberOfErrors = prefs.getInt(KEY_MAX_NUMBER_OF_ERRORS, 0);
        long elapsedTime = prefs.getLong(KEY_ELAPSED_TIME, 0);
        currNumberOfErrors = prefs.getInt(KEY_CURR_ERROR_CNT, 0);

        /** add time left from previous interval (saved) */
        long now = new Date().getTime();
        currIntervalBeginTime = now;
        currIntervalEndTime = now + elapsedTime;

    }

    private void saveValues(){
        SharedPreferences prefs = getSharedPreferences();
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong(KEY_INTERVAL_DURATION, intervalDuration);
        editor.putInt(KEY_MAX_NUMBER_OF_ERRORS, maxNumberOfErrors);
        editor.putInt(KEY_CURR_ERROR_CNT, currNumberOfErrors);
        long now = new Date().getTime();
        long elapsedTime = now - currIntervalBeginTime;
        editor.putLong(KEY_ELAPSED_TIME, elapsedTime);
        /** sync call */
        editor.commit();
    }

    private void clear(){
        intervalDuration = 0;
        maxNumberOfErrors = 0;
        currIntervalBeginTime = 0;
        currIntervalEndTime = 0;
        currNumberOfErrors = 0;
    }


    private void checkInterval(){
        if (new Date().getTime() >= currIntervalEndTime){
            currIntervalBeginTime = new Date().getTime();
            currIntervalEndTime = currIntervalBeginTime + intervalDuration;
            currNumberOfErrors = 0;
        }
    }

}
