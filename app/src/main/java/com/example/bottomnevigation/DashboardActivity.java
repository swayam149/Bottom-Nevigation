package com.example.bottomnevigation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    SharedPreferences preferences; //temp database
    SharedPreferences.Editor editor;  //edit or modify the temp database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        preferences = PreferenceManager.getDefaultSharedPreferences(DashboardActivity.this);
        editor = preferences.edit();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nev);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_cntainer, new HomeFragment()).commit();


    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            int id = item.getItemId();
                if (id == R.id.dashboard_cycle) {
            selectedFragment = new cycleFragment();
                }
                if (id == R.id.dashboard_home1){
                    selectedFragment = new HomeFragment();

                }
                if (id==R.id.dashboard_Ecycle){
                    selectedFragment = new ECycleFragment();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_cntainer,selectedFragment).commit();
            return true;
        }
    };
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.top_nev, menu);
//        return true;
//    }

//    @SuppressLint("NonConstantResourceId")
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.dashboard_profile) {
//            Intent intent = new
//                    Intent(DashboardActivity.this, ProfileActivity.class);
//            startActivity(intent);
//            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
//        }
//        if (id == R.id.dashboard_home1) {
//            Intent intent = new
//                    Intent(DashboardActivity.this, ProfileActivity.class);
//            startActivity(intent);
//            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
//        }
//        if (id == R.id.dashboard_settings) {
//            Intent intent = new
//                    Intent(DashboardActivity.this, ProfileActivity.class);
//            startActivity(intent);
//            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
//        }
////        if (id == R.id.dashboard_share) {
////            Intent intent = new
////                    Intent(DashboardActivity.this, ProfileActivity.class);
////            startActivity(intent);
////            Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
////        }
////        if (id == R.id.dashboard_logout) {
////            AlertDialog.Builder ad = new
////                    AlertDialog.Builder(DashboardActivity.this);
////            ad.setTitle("logout");
////            ad.setMessage("Do you Want to Logout");
////            ad.setPositiveButton("cancle", new DialogInterface.OnClickListener() {
////                @Override
////                public void onClick(DialogInterface dialog, int which) {
////                    dialog.cancel();
////                }
////            });
////            ad.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
////                @Override
////                public void onClick(DialogInterface dialogInterface, int which) {
////                    Intent intent1 = new Intent(DashboardActivity.this, LoginActivity.class);
////                    editor.putBoolean("isLogin",false).commit();
////                    startActivity(intent1);
////                    finish();
////                }
////            }).create().show();
////        }
//        return super.onOptionsItemSelected(item);
//    }

}

//    private RecyclerView chatsRV;
//    private EditText userMshEdt;
//    private Button sendMsgFAB;
//    private final String BOT_KEY = "bot";
//    private final String USER_KEY = "user";
//    private ArrayList<ChatModel>chatModelArrayList;
//    private  ChatRVAdapter chatRVAdapter;
//        chatsRV = findViewById(R.id.recycler_view_chat);
//        userMshEdt = findViewById(R.id.edit_text_input);
//        sendMsgFAB = findViewById(R.id.button_send);
//        chatModelArrayList = new ArrayList<>();
//        chatRVAdapter = new ChatRVAdapter(chatModelArrayList,this);
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        chatsRV.setLayoutManager(manager);
//        chatsRV.setAdapter(chatRVAdapter);


//        sendMsgFAB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (userMshEdt.getText().toString().isEmpty()){
//                    Toast.makeText(DashboardActivity.this, "Please enter your message", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                getResponse(userMshEdt.getText().toString());
//                userMshEdt.setText("");
//            }
//        });


//    private void getResponse(String message){
//        chatModelArrayList.add(new ChatModel(message,USER_KEY));
//        chatRVAdapter.notifyDataSetChanged();
//        String url = "http://api.brainshop.ai/get?bid=178408&key=tvBdyJb1lnabcohj&uid=[uid]&msg="+message;
//        String BASE_URL ="http://api.brainshop.ai/";
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
//        Call<MsgModel>  call=retrofitAPI.getMessage(url);
//        call.enqueue(new Callback<MsgModel>() {
//            @Override
//            public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
//                if (response.isSuccessful()){
//                    MsgModel model=response.body();
//                    chatModelArrayList.add(new ChatModel(model.getCnt(),BOT_KEY));
//                    chatRVAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MsgModel> call, Throwable t) {
//                chatModelArrayList.add(new ChatModel("Please revert your question",BOT_KEY));
//                chatRVAdapter.notifyDataSetChanged();
//            }
//        });
//    }