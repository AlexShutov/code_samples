
import android.os.SystemClock;

import com.myapp.common.Action1;
import com.myapp.model.TestResult;
import com.myapp.protobuffbackup.TestType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Jeff Jones on 1/27/2017.
 */

public class URLTask  extends BaseProtoBuffTask{
    private final String wanServer;
    private final String deviceID;
    private TestResult result;

    public URLTask(String wanServer, String deviceID, Action1<TestResult> successCallback, Action1<Throwable> errorCallback) {
        super(TestType.TEST_URL,deviceID,successCallback, errorCallback);
        this.wanServer = wanServer;
        this.deviceID = deviceID;
    }


    public TestResult getResult() {
        return result;
    }

    protected String RunTest() throws Exception {
        URL url = new URL(wanServer);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        int responseCode = urlConnection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK){
            return ReadStream(urlConnection.getInputStream());
        }else{
            throw new Exception("Error:"+responseCode);
        }
    }

    private String ReadStream(InputStream inputStream) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();

    }
}
