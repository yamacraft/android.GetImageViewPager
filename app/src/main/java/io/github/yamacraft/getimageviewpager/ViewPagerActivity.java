package io.github.yamacraft.getimageviewpager;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

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
        }

        final GetImageFragmentPagerAdapter adapter
                = new GetImageFragmentPagerAdapter(getSupportFragmentManager(), images);
        mViewPager.setAdapter(adapter);
    }
}
