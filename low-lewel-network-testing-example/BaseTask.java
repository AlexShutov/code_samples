import com.arubanetworks.airo2.common.Action1;
import com.google.common.base.Throwables;

/**
 * Created by alex on 06.01.17.
 */
public class BaseTask<T> {

    private static final String LOG_TAG = BaseTask.class.getSimpleName();
    private final String TASK_NAME;

    private Action1<T> mSuccessCallback;
    private Action1<Throwable> mErrorCallback;

    public BaseTask(Action1<T> successCallback, Action1<Throwable> errorCallback) {
        this.mSuccessCallback = successCallback;
        this.mErrorCallback = errorCallback;
        TASK_NAME = this.getClass().getSimpleName();
    }

    protected void handleSuccess(T payloadData){
        if(mSuccessCallback != null){
            mSuccessCallback.onNext(payloadData);
        } else {
            android.util.Log.i(LOG_TAG, String.format("No success callback for %s, result is skipped", TASK_NAME));
        }
    }

    protected void handleError(Throwable t){
        if(mErrorCallback != null){
            mErrorCallback.onNext(t);
        } else {
            android.util.Log.i(LOG_TAG, String.format("No error callback for %s, result is skipped", TASK_NAME));
        }
    }
}