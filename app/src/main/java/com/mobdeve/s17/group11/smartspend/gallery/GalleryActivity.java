package com.mobdeve.s17.group11.smartspend.gallery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s17.group11.smartspend.R;
import com.mobdeve.s17.group11.smartspend.util.NavigationBar;
import com.mobdeve.s17.group11.smartspend.util.SessionCache;

import java.io.File;
import java.util.Collections;

@SuppressLint("NotifyDataSetChanged")
public class GalleryActivity extends AppCompatActivity {

    public GalleryListAdapter galleryListAdapter;

    public static boolean refreshThumbnails = false;

    private RecyclerView rvGalleryList;
    private TextView tvPromptEmptyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gallery);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NavigationBar.init(this);

        initViews();
        initRecyclerViews();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(refreshThumbnails) {
            galleryListAdapter.items.clear();

            File[] files = SessionCache.galleryDirectory.listFiles();

            if(files != null) {
                for(File file : files) {
                    String name = file.getName();

                    if(!name.toLowerCase().endsWith(".jpg"))
                        continue;

                    String base = name.substring(0, name.length() - 4);

                    if(base.matches("[1-9]\\d*"))
                        galleryListAdapter.items.add(Integer.parseInt(base));
                }

                galleryListAdapter.items.sort(Collections.reverseOrder());
            }

            galleryListAdapter.notifyDataSetChanged();
            validateUI();

            refreshThumbnails = false;
        }
    }

    private void initViews() {
        rvGalleryList = findViewById(R.id.rv_gallery);
        tvPromptEmptyList = findViewById(R.id.tv_empty_list);
    }

    private void initRecyclerViews() {
        galleryListAdapter = new GalleryListAdapter(this);

        rvGalleryList.setLayoutManager(new GridLayoutManager(this, 4));
        rvGalleryList.setAdapter(galleryListAdapter);

        refreshThumbnails = true;
    }

    public void validateUI() {
        if(!galleryListAdapter.items.isEmpty()) {
            rvGalleryList.setVisibility(View.VISIBLE);
            tvPromptEmptyList.setVisibility(View.GONE);
        } else {
            rvGalleryList.setVisibility(View.GONE);
            tvPromptEmptyList.setVisibility(View.VISIBLE);
        }
    }


}
