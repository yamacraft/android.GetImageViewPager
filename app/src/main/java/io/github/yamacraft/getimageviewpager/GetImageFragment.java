package io.github.yamacraft.getimageviewpager;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

/**
 * 画像表示Fragment
 * Created by yamacraft on 2016/03/01.
 */
public class GetImageFragment extends Fragment {
    private static final String ARG_IMAGE_URL = GetImageFragment.class.getName() + ".image_url";

    @Bind(R.id.image_photo_view)
    protected PhotoView mImagePhotoView;

    @Bind(R.id.download_progress_bar)
    protected ProgressBar mDownloadProgressBar;

    private String mImageUrl;

    public static GetImageFragment newInstance(@Nullable String imageUrl) {
        GetImageFragment fragment = new GetImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_image, container, false);
        ButterKnife.bind(this, view);

        mDownloadProgressBar.setIndeterminate(true);

        GetImageAsyncTask getImageAsyncTask
                = new GetImageAsyncTask(new GetImageAsyncTask.OnDownloadedListener() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                mImagePhotoView.setImageBitmap(bitmap);
                mDownloadProgressBar.setVisibility(View.GONE);
            }
        });
        getImageAsyncTask.execute(mImageUrl);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mImageUrl = getArguments().getString(ARG_IMAGE_URL, "");
        }
    }
}
