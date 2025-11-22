package com.mobdeve.s17.group11.smartspend.gallery;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.exifinterface.media.ExifInterface;

import com.mobdeve.s17.group11.smartspend.R;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import io.getstream.photoview.PhotoView;

public class GalleryImageViewerActivity extends AppCompatActivity {

    public static File imageFile;

    private ImageButton btnClose;
    private PhotoView pvImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(imageFile != null && !imageFile.exists())
            return;

        setContentView(R.layout.activity_image_view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fl_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
    }

    private void initViews() {
        btnClose = findViewById(R.id.btn_close);
        pvImage = findViewById(R.id.pv_image);

        btnClose.setOnClickListener(view -> finish());

        Uri imageUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", imageFile);

        Bitmap imageBitmap;
        ExifInterface exifInterface;

        try {
            imageBitmap = MediaStore.Images.Media.getBitmap(
                    this.getContentResolver(),
                    imageUri
            );

            exifInterface = new ExifInterface(
                    Objects.requireNonNull(this.getContentResolver().openInputStream(imageUri))
            );
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        pvImage.setImageBitmap(GalleryUtils.Filter.rotateBitmap(
                imageBitmap,
                exifInterface.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL
                )
        ));
    }

}
