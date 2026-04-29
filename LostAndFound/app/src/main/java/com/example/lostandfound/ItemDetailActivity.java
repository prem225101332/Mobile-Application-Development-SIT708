package com.example.lostandfound;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        setTitle("Item Detail");

        int itemId = getIntent().getIntExtra("item_id", -1);
        Log.d("DETAIL", "Received item_id = " + itemId);

        if (itemId == -1) {
            Toast.makeText(this, "No item ID received", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        DatabaseHelper db = new DatabaseHelper(this);
        LostFoundItem item = db.getItemById(itemId);
        Log.d("DETAIL", "Item from DB = " + item);

        if (item == null) {
            Toast.makeText(this, "Item not found in database", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        TextView tvType     = findViewById(R.id.tvType);
        TextView tvName     = findViewById(R.id.tvName);
        TextView tvDesc     = findViewById(R.id.tvDescription);
        TextView tvDate     = findViewById(R.id.tvDate);
        TextView tvLocation = findViewById(R.id.tvLocation);
        TextView tvPhone    = findViewById(R.id.tvPhone);
        ImageView imgDetail = findViewById(R.id.detailImage);
        Button btnRemove    = findViewById(R.id.btnRemove);

        tvType.setText(item.getType().equals("Lost") ? "🔴 LOST" : "🟢 FOUND");
        tvName.setText(item.getName());
        tvDesc.setText(item.getDescription());
        tvDate.setText("📅 " + item.getDate());
        tvLocation.setText("📍 " + item.getLocation());
        tvPhone.setText("📞 " + item.getPhone());

        if (item.getImagePath() != null && !item.getImagePath().isEmpty()) {
            Bitmap bmp = BitmapFactory.decodeFile(item.getImagePath());
            if (bmp != null) {
                imgDetail.setImageBitmap(bmp);
            }
        }

        final int finalId = item.getId();
        btnRemove.setOnClickListener(v -> {
            db.deleteItem(finalId);
            Toast.makeText(this, "Listing removed!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}