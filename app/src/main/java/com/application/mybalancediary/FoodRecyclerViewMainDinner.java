package com.application.mybalancediary;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator;



public class FoodRecyclerViewMainDinner extends Fragment {

    private static final String ARG_MOVIE = "R.id.mdf_main_replace";
    FoodDataJson foodData;
    private FoodDinnerAdapter mRecyclerViewAdapter;
    android.widget.SearchView search;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    public static String voice_query = "";
    private FloatingActionButton voice;


    public FoodRecyclerViewMainDinner() {

    }

    public static FoodRecyclerViewMainDinner newInstance() {
        FoodRecyclerViewMainDinner fragment = new FoodRecyclerViewMainDinner();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIE, "R.id.rcmainDinner");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        foodData = new FoodDataJson();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_recyclerview_dinner, container, false);
        RecyclerView mRecyclerView = rootView.findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerViewAdapter = new FoodDinnerAdapter(getActivity(), foodData.foodList);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setItemAnimator(new ScaleInBottomAnimator());
        mRecyclerView.getItemAnimator().setAddDuration(100);
        mRecyclerView.getItemAnimator().setRemoveDuration(1000);
        mRecyclerView.getItemAnimator().setMoveDuration(100);
        mRecyclerView.getItemAnimator().setChangeDuration(100);
        ScaleInBottomAnimator animator = new ScaleInBottomAnimator();
        mRecyclerView.setItemAnimator(animator);
        mRecyclerView.setAdapter(new ScaleInAnimationAdapter(mRecyclerViewAdapter));
        ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(mRecyclerViewAdapter);
        alphaAdapter.setDuration(500);
        mRecyclerView.setAdapter(alphaAdapter);
        voice = rootView.findViewById(R.id.vsfbDinner);
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        return rootView;
    }
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getContext(), "Not Supported",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_SPEECH_INPUT && resultCode == Activity.RESULT_OK && null != data) {

            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            voice_query = (results.get(0));        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_actionview, menu);
        super.onCreateOptionsMenu(menu, inflater);

        search = (android.widget.SearchView) menu.findItem(R.id.action_search).getActionView();
        if (search != null) {
            search.setIconifiedByDefault(true);
            search.setQuery(voice_query, true);
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    String food;
                    if (voice_query != "") {
                        food = voice_query;
                        search.setQueryHint(voice_query);
                    }
                    else {
                        food = query;
                    }
                    food = food.replace(" ", "");
                    String f_url = "https://api.nutritionix.com/v1_1/search/" + food + "?results=0%3A20&cal_min=0&cal_max=50000&fields=item_name%2Cbrand_name%2Citem_id%2Cbrand_id%2Citem_description%2Cnf_protein%2Cnf_calories%2Cnf_total_carbohydrate%2Cnf_total_fat&appId=42e8cbe9&appKey=a4e373fe0f10ab1de40cffbffb9db544";
                    MyDownloadJsonAsyncTask downloadJson = new MyDownloadJsonAsyncTask(mRecyclerViewAdapter);
                    downloadJson.execute(f_url);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private class MyDownloadJsonAsyncTask extends AsyncTask<String, Void, FoodDataJson> {
        private final WeakReference<FoodDinnerAdapter> adapterReference;

        public MyDownloadJsonAsyncTask(FoodDinnerAdapter adapter) {
            adapterReference = new WeakReference<>(adapter);
        }

        @Override
        protected FoodDataJson doInBackground(String... urls) {
            FoodDataJson threadMovieData = new FoodDataJson();
            for (String url : urls) {
                try {
                    threadMovieData.downloadFoodDataJson(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return threadMovieData;
        }


        @Override
        protected void onPostExecute(FoodDataJson threadMovieData) {
            foodData.foodList.clear();
            for (int i = 0; i < threadMovieData.getSize(); i++)
                foodData.foodList.clear();
            for (int k = 0; k < threadMovieData.getSize(); k++)
                foodData.foodList.add(threadMovieData.foodList.get(k));
            final FoodDinnerAdapter adapter = adapterReference.get();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

}
