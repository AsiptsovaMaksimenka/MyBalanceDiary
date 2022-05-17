package com.application.mybalancediary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.application.mybalancediary.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class FoodBreakfastAdapter extends RecyclerView.Adapter<FoodBreakfastAdapter.ViewHolder> {
    float calorieCount = 0f;
    float totalFat = 0f;
    float totalCarbs = 0f;
    float totalProtein = 0f;
    public static String name="";
    public static int count = 0;
    private final List<Map<String, ?>> mDataset;
    private final Context mContext;
    public static Vector<String> nameBreakfast = new Vector<String>(50);
    public static Vector<Float> calories_breakfast = new Vector<Float>(50);
    public static Vector<Float> proteins_breakfast  = new Vector<Float>(50);
    public static Vector<Float> fats_breakfast  = new Vector<Float>(50);
    public static Vector<Float>  carbs_breakfast  = new Vector<Float>(50);
    String eatBreakfast;
    public static int total_cal =0;
    public static int total_proteins =0;
    public static int total_fats =0;
    public static int total_carb =0;
    Date date = new Date();
    String today= new SimpleDateFormat("yyyy-MM-dd").format(date);

    public FoodBreakfastAdapter(Context myContext, List<Map<String, ?>> myDataset) {
        mContext = myContext;
        mDataset = myDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_food, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, ?> food = mDataset.get(position);
        holder.bindMovieData((food));
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView vTitle;
        public TextView vCal;
        public Button vAdd;
        public RelativeLayout mRelativeLayout;
        public PopupWindow mPopupWindow;

        public ViewHolder(View v) {
            super(v);
            vTitle = v.findViewById(R.id.title);
            vCal = v.findViewById(R.id.calories);
            vAdd = v.findViewById(R.id.addfood);
        }

        private DatabaseReference getCaloriesRef(String ref) {
            return  FirebaseDatabase.getInstance().getReference().child("Breakfast")
                    .child(today).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ref);
        }

        public void bindMovieData(final Map<String, ?> foodItem) {
            vTitle.setText((String) foodItem.get(("iname")));
            vCal.setText((String) foodItem.get("ical"));
            vAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count++;
                    calorieCount =  (Float.parseFloat(String.valueOf(foodItem.get("ical"))));
                    totalCarbs = (Float.parseFloat((String.valueOf(foodItem.get("icarbs")))));
                    totalFat =  (Float.parseFloat((String.valueOf(foodItem.get("ifat")))));
                    totalProtein =(Float.parseFloat((String.valueOf(foodItem.get("iprotein")))));
                    name= String.valueOf(foodItem.get("iname"));
                    nameBreakfast.add(name);
                    calories_breakfast.add(calorieCount);
                    proteins_breakfast.add(totalProtein);
                    fats_breakfast.add(totalFat);
                    carbs_breakfast.add(totalCarbs);

                    total_cal += calorieCount;
                    total_proteins+= totalProtein;
                    total_carb+= totalCarbs;
                    total_fats+= totalFat;
                    for(int i=0;i<nameBreakfast.size();i++)
                    {
                        eatBreakfast+=(nameBreakfast.get(i)+',');
                    }

                    getCaloriesRef("total").setValue(Math.round(total_cal*10.0 ) / 10.0);
                    getCaloriesRef("totalfats").setValue(Math.round(total_fats*10.0 ) / 10.0);
                    getCaloriesRef("totalcarbs").setValue(Math.round(total_carb*10.0 ) / 10.0);
                    getCaloriesRef("totalprotein").setValue(Math.round(total_proteins*10.0 ) / 10.0);
                    getCaloriesRef("NameBreakfast").setValue(eatBreakfast);

                    if (count >= 1) {
                        String toast1 = String.valueOf(count) + "item added";
                        Toast.makeText(mContext, toast1, Toast.LENGTH_SHORT).show();
                    } }
            });
            JSONArray j = null;
        }
    }
}


