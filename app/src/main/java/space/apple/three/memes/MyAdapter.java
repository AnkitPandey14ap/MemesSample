package space.apple.three.memes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> mDataset;
    private LayoutInflater layoutInflater;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<String> myDataset, Context context) {
        layoutInflater = LayoutInflater.from(context);
        mDataset = myDataset;
        this.context = context;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mImageView.setImageBitmap(BitmapFactory.decodeFile(mDataset.get(position)));


        Picasso.with(context)
                .load(mDataset.get(position))
                .resize(500, 500)
                .centerCrop()
                .into(holder.mImageView);

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
//        public ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (SquareImageView) itemView.findViewById(R.id.imageView);
//            mImageView =  itemView.findViewById(R.id.imageView);
        }
    }
}