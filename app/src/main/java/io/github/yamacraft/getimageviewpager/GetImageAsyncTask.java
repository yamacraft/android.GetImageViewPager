package io.github.yamacraft.getimageviewpager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import timber.log.Timber;

/**
 * 画像取得AsyncTask
 * Created by yamacraft on 2016/03/04.
 */
public class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
    private static final long MAX_IMAGE_SIZE = 1920 * 1080;

    public interface OnDownloadedListener {
        void onSuccess(@Nullable Bitmap bitmap);
    }

    private OnDownloadedListener mListener;

    public GetImageAsyncTask(OnDownloadedListener listener) {
        this.mListener = listener;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        if (params == null || params.length < 1) {
            return null;
        }
        String url = params[0];
        return downloadFile(url);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (mListener != null) {
            mListener.onSuccess(bitmap);
        }
    }

    /**
     * 画像取得
     *
     * @param url 取得先URL
     * @return リサイズ済みBitmap画像
     */
    @WorkerThread
    private static Bitmap downloadFile(@NonNull String url) {
        ResponseBody response = getResponse(url);
        if (response == null) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(response.byteStream(), null, options);
        Timber.d("original image w:%d h:%d", options.outWidth, options.outHeight);

        options.inSampleSize = 1;
        while ((options.outWidth * options.outHeight) / options.inSampleSize > MAX_IMAGE_SIZE * 4) {
            options.inSampleSize *= 2;
        }
        Timber.d("options.inSampleSize: %d", options.inSampleSize);

        options.inJustDecodeBounds = false;
        response = getResponse(url);
        if (response == null) {
            return null;
        }

        Bitmap bitmap = BitmapFactory.decodeStream(response.byteStream(), null, options);
        response.close();
        return bitmap;
    }

    /**
     * APIからレスポンスを取得する
     *
     * @param url リクエストURL
     * @return レスポンス
     */
    private static ResponseBody getResponse(@NonNull String url) {
        Timber.d("get url:%s", url);
        Request request = new Request.Builder().url(url).get().build();
        try {
            Response response = new OkHttpClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                Timber.d("response error %d: %s", response.code(), response.message());
                return null;
            }
            return response.body();
        } catch (IOException e) {
            Timber.e("IOException %s", e.getMessage());
            return null;
        }
    }
}
