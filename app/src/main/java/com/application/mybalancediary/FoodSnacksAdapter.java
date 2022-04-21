package com.application.mybalancediary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;
import java.util.Vector;


public class FoodSnacksAdapter extends RecyclerView.Adapter<FoodSnacksAdapter.ViewHolder> {
    float caloriecount = 0f;
    float totalfat = 0f;
    float totalcarbs = 0f;
    float totalprotein = 0f;
    public static String name="";
    public static int count = 0;
    private final List<Map<String, ?>>mDatasetSnack;
    private final Context mContextSnack;
    public static Vector<String> nameSnack = new Vector<String>(50);
    public static Vector<Float> calories_snack = new Vector<Float>(50);
    public static Vector<Float> proteins_snack  = new Vector<Float>(50);
    public static Vector<Float> fats_snack  = new Vector<Float>(50);
    public static Vector<Float>  carbs_snack  = new Vector<Float>(50);

    public static Float total_cal =0.0f;
    public static Float total_proteins =0.0f;
    public static Float total_fats =0.0f;
    public static  Float total_carb =0.0f;

    public FoodSnacksAdapter(Context myContext, List<Map<String, ?>> myDataset) {
        mContextSnack = myContext;
        mDatasetSnack = myDataset;
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
        Map<String, ?> food = mDatasetSnack.get(position);
        holder.bindMovieData((food));
    }
    @Override
    public int getItemCount() {
        return mDatasetSnack.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView vTitle;
        public TextView vCal;
        public Button vAdd;
        public RelativeLayout mRelativeLayout;
        public PopupWindow mPopupWindow;

        private final FirebaseAuth mAuth;
        private DatabaseReference mDatabase;

        public ViewHolder(View v) {
            super(v);
            vTitle = v.findViewById(R.id.title);
            vCal = v.findViewById(R.id.calories);
            vAdd = v.findViewById(R.id.addfood);
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }

        private DatabaseReference getCaloriesRef(String ref) {
            FirebaseUser user = mAuth.getCurrentUser();
            String userId = user.getUid();
            return mDatabase.child("Snacks").child(userId).child(ref);
        }

        public void bindMovieData(final Map<String, ?> foodItem) {
            vTitle.setText((String) foodItem.get(("iname")));
            vCal.setText((String) foodItem.get("ical"));
            vAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count++;
                    caloriecount =  (Float.parseFloat(String.valueOf(foodItem.get("ical"))));
                    totalcarbs = (Float.parseFloat((String.valueOf(foodItem.get("icarbs")))));
                    totalfat =  (Float.parseFloat((String.valueOf(foodItem.get("ifat")))));
                    totalprotein =(Float.parseFloat((String.valueOf(foodItem.get("iprotein")))));
                    name= String.valueOf(foodItem.get("iname"));
                    nameSnack.add(name);
                    calories_snack.add(caloriecount);
                    proteins_snack.add(totalprotein);
                    fats_snack.add(totalfat);
                    carbs_snack.add(totalcarbs);

                    total_cal +=caloriecount;
                    total_proteins+=totalprotein;
                    total_carb+=totalcarbs;
                    total_fats+=totalfat;
                    getCaloriesRef("total").setValue(Math.round(total_cal*10.0 ) / 10.0);
                    getCaloriesRef("totalfats").setValue(Math.round(total_fats*10.0 ) / 10.0);
                    getCaloriesRef("totalcarbs").setValue(Math.round(total_carb*10.0 ) / 10.0);
                    getCaloriesRef("totalprotein").setValue(Math.round(total_proteins*10.0 ) / 10.0);

                    if (count >= 1) {
                        String toast1 = String.valueOf(count) + "item added";
                        Toast.makeText(mContextSnack, toast1, Toast.LENGTH_SHORT).show();
                    } }
            });
            JSONArray j = null;
        }
    }
}


