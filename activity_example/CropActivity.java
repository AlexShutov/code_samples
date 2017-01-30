package com.myapp.android.activities;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.edmodo.cropper.CropImageView;
import com.myapp.android.R;
import com.myapp.android.globals.GlobalConstant;
import com.myapp.android.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class CropActivity extends BaseActivity implements OnClickListener {
	@SuppressWarnings("unused")
	private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
	@SuppressWarnings("unused")
	private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
	@SuppressWarnings("unused")
	private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
	@SuppressWarnings("unused")
	private static final int ON_TOUCH = 1;
	@SuppressWarnings("unused")
	private static final int ROTATE_NINETY_DEGREES = 90;
	Bitmap bitImage;
	private CropImageView imgView;
	private int mAspectRatioX = 10;
	private int mAspectRatioY = 10;

	private String path = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crop);

		this.imgView = ((CropImageView) findViewById(R.id.cropImageView));
		this.imgView.setGuidelines(2);

		this.imgView.setFixedAspectRatio(false);
		this.imgView.setAspectRatio(mAspectRatioX, mAspectRatioY);
		
		path = getIntent().getExtras().getString("FILE_PATH");
		setImageBitmap();

        findViewById(R.id.btnBack).setOnClickListener(this);
        findViewById(R.id.btnDone).setOnClickListener(this);
	}

	private void setImageBitmap(){
		File file = new File(path);
		long length = file.length();
		length = length / (1024L * 1024L);
		if (length > 4) {
			try {
				Bitmap _bmpResized = Utils.getSafeDecodeBitmap(path,
						1400);
				File file_new = new File(
						GlobalConstant.getLargeImageTempFilePath());
				FileOutputStream out = new FileOutputStream(file_new);
				_bmpResized.compress(CompressFormat.JPEG, 100, out);
				path = GlobalConstant.getLargeImageTempFilePath();
				_bmpResized.recycle();
				_bmpResized = null;
				
				imgView.setImageBitmap(Utils.getSafeDecodeBitmap(path, 2000));
			} catch (Exception e) {
			}
		} else {
			imgView.setImageBitmap(Utils.getSafeDecodeBitmap(path, 2000));
		}
	}
	
	private void doCropAndUpload() {
		bitImage = this.imgView.getCroppedImage();

		// Saving Image to Crop Path
		try {
			byte[] byteArray = bitmapToByteArray(bitImage);
			File file = new File(GlobalConstant.getCropTempFilePath());

			FileOutputStream fos = new FileOutputStream(file);
			fos.write(byteArray);
			fos.flush();
			fos.close();

			bitImage.recycle();
			bitImage = null;

			setResult(RESULT_OK);
			finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public byte[] bitmapToByteArray(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.btnDone:
			doCropAndUpload();
			break;
		}
	}

	protected void onRestoreInstanceState(Bundle paramBundle) {
		super.onRestoreInstanceState(paramBundle);
		this.mAspectRatioX = paramBundle.getInt("ASPECT_RATIO_X");
		this.mAspectRatioY = paramBundle.getInt("ASPECT_RATIO_Y");
	}

	protected void onSaveInstanceState(Bundle paramBundle) {
		super.onSaveInstanceState(paramBundle);
		paramBundle.putInt("ASPECT_RATIO_X", this.mAspectRatioX);
		paramBundle.putInt("ASPECT_RATIO_Y", this.mAspectRatioY);
	}
}
