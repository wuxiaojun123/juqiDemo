package cn.com.sfn.juqi.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.com.sfn.juqi.cache.BaseDiskCache;
import cn.com.sfn.juqi.cache.BaseImageDecoder;
import cn.com.sfn.juqi.net.MyHttpClient;
import cn.com.sfn.juqi.widgets.ActionSheetDialog;
import cn.com.sfn.juqi.widgets.MyProcessDialog;
import cn.com.wx.util.LogUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/*
 * 图片选择弹出框
 */
public class ChooseCameraPopuUtils {
    Activity activity;

    //    private File mFile;
    private Bitmap mBitmap;
    public static final String PIC_CAMERA_IMG_DIR = "hbysUpImg";
    public static final String PIC_CAMERA_IMG_NAME = "camera.jpg";
    /**
     * 调用摄像头或从相册选取照片
     */
    public static final int PIC_RROM_CAMERA = 13;
    public static final int PIC_RROM_PHONO = 14;
    public static final int PIC_RROM_VIDEO = 15;
    public static final int PIC_SIZE = 16;
    private String uploadType;
    public int flag;

    public ChooseCameraPopuUtils(Activity activity, String type) {
        this.activity = activity;
        this.uploadType = type;
    }

    public void showPopupWindow() {
        // 点击选择照片和拍照上传
        new ActionSheetDialog(activity)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {

                                // 调用系统摄像头，进行拍照
                                String status = Environment.getExternalStorageState();
                                if (status.equals(Environment.MEDIA_MOUNTED)) {
                                    try {
                                        File dir = new File(
                                                Environment.getExternalStorageDirectory() + "/"
                                                        + PIC_CAMERA_IMG_DIR);
                                        if (!dir.exists())
                                            dir.mkdirs();
                                        File f = new File(dir, PIC_CAMERA_IMG_NAME);// localTempImgDir和localTempImageFileName是自己定义的名字
                                        Uri u = Uri.fromFile(f);
                                        Intent intent = new Intent(
                                                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                                        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, u);

                                        activity.startActivityForResult(intent,
                                                PIC_RROM_CAMERA);

                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        ToastUtil.show(activity, "没有找到储存目录");

                                    }
                                } else {
                                    ToastUtil.show(activity, "没有储存卡");

                                }

                            }
                        })
                .addSheetItem("从相册选择", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                openImage();
                            }
                        }).show();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PIC_RROM_PHONO && resultCode == RESULT_OK) {
            // 获取选择本地的图片
            Uri selectedImageUri = data.getData();
            if (selectedImageUri == null) {
                ToastUtil.show(activity, "选择图片失败");
                return;
            }
            String file = getImageAbsolutePath(activity, selectedImageUri);
            if (file == null) {
                ToastUtil.show(activity, "选择图片失败");
                return;
            }
            extractPhoto(file);

        } else if (requestCode == PIC_RROM_CAMERA && resultCode == RESULT_OK) {
            // 获取拍照的图片
            File f = new File(Environment.getExternalStorageDirectory() + "/"
                    + PIC_CAMERA_IMG_DIR + "/"
                    + PIC_CAMERA_IMG_NAME);
            if (f != null) {
                // 上传图片
                extractPhoto(Environment.getExternalStorageDirectory() + "/"
                        + PIC_CAMERA_IMG_DIR + "/"
                        + PIC_CAMERA_IMG_NAME);
            }
        }
    }


    private void openImage() {
        try {
//				Uri uri =  Uri.parse("content://media/external/images/media/*");
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activity.startActivityForResult(intent,
                    PIC_RROM_PHONO);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

            Intent localIntent = new Intent();
            localIntent.setType("image/*");
            localIntent.setAction("android.intent.action.GET_CONTENT");
            Intent localIntent2 = Intent.createChooser(localIntent, "选择图片");
            activity.startActivityForResult(localIntent2,
                    PIC_RROM_PHONO);

        }
    }


    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     */

    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        final String scheme = imageUri.getScheme();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if (scheme == null) {

            return imageUri.getPath();

        } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(imageUri.getScheme())) {
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(imageUri.getScheme())) {

            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public void extractPhoto(String path) {
        Bitmap bmp;
        String fileName = null;
        try {
            bmp = new BaseImageDecoder(activity).calculateInSampleSize(path,
                    750);
            fileName = System.currentTimeMillis() + ".jpg";
            BaseDiskCache.getInstance(activity).save(fileName, bmp);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ToastUtil.show(activity, "存储图片失败");
            return;
        }
        BaseDiskCache cache = BaseDiskCache.getInstance(activity);
        File file = cache.get(fileName);
        LogUtils.e("文件名是:" + fileName + "------file=" + file);
        uploadHeadPhoto(file);
    }

    private void uploadHeadPhoto(File file) {
        if (file == null) {
            ToastUtil.show(activity, "请选择图片");
            return;
        }
//        MyProcessDialog.showDialog(activity, "正在上传...");
        MyHttpClient uploadClient = new MyHttpClient();
        String imgName = uploadClient.uploadFile(Config.URL_UPLOAD, uploadType, file);
//        MyProcessDialog.closeDialog();
        // 发送更新到个人首页
        if (onUploadImageListener != null) {
            onUploadImageListener.onLoadSucced(flag,imgName);
        }
    }

    public void uploadHeadPhoto1(File mFile) {

        if (mFile == null) {
            ToastUtil.show(activity, "请选择图片");
            return;
        }
        // 请求携带的参数
        Map<String, RequestBody> params = new HashMap<>();
        params.put("type", toRequestBody(uploadType));
//        params.put("key", toRequestBody(App.APP_CLIENT_KEY));

        // 上传的图片
        //设置Content-Type:application/octet-stream
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
        //设置Content-Disposition:form-data; name="photo"; filename="xuezhiqian.png"
        MultipartBody.Part photo = MultipartBody.Part.createFormData("file", mFile.getName(), photoRequestBody);

        MyProcessDialog.showDialog(activity, "正在上传...");
        /*PersonalNetwork.getResponseApi()
                .uploadImage(params, photo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<UploadHeadImageReponse>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        ToastUtils.show(activity, R.string.string_error);
                    }

                    @Override
                    public void onNext(UploadHeadImageReponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            resetFileAndBitmap();
                            if (response.data != null) {
                                LogUtils.e("返回上传图片的数据是：" + response.data.url + "====" + response.data.file_name);
                                // 发送更新到个人首页
                                if (onUploadImageListener != null) {
                                    onUploadImageListener.onLoadSucced(response.data.file_name, response.data.url);
                                }
                            }
                        } else {
                            ToastUtils.show(activity, response.msg);
                        }
                    }
                });*/

    }

    public RequestBody toRequestBody(String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }

    private void resetFileAndBitmap() {
//        mFile = null;
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    public interface OnUploadImageListener {
        void onLoadError();

        void onLoadSucced(int flag,String url);
    }

    OnUploadImageListener onUploadImageListener;

    public void
    setOnUploadImageListener(OnUploadImageListener onUploadImageListener) {
        this.onUploadImageListener = onUploadImageListener;
    }
}
