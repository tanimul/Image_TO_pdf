package com.example.imagetopdf.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imagetopdf.Adapter.AdapterImageList;
import com.example.imagetopdf.Interface.ImageOnClickListener;
import com.example.imagetopdf.KEYS;
import com.example.imagetopdf.R;
import com.example.imagetopdf.Tools;
import com.example.imagetopdf.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ActivityHome extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, ImageOnClickListener {
    private static final String TAG = "ActivityHome";
    private ActivityHomeBinding activityHomeBinding;
    Animation fav_open, fav_close, rotate_clockwise, rotate_anticlockwise;
    boolean isOpen = false;
    private int image_rec_code = 1;
    Bitmap bitmap;
    ArrayList<Uri> mArrayUri;
    int position = 0;
    private AdapterImageList adapterImageList;
    static final int REQUEST_IMAGE_CAPTURE = 0;
    private long backpressed;
    private Toast backtost;
    private AlertDialog dialog;
    private EditText editTextfilename;
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(activityHomeBinding.getRoot());

        initNavAndToolbar();
        initAnimation();
        initAleartDialog();
        checkPermission();
       // iniDayNightMode();


        mArrayUri = new ArrayList<Uri>();

        activityHomeBinding.recyclerviewImage.setFitsSystemWindows(true);
        activityHomeBinding.recyclerviewImage.setLayoutManager(new GridLayoutManager(this, 2));

        adapterImageList = new AdapterImageList(this, mArrayUri, this);
        activityHomeBinding.recyclerviewImage.setAdapter(adapterImageList);

        activityHomeBinding.navViewActivityHome.getMenu().getItem(0).setChecked(true);

        activityHomeBinding.flotingbuttonHomeAdd.setOnClickListener(this);
        activityHomeBinding.extflotingbuttonTakeimage.setOnClickListener(this);
        activityHomeBinding.extflotingbuttonHomeImportfromgalary.setOnClickListener(this);
        activityHomeBinding.navViewActivityHome.setNavigationItemSelectedListener(this);
    }

    private void initDayNightMode() {
        if (Tools.getPrefBoolean(KEYS.IsDartMode, false)) {
            Log.d(TAG, "Dart Mode");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            Log.d(TAG, "Day Mode");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void initNavAndToolbar() {
        setTitle(null);
        setSupportActionBar(activityHomeBinding.toolbarActivityHome);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, activityHomeBinding.drwerlayoutActivityMain,
                activityHomeBinding.toolbarActivityHome,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        activityHomeBinding.drwerlayoutActivityMain.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void initAnimation() {
        fav_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fav_open);
        fav_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fav_close);
        rotate_clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        rotate_anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);
    }

    private void initAleartDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHome.this);
        final View layout_filesaving = getLayoutInflater().inflate(R.layout.layout_file_saving, null);
        builder.setView(layout_filesaving);
        builder.setTitle("Set the File Name");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                editTextfilename = layout_filesaving.findViewById(R.id.edittext_layout_saving_fileName);
                if (!editTextfilename.getText().toString().isEmpty()) {
                    Log.d(TAG, "File Saving");
                    dialog.cancel();
                    createPDFWithMultipleImage(editTextfilename.getText().toString());
                } else {
                    Log.d(TAG, "Please Enter a File Name");
                    Toast.makeText(ActivityHome.this, "Please Enter a File Name", Toast.LENGTH_SHORT).show();

                }

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ActivityHome.this, "File Saving Cancel", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        dialog = builder.create();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.homemenu, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder menuBuilder = (MenuBuilder) menu;
            menuBuilder.setOptionalIconsVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.listview:
                activityHomeBinding.recyclerviewImage.setLayoutManager(new LinearLayoutManager(this));
                activityHomeBinding.recyclerviewImage.setAdapter(adapterImageList);
                break;
            case R.id.gridview:
                activityHomeBinding.recyclerviewImage.setLayoutManager(new GridLayoutManager(this, 2));
                activityHomeBinding.recyclerviewImage.setAdapter(adapterImageList);
                break;

            case R.id.save:
                if (mArrayUri.size() != 0) {
                    dialog.show();
                } else {
                    Toast.makeText(this, "Please Select Images", Toast.LENGTH_SHORT).show();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == activityHomeBinding.flotingbuttonHomeAdd) {
            extendFloatingButton();
        }
        if (v == activityHomeBinding.extflotingbuttonHomeImportfromgalary) {
            Log.d(TAG, "Import from Gallery .");
            extendFloatingButton();
            openGallery();
        }
        if (v == activityHomeBinding.extflotingbuttonTakeimage) {
            Log.d(TAG, "Take Image .");
            extendFloatingButton();
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
// Else ask for permission
                else {
                    ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }

            } catch (ActivityNotFoundException e) {
                // display error state to the user
                Log.d(TAG, "display error state to the user");
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "Navigation Item selected: " + item.toString());
        switch (item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(ActivityHome.this, ActivityHome.class));
                finish();
                break;

            case R.id.nav_setting:
                startActivity(new Intent(ActivityHome.this, ActivitySetting.class));
                break;
        }
        return true;
    }

    public void share(File file) {
        Log.d(TAG, "File: " + file);
        Log.d(TAG, "File Name: " + file.getName());
        Log.d(TAG, "File Path: " + file.getPath());
        Uri sharingUri = Uri.parse(file.getPath());

        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, sharingUri);
        startActivity(share);
    }

    private void createPDFWithMultipleImage(String filename) {
        File file = getOutputFile(filename);
        if (file != null) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                PdfDocument pdfDocument = new PdfDocument();
                Log.d(TAG, "getOutputFile: try");
                for (int i = 0; i < mArrayUri.size(); i++) {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mArrayUri.get(i));
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), (i + 1)).create();
                    PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                    Canvas canvas = page.getCanvas();
                    Paint paint = new Paint();
                    paint.setColor(Color.BLUE);
                    canvas.drawPaint(paint);
                    canvas.drawBitmap(bitmap, 0f, 0f, null);
                    pdfDocument.finishPage(page);
                    bitmap.recycle();
                }
                pdfDocument.writeTo(fileOutputStream);
                pdfDocument.close();
                Toast.makeText(ActivityHome.this, "File Saved", Toast.LENGTH_SHORT).show();
                share(file);
            } catch (IOException e) {
                Log.d(TAG, "IO Exception: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private File getOutputFile(String filename) {
        File root = new File(Environment.getExternalStorageDirectory(), "Image TO Pdf");
        boolean isFolderCreated = true;

        if (!root.exists()) {
            Log.d(TAG, "getOutputFile: not exist");
            isFolderCreated = root.mkdir();
        } else {
            Log.d(TAG, "getOutputFile:  exist");
        }

        if (isFolderCreated) {
            Log.d(TAG, "getOutputFile: created");
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            String imageFileName = filename + "_" + timeStamp;

            return new File(root, imageFileName + ".pdf");
        } else {
            Toast.makeText(this, "Folder is not created", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public void extendFloatingButton() {
        if (isOpen) {
            activityHomeBinding.flotingbuttonHomeAdd.startAnimation(rotate_clockwise);
            activityHomeBinding.extflotingbuttonHomeImportfromgalary.startAnimation(fav_close);
            activityHomeBinding.extflotingbuttonTakeimage.startAnimation(fav_close);
            isOpen = false;
        } else {
            activityHomeBinding.flotingbuttonHomeAdd.startAnimation(rotate_anticlockwise);
            activityHomeBinding.extflotingbuttonHomeImportfromgalary.startAnimation(fav_open);
            activityHomeBinding.extflotingbuttonTakeimage.startAnimation(fav_open);
            isOpen = true;
        }
    }

    //Galary open for place picture
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), image_rec_code);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        closeDrawer();
        initDayNightMode();
    }

    public void closeDrawer() {
        activityHomeBinding.drwerlayoutActivityMain.closeDrawer(Gravity.LEFT, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == image_rec_code && resultCode == RESULT_OK && data != null) {
            // Get the Image from data
            mArrayUri.clear();
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                int cout = data.getClipData().getItemCount();
                for (int i = 0; i < cout; i++) {
                    // adding imageuri in array
                    Uri imageurl = mClipData.getItemAt(i).getUri();
                    Log.d(TAG, "Url: " + imageurl);
                    mArrayUri.add(imageurl);
                }
                position = 0;
            } else {
                Uri imageurl = data.getData();
                mArrayUri.add(imageurl);
                position = 0;
            }
            adapterImageList.notifyDataSetChanged();
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {

            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);
            Log.d(TAG, "Uri: " + tempUri);
            mArrayUri.add(tempUri);
            adapterImageList.notifyDataSetChanged();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onBackPressed() {
        if (backpressed + 2000 > System.currentTimeMillis()) {
            backtost.cancel();
            super.onBackPressed();
            return;
        } else {
            backtost = Toast.makeText(ActivityHome.this, "press BACK again to Exit", Toast.LENGTH_SHORT);
            backtost.show();
        }

        backpressed = System.currentTimeMillis();
    }

    void checkPermission() {
        if (ContextCompat.checkSelfPermission(ActivityHome.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityHome.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(ActivityHome.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        Log.d(TAG, "position delete: " + position);
        mArrayUri.remove(position);
        adapterImageList.notifyDataSetChanged();
    }
}