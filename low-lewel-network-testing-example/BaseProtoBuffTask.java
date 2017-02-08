import android.os.SystemClock;

import com.arubanetworks.airo2.common.Action1;
import com.arubanetworks.airo2.model.TestResult;
import com.arubanetworks.airo2.protobuffbackup.TestType;

/**
 * Base task prototype for testing networks using protobufs
 */

public abstract class BaseProtoBuffTask  implements Runnable {
    private static final String LOG_TAG = BaseProtoBuffTask.class.getSimpleName();
    private final Action1<TestResult> successCallback;
    private final Action1<Throwable> errorCallback;
    private final TestType testType;
    private final TestResult result;


    public BaseProtoBuffTask(TestType testType, String deviceID, Action1<TestResult> successCallback, Action1<Throwable> errorCallback) {
        this.testType = testType;
        this.successCallback = successCallback;
        this.errorCallback = errorCallback;
        this.result = new TestResult(deviceID);
        result.setTestType(TestType.TEST_FTP);
    }

    protected void handleSuccess(TestResult payloadData){
        if(successCallback != null){
            successCallback.onNext(payloadData);
        } else {
            android.util.Log.i(LOG_TAG, String.format("No success callback for %s, result is skipped", testType.getSimpleName()));
        }
    }

    @Override
    public void run() {
        long latency = SystemClock.uptimeMillis();

        try {
            String serverMessage = RunTest();

            latency = SystemClock.uptimeMillis() - latency;

            result.setTestLatency(latency);
            result.setTestResult(serverMessage);
            handleSuccess(result);
        } catch (Exception e) {
            handleError(e);
        }

    }

    abstract protected String RunTest() throws Exception;

    protected void handleError(Throwable t){
        if(errorCallback != null){
            errorCallback.onNext(t);
        } else {
            android.util.Log.i(LOG_TAG, String.format("No error callback for %s, result is skipped", testType.getSimpleName()));
        }
    }

    /**
     * just for testing
     * TODO remove
     */
    protected void FAKESLEEP(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}

    }
}
