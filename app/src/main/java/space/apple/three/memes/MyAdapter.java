package space.apple.three.memes;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context context;
//    private ArrayList<String> mDataset;
    private HashMap<String,String> mDataset;
    private ArrayList<String> keyList;
    private LayoutInflater layoutInflater;

    DownloadManager downloadManager;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(HashMap<String, String> myDataset, ArrayList<String> keyList, Context context) {
        layoutInflater = LayoutInflater.from(context);
        mDataset = myDataset;
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mImageView.setImageBitmap(BitmapFactory.decodeFile(mDataset.get(position)));


        holder.idTV.setText("Ref: #"+keyList.get(position));
        Picasso.with(context)
                .load(mDataset.get(keyList.get(position)))
                .into(holder.mImageView);

        holder.downloadAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*downloadManager=(DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(mDataset.get(position));
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Long refrence = downloadManager.enqueue(request);
*/
//                file_download(mDataset.get(position));
                file_download(mDataset.get(keyList.get(position)));

            }
        });
        //start fullSizeActivity on click
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,FullSizeImage.class);
                intent.putExtra("URL", mDataset.get(position));
                context.startActivity(intent);
            }
        });



//        Picasso.with(context).load(mDataset.get(position)).into(holder.mImageView);
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a Image in this case
        public SquareImageView mImageView;
        public TextView idTV;
        public ImageView downloadAction;
        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (SquareImageView) itemView.findViewById(R.id.imageView);
            idTV =  itemView.findViewById(R.id.idTV);

            downloadAction = itemView.findViewById(R.id.downloadAction);
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