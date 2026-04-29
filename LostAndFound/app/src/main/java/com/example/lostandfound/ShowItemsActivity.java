package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class ShowItemsActivity extends AppCompatActivity {

    private ListView listView;
    private Spinner spinnerFilter;
    private DatabaseHelper db;
    private List<LostFoundItem> currentItems;

    private final String[] filterOptions = {
            "All", "Electronics", "Pets", "Wallets", "Clothing", "Keys", "Bags", "Jewelry", "Documents", "Other"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items);
        setTitle("Lost & Found Items");

        db = new DatabaseHelper(this);
        listView = findViewById(R.id.listView);
        spinnerFilter = findViewById(R.id.spinnerFilter);

        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filterOptions);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(filterAdapter);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int pos, long id) {
                loadItems(filterOptions[pos]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            LostFoundItem item = currentItems.get(position);
            Log.d("SHOW", "Clicked item id = " + item.getId());
            Intent intent = new Intent(ShowItemsActivity.this, ItemDetailActivity.class);
            intent.putExtra("item_id", item.getId());
            startActivity(intent);
        });

        loadItems("All");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItems(filterOptions[spinnerFilter.getSelectedItemPosition()]);
    }

    private void loadItems(String category) {
        currentItems = category.equals("All") ? db.getAllItems() : db.getItemsByCategory(category);

        ArrayAdapter<LostFoundItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currentItems);
        listView.setAdapter(adapter);
    }
}