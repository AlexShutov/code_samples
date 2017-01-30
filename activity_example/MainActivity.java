package com.myapp.android.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.myapp.android.R;
import com.myapp.android.WorkforceApp;
import com.myapp.android.adapters.MainPagerAdapter;
import com.myapp.android.dialogs.CustomProgressDialog;
import com.myapp.android.fragments.JobMapFragment;
import com.myapp.android.fragments.JobNotesAndAssetsFragment;
import com.myapp.android.fragments.TabJobsFragment;
import com.myapp.android.globals.GlobalConstant;
import com.myapp.android.listeners.FragmentLifecycleListener;
import com.myapp.android.listeners.GCMArrivedListener;
import com.myapp.android.listeners.OnBackPressedListener;
import com.myapp.android.listeners.OnSignatureCompleteListener;
import com.myapp.android.listeners.PageSelectedListener;
import com.myapp.android.listeners.PhotoCropCompleteListener;
import com.myapp.android.models.DocumentData;
import com.myapp.android.models.JobData;
import com.myapp.android.models.MapData;
import com.myapp.android.preferences.AppPreference;
import com.myapp.android.utils.EmptyServiceConnection;
import com.myapp.android.utils.RestClientUtils;
import com.myapp.android.utils.Utils;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import io.intercom.android.sdk.Intercom;

public class MainActivity extends BaseActivity implements View.OnClickListener, FragmentLifecycleListener, ImageChooserListener {
    private CustomProgressDialog progressDialog;

    private static final int BACK_PRESS_TIME_INTERVAL = 3000;
    public final static int TAKE_GALLERY = 51;
    public final static int TAKE_CAMERA = 52;
    public final static int REQ_CODE_CROP = 40;
    public final static int REQ_SIGNATURE = 100;

    public static final int TAB_ID_JOBS = 0;

    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private int selectedTab = 0;
    ViewPager pager;
    MainPagerAdapter pagerAdapter;

    private long mPrevBackPressed = 0;

    private ImageView btnTabProfile, btnTabJobs;

    private LinearLayout layoutProfile;

    private List<JobData> jobsList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        doSelectTab(TAB_ID_JOBS);
        updateProfileUIs();

        checkGCMRegistration();
        doLoginForGCMRegistration();
        //BUILD.MANUFACTURER;

        registerGCMBroadcast();
    }

    private void initViews() {
        layoutProfile = (LinearLayout) findViewById(R.id.layoutProfile);
        btnTabJobs = (ImageView) findViewById(R.id.btnTabJobs);
        btnTabProfile = (ImageView) findViewById(R.id.btnTabProfile);

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(4);
        pager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                Fragment fragment = getRegisteredFragment(position);
                if (fragment instanceof PageSelectedListener) {
                    ((PageSelectedListener) fragment).onPageSelected();
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrollStateChanged(int position) {
                // TODO Auto-generated method stub
            }
        });

        btnTabProfile.setOnClickListener(this);
        btnTabJobs.setOnClickListener(this);
        findViewById(R.id.btnViewJobsOnMap).setOnClickListener(this);
    }

    private void doSelectTab(int tabNo){
        selectedTab = tabNo;
        pager.setCurrentItem(selectedTab);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnTabProfile:
                showProfileLayout();
                break;
            case R.id.btnViewJobsOnMap:
                goToMapFragmentForMultiJobs();
                break;
            case R.id.btnTabJobs:
                break;
            case R.id.btnLogout:
                doLogout();
                break;
        }
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public void onFragmentAttached(int idx, Fragment fragment) {
        // TODO Auto-generated method stub
        Log.i(getClass().getName(), "onAttached:" + idx);
        registeredFragments.append(idx, fragment);
    }

    @Override
    public void onFragmentDetached(int idx) {
        // TODO Auto-generated method stub
        registeredFragments.remove(idx);
    }

    @Override
    public void onFragmentResumed(int idx) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mGCMArrivedReceiver);
    }

    private boolean doBackPressed(){
        Fragment fragment = getRegisteredFragment(selectedTab);
        Log.i(getClass().getName(), "onBackPressed:" + fragment);
        if(fragment != null && fragment instanceof OnBackPressedListener) {
            return ((OnBackPressedListener)fragment).onBackPressed();
        }else{
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (!doBackPressed()) {
            if ((System.currentTimeMillis() - mPrevBackPressed) < BACK_PRESS_TIME_INTERVAL) {
                finish();
            } else {
                Toast.makeText(this, R.string.toast_msg_press_back_again, Toast.LENGTH_SHORT).show();
                mPrevBackPressed = System.currentTimeMillis();
            }
        }
    }

    public void setJobsList(List<JobData> dataList){
        this.jobsList = dataList;
    }

    public void goToMapFragmentForMultiJobs(){
        if (jobsList == null) return;

        Fragment fragment = getRegisteredFragment(selectedTab);
        MapData mapData = new MapData();
        mapData.setJobsList(jobsList);
        JobMapFragment frg = JobMapFragment.newInstance(mapData);
        int nCount = fragment.getChildFragmentManager().getBackStackEntryCount();
        fragment.getChildFragmentManager().beginTransaction().addToBackStack("job_map_" + nCount).add(R.id.fragmentTabJobs, frg, "job_map_" + nCount).commit();
    }

    public void goToMapFragmentForSingleJobDetail(View view, JobData jobData){
        Fragment fragment = getRegisteredFragment(selectedTab);
        MapData mapData = new MapData();
        mapData.setJobData(jobData);
        JobMapFragment frg = JobMapFragment.newInstance(mapData);
        int nCount = fragment.getChildFragmentManager().getBackStackEntryCount();
        //fragment.getChildFragmentManager().beginTransaction().addToBackStack("job_map_" + nCount).add(((ViewGroup)view.getParent()).getId(), frg, "job_map_" + nCount).commit();
        fragment.getChildFragmentManager().beginTransaction().addToBackStack("job_map_" + nCount).add(R.id.fragmentTabJobs, frg, "job_map_" + nCount).commit();
    }

    public void goToJobNotesAndAssetsFragment(JobData jobData){
        Fragment fragment = getRegisteredFragment(selectedTab);
        JobNotesAndAssetsFragment frg = JobNotesAndAssetsFragment.newInstance(jobData);
        int nCount = fragment.getChildFragmentManager().getBackStackEntryCount();
        fragment.getChildFragmentManager().beginTransaction().addToBackStack("job_notes_assets_" + nCount).add(R.id.fragmentTabJobs, frg, "job_notes_assets_" + nCount).commit();
    }

    /******************************** Profile Relative *********************************/
    private void doLogout(){
        WorkforceApp.getInstance().clearUser();
        AppPreference.setPin(MainActivity.this, "");

        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);

        // This reset's the Intercom SDK's cache of your user's identity and wipes the slate clean.
        Intercom.client().reset();

        finish();
    }

    private void updateProfileUIs(){
        ImageView imgProfile = (ImageView) layoutProfile.findViewById(R.id.imgProfile);
        ImageLoader.getInstance().displayImage(WorkforceApp.getInstance().getUser(false).getProfileImage(), imgProfile, WorkforceApp.getInstance().getDisplayOptions());
        TextView txtUsername = (TextView) layoutProfile.findViewById(R.id.txtUsername);
        txtUsername.setText(WorkforceApp.getInstance().getUser(false).getFullName());

        layoutProfile.findViewById(R.id.btnLogout).setOnClickListener(this);
    }

    private void showProfileLayout(){
        final boolean isShown = layoutProfile.getVisibility() == View.VISIBLE;

        btnTabProfile.setSelected(!isShown);

        AlphaAnimation alphaAnim = new AlphaAnimation(isShown? 1.0f:0.0f, isShown? 0.0f:1.0f);
        alphaAnim.setDuration(300);
        alphaAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        layoutProfile.startAnimation(alphaAnim);

        layoutProfile.postDelayed(new Runnable() {
            @Override
            public void run() {
                layoutProfile.setVisibility(isShown? View.INVISIBLE:View.VISIBLE);
            }
        }, 300);
    }

    ImageChooserManager imageChooserManager;
    public void doChoosePhoto(){
        try {
            imageChooserManager = new ImageChooserManager(this, ChooserType.REQUEST_PICK_PICTURE);
            imageChooserManager.setImageChooserListener(this);
            imageChooserManager.choose();
        }catch(Exception e){
            e.printStackTrace();
        }

		/*Intent intent = new Intent( Intent.ACTION_PICK ) ;
		intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE) ;
		startActivityForResult(intent, TAKE_GALLERY) ;*/
    }

    public void doTakePhoto(){
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE ) ;
        File file = new File( GlobalConstant.getCameraTempFilePath()) ;
        //it.putExtra(MediaStore.EXTRA_OUTPUT, tempPictuePath ) ;
        it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(it, TAKE_CAMERA);
    }

    public void goCrop(String path){
        Intent intent = new Intent(this, CropActivity.class);
        intent.putExtra("FILE_PATH", path);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, REQ_CODE_CROP);
    }

    public void goToSignaturePad(JobData jobData){
        Intent intent = new Intent(this, SignatureActivity.class);
        intent.putExtra("job_data", jobData);
        startActivityForResult(intent, REQ_SIGNATURE);
    }

    @Override
    public void onImageChosen(final ChosenImage chosenImage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (chosenImage != null) {
                    // Use the image
                    // chosenImage.getFilePathOriginal();
                    // chosenImage.getFileThumbnail();
                    // chosenImage.getFileThumbnailSmall();
                    dismissProgressDialog(progressDialog);
                    goCrop(chosenImage.getFileThumbnail());
                }
            }
        });
    }

    @Override
    public void onError(final String reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast("Can't load the image", Toast.LENGTH_SHORT);
                dismissProgressDialog(progressDialog);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == TAKE_GALLERY) {
            Uri imgUri = data.getData();
            String _strPhotoPath = Utils.getRealPathFromURI(this, imgUri);
            goCrop(_strPhotoPath);
        }else if (requestCode == ChooserType.REQUEST_PICK_PICTURE){
            progressDialog = showProgressDialog(progressDialog, getString(R.string.please_wait));
            imageChooserManager.submit(requestCode, data);
        }else if (requestCode == TAKE_CAMERA) {
            goCrop(GlobalConstant.getCameraTempFilePath());
        } else if (requestCode == REQ_CODE_CROP) {
            /*if (getRegisteredFragment(0) != null && getRegisteredFragment(0) instanceof PhotoCropCompleteListener){
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        //((PhotoCropCompleteListener) getRegisteredFragment(0)).onCropCompleted();
                    }
                });
            }*/
            TabJobsFragment fragment = (TabJobsFragment) getRegisteredFragment(selectedTab);
            Fragment curFragment = fragment.getCurrentFragment();
            if (curFragment instanceof PhotoCropCompleteListener){
                ((PhotoCropCompleteListener) curFragment).onCropCompleted();
            }
        } else if (requestCode == REQ_SIGNATURE){
            DocumentData document = (DocumentData) data.getExtras().get("document");
            TabJobsFragment fragment = (TabJobsFragment) getRegisteredFragment(selectedTab);
            Fragment curFragment = fragment.getCurrentFragment();
            if (curFragment instanceof OnSignatureCompleteListener){
                ((OnSignatureCompleteListener) curFragment).onSignatureCompleted(document);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //******************************** Registering GCM Token by Calling Login API again ******************************/
    private void doLoginForGCMRegistration(){
        try{
            JSONObject objParams = new JSONObject();
            objParams.put("email", AppPreference.getUserId(MainActivity.this));
            objParams.put("password", AppPreference.getUserPass(MainActivity.this));
            objParams.put("device_token", AppPreference.getGCMRegId(MainActivity.this));
            objParams.put("device_type", "Android");
            RestClientUtils.post(this, getString(R.string.PATH_USER_LOGIN), objParams, false, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i(getClass().getName(), "Login response:" + response.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, java.lang.String responseString, java.lang.Throwable throwable) {
                    Log.e(getClass().getName(), "Failed, code:" + statusCode + ", response:" + responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject obj) {
                    Log.e(getClass().getName(), "Failed, code:" + statusCode + ", response:" + obj);
                }
            });
        }catch(Exception e){}
    }

    /** Call when a gcm message is arrived
     * Added by Alex 2016/05/19 **/
    private void registerGCMBroadcast(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("gcm_arrived");
        registerReceiver(mGCMArrivedReceiver, intentFilter);
    }

    public BroadcastReceiver mGCMArrivedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("gcm_arrived")){
                Fragment fragment = getRegisteredFragment(0);
                if (fragment instanceof GCMArrivedListener){
                    ((GCMArrivedListener) fragment).onGCMArrived();
                }
            }
        }
    };
}
