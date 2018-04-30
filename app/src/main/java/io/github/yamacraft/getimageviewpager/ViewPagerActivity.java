package io.github.yamacraft.getimageviewpager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * ViewPagerによる画像大量表示Activity
 * Created by yamacraft on 2016/03/01.
 */
public class ViewPagerActivity extends AppCompatActivity {

    @Bind(R.id.view_pager)
    protected ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        ButterKnife.bind(this);

        ArrayList<String> images = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            images.add("http://yamacraft.github.io/img/samples/002.jpg");
            //images.add("http://yamacraft.github.io/img/app_dic128.png");
        }

        final GetImageFragmentPagerAdapter adapter
                = new GetImageFragmentPagerAdapter(getSupportFragmentManager(), images);
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Timber.d("Permission OK");
                return;
            }
            Timber.d("Permission No!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viewpager, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_download) {
            int permissionCheck = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            Timber.d("permission:%d", permissionCheck);
            if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Timber.d("shouldShowRequestPermissionRationale");
                    Toast.makeText(this, "ストレージ許可がないとダウンロードできません", Toast.LENGTH_SHORT).show();
                    return true;
                }
                ActivityCompat.requestPermissions(this,
                        new String []{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);
                return true;
            }

            // ダウンロード実行
            Timber.d("Start Download");
            DownloadImageAsyncTask task = new DownloadImageAsyncTask(new DownloadImageAsyncTask.OnDownloadedListener() {
                @Override
                public void onSuccess(String filename) {
                    Toast.makeText(ViewPagerActivity.this, String.format("success:%s", filename), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure() {
                    Toast.makeText(ViewPagerActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                }
            });
            task.execute("http://yamacraft.github.io/img/samples/002.jpg");

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
