package io.github.yamacraft.getimageviewpager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.start_button)
    protected void onClickStartButton() {
        Intent intent = new Intent(this, ViewPagerActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        startActivity(intent);
    }
}
