package io.github.yamacraft.getimageviewpager;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * 画像のページ表示用FragmentStatePagerAdapter
 * Created by yamacraft on 2016/03/02.
 */
public class GetImageFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> images;

    public GetImageFragmentPagerAdapter(FragmentManager fm, @NonNull ArrayList<String> images) {
        super(fm);
        this.images = images;
    }

    @Override
    public Fragment getItem(int position) {
        String imageUrl = (images != null && images.size() > position) ? images.get(position) : "";
        return GetImageFragment.newInstance(imageUrl);
    }

    @Override
    public int getCount() {
        return images.size();
    }
}
