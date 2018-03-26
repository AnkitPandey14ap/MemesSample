package space.apple.three.memes;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.io.File;
import java.util.ArrayList;

import space.apple.three.memes.model.Meme;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Meme> urls;
    private ArrayList<String> keyList;
    private LayoutInflater layoutInflater;

    DownloadManager downloadManager;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Meme> myDataset, ArrayList<String> keyList, Context context) {
        layoutInflater = LayoutInflater.from(context);
        urls = myDataset;
        this.context = context;
        this.keyList = keyList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v =layoutInflater.inflate(R.layout.image_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mImageView.setImageBitmap(BitmapFactory.decodeFile(urls.get(position)));


        SharedPref sp = new SharedPref(context);
        if(sp.isLiked(urls.get(position).getRef())){
            holder.likeButton.setChecked(true);
        }

        holder.idTV.setText("Ref: #"+urls.get(position).getRef());
        Picasso.with(context)
                .load(urls.get(position).getUrl())
                .into(holder.mImageView);

        holder.downloadAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

                    Toast.makeText(context, "Permission issue.", Toast.LENGTH_SHORT).show();
                    return;
                }
                file_download(urls.get(position).getUrl());

            }
        });
        //start fullSizeActivity on click
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,FullSizeImage.class);
                intent.putExtra("URL", urls.get(position).getUrl());
                context.startActivity(intent);
            }
        });

        holder.likeButton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (buttonState) {
                    // Button is active
                    SharedPref sp = new SharedPref(context);
                    sp.saveLike(urls.get(position).getRef());

                    new DataManager(context,urls.get(position)).increaseLike();

                    Snackbar snackbar = Snackbar.make(button, "liked", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    // Button is inactive
                    SharedPref sp = new SharedPref(context);
                    sp.saveDislike(urls.get(position).getRef());
                    new DataManager(context,urls.get(position)).decreaseLike();



                    Snackbar snackbar = Snackbar.make(button, "dislike", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });


//        Picasso.with(context).load(urls.get(position)).into(holder.mImageView);
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return urls.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a Image in this case
        public SquareImageView mImageView;
        public TextView idTV;
        public ImageView downloadAction;

        public SparkButton likeButton;


        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (SquareImageView) itemView.findViewById(R.id.imageView);
            idTV =  itemView.findViewById(R.id.idTV);

            downloadAction = itemView.findViewById(R.id.downloadAction);
            likeButton = itemView.findViewById(R.id.spark_button);

            likeButton.setChecked(false);


        }
    }

    public void file_download(String uRl) {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/dhaval_files");

        if (!direct.exists()) {
            direct.mkdirs();
        }
        Toast.makeText(context, "Downloading....", Toast.LENGTH_SHORT).show();

        DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);
        
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("Demo")
                .setDescription("Something useful. No, really.")
                .setDestinationInExternalPublicDir("/iMemes", "iMemes.jpg");

        mgr.enqueue(request);

    }
}