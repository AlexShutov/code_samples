package com.lodoss.examples.fraudmonitor;

/**
 * Fraud monitor, for counting failed CVV attempts, and handling
 * extract the frauder
 */

public interface FraudTimeManager {
    /**
     * Time interval (in minutes), in which user can enter fraudulent data
     * @param nMinutes
     */
    void updateTimeInterval(int nMinutes);

    /**
     * number of tries
     * @param numberOfTimes
     */
    void setMaxErrorCount(int numberOfTimes);


    void start();
    void stop();

    /**
     * Reset number of misspelled entries and current time interval
     */
    void reset();

    void notifyAboutFraud();

    /**
     * @return true if fraud treshold is exceeded and user needs to be banned
     */
    boolean isErrorThresholdExceeded();

    /**
     * @return number of fraudulent tries within current time interval
     */
    int getNumberOfWrongTries();

    /**
     * returns time elapsed since first error
     * @return
     */
    long getElapsedTime();

}
