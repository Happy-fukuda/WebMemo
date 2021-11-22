package es.exsample;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    List<ImageData> data_ls;
    private onItemClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //add view
        public static View view;
        TextView textView;
        ImageView imageView;


        public ViewHolder(View view) {
            super(view);
            this.view=view;
            // Define click listener for the ViewHolder's View
            textView = (TextView) view.findViewById(R.id.text_view);
            imageView = (ImageView) view.findViewById(R.id.image_view);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public RecyclerAdapter(List<ImageData> image) {
        data_ls = image;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        ImageData data=data_ls.get(position);//自作クラスのインスタンスを取り出す
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.imageView.setImageResource(data.Image);
        if (data.name==null) viewHolder.textView.setText("名前なし");
        else viewHolder.textView.setText(data.name);
        ViewHolder.view.setId(viewHolder.getAdapterPosition());
        if (data.Folder){
            ViewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view,data.ID);
                }
            });
        }
        else {
            ViewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, SubActivity.class);
                    intent.putExtra("ID", data.ID);
                    context.startActivity(intent);
                }
            });
        }
    }
    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }
    public interface onItemClickListener {
        void onClick(View view, int id);
    }
    @Override
    public int getItemCount() {
        return data_ls.size();
    }

}

