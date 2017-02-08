import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.myapp.Iperf;
import com.myapp.common.Action1;
import com.myapp.model.TestResult;
import com.myapp.protobuffbackup.TestType;

/**
 * Created by Jeff Jones on 1/27/2017.
 */

public class PERFTask  extends BaseProtoBuffTask{
    private static final String TAG = "PERFTask";
    private final String wanServer;
    private final String deviceID;
    private final Iperf iperf;
    private TestResult result;

    public PERFTask(Context context, String wanServer, String deviceID, Action1<TestResult> successCallback, Action1<Throwable> errorCallback) {
        super(TestType.TEST_IPERF,deviceID,successCallback, errorCallback);
        this.wanServer = wanServer;
        this.deviceID = deviceID;
        this.result = new TestResult(deviceID);
        result.setTestType(TestType.TEST_IPERF);
        iperf = new Iperf(context);
        iperf.copyIperfAsset();
    }

    protected String RunTest() {
        String[] iperfCommandString = {"-c", wanServer, "-r", "-i", "2", "-t", parseDurationFieldAsInt(iperf.getiperfDuration())};

        iperf.Run(iperfCommandString);
        StringBuilder sb = new StringBuilder("");

        sb.append("iperf uplink rate Mbps,"+iperf.getiperfUplinkRate()[1]);
        sb.append("iperf dnlink rate Mbps,"+iperf.getiperfDnlinkRate()[1]);

        return sb.toString();
    }

    private String parseDurationFieldAsInt(String newDuration){
        String result = "4";
        try{
            int resultInt = Integer.parseInt(newDuration);
            if(resultInt < 1 ) { result = "1"; }
            if(resultInt > 1000 ) { result = "1000"; }
            result = Integer.toString(resultInt);
        } catch (Exception e) { Log.e(TAG, "Exception parsing new duration setting "+e); }
        return result;
    }
}
