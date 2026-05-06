package com.example.chatbot;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {

    private static final String API_KEY = "AIzaSyAQUxH4mA1cooQXNO8VthfEmDn7DIpj0v0";

    RecyclerView recyclerView;
    EditText editTextMessage;
    ImageButton buttonSend;
    TextView textViewUsername;

    MessageAdapter adapter;
    AppDatabase database;
    GeminiApiService apiService;
    String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        currentUsername = getIntent().getStringExtra("USERNAME");
        recyclerView = findViewById(R.id.recyclerView);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        textViewUsername = findViewById(R.id.textViewUsername);

        textViewUsername.setText(currentUsername);
        adapter = new MessageAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        database = AppDatabase.getInstance(this);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://generativelanguage.googleapis.com/").addConverterFactory(GsonConverterFactory.create()).build();

        apiService = retrofit.create(GeminiApiService.class);
        loadChatHistory();

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editTextMessage.getText().toString().trim();

                if (text.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "Type a message first", Toast.LENGTH_SHORT).show();
                    return;
                }

                editTextMessage.setText("");
                sendMessage(text);
            }
        });
    }

    private void loadChatHistory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Message> history = database.messageDao().getMessagesForUser(currentUsername);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setMessages(history);
                        scrollToBottom();
                    }
                });
            }
        }).start();
    }

    private void sendMessage(String text) {
        Message userMessage = new Message(text, "user", System.currentTimeMillis(), currentUsername);
        adapter.addMessage(userMessage);
        scrollToBottom();

        new Thread(new Runnable() {
            @Override
            public void run() {
                database.messageDao().insertMessage(userMessage);
            }
        }).start();

        callGeminiApi(text);
    }

    private void callGeminiApi(String userText) {
        GeminiRequest request = new GeminiRequest(userText);

        apiService.sendMessage(API_KEY, request).enqueue(new Callback<GeminiResponse>() {

            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String replyText = response.body().getReplyText();
                    showBotReply(replyText);
                } else {
                    showBotReply("Error: Could not get a response (code " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<GeminiResponse> call, Throwable t) {
                showBotReply("Error: " + t.getMessage());
            }
        });
    }

    private void showBotReply(String replyText) {
        Message botMessage = new Message(replyText, "bot", System.currentTimeMillis(), currentUsername);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.addMessage(botMessage);
                scrollToBottom();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                database.messageDao().insertMessage(botMessage);
            }
        }).start();
    }
    private void scrollToBottom() {
        if (adapter.getItemCount() > 0) {
            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        }
    }
}