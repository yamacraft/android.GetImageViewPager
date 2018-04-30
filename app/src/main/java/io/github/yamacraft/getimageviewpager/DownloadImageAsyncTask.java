package io.github.yamacraft.getimageviewpager;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import timber.log.Timber;

/**
 * 画像ダウンロードAsyncTask
 * Created by yamacraft on 2016/05/16.
 */
public class DownloadImageAsyncTask extends AsyncTask<String, Void, String> {

    public interface OnDownloadedListener {
        void onSuccess(String filename);

        void onFailure();
    }

    private OnDownloadedListener mLietener;

    public DownloadImageAsyncTask(OnDownloadedListener listener) {
        this.mLietener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        if (params == null || params.length < 1) {
            return null;
        }

        String url = params[0];

        Timber.d("create file");
        String filename = System.currentTimeMillis() + ".jpg";
        String folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .getAbsoluteFile() + "/GetImages/";
        File folderFile = new File(folderPath);
        if (!folderFile.exists()) {
            if (!folderFile.mkdirs()) {
                return null;
            }
        }

        Timber.d("download start");
        File outputFile = new File(folderPath + filename);
        if (!downloadFile(url, outputFile)) {
            return null;
        }

        return filename;
    }

    @Override
    protected void onPostExecute(String filename) {
        super.onPostExecute(filename);
        if (this.mLietener == null) {
            return;
        }
        if (filename == null) {
            Timber.d("onFailure");
            this.mLietener.onFailure();
            return;
        }
        Timber.d("onSuccess: %s", filename);
        this.mLietener.onSuccess(filename);
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
            Timber.d("getResponse Success");
            return response.body();
        } catch (IOException e) {
            Timber.e("IOException %s", e.getMessage());
            return null;
        }
    }

    /**
     * URL上のファイルを保存
     *
     * @param url    URL
     * @param output 保存先
     * @return true:成功
     */
    private static boolean downloadFile(@NonNull String url, File output) {
        ResponseBody response = getResponse(url);
        if (response == null) {
            return false;
        }
        BufferedSource in = null;
        BufferedSink out = null;
        try {

            in = response.source();
            out = Okio.buffer(Okio.sink(output));

            long contentLength = response.contentLength();
            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = in.read(data)) != -1) {
                total += count;
                Timber.v("%d / %d", contentLength, total);
                out.write(data, 0, count);
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.flush();
                    out.close();
                }
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
