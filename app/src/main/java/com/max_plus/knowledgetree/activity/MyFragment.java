package com.max_plus.knowledgetree.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.max_plus.knowledgetree.R;
import com.max_plus.knowledgetree.tools.AllToast;
import com.max_plus.knowledgetree.tools.FaceUtil;
import com.max_plus.knowledgetree.tools.LoadingDailog;
import com.max_plus.knowledgetree.tools.NetworkUtils;
import com.max_plus.knowledgetree.tools.TakePhotoPopWin;
import com.max_plus.knowledgetree.view.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;
import static com.max_plus.knowledgetree.tools.FaceUtil.GALLERY;
import static com.max_plus.knowledgetree.tools.FaceUtil.REQUEST_CAMERA_IMAGE;
import static com.max_plus.knowledgetree.tools.FaceUtil.REQUEST_CROP_IMAGE;
import static com.max_plus.knowledgetree.tools.FaceUtil.REQUEST_PICTURE_CHOOSE;
import static com.max_plus.knowledgetree.tools.FaceUtil.getImagePath;

public class MyFragment extends Fragment implements View.OnClickListener {
    private View mRootView;
    private CircleImageView takePhoto;
    private Context mContext = this.getActivity();
    private WindowManager.LayoutParams params;
    private TakePhotoPopWin takePhotoPopWin;
    public static File mPictureFile;
    public static Bitmap mImage;
    public static byte[] mImageData;
    private String userName, password, token;
    Dialog loadingDailog;
    private String nickName, mobile, email;
    private String avatarPath;
    private Bitmap bitmap1;
    private TextView tv_nickName, tv_fixNickeName, tv_fixPassword, tv_set;
    public final static int FIX_NICK_NAME = 5;

    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        mRootView = layoutInflater.inflate(R.layout.fragment_my,
                (ViewGroup) getActivity().findViewById(R.id.fragment_container), false);
        initView();
    }

    private void initView() {
        takePhoto = mRootView.findViewById(R.id.iv_takePhoto);
        takePhoto.setOnClickListener(this);
        tv_nickName = mRootView.findViewById(R.id.tv_nickName);
        tv_fixNickeName = mRootView.findViewById(R.id.tv_fixNickeName);
        tv_fixNickeName.setOnClickListener(this);
        tv_fixPassword = mRootView.findViewById(R.id.tv_fixPassword);
        tv_fixPassword.setOnClickListener(this);
        tv_set = mRootView.findViewById(R.id.tv_set);
        tv_set.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_my, container, false);
        initView();
        initDate();
        return mRootView;
    }

    //获取个人信息
    private void initDate() {
        getUser();
        String url = NetworkUtils.returnUrl() + NetworkUtils.returnUserInfoApi() + "?token=" + token;
        Log.d("url..>>", url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                int code;
                try {
                    code = response.getInt("code");
                    if (code == 0) {
                        //保存用户详情
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        JSONObject jsonObject = response.getJSONObject("data");
                        Log.d("userInfo==>>>>", jsonObject.toString());
                        nickName = jsonObject.getString("username");
                        edit.putString("nickName", nickName);
                        mobile = jsonObject.getString("mobile");
                        edit.putString("mobile", mobile);
                        email = jsonObject.getString("email");
                        edit.putString("email", email);
                        avatarPath = jsonObject.getString("face");
                        edit.putString("avatarPath", avatarPath);
                        edit.commit();
                        //创建一个新线程，用于从网络上获取图片
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    getBitmap(avatarPath);
                                    Log.d("bitmap==>>", bitmap1.toString());
                                    takePhoto.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            takePhoto.setImageBitmap(bitmap1);
                                            Log.d("avatarPath.....>>>", avatarPath);
                                            if (!nickName.equals("null")) {
                                                tv_nickName.setText(nickName);
                                            }
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();//开启线程

                    } else {
                        AllToast.doToast(getActivity(), getString(R.string.sever_busy));
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                AllToast.doToast(getActivity(), getString(R.string.sever_busy));
                return;
            }

        });
    }

    //根据图片的URL路径来获取图片
    public Bitmap getBitmap(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            bitmap1 = BitmapFactory.decodeStream(inputStream);
            return bitmap1;
        }
        return null;

    }

    public void getUser() {
        SharedPreferences sp = getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE);
        userName = sp.getString("username", "");
        password = sp.getString("password", "");
        token = sp.getString("token", "");

    }

    //主页面点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_takePhoto:
                showPopFormBottom(mRootView);
                break;
            case R.id.tv_fixNickeName:
                Intent nickIntent = new Intent();
                nickIntent.setClass(getActivity(), FixNickNameActivity.class);
                startActivityForResult(nickIntent, FIX_NICK_NAME);
                break;
            case R.id.tv_fixPassword:
                Intent fpIntent = new Intent();
                fpIntent.setClass(getActivity(), FixPasswordActivity.class);
                startActivity(fpIntent);
                break;
            case R.id.tv_set:
                Intent setIntent = new Intent();
                setIntent.setClass(getActivity(), SetActivity.class);
                startActivity(setIntent);
        }
    }

    private void showPopFormBottom(View view) {

        takePhotoPopWin = new TakePhotoPopWin(mContext, itemsOnclick);
        //        设置Popupwindow显示位置（从底部弹出）
        takePhotoPopWin.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        params = getActivity().getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha = 0.7f;
        getActivity().getWindow().setAttributes(params);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        takePhotoPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params = getActivity().getWindow().getAttributes();
                params.alpha = 1f;
                getActivity().getWindow().setAttributes(params);
            }
        });

    }

    ////为PopupWindow三个按钮设置点击事件
    private View.OnClickListener itemsOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            takePhotoPopWin.dismiss();
            switch (view.getId()) {
                case R.id.take_phone:
                    mPictureFile = new File(Environment.getExternalStorageDirectory(),
                            "picture" + System.currentTimeMillis() / 1000 + ".jpg");
                    // 启动拍照,并保存到临时文件
                    Intent mIntent = new Intent();
                    mIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                    mIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileProvider", mPictureFile));
                    mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPictureFile));
                    mIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                    startActivityForResult(mIntent, REQUEST_CAMERA_IMAGE);
                    break;
                case R.id.choose_album:
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(intent, REQUEST_PICTURE_CHOOSE);
                    break;
                case R.id.choose_system:
                    break;
            }
        }
    };

    //处理返回
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode===////", String.valueOf(requestCode));
        takePhoto = mRootView.findViewById(R.id.iv_takePhoto);
        takePhoto.setOnClickListener(this);
        //从相册里选
        if (resultCode == RESULT_OK && requestCode == GALLERY) {
            Uri selectedImage = data.getData();
            String selectedString = selectedImage.toString();
            if (selectedString.indexOf("/document/") > 0 && selectedString.indexOf("%3A") > 0) {
                String id = selectedString.split("%3A")[1];
                selectedImage = Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "/" + id);
            }

            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            if (picturePath == null) {
                AllToast.doToast(mContext, "请从相册里选择");
            } else {
                cursor.close();
            }
        }


        //从相册选取
        if (requestCode == REQUEST_PICTURE_CHOOSE && resultCode == RESULT_OK) {


            dealPic(getActivity(), takePhoto, REQUEST_PICTURE_CHOOSE, RESULT_OK, data);


        }
        //拍照
        if (requestCode == REQUEST_CAMERA_IMAGE && resultCode == RESULT_OK
                ) {

            dealPic(getActivity(), takePhoto, REQUEST_CAMERA_IMAGE, RESULT_OK, data);

        }

        //图片缩放完后
        if (requestCode == REQUEST_CROP_IMAGE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Log.d("extras==.>>>>", extras.toString());
            if (extras != null) {
                dealPic(getActivity(), takePhoto, REQUEST_CROP_IMAGE, RESULT_OK, data);
            }


        }
        //返回修改的昵称
        if (requestCode == FIX_NICK_NAME && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            nickName = bundle.getString("nikeName");
            SharedPreferences s = getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE);
            SharedPreferences.Editor edit = s.edit();
            edit.putString("nickName", nickName);
            edit.commit();
            new Thread() {
                @Override
                public void run() {
                    Message message = new Message();
                    Bundle b = new Bundle();
                    b.putString("nickName", nickName);
                    message.setData(b);
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }.start();

        }
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Bundle bundle = msg.getData();
                    String n = bundle.getString("nickName");
                    tv_nickName.setText(n);
                    break;
            }
        }
    };

    /**
     * 处理拍照、选择图片、裁剪的回调
     *
     * @param activity
     * @param imageView
     * @param requestCode
     * @param resultCode
     * @param data
     */

    public void dealPic(Activity activity, ImageView imageView, int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Log.e("FaceUtil", "未完成");
            return;
        }
        Log.e("FaceUtil", "完成" + requestCode);
        String fileSrc = null;
        if (requestCode == REQUEST_PICTURE_CHOOSE) {
            if ("file".equals(data.getData().getScheme())) {
                // 有些低版本机型返回的Uri模式为file
                fileSrc = data.getData().getPath();
            } else {
                // Uri模型为content
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = activity.getContentResolver().query(data.getData(), proj,
                        null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                fileSrc = cursor.getString(idx);
                cursor.close();
            }
            // 跳转到图片裁剪页面
            cropPicture(activity, Uri.fromFile(new File(fileSrc)));
//            cropPicture(activity, FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileProvider", new File(fileSrc)));
        } else if (requestCode == REQUEST_CAMERA_IMAGE) {
            if (null == mPictureFile) {
//                        showTip("拍照失败，请重试");
                Log.e("FaceUtil", "拍照失败，请重试");
                return;
            }
            Log.e("FaceUtil", "拍照成功");
            fileSrc = mPictureFile.getAbsolutePath();
//                    updateGallery(fileSrc);
            // 跳转到图片裁剪页面
            Log.e("FaceUtil", "跳转裁剪界面");
            cropPicture(activity, Uri.fromFile(new File(fileSrc)));
//            cropPicture(activity, FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileProvider", new File(fileSrc)));
        } else if (requestCode == REQUEST_CROP_IMAGE) {
            Log.e("FaceUtil", "图片剪裁成功！");
            // 获取返回数据
            Bitmap bmp = data.getParcelableExtra("data");
            // 若返回数据不为null，保存至本地，防止裁剪时未能正常保存
            if (null != bmp) {
                FaceUtil.saveBitmapToFile(activity, bmp);
            }
            // 获取图片保存路径
            fileSrc = getImagePath(activity);
            // 获取图片的宽和高
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Log.e("FaceUtil", "图片信息路径！" + fileSrc);
            mImage = BitmapFactory.decodeFile(fileSrc, options);

            // 压缩图片
            options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
                    (double) options.outWidth / 1024f,
                    (double) options.outHeight / 1024f)));
            options.inJustDecodeBounds = false;
            mImage = BitmapFactory.decodeFile(fileSrc, options);

            // 若mImageBitmap为空则图片信息不能正常获取
            if (null == mImage) {
//                        showTip("图片信息无法正常获取！");
                Log.e("FaceUtil", "图片信息无法正常获取！");
                return;
            }

            // 部分手机会对图片做旋转，这里检测旋转角度
            int degree = FaceUtil.readPictureDegree(fileSrc);
            if (degree != 0) {
                // 把图片旋转为正的方向
                mImage = FaceUtil.rotateImage(degree, mImage);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            //可根据流量及网络状况对图片进行压缩
            mImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            mImageData = baos.toByteArray();
            Log.e("mImage==>>>", mImage.toString());
            doUploadAvatar(imageView, mImage, fileSrc);
        }
        mPictureFile = null;
        mImage = null;
        mImageData = null;
    }

    /**
     * 上传头像
     *
     * @param fileSrc
     */
    private void doUploadAvatar(final ImageView imageView, final Bitmap mImage, String fileSrc) {
        loadingDailog = LoadingDailog.LoadingDailog(getActivity(), getString(R.string.upload_avatar));
        loadingDailog.show();
        getUser();
        String url = NetworkUtils.returnUrlForAvatar() + NetworkUtils.returnGetPath();
        Log.d("url==>>>>", url);
        File file = new File(fileSrc);
        Log.d("file==.....>>>", String.valueOf(file.length() / 8 / 1024) + "kb");
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            params.put("face", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
                Log.d("doUploadAvatar.>>>", responseString);
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int code = response.getInt("code");
                    Log.d("doUploadAvatar.response....>>>", response.toString());
                    if (code == 0) {
                        try {
                            Thread.sleep(2 * 1000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        loadingDailog.dismiss();
                        AllToast.doToast(getActivity(), "上传头像成功");
                        imageView.setImageBitmap(mImage);
                    } else {
                        AllToast.doToast(getActivity(), getString(R.string.sever_busy));
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDailog.dismiss();
                AllToast.doToast(getActivity(), getString(R.string.sever_busy));
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                loadingDailog.dismiss();
                AllToast.doToast(getActivity(), getString(R.string.sever_busy));
                return;
            }
        });

    }

    /***
     * 裁剪图片
     * @param activity Activity
     * @param uri 图片的Uri
     */
    public void cropPicture(Activity activity, Uri uri) {
        Intent innerIntent = new Intent("com.android.camera.action.CROP");
        innerIntent.setDataAndType(uri, "image/*");
        innerIntent.putExtra("crop", "true");// 才能出剪辑的小方框，不然没有剪辑功能，只能选取图片
        innerIntent.putExtra("aspectX", 1); // 放大缩小比例的X
        innerIntent.putExtra("aspectY", 1);// 放大缩小比例的X   这里的比例为：   1:1
        innerIntent.putExtra("outputX", 320);  //这个是限制输出图片大小
        innerIntent.putExtra("outputY", 320);
        innerIntent.putExtra("return-data", true);
        // 切图大小不足输出，无黑框
        innerIntent.putExtra("scale", true);
        innerIntent.putExtra("scaleUpIfNeeded", true);
        Log.e("FaceUtil", "图片path:" + getImagePath(activity.getApplicationContext()));
        File imageFile = new File(getImagePath(activity.getApplicationContext()));
        innerIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
//        innerIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileProvider", imageFile));
        innerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(innerIntent, REQUEST_CROP_IMAGE);
    }
}
