package com.bt_121shoppe.motorbike.loan.child;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.loan.Create_Load;
import com.bt_121shoppe.motorbike.loan.model.item_two;
import com.bt_121shoppe.motorbike.loan.model.loan_item;
import com.bt_121shoppe.motorbike.loan.model.province_Item;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.bt_121shoppe.motorbike.utils.CommonFunction.getEncodedString;


/**
 *
 */
public class three extends Fragment {
    private static final String ARG_NUMBER = "arg_number";

    private Toolbar mToolbar;
    private TextView mTvName;
    private Button mBtnSubmit, mBtnNextWithFinish,mBtnback;

    private int mNumber;
    AlertDialog dialog;
    private EditText etID_card,etFamily_book,etPhotos,etEmployment_card,etID_card1,etFamily_book1,etPhotos1,etEmployment_card1;
    String[] values = {"មាន","មិនមាន"};
    private Create_Load createLoad;
    private item_two itemTwo;
    private LinearLayout linearLayout;
    private ImageView img1,img2,img3,img4,img5,img6,img7,img8;

    private SharedPreferences preferences;
    private String username,password,Encode;
    private int pk;
    loan_item loanItem;
    String basicEncode;
    boolean ischeck;
    boolean mCard_ID,mFamily_Book,mPhoto,mCard_Work,mCard_ID1=false,mFamily_Book1=false,mPhoto1=false,mCard_Work1=false;

    public static three newInstance(item_two itemTwo) {
        Bundle args = new Bundle();
//        args.putParcelable(ARG_NUMBER,itemOne);
        args.putParcelable(ARG_NUMBER, itemTwo);
        three fragment = new three();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            itemTwo =  args.getParcelable(ARG_NUMBER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_create__load_three, container, false);
        initView(view);
        createLoad = (Create_Load)this.getActivity();
        mBtnback = view.findViewById(R.id.btn_back);
        mBtnSubmit = view.findViewById(R.id.btn_submit);

        checkEd();
        mBtnback.setOnClickListener(view1 -> { createLoad.setBack(); });
        mBtnSubmit.setOnClickListener(v -> {
            if (checkEd()){
                Log.d("Pk",""+ pk + Encode+"  user "+ username+"  pass  "+password);
                putapi();
            }
        });

        preferences= getContext().getSharedPreferences("Register",MODE_PRIVATE);
        username=preferences.getString("name","");
        password=preferences.getString("pass","");
        Encode = getEncodedString(username,password);
        basicEncode = "Basic "+Encode;
        if (preferences.contains("token")) {
            pk = preferences.getInt("Pk",0);
        }else if (preferences.contains("id")) {
            pk = preferences.getInt("id", 0);
        }

//        loanItem = new loan_item(itemTwo.getLoan_Amount(),0,1,itemTwo.getItemOne().getTotal_Income(),itemTwo.getItemOne().getTotal_Expense(),pk,202,1,202,null,null,itemTwo.getItemOne().getName(),null,null,itemTwo.getItemOne().getJob(),itemTwo.getItemOne().getPhone_Number(),itemTwo.getItemOne().getAddress(),createLoad.Checked_Radio(),);


        return view;
    }

    private void AlertDialog(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle("Choose item");
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0)
                mCard_ID = true;
            else mCard_ID = false;
//            Toast.makeText(this, "Position: " + which + " Value: " + items[which], Toast.LENGTH_LONG).show();
            editText.setText(items[which]);
            dialog.dismiss();
        });
//        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
    }
    private void AlertDialog1(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle("Choose item");
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0)
                mFamily_Book = true;
            else mFamily_Book = false;
//            Toast.makeText(this, "Position: " + which + " Value: " + items[which], Toast.LENGTH_LONG).show();
            editText.setText(items[which]);
            dialog.dismiss();
        });
//        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
    }
    private void AlertDialog2(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle("Choose item");
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0)
                mPhoto = true;
            else mPhoto = false;
//            Toast.makeText(this, "Position: " + which + " Value: " + items[which], Toast.LENGTH_LONG).show();
            editText.setText(items[which]);
            dialog.dismiss();
        });
//        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
    }
    private void AlertDialog3(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle("Choose item");
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0)
                mCard_Work = true;
            else mCard_Work = false;
//            Toast.makeText(this, "Position: " + which + " Value: " + items[which], Toast.LENGTH_LONG).show();
            editText.setText(items[which]);
            dialog.dismiss();
        });
//        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
    }
    private void AlertDialog4(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle("Choose item");
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0)
                mCard_ID1 = true;
            else mCard_ID1 = false;
//            Toast.makeText(this, "Position: " + which + " Value: " + items[which], Toast.LENGTH_LONG).show();
            editText.setText(items[which]);
            dialog.dismiss();
        });
//        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
    }
    private void AlertDialog5(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle("Choose item");
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0)
                mFamily_Book1 = true;
            else mFamily_Book1 = false;
//            Toast.makeText(this, "Position: " + which + " Value: " + items[which], Toast.LENGTH_LONG).show();
            editText.setText(items[which]);
            dialog.dismiss();
        });
//        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
    }
    private void AlertDialog6(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle("Choose item");
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0)
                mPhoto = true;
            else mPhoto = false;
//            Toast.makeText(this, "Position: " + which + " Value: " + items[which], Toast.LENGTH_LONG).show();
            editText.setText(items[which]);
            dialog.dismiss();
        });
//        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
    }
    private void AlertDialog7(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle("Choose item");
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0)
                mCard_Work1 = true;
            else mCard_Work1 = false;
//            Toast.makeText(this, "Position: " + which + " Value: " + items[which], Toast.LENGTH_LONG).show();
            editText.setText(items[which]);
            dialog.dismiss();
        });
//        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
    }
    private void putapi(){
        Service api1 = Client.getClient().create(Service.class);
        loanItem = new loan_item(itemTwo.getLoan_Amount(),0,1,itemTwo.getItemOne().getTotal_Income(),itemTwo.getItemOne().getTotal_Expense(),1,1,pk,itemTwo.getItemOne().getmProductId(),pk,pk,null,itemTwo.getItemOne().getName(),null,0,itemTwo.getItemOne().getJob(),itemTwo.getItemOne().getPhone_Number(),itemTwo.getItemOne().getAddress(),mCard_ID,mFamily_Book,mCard_Work,mPhoto,itemTwo.getItemOne().getmProvinceID(),itemTwo.getItemOne().getJob(),itemTwo.getItemOne().getJob_Period(),itemTwo.getLoan_RepaymentType(),0,itemTwo.getBuying_InsuranceProduct(),itemTwo.getAllow_visito_home(),0,mCard_ID1,mFamily_Book1,mPhoto1,mCard_Work1,itemTwo.getItemOne().getRelationship(),itemTwo.getItemOne().getCo_borrower_Job(),itemTwo.getItemOne().getCo_Job_Period());
//        loanItem = new loan_item(158,1200,0,3,"Test",1,1,"Thou","male",19,"Student",600,300,"1234567","st 273",true,false,true,false,2,"185","null",185,null,null,null,null,202,7,null,null,null,"seller","2","1",false,true,4,"1234",false,false,true,false,true);
        Call<loan_item> call = api1.setCreateLoan(loanItem,basicEncode);
        call.enqueue(new Callback<loan_item>() {
            @Override
            public void onResponse(Call<loan_item> call, Response<loan_item> response) {
//                try {
//                    Log.d("Bodybody", response.errorBody().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                if (!response.isSuccessful()){
                    Log.d("Error121212", response.code() +"  "+ response.message());
                }
            }

            @Override
            public void onFailure(Call<loan_item> call, Throwable t) {
                Log.d("ErroronFailure121212", t.getMessage());
            }
        });
    }
    private void initView(View view) {
        etID_card = view.findViewById(R.id.etID_card);
        etID_card.setOnClickListener(v -> {
            AlertDialog(values,etID_card);
        });
        etFamily_book = view.findViewById(R.id.etFamily_book);
        etFamily_book.setOnClickListener(v -> {
            AlertDialog1(values,etFamily_book);
        });
        etPhotos = view.findViewById(R.id.etPhotos);
        etPhotos.setOnClickListener(v -> {
            AlertDialog2(values,etPhotos);
        });
        etEmployment_card = view.findViewById(R.id.etEmployment_card);
        etEmployment_card.setOnClickListener(v -> {
            AlertDialog3(values,etEmployment_card);
        });

        etID_card1 = view.findViewById(R.id.etID_card1);
        etID_card1.setOnClickListener(v -> {
            AlertDialog4(values,etID_card1);
        });
        etFamily_book1 = view.findViewById(R.id.etFamily_book1);
        etFamily_book1.setOnClickListener(v -> {
            AlertDialog5(values,etFamily_book1);
        });
        etPhotos1 = view.findViewById(R.id.etPhotos1);
        etPhotos1.setOnClickListener(v -> {
            AlertDialog6(values,etPhotos1);
        });
        etEmployment_card1 = view.findViewById(R.id.etEmployment_card1);
        etEmployment_card1.setOnClickListener(v -> {
            AlertDialog7(values,etEmployment_card1);
        });
        linearLayout = view.findViewById(R.id.linealayout);
        if (itemTwo.getItemOne().getIndex()==1){
            linearLayout.setVisibility(View.GONE);
        }else {
            linearLayout.setVisibility(View.VISIBLE);
        }

        img1 = view.findViewById(R.id.img_1);
        img2 = view.findViewById(R.id.img_2);
        img3 = view.findViewById(R.id.img_3);
        img4 = view.findViewById(R.id.img_4);
        img5 = view.findViewById(R.id.img_5);
        img6 = view.findViewById(R.id.img_6);
        img7 = view.findViewById(R.id.img_7);
        img8 = view.findViewById(R.id.img_8);
    }
    private boolean checkEd(){
        boolean bID_Card,bFramily_Book,bPhotos,bEmployment_card,bNumber_institution,bMonthly_Amount_Paid;

        bID_Card = createLoad.CheckedYear(etID_card);
        bFramily_Book = createLoad.CheckedYear(etFamily_book);
        bPhotos = createLoad.Checked(etPhotos);
        bEmployment_card = createLoad.CheckedYear(etEmployment_card);

//        bNumber_institution = createLoad.CheckedYear(mNumber_institution);
//        bMonthly_Amount_Paid = createLoad.CheckedYear(mMonthly_Amount_Paid);

        createLoad.ConditionYear(img1,etID_card);
        createLoad.ConditionYear(img2,etFamily_book);
        createLoad.Condition(img3,etPhotos);
        createLoad.ConditionYear(img4,etEmployment_card);

        createLoad.ConditionYear(img5,etID_card1);
        createLoad.ConditionYear(img6,etFamily_book1);
        createLoad.Condition(img7,etPhotos1);
        createLoad.ConditionYear(img8,etEmployment_card1);

        return bID_Card&&bFramily_Book&&bPhotos&&bEmployment_card;
    }
}
