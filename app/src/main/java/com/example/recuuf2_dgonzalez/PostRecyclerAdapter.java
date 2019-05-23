package com.example.recuuf2_dgonzalez;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.PostViewHolder> {

    List<Post> list;
    DatabaseReference mRef;


    PostRecyclerAdapter(List<Post> list){
        this.list = list;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View Postitem = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(Postitem);
    }

    @Override
    public void onBindViewHolder(final PostViewHolder holder, final int position) {
        final Post post = list.get(position);
        holder.postContext.setText(post.context);
        holder.postTitle.setText(post.title);
        GlideApp.with(holder.constraintLayout.getContext()).load(post.img).circleCrop().into(holder.img);


        holder.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(post);
            }
        });
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(),DisplayPost.class);

                intent.putExtra("id", list.get(holder.getAdapterPosition()).id);


                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView postTitle;
        private TextView postContext;
        ImageView trash;
        ConstraintLayout constraintLayout;
        ImageView img;
        PostViewHolder(View itemPost) {
            super(itemPost);
            postTitle = itemPost.findViewById(R.id.post_title);
            postContext = itemPost.findViewById(R.id.post_context);
            trash = itemPost.findViewById(R.id.trash);
            constraintLayout = itemPost.findViewById(R.id.id_post);
            img = itemPost.findViewById(R.id.post_image);
        }
    }

    public void remove(Post post){
        int position = list.indexOf(post);
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,list.size());
        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("Posts").child(post.id).removeValue();
    }
}