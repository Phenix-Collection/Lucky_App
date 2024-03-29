package com.bt_121shoppe.motorbike.adapters;

import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Product_New_Post.Detail_New_Post;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.models.PostProduct;
import com.bt_121shoppe.motorbike.viewholders.BaseViewHolder;
import com.bumptech.glide.Glide;

import java.util.List;


public class PostBestDealAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String TAG=PostBestDealAdapter.class.getSimpleName();
    private static final int VIEW_TYPE_EMPTY=0;
    private static final int VIEW_TYPE_NORMAL=1;

    private Callback mCallback;
    private List<PostProduct> mPostList;

    public PostBestDealAdapter(List<PostProduct> postList){
        mPostList=postList;
    }

    public void setCallBack(Callback callBack){
        mCallback=callBack;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discount,parent,false));
            case VIEW_TYPE_EMPTY:
                default:
                    return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_loading,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (mPostList != null && mPostList.size() > 0) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if(mPostList!=null && mPostList.size()>0){
            return mPostList.size();
        }
        else {
            return 1;
        }
    }

    public void addItems(List<PostProduct> postList){
        mPostList.addAll(postList);
        notifyDataSetChanged();
    }

    public interface Callback{
        void onEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder{

        ImageView coverImageView;
        ImageView typeImageView;
        TextView postTitle;
        TextView postLocationDT;
        TextView postPrice;
        TextView postOriginalPrice;
        TextView postView;
        TextView postLang;

        public ViewHolder(View itemView) {
            super(itemView);
            coverImageView=itemView.findViewById(R.id.image);
//            typeImageView=itemView.findViewById(R.id.thumbnailType);
            postTitle=itemView.findViewById(R.id.title);
            postLocationDT=itemView.findViewById(R.id.location);
            postPrice=itemView.findViewById(R.id.tv_discount);
            postOriginalPrice=itemView.findViewById(R.id.tv_price);
            postView=itemView.findViewById(R.id.view);
            postLang=itemView.findViewById(R.id.user_view);
        }

        @Override
        protected void clear() {
            coverImageView.setImageDrawable(null);
//            typeImageView.setImageDrawable(null);
            postTitle.setText("");
            postLocationDT.setText("");
            postPrice.setText("");
            postOriginalPrice.setText("");
            postView.setText("");
        }

        public void onBind(int position){
            super.onBind(position);
            final PostProduct mPost=mPostList.get(position);

            Glide.with(itemView.getContext()).load(mPost.getPostImage()).placeholder(R.drawable.no_image_available).thumbnail(0.1f).into(coverImageView);
//            if(mPost.getPostType().equals("sell"))
//                Glide.with(itemView.getContext()).load(R.drawable.sell).thumbnail(0.1f).into(typeImageView);
//            else if(mPost.getPostType().equals("rent"))
//                Glide.with(itemView.getContext()).load(R.drawable.rent).thumbnail(0.1f).into(typeImageView);
//            else if(mPost.getPostType().equals("buy"))
//                Glide.with(itemView.getContext()).load(R.drawable.buy).thumbnail(0.1f).into(typeImageView);

            String lang=postLang.getText().toString();
            String strPostTitle="";
            if(lang.equals("View:"))
                strPostTitle=mPost.getPostTitle().split(",")[0];
//            else
//                strPostTitle=mPost.getPostTitle().split(",")[1];
            postTitle.setText(strPostTitle);

            postLocationDT.setText(mPost.getLocationDuration());
            double mPrice=0;
            if(Double.parseDouble(mPost.getDiscountAmount())>0) {
                postOriginalPrice.setText("$ "+mPost.getPostPrice());
                postOriginalPrice.setPaintFlags(postOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                Double cost=Double.parseDouble(mPost.getPostPrice());
                if(mPost.getDiscountType().equals("amount")){
                    cost=cost-Double.parseDouble(mPost.getDiscountAmount());
                }else if(mPost.getDiscountType().equals("percent")){
                    Double discountPrice=cost*(Double.parseDouble(mPost.getDiscountAmount())/100);
                    cost=cost-discountPrice;
                }
                mPrice=cost;
                postPrice.setText("$ "+cost.toString());
            }else{
                postOriginalPrice.setVisibility(View.GONE);
            }
            postView.setText(String.valueOf(mPost.getCountView()));
            double finalMPrice = mPrice;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(itemView.getContext(), Detail_New_Post.class);
                    intent.putExtra("Discount", finalMPrice);
                    intent.putExtra("Price",mPost.getPostPrice());
                    intent.putExtra("ID",mPost.getPostId());
                    itemView.getContext().startActivity(intent);
                }
            });
        }

    }

    public class LoadingViewHolder extends BaseViewHolder{

        ProgressBar progressBar;
        public LoadingViewHolder(View itemView){
            super(itemView);
            progressBar=itemView.findViewById(R.id.progressBar);
        }

        @Override
        protected void clear() {

        }
    }
}
