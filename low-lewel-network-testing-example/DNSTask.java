import android.os.SystemClock;

import com.myapp.common.Action1;
import com.myapp.model.TestResult;
import com.myapp.protobuffbackup.TestType;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.database.sqlite.SQLiteDatabaseCorruptException;
import android.util.Log;


/**
 * Created by Jeff Jones on 1/27/2017.
 */

public class DNSTask  extends BaseProtoBuffTask {
    private final String wanServer;
    private final String deviceID;

    public DNSTask(String wanServer, String deviceID, Action1<TestResult> successCallback, Action1<Throwable> errorCallback) {
        super(TestType.TEST_DNS,deviceID,successCallback, errorCallback);
        this.wanServer = wanServer;
        this.deviceID = deviceID;
    }


     protected String RunTest() throws UnknownHostException {
        InetAddress address = InetAddress.getByName(wanServer);
        return address.getHostAddress();
    }
}