import android.content.Context;
import android.content.res.AssetManager;
import android.os.SystemClock;

import com.myapp.AiroObserver;
import com.myapp.common.Action1;
import com.myapp.model.TestResult;
import com.myapp.protobuffbackup.TestType;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

/**
 * Created by Jeff Jones on 1/26/2017.
 */

public class FTPTask extends BaseProtoBuffTask {

    private Context context;

    public enum FTP_TYPE{UP,DOWN,ROUND_TRIP};

    private final String deviceID;
    private final String wanServer;
    private final FTP_TYPE ftpType;

    public FTPTask(Context context,String wanServer, String deviceID, FTP_TYPE ftpType, Action1<TestResult> successCallback, Action1<Throwable> errorCallback) {
        super(TestType.TEST_FTP,deviceID,successCallback, errorCallback);
        this.wanServer = wanServer;
        this.deviceID = deviceID;
        this.ftpType = ftpType;
        this.context = context;
    }

    protected String RunTest() throws Exception {
        String user = "jefftesting1@luckmeup.com";
        String password = "impekable1";

        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(wanServer);
        boolean connectionSuccessful = ftpClient.login(user, password);
        if(!connectionSuccessful) return ftpClient.getReplyString();

        switch (ftpType){
            case UP:
                UpTest(ftpClient);
                break;
            case DOWN:
                DownTest(ftpClient);
                break;
            case ROUND_TRIP:
                RoundTripTest(ftpClient);
                break;
        }


        ftpClient.logout();
        ftpClient.disconnect();

        return ftpClient.getReplyString();
    }

    private boolean DownTest(FTPClient ftpClient) throws IOException {
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);


        String fileName = "jeffTest.txt";
        String data = "/sdcard/"+fileName;

        OutputStream out = new FileOutputStream(new File(data));
        boolean result = ftpClient.retrieveFile(fileName, out);
        out.close();
        return result;
    }

    private boolean UpTest(FTPClient ftpClient) throws IOException {
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        AssetManager assetsMan = context.getAssets();
        InputStream in = assetsMan.open("audioData.raw");
        String fileName = "jeffTest.txt";

        boolean result = ftpClient.storeFile("/" + fileName, in);
        in.close();
        return result;
    }

    private boolean RoundTripTest(FTPClient ftpClient) throws IOException {
        boolean upSuccessful = UpTest(ftpClient);
        boolean downSuccessful = DownTest(ftpClient);
        return upSuccessful? downSuccessful:false;
    }

}
