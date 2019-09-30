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
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Product_New_Post.Detail_New_Post;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.classes.APIResponse;
import com.bt_121shoppe.motorbike.models.PostViewModel;
import com.bt_121shoppe.motorbike.utils.CommomAPIFunction;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bt_121shoppe.motorbike.viewholders.BaseViewHolder;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostBestDealAdapterV2 extends RecyclerView.Adapter<BaseViewHolder>{
    private static final String TAG=PostBestDealAdapter.class.getSimpleName();
    private static final int VIEW_TYPE_EMPTY=0;
    private static final int VIEW_TYPE_NORMAL=1;

    private PostBestDealAdapter.Callback mCallback;
    private List<PostViewModel> mPostList;

    public PostBestDealAdapterV2(List<PostViewModel> postList){
        mPostList=postList;
    }

    public void setCallBack(PostBestDealAdapter.Callback callBack){
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

    public void addItems(List<PostViewModel> postList){
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
        CircleImageView postProfile;

        public ViewHolder(View itemView) {
            super(itemView);
            coverImageView=itemView.findViewById(R.id.image);
//            typeImageView=itemView.findViewById(R.id.thumbnailType);
            postTitle=itemView.findViewById(R.id.title);
            postLocationDT=itemView.findViewById(R.id.location);
            postPrice=itemView.findViewById(R.id.tv_discount);
            postOriginalPrice=itemView.findViewById(R.id.tv_price);
            postView=itemView.findViewById(R.id.view);
            postProfile=itemView.findViewById(R.id.img_user);
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
            postProfile.setImageDrawable(null);
        }

        public void onBind(int position){
            super.onBind(position);
            final PostViewModel mPost=mPostList.get(position);
            String strPostTitle="";
            if(mPost.getPost_sub_title().isEmpty()){
                strPostTitle=CommonFunction.generatePostSubTitle(mPost.getModeling(),mPost.getYear(),mPost.getColor());
            }else
                strPostTitle=mPost.getPost_sub_title().split(",")[0];

            Glide.with(itemView.getContext()).load(mPost.getFront_image_path()).placeholder(R.drawable.no_image_available).thumbnail(0.1f).into(coverImageView);

            postTitle.setText(strPostTitle);
            double mPrice=0;
            if(Double.parseDouble(mPost.getDiscount())>0) {
                postOriginalPrice.setText("$ "+mPost.getCost());
                postOriginalPrice.setPaintFlags(postOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                Double cost=Double.parseDouble(mPost.getCost());
                if(mPost.getDiscount_type().equals("amount")){
                    cost=cost-Double.parseDouble(mPost.getDiscount());
                }else if(mPost.getDiscount_type().equals("percent")){
                    Double discountPrice=cost*(Double.parseDouble(mPost.getDiscount())/100);
                    cost=cost-discountPrice;
                }
                mPrice=cost;
                postPrice.setText("$ "+cost.toString());
            }else{
                postOriginalPrice.setVisibility(View.GONE);
            }

            int countView=0;
            try{
                String response= CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"countview/?post="+mPost.getId());
                APIResponse APIResponse =new APIResponse();
                Gson gson=new Gson();
                APIResponse =gson.fromJson(response, APIResponse.class);
                countView= APIResponse.getCount();
            }catch (IOException io){
                io.printStackTrace();
            }
            postView.setText(String.valueOf(countView));
            double finalMPrice = mPrice;

            try{
                String userResponse=CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/users/"+mPost.getCreated_by());
                try{
                    JSONObject obj=new JSONObject(userResponse);
                    String username=obj.getString("username");
                    CommomAPIFunction.getUserProfileFB(itemView.getContext(),postProfile,username);
                }catch (JSONException joe){
                    joe.printStackTrace();
                }

            }catch (IOException ioe){
                ioe.printStackTrace();
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(itemView.getContext(), Detail_New_Post.class);
                    intent.putExtra("Discount", finalMPrice);
                    intent.putExtra("Price",mPost.getCost());
                    intent.putExtra("ID",mPost.getId());
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