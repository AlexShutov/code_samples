
import android.os.SystemClock;

import com.myapp.common.Action1;
import com.myapp.model.TestResult;
import com.myapp.protobuffbackup.TestType;

import org.openobservatory.measurement_kit.android.LoadLibraryUtils;
import org.openobservatory.measurement_kit.sync.PortolanSyncApi;

/**
 * Created by Jeff Jones on 1/27/2017.
 */

public class TRACETask  extends BaseProtoBuffTask {
    private final String wanServer;
    private final String deviceID;
    private final long proberInstancesPointer;
    private TestResult result;

    public TRACETask(String wanServer, String deviceID, Action1<TestResult> successCallback, Action1<Throwable> errorCallback) {
        super(TestType.TEST_TRACE_RT,deviceID,successCallback, errorCallback);
        this.wanServer = wanServer;
        this.deviceID = deviceID;
        this.result = new TestResult(deviceID);
        result.setTestType(TestType.TEST_TRACE_RT);
        proberInstancesPointer =  PortolanSyncApi.openProber(true, 80);

    }

    public TestResult getResult() {
        return result;
    }


    protected String RunTest() {
        String[] outputData= new String[2];
        int[] outputInts = new int[3];
        double[] outputDoub = new double[1] ;

        // statusCode
        // interfaceIp

        PortolanSyncApi.sendProbe(proberInstancesPointer,wanServer,1000,10000,outputData,outputInts,outputDoub);
        StringBuilder sb = new StringBuilder();
        sb.append("statusCode"+outputData[0]);
        sb.append("interfaceIp"+outputData[1]);

        sb.append("interfaceIp"+outputInts[0]);
        sb.append("interfaceIp"+outputInts[1]);
        sb.append("interfaceIp"+outputInts[2]);

        sb.append("interfaceIp"+outputDoub[0]);
        sb.append("interfaceIp"+outputDoub[1]);
        // ttl
        // recvBytes
        // isIpv4
        return sb.toString();
    }

    static {
        LoadLibraryUtils.load_measurement_kit();
    }
}
