package com.example.dacn_murkoff_care_android.Chatbot;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dacn_murkoff_care_android.Adapter.MessageAdapter;
import com.example.dacn_murkoff_care_android.Model.MessageChat;
import com.example.dacn_murkoff_care_android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatbotActivity extends AppCompatActivity {

    ImageButton btnBack, sendButton;
    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    List<MessageChat> messageChatList;
    MessageAdapter messageAdapter;

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        setupComponent();
        setupRecyclerView();
        setupEvent();
    }

    private void setupComponent() {
        btnBack = findViewById(R.id.btnBack);
        sendButton = findViewById(R.id.send_btn);
        messageEditText = findViewById(R.id.message_edit_text);
        recyclerView = findViewById(R.id.recycler_view);
        welcomeTextView = findViewById(R.id.welcome_text);
        messageChatList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        messageAdapter = new MessageAdapter(messageChatList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
    }

    private void setupEvent() {
        sendButton.setOnClickListener(v -> {
            String question = messageEditText.getText().toString().trim();
            if (!question.isEmpty()) {
                addToChat(question, MessageChat.SENT_BY_ME);
                messageEditText.setText("");
                callAPI(question);
                welcomeTextView.setVisibility(View.GONE);
            }
        });

        btnBack.setOnClickListener(view -> finish());
    }

    void addToChat(String message, String sentBy) {
        runOnUiThread(() -> {
            messageChatList.add(new MessageChat(message, sentBy));
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        });
    }

    void addResponse(String response) {
        addToChat(response, MessageChat.SENT_BY_BOT);
    }


    /**NOTE:
     * Chatbot API by https://www.coze.com/
     * Before active to chat you need [id] and [token] chatbot
     * Attention: every chatbot limit at 30 token
     **/
    void callAPI(String question) {
        // Thêm tin nhắn "Typing..." vào giao diện
        messageChatList.add(new MessageChat("Typing...", MessageChat.SENT_BY_BOT));
        messageAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());

        // Tạo JSON payload hợp lệ
        String jsonPayload = "{"
                + "\"conversation_id\": \"123\","
                + "\"bot_id\": \"7445222212816224263\","
                + "\"user\": \"29032201862555\","
                + "\"query\": \"" + question + "\","
                + "\"stream\": false"
                + "}";

        // Tạo RequestBody
        RequestBody body = RequestBody.create(
                jsonPayload, MediaType.parse("application/json"));

        // Tạo Request
        Request request = new Request.Builder()
                .url("https://api.coze.com/open_api/v2/chat")
                .header("Authorization", "Bearer pat_N05vNsB02JPWL2gPTu1DSGX82BO76zru3QKUUrGR58V2IvvqgT4D23BKhvou0K81")
                .post(body)
                .build();

        // Gửi yêu cầu HTTP
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> addResponse("Đã xảy ra lỗi. Vui lòng thử lại sau!"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        // Parse JSON response
                        String responseBody = response.body().string();
                        System.out.println("API Response: " + responseBody);

                        JSONObject jsonObject = new JSONObject(responseBody);

                        // Lấy mảng messages
                        JSONArray messages = jsonObject.getJSONArray("messages");

                        // Lấy nội dung từ phần tử đầu tiên
                        if (messages.length() > 0) {
                            String content = messages.getJSONObject(0).getString("content");

                            // Thêm phản hồi từ bot vào giao diện
                            runOnUiThread(() -> addResponse(content));
                        } else {
                            runOnUiThread(() -> addResponse("Phản hồi không chứa nội dung."));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> addResponse("Đã xảy ra lỗi khi xử lý phản hồi: " + e.getMessage()));
                    }
                } else {
                    runOnUiThread(() -> addResponse("Yêu cầu không thành công: " + response.code()));
                }
            }
        });
    }

}
