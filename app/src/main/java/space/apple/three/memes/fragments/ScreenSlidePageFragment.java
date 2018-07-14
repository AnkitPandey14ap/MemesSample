package space.apple.three.memes.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.io.File;
import java.util.ArrayList;

import space.apple.three.memes.R;
import space.apple.three.memes.utils.FullSizeImage;
import space.apple.three.memes.data_manager.DataManager;
import space.apple.three.memes.data_manager.SharedPref;
import space.apple.three.memes.model.Meme;
import space.apple.three.memes.model.RowPostion;

import static space.apple.three.memes.utils.Constants.POSITION;
import static space.apple.three.memes.activities.SplashActivity.urls;

public class ScreenSlidePageFragment extends Fragment {
    private int position = 0;
    private ArrayList<Meme> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.image_view, container, false);
        arrayList = new ArrayList<>();
        arrayList = urls;

        Bundle bundle = getArguments();
        if (bundle != null) {
            position = bundle.getInt(POSITION);
        }
        setUpUI(rootView);

        return rootView;
    }

    private void setUpUI(View view) {
        ImageView mImageView = view.findViewById(R.id.imageView);
        ImageView ivShare = view.findViewById(R.id.share_image_view);
        ImageView downloadAction = view.findViewById(R.id.downloadAction);
        final TextView likeCount = view.findViewById(R.id.likeCount);
        SparkButton likeButton = view.findViewById(R.id.spark_button);
        likeButton.setChecked(false);

        SharedPref sp = new SharedPref(getContext());
        if (sp.isLiked(arrayList.get(position).getRef())) {
            likeButton.setChecked(true);
        }

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImage(position);
            }
        });
        Picasso.with(getContext())
                .load(arrayList.get(position).getUrl())
                .into(mImageView);
        RowPostion.pos = position;

        downloadAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                    Toast.makeText(getContext(), "Permission issue.", Toast.LENGTH_SHORT).show();
                    return;
                }
                fileDownload(arrayList.get(position).getUrl(), position);

            }
        });
        //start fullSizeActivity on click
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FullSizeImage.class);
                intent.putExtra("URL", arrayList.get(position).getUrl());
                getContext().startActivity(intent);
            }
        });
        likeCount.setText("(" + arrayList.get(position).getLike() + ")");

        likeButton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (buttonState) {
                    // Button is active
                    SharedPref sp = new SharedPref(getContext());
                    sp.saveLike(arrayList.get(position).getRef());

                    new DataManager(getContext(), arrayList.get(position)).increaseLike();
                    int like = Integer.parseInt(arrayList.get(position).getLike()) + 1;
                    likeCount.setText("(" + String.valueOf(like) + ")");

                } else {
                    // Button is inactive
                    SharedPref sp = new SharedPref(getContext());
                    sp.saveDislike(arrayList.get(position).getRef());
                    new DataManager(getContext(), arrayList.get(position)).decreaseLike();
                    int like = Integer.parseInt(arrayList.get(position).getLike());
                    likeCount.setText("(" + String.valueOf(like) + ")");
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });
    }

    private void shareImage(int position) {
        Picasso.with(getContext())
                .load(arrayList.get(position).getUrl())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

                        String shareBody = "For more funny Memes and Images download iMEMES android app ";
                        shareBody = shareBody + "https://play.google.com/store/apps/details?id=space.apple.three.memes \n\n";


                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, shareBody);

                        String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, "", null);
                        Uri screenshotUri = Uri.parse(path);


                        intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                        intent.setType("image/*");
                        getContext().startActivity(Intent.createChooser(intent, "Share image via..."));


                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

    }

    private void fileDownload(String uRl, int position) {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/dhaval_files");

        if (!direct.exists()) {
            direct.mkdirs();
        }
        Toast.makeText(getContext(), "Downloading....", Toast.LENGTH_SHORT).show();

        DownloadManager mgr = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("Demo" + position)
                .setDescription("Something useful. No, really.")
                .setDestinationInExternalPublicDir("/iMemes", "iMemes.jpg");

        mgr.enqueue(request);

    }


    public static ScreenSlidePageFragment newInstance() {

        Bundle args = new Bundle();

        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        fragment.setArguments(args);
        return fragment;
    }
}