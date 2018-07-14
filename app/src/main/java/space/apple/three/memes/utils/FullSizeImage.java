package space.apple.three.memes.utils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import space.apple.three.memes.R;

public class FullSizeImage extends AppCompatActivity {
    ZoomageView fullImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_size_image);

        Intent intent = getIntent();
        String url = intent.getStringExtra("URL");
        fullImage = findViewById(R.id.fullImage);
        Picasso.with(FullSizeImage.this)
                .load(url)
                .into(fullImage);
    }
}
