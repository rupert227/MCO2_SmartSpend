package com.mobdeve.s17.group11.smartspend.gallery;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class GalleryUtils {

    public static class Camera {

        private ActivityResultLauncher<Intent> cameraLauncher;
        private ActivityResultLauncher<String> permissionLauncher;
        private File imageFile;

        private final AppCompatActivity activity;
        private final Callback callback;

        public Camera(AppCompatActivity activity, Callback callback) {
            this.activity = activity;
            this.callback = callback;

            initLaunchers();
        }

        private void initLaunchers() {
            cameraLauncher = activity.registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(), result -> {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            if(callback == null)
                                return;

                            try {
                                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                                ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());

                                bitmap = Filter.rotateBitmap(bitmap, exif.getAttributeInt(
                                        ExifInterface.TAG_ORIENTATION,
                                        ExifInterface.ORIENTATION_NORMAL
                                ));

                                FileOutputStream outputStream = new FileOutputStream(imageFile);

                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                                outputStream.flush();
                                outputStream.close();

                                callback.onPhotoCaptured(imageFile);
                            } catch(IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            if(callback != null)
                                callback.onCancelled();
                        }
                    }
            );

            permissionLauncher = activity.registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(), granted -> {
                        if(granted) {
                            launchCameraInternal();
                        } else {
                            if(callback != null)
                                callback.onCancelled();
                        }
                    }
            );
        }

        public void launchCamera(File imageFile) {
            this.imageFile = imageFile;

            if(ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                permissionLauncher.launch(Manifest.permission.CAMERA);
                return;
            }

            launchCameraInternal();
        }

        private void launchCameraInternal() {
            Uri imageUri = FileProvider.getUriForFile(
                    activity,
                    activity.getPackageName() + ".provider",
                    imageFile
            );

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            cameraLauncher.launch(intent);
        }

        public interface Callback {

            void onPhotoCaptured(File file);
            void onCancelled();

        }

    }

    public static class Filter {

        public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
            Matrix matrix = new Matrix();

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.postRotate(90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.postRotate(180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.postRotate(270);
                    break;

                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    matrix.preScale(-1, 1);
                    break;

                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    matrix.preScale(1, -1);
                    break;

                default:
                    return bitmap;
            }

            return Bitmap.createBitmap(
                    bitmap,
                    0, 0,
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    matrix,
                    true
            );
        }

    }

    public static class GalleryPicker {

        private ActivityResultLauncher<Intent> galleryLauncher;
        private File imageFile;

        private final AppCompatActivity activity;
        private final Callback callback;

        public GalleryPicker(AppCompatActivity activity, Callback callback) {
            this.activity = activity;
            this.callback = callback;

            initLaunchers();
        }

        private void initLaunchers() {
            galleryLauncher = activity.registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(), result -> {
                        if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            if(callback == null)
                                return;

                            try {
                                Uri imageUri = result.getData().getData();

                                if(imageUri == null)
                                    return;

                                InputStream inputStream = activity.getContentResolver().openInputStream(imageUri);
                                Bitmap bitmap = BitmapFactory.decodeStream(Objects.requireNonNull(inputStream));
                                inputStream.close();

                                InputStream exifStream = activity.getContentResolver().openInputStream(imageUri);
                                ExifInterface exif = new ExifInterface(Objects.requireNonNull(exifStream));
                                exifStream.close();

                                bitmap = Filter.rotateBitmap(bitmap, exif.getAttributeInt(
                                        ExifInterface.TAG_ORIENTATION,
                                        ExifInterface.ORIENTATION_NORMAL
                                ));

                                FileOutputStream outputStream = new FileOutputStream(imageFile);

                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                                outputStream.flush();
                                outputStream.close();

                                callback.onImagePicked(imageFile);
                            } catch(IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
            );
        }

        public void launchGallery(File imageFile) {
            this.imageFile = imageFile;

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

            galleryLauncher.launch(intent);
        }

        public interface Callback {

            void onImagePicked(File file);

            void onCancelled();

        }

    }

}
