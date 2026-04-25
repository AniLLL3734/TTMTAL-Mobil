package tr.edu.ttmtal.mobil.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import tr.edu.ttmtal.mobil.R;
import tr.edu.ttmtal.mobil.adapters.ChatAdapter;
import tr.edu.ttmtal.mobil.models.ChatMessage;

public class AiFragment extends Fragment {

    private RecyclerView rvChat;
    private EditText etMessage;
    private FloatingActionButton btnSend;
    private ProgressBar pbLoading;
    private List<ChatMessage> messageList;
    private ChatAdapter adapter;
    private String groqApiKey;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ai, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Firebase'den çekilen anahtarı SharedPreferences'tan oku, yoksa strings'ten oku (fallback)
        android.content.SharedPreferences prefs = requireContext().getSharedPreferences("ttmtal_secure_cache", android.content.Context.MODE_PRIVATE);
        groqApiKey = prefs.getString("groq_api_key", getString(R.string.groq_api_key));

        rvChat = view.findViewById(R.id.rvChat);
        etMessage = view.findViewById(R.id.etMessage);
        btnSend = view.findViewById(R.id.btnSend);
        pbLoading = view.findViewById(R.id.pbAiLoading);

        messageList = new ArrayList<>();
        adapter = new ChatAdapter(messageList);
        rvChat.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvChat.setAdapter(adapter);

        // Welcome message
        addMessage("Merhaba! Ben TTMTAL Okul Asistanı. Okulumuz, sınavlar, öğretmenlerimiz veya bölümlerimiz hakkında sana nasıl yardımcı olabilirim?", false);

        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                sendMessageToAi(text);
                etMessage.setText("");
            }
        });
    }

    private void addMessage(String text, boolean isUser) {
        messageList.add(new ChatMessage(text, isUser));
        adapter.notifyItemInserted(messageList.size() - 1);
        rvChat.scrollToPosition(messageList.size() - 1);
    }

    private void sendMessageToAi(String userText) {
        addMessage(userText, true);
        pbLoading.setVisibility(View.VISIBLE);
        btnSend.setEnabled(false);

        new Thread(() -> {
            try {
                URL url = new URL("https://api.groq.com/openai/v1/chat/completions");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + groqApiKey);
                conn.setDoOutput(true);

                // System Prompt - The Brain of the AI
                String systemPrompt = "Sen Pendik Türk Telekom Şehit Murat Mertel Mesleki ve Teknik Anadolu Lisesi (TTMTAL) resmi asistanısın. " +
                        "Okulun web sitesinden (turktelekomatl.meb.k12.tr) alınan güncel bilgilerle donatıldın. " +
                        "Okul Bilgileri: İstanbul Pendik'te yer alır. Bilişim Teknolojileri alanında uzmanlaşmış bir proje okuludur. " +
                        "İçerik: Okulumuzda Bilişim Teknolojileri (Web Programcılığı), Yenilenebilir Enerji gibi bölümler bulunur. " +
                        "Kadro: Okul Müdürü, Müdür Yardımcıları ve uzman öğretmen kadrosuna sahiptir. " +
                        "Misyon: Teknolojiyi takip eden, nitelikli teknik elemanlar yetiştirmek. " +
                        "Kurallar: Her zaman nazik, yardımcı ve resmi bir dil kullan. Kısa ve öz cevaplar ver. " +
                        "Bilmediğin bir konu olursa okulumuzun web sitesini ziyaret etmelerini söyle.";

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("model", "llama-3.3-70b-versatile");
                
                JSONArray messages = new JSONArray();
                messages.put(new JSONObject().put("role", "system").put("content", systemPrompt));
                
                // Add conversation history if needed, for now just current
                messages.put(new JSONObject().put("role", "user").put("content", userText));
                
                jsonBody.put("messages", messages);

                OutputStream os = conn.getOutputStream();
                os.write(jsonBody.toString().getBytes());
                os.flush();
                os.close();

                Scanner scanner = new Scanner(conn.getInputStream());
                String response = scanner.useDelimiter("\\A").next();
                scanner.close();

                JSONObject jsonResponse = new JSONObject(response);
                String aiText = jsonResponse.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");

                new Handler(Looper.getMainLooper()).post(() -> {
                    pbLoading.setVisibility(View.GONE);
                    btnSend.setEnabled(true);
                    addMessage(aiText, false);
                });

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    pbLoading.setVisibility(View.GONE);
                    btnSend.setEnabled(true);
                    Toast.makeText(getContext(), "Bağlantı hatası: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}
