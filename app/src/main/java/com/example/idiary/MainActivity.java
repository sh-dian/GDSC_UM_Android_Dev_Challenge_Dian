package com.example.idiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface{
    private MaterialToolbar mToolbar;
    private FirebaseAuth mAuth;

    ExtendedFloatingActionButton mDiary;
    private RecyclerView recyclerView;
    private ArrayList<Diary> diariesList;
    private DiaryAdapter diaryAdapter;
    private TextView noDiaryTextView;
    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        mToolbar = findViewById(R.id.main_activity_materialToolbar);
        //setSupportActionBar(mToolbar);
        mDiary = findViewById(R.id.add_diary_button);

        noDiaryTextView = findViewById(R.id.no_diary_text_view);
        lottieAnimationView = findViewById(R.id.lottie_animation);

        recyclerView=findViewById(R.id.recycler_view);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);

        if(user != null){
            getDiaries(user.getUid());
        }

        mToolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(user != null){
                    mAuth.getInstance().signOut();
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                return true;
            }
        });

        mDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user != null){
                    startActivity(new Intent(MainActivity.this,AddDiary.class));
                }
            }
        });
    }

    private void getDiaries(String uid) {
        diariesList = new ArrayList<>();
        diaryAdapter = new DiaryAdapter(MainActivity.this,diariesList,this);
        recyclerView.setAdapter(diaryAdapter);
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                diariesList.clear();
                if(snapshot.exists()){
                    for (DataSnapshot snap: snapshot.getChildren()) {
                        Diary diary = snap.getValue(Diary.class);
                        diariesList.add(diary);
                    }
                    diaryAdapter.notifyDataSetChanged();
                }else{
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    noDiaryTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //delete diary - just in interface
    @Override
    public void onItemLongClick(int position) {
        diariesList.remove(position);
        diaryAdapter.notifyItemRemoved(position);
        Toast.makeText(MainActivity.this,"Diary Delete", Toast.LENGTH_SHORT).show();
    }
}