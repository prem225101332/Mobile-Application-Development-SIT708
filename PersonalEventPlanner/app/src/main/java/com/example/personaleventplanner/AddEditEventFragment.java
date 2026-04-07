package com.example.personaleventplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.material.snackbar.Snackbar;
import java.util.Calendar;
import java.util.concurrent.Executors;

public class AddEditEventFragment extends Fragment {

    private EditText etTitle, etLocation, etDate, etTime;
    private Spinner spinnerCategory;
    private TextView tvTitleError, tvDateError;
    private EventDB db;
    private long eventId = -1;
    private long selectedDateTimestamp = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.addevent, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = EventDB.getInstance(requireContext());

        etTitle = view.findViewById(R.id.etTitle);
        etLocation = view.findViewById(R.id.etLocation);
        etDate = view.findViewById(R.id.etDate);
        etTime = view.findViewById(R.id.etTime);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        tvTitleError = view.findViewById(R.id.tvTitleError);
        tvDateError = view.findViewById(R.id.tvDateError);
        Button btnSave = view.findViewById(R.id.btnSave);

        String[] categories = {"Work", "Social", "Travel", "Personal"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categories
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(requireContext(), (datePicker, year, month, day) -> {
                Calendar selected = Calendar.getInstance();
                selected.set(year, month, day, 0, 0, 0);
                selected.set(Calendar.MILLISECOND, 0);

                if (selected.getTimeInMillis() < Calendar.getInstance().getTimeInMillis() - 86400000) {
                    tvDateError.setVisibility(View.VISIBLE);
                    etDate.setText("");
                    selectedDateTimestamp = -1;
                } else {
                    tvDateError.setVisibility(View.GONE);
                    selectedDateTimestamp = selected.getTimeInMillis();
                    etDate.setText(day + "/" + (month + 1) + "/" + year);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        etTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(requireContext(), (timePicker, hour, minute) -> {
                String time = String.format("%02d:%02d", hour, minute);
                etTime.setText(time);
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        });

        if (getArguments() != null) {
            eventId = getArguments().getLong("eventId", -1);
        }

        if (eventId != -1) {
            db.eventDao().getEventById(eventId).observe(getViewLifecycleOwner(), event -> {
                if (event != null) {
                    etTitle.setText(event.getTitle());
                    etLocation.setText(event.getLocation());
                    etDate.setText(event.getDate());
                    etTime.setText(event.getTime());
                    selectedDateTimestamp = event.getDateTimestamp();

                    for (int i = 0; i < categories.length; i++) {
                        if (categories[i].equals(event.getCategory())) {
                            spinnerCategory.setSelection(i);
                            break;
                        }
                    }
                }
            });
        }

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String location = etLocation.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String time = etTime.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();

            boolean valid = true;

            if (title.isEmpty()) {
                tvTitleError.setVisibility(View.VISIBLE);
                valid = false;
            } else {
                tvTitleError.setVisibility(View.GONE);
            }

            if (date.isEmpty() || selectedDateTimestamp == -1) {
                tvDateError.setVisibility(View.VISIBLE);
                valid = false;
            } else {
                tvDateError.setVisibility(View.GONE);
            }

            if (!valid) return;

            Executors.newSingleThreadExecutor().execute(() -> {
                Event event = new Event(title, category, location, date, time, selectedDateTimestamp);
                if (eventId == -1) {
                    db.eventDao().insert(event);
                } else {
                    event.setId(eventId);
                    db.eventDao().update(event);
                }

                requireActivity().runOnUiThread(() -> {
                    Snackbar.make(view, "Event saved!", Snackbar.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigateUp();
                });
            });
        });
    }
}