package space.apple.three.memes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import space.apple.three.memes.R;

public class FullSizeImage extends AppCompatActivity {

    ImageView fullImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_size_image);

        Intent intent = getIntent();
        String url = intent.getStringExtra("URL");

        fullImage = findViewById(R.id.fullImage);

    /*    String url="https://scontent.fdel8-1.fna.fbcdn.net/v/t1.0-9/23794839_1285584514920595_6188564196566988235_n.jpg?oh=ec4573ec0aee0298200c7a8862d66483&oe=5ABCA243";
        String url1="https://scontent.fdel8-1.fna.fbcdn.net/v/t1.0-9/25299138_1643898825691301_624863954397815819_n.jpg?oh=1a216fe11d4e592cf483eaa0df125889&oe=5AFBF64D";
*/

        Picasso.with(FullSizeImage.this)
                .load(url)
                .into(fullImage);

    }
}