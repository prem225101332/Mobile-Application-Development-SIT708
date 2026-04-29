package com.example.lostandfound;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.io.*;
import java.util.Calendar;

public class CreateAdvertActivity extends AppCompatActivity {

    private EditText etName, etPhone, etDesc, etDate, etLocation;
    private RadioButton radioLost, radioFound;
    private Spinner spinnerCategory;
    private ImageView imagePreview;
    private String savedImagePath = "";
    private final String[] categories = {
            "Electronics", "Pets", "Wallets", "Clothing",
            "Keys", "Bags", "Jewelry", "Documents", "Other"
    };

    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    savedImagePath = copyUriToInternalStorage(uri);
                    if (!savedImagePath.isEmpty()) {
                        imagePreview.setImageBitmap(BitmapFactory.decodeFile(savedImagePath));
                        imagePreview.setVisibility(android.view.View.VISIBLE);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);
        setTitle("Create New Advert");

        etName     = findViewById(R.id.etName);
        etPhone    = findViewById(R.id.etPhone);
        etDesc     = findViewById(R.id.etDescription);
        etDate     = findViewById(R.id.etDate);
        etLocation = findViewById(R.id.etLocation);
        radioLost  = findViewById(R.id.radioLost);
        radioFound = findViewById(R.id.radioFound);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        imagePreview    = findViewById(R.id.imagePreview);

        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(catAdapter);

        etDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view, y, m, d) ->
                    etDate.setText(y + "-" + (m + 1) + "-" + d),
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        findViewById(R.id.btnPickImage).setOnClickListener(v ->
                imagePickerLauncher.launch("image/*"));

        findViewById(R.id.btnSave).setOnClickListener(v -> saveItem());
    }

    private String copyUriToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) return "";

            File dir = new File(getFilesDir(), "images");
            if (!dir.exists()) dir.mkdirs();

            File outFile = new File(dir, "img_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(outFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            return outFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
            return "";
        }
    }

    private void saveItem() {
        String name     = etName.getText().toString().trim();
        String phone    = etPhone.getText().toString().trim();
        String desc     = etDesc.getText().toString().trim();
        String date     = etDate.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || desc.isEmpty()
                || date.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        LostFoundItem item = new LostFoundItem();
        item.setType(radioLost.isChecked() ? "Lost" : "Found");
        item.setName(name);
        item.setPhone(phone);
        item.setDescription(desc);
        item.setDate(date);
        item.setLocation(location);
        item.setCategory(categories[spinnerCategory.getSelectedItemPosition()]);
        item.setImagePath(savedImagePath);

        new DatabaseHelper(this).insertItem(item);
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
        finish();
    }
}