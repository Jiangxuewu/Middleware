package com.j.widget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.SpinnerAdapter;

import com.j.R;
import com.j.utils.FileUtils;

public class MyCamera extends BaseWidget implements OnClickListener, Callback,
		PictureCallback, SpinnerAdapter {

	private SurfaceView cameraSurfaceView;
	private Gallery imgsGallery;
	private Button phoBtn, changeBtn;
	private Camera camera;
	private Parameters parameters;
	private boolean isFrontCamera = false;// 当前显示的是前置摄像头还是后置摄像头
	private int rotation;

	private final static int TAKE_PICTURE = 1000;
	private final static int CHANGE_CAMERA = 1001;

	public MyCamera(Context context) {
		this(context, null);
	}

	public MyCamera(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.camera_layout, this);
		cameraSurfaceView = (SurfaceView) findViewById(R.id.camera_surface_view);
		imgsGallery = (Gallery) findViewById(R.id.gallery_view);
		phoBtn = (Button) findViewById(R.id.camera_btn);
		changeBtn = (Button) findViewById(R.id.camera_change);
		phoBtn.setTag(TAKE_PICTURE);
		changeBtn.setTag(CHANGE_CAMERA);
		phoBtn.setOnClickListener(this);
		changeBtn.setOnClickListener(this);

		initSurfaceView();
	}
	
	public void destory(){
		if (null != camera) {
			camera.release();
			camera = null;
		}
		if (null != cameraSurfaceView) {
			cameraSurfaceView.clearAnimation();
			cameraSurfaceView.clearFocus();
			cameraSurfaceView.destroyDrawingCache();
			cameraSurfaceView = null;
		}
		setVisibility(View.GONE);
	}

	public void init() {
		if (null == cameraSurfaceView) {
			cameraSurfaceView = (SurfaceView) findViewById(R.id.camera_surface_view);
		}
		initSurfaceView();
	}

	private void updateGallery() {
		imgsGallery.setAdapter(this);
	}

	private void initSurfaceView() {
		cameraSurfaceView.getHolder().setType(
				SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		cameraSurfaceView.getHolder().setFixedSize(176, 144);
		cameraSurfaceView.getHolder().setKeepScreenOn(true);
		cameraSurfaceView.getHolder().addCallback(this);
	}
	

	@Override
	public void onClick(View v) {
		int key = (Integer) v.getTag();
		switch (key) {
		case TAKE_PICTURE:
			takePicture();
			break;
		case CHANGE_CAMERA:
			changeCamera();
			break;

		default:
			break;
		}
	}

	private void changeCamera() {
		int count = Camera.getNumberOfCameras();// 2.3.3上才能用
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		for (int camIdx = 0; camIdx < count; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo);
			if (!isFrontCamera) {
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
					try {
						if (camera != null) {
							camera.release(); // 释放照相机
							camera = null;
						}
						camera = Camera.open(camIdx);
						camera.setPreviewDisplay(cameraSurfaceView.getHolder());
						camera.setDisplayOrientation(getPreviewDegree());
						camera.startPreview();
						isFrontCamera = !isFrontCamera;
						return;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
					try {
						if (camera != null) {
							camera.release(); // 释放照相机
							camera = null;
						}
						camera = Camera.open(camIdx);
						camera.setPreviewDisplay(cameraSurfaceView.getHolder());
						camera.setDisplayOrientation(getPreviewDegree());
						camera.startPreview();
						isFrontCamera = !isFrontCamera;
						return;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		}
	}

	private void takePicture() {
		if (null == camera) {
			camera = Camera.open();
		}
		if (null != camera) {
			phoBtn.setVisibility(View.GONE);
			camera.takePicture(null, null, this);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		createCamera(holder);
	}

	private void createCamera(SurfaceHolder holder) {
		try {
			if (null == camera) {
				camera = Camera.open();
			}
			camera.setPreviewDisplay(holder);
			camera.setDisplayOrientation(getPreviewDegree());
			camera.startPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int getPreviewDegree() {
		rotation = ((Activity) getContext()).getWindowManager()
				.getDefaultDisplay().getRotation();
		int degree = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degree = 90;
			break;
		case Surface.ROTATION_90:
			degree = 180;
			break;
		case Surface.ROTATION_180:
			degree = 270;
			break;
		case Surface.ROTATION_270:
			degree = 180;
			break;

		default:
			break;
		}
		return degree;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		parameters = camera.getParameters(); // 获取各项参数
		parameters.setPictureFormat(PixelFormat.JPEG); // 设置图片格式
		parameters.setPreviewSize(width, height); // 设置预览大小
		parameters.setPreviewFrameRate(5); // 设置每秒显示4帧
		parameters.setPictureSize(width, height); // 设置保存的图片尺寸
		parameters.setJpegQuality(80); // 设置照片质量

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (camera != null) {
			camera.release(); // 释放照相机
			camera = null;
		}

	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		try {
			saveToSDCard(data); // 保存图片到sd卡中
			phoBtn.setVisibility(View.VISIBLE);
			destory();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveToSDCard(byte[] data) throws IOException {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
		String filename = format.format(date) + ".jpg";
		File fileFolder = new File(FileUtils.getSaveFilePath());
		if (!fileFolder.exists()) {
			fileFolder.mkdir();
		}
		File jpgFile = new File(fileFolder, filename);
		FileOutputStream outputStream = new FileOutputStream(jpgFile); // 文件输出流
		outputStream.write(data); // 写入sd卡中
		outputStream.close(); // 关闭输出流
		if (null != doneListener) {
			doneListener.done(jpgFile.getPath());
		}
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	private OnDoneListener doneListener;

	public void setDoneListener(OnDoneListener doneListener) {
		this.doneListener = doneListener;
	}

	public interface OnDoneListener {
		public void done(String filePath);
	}

}
