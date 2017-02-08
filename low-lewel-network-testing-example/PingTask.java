import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.myapp.AiroNetworkInfoProvider;
import com.myapp.common.Action1;
import com.myapp.impl.AiroNetworkInfoProviderImpl;
import com.myapp.model.TestResult;
import com.myapp.protobuffbackup.TestType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Pings local gateway and provides ping time
 */
public class PingTask extends BaseProtoBuffTask {

    private static final String LOG_TAG = PingTask.class.getSimpleName();

    private final String GATEWAY;
    private Process process;
    private TestResult result;

    public PingTask(String gateway, String deviceID, Action1<TestResult> successCallback, Action1<Throwable> errorCallback) {
        super(TestType.TEST_PING,deviceID,successCallback, errorCallback);
        GATEWAY = gateway;
    }

    protected String RunTest() throws Exception {
        long timeStart = System.currentTimeMillis();
        String[] cmdString = {"ping", "-A", "-w", "2", GATEWAY};  // used to be -w 2 -i 3 no -c or -n
        ProcessBuilder processBuilder = new ProcessBuilder(cmdString);
        processBuilder.redirectErrorStream(true);
        process = processBuilder.start();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while (((line = bufferedReader.readLine()) != null)) {
            if((!line.contains("transmitted,") && (line.contains("127.0.0.1") || line.indexOf("time") != -1))){
                Long[] result = {timeStart, parsePing(line)};
                if(result[1] != -1) {
                    return "Ping time = "+result;
                }
            }
        }
        throw new Exception("Error during pinging gateway");
    }

    private Long parsePing(String in){
        long result = -1;
        if(in.contains("127.0.0.1")) return result;
        if(in.indexOf("time") > 0){
            try{
                in = in.substring(in.indexOf("time")+5);
                in = in.substring(0, in.indexOf("ms"));
                in = in.trim();
                if (in.equals("0")) { result = -1; }
                else { result = (long) Double.parseDouble(in); }
            } catch (Exception e) {
                Log.e(LOG_TAG ,"parsePing exception "+e);
                handleError(e);
            }
        }
        return result;
    }

    public TestResult getResult() {
        return result;
    }
}
