package com.example.imagetopdf.UI;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.imagetopdf.Adapter.AdapterImageList;
import com.example.imagetopdf.KEYS;
import com.example.imagetopdf.R;
import com.example.imagetopdf.Tools;
import com.example.imagetopdf.databinding.ActivityHomeBinding;
import com.facebook.internal.LockOnGetVariable;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityHome extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "ActivityHome";
    private ActivityHomeBinding activityHomeBinding;
    private TextView headerName;
    Animation fav_open, fav_close, rotate_clockwise, rotate_anticlockwise;
    boolean isOpen = false;
    private int image_rec_code = 1;
    private Uri filepath_uri;
    Bitmap bitmap;
    ArrayList<Uri> mArrayUri;
    int position = 0;
    private AdapterImageList adapterImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(activityHomeBinding.getRoot());

        initHeader();
        initNavAndToolbar();
        initAnimation();


        mArrayUri = new ArrayList<Uri>();

        activityHomeBinding.recyclerviewImage.setFitsSystemWindows(true);
        activityHomeBinding.recyclerviewImage.setLayoutManager(new LinearLayoutManager(this));
        adapterImageList = new AdapterImageList(mArrayUri);
        activityHomeBinding.recyclerviewImage.setAdapter(adapterImageList);


        // click here to select next image
        activityHomeBinding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < mArrayUri.size() - 1) {
                    // increase the position by 1
                    position++;
                    activityHomeBinding.image.setImageURI(mArrayUri.get(position));
                    getDropboxIMGSize(mArrayUri.get(position));
                } else {
                    Toast.makeText(ActivityHome.this, "Last Image Already Shown", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // click here to view previous image
        activityHomeBinding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0) {
                    // decrease the position by 1
                    position--;
                    activityHomeBinding.image.setImageURI(mArrayUri.get(position));
                    getDropboxIMGSize(mArrayUri.get(position));
                }
            }
        });


        activityHomeBinding.flotingbuttonHomeAdd.setOnClickListener(this);
        activityHomeBinding.extflotingbuttonTakeimage.setOnClickListener(this);
        activityHomeBinding.extflotingbuttonHomeImportfromgalary.setOnClickListener(this);
        activityHomeBinding.extflotingbuttonHomeNewfolder.setOnClickListener(this);
        activityHomeBinding.navViewActivityHome.setNavigationItemSelectedListener(this);
    }

    private void getDropboxIMGSize(Uri uri) {
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            int imageWidth = bitmap.getWidth();
            int imageHeight = bitmap.getHeight();
            Log.d(TAG, "check Information: h=" + imageHeight + " w=" + imageWidth);
        } catch (Exception e) {
            Log.d(TAG, "check Information: error:" + e.getMessage());
        }

    }

    private void initHeader() {
        View header = activityHomeBinding.navViewActivityHome.getHeaderView(0);
        headerName = header.findViewById(R.id.header_name);
        headerName.setText(Tools.getPref(KEYS.USER_NAME, "User Name"));
    }

    void initNavAndToolbar() {
        setTitle(null);
        setSupportActionBar(activityHomeBinding.toolbarActivityMain);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, activityHomeBinding.drwerlayoutActivityMain,
                activityHomeBinding.toolbarActivityMain,
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
    public void onClick(View v) {
        if (v == activityHomeBinding.flotingbuttonHomeAdd) {
            extendFloatingButton();
        }
        if (v == activityHomeBinding.extflotingbuttonTakeimage) {
            Log.d(TAG, "Take Image .");
            extendFloatingButton();
            openGallery();

        }
        if (v == activityHomeBinding.extflotingbuttonHomeNewfolder) {
            Log.d(TAG, "Create a New Folder .");
            extendFloatingButton();
            createPDFWithMultipleImage();
        }
        if (v == activityHomeBinding.extflotingbuttonHomeImportfromgalary) {
            Log.d(TAG, "Import from Gallery .");
            extendFloatingButton();
            openGallery();
        }
    }

    private void createPDFWithMultipleImage(){
        File file = getOutputFile();
        if (file != null){
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                PdfDocument pdfDocument = new PdfDocument();
                Log.d(TAG, "getOutputFile: try");
                for (int i = 0; i < mArrayUri.size(); i++){
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mArrayUri.get(i));
                   // Bitmap bitmap = BitmapFactory.decodeFile(mArrayUri.get(i).getPath());
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

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private File getOutputFile(){
        File root = new File(this.getExternalFilesDir(null),"Fuad");

        boolean isFolderCreated = true;

        if (!root.exists()){
            Log.d(TAG, "getOutputFile: not exist");
            isFolderCreated = root.mkdir();
        }else {
            Log.d(TAG, "getOutputFile:  exist");
        }

        if (isFolderCreated) {
            Log.d(TAG, "getOutputFile: created");
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            String imageFileName = "PDF_" + timeStamp;

            return new File(root, imageFileName + ".pdf");
        }
        else {
            Toast.makeText(this, "Folder is not created", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public void extendFloatingButton() {
        if (isOpen) {
            activityHomeBinding.flotingbuttonHomeAdd.startAnimation(rotate_clockwise);
            activityHomeBinding.extflotingbuttonHomeImportfromgalary.startAnimation(fav_close);
            activityHomeBinding.extflotingbuttonHomeNewfolder.startAnimation(fav_close);
            activityHomeBinding.extflotingbuttonTakeimage.startAnimation(fav_close);
            isOpen = false;
        } else {
            activityHomeBinding.flotingbuttonHomeAdd.startAnimation(rotate_anticlockwise);
            activityHomeBinding.extflotingbuttonHomeImportfromgalary.startAnimation(fav_open);
            activityHomeBinding.extflotingbuttonHomeNewfolder.startAnimation(fav_open);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "Navigation Item selected: " + item.toString());
        switch (item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(ActivityHome.this, ActivityHome.class));
                finish();
                break;
            case R.id.nav_trash:
                startActivity(new Intent(ActivityHome.this, ActivityTrash.class));
                break;
        }
        activityHomeBinding.drwerlayoutActivityMain.closeDrawer(GravityCompat.START);
        return true;
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
                    Uri imageurl = data.getClipData().getItemAt(i).getUri();
                    mArrayUri.add(imageurl);
                }
                // setting 1st selected image into image switcher
                activityHomeBinding.image.setImageURI(mArrayUri.get(0));
                position = 0;
            } else {
                Uri imageurl = data.getData();
                mArrayUri.add(imageurl);
                activityHomeBinding.image.setImageURI(mArrayUri.get(0));
                position = 0;
            }
            adapterImageList.notifyDataSetChanged();
        } else {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }
}