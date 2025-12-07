package com.ngocminh.batdongsan_be.service;

import com.ngocminh.batdongsan_be.dto.request.ChatRequest;
import com.ngocminh.batdongsan_be.dto.response.ChatResponse;
import com.ngocminh.batdongsan_be.model.ChatMessage;
import com.ngocminh.batdongsan_be.model.Conversation;
import com.ngocminh.batdongsan_be.repository.ChatMessageRepository;
import com.ngocminh.batdongsan_be.repository.ConversationRepository;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.cloudinary.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ConversationRepository conversationRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final OpenAiService openAiService;

    public ChatResponse processMessage(ChatRequest request) {
        Conversation conversation = conversationRepository
                .findTopByUserIdOrderByLastUpdatedDesc(request.getUserId())
                .orElseGet(() -> conversationRepository.save(
                        Conversation.builder()
                                .userId(request.getUserId())
                                .startTime(LocalDateTime.now())
                                .lastUpdated(LocalDateTime.now())
                                .status("ACTIVE")
                                .build()
                ));

        // Gọi GPT để tạo câu trả lời
        String reply = generateReplyFromGPT(request.getMessage());

        chatMessageRepository.save(ChatMessage.builder()
                .sender("user")
                .message(request.getMessage())
                .timestamp(LocalDateTime.now())
                .conversation(conversation)
                .build());

        chatMessageRepository.save(ChatMessage.builder()
                .sender("ai")
                .message(reply)
                .timestamp(LocalDateTime.now())
                .conversation(conversation)
                .build());

        conversation.setLastUpdated(LocalDateTime.now());
        conversationRepository.save(conversation);

        return ChatResponse.builder()
                .reply(reply)
                .conversationId(conversation.getId())
                .build();
    }

    private String generateReplyFromGPT(String message) {
        String apiUrl = "https://v98store.com/v1/chat/completions";
        String apiKey = "sk-cuStztjQbInY9aFkK8iUvo89Coq4EybQ5pgIvMbFx5Krb2ge";

        OkHttpClient client = new OkHttpClient();

        // Prompt hệ thống chuyên về bất động sản
        String jsonBody = """
        {
          "model": "gpt-4o-mini",
          "messages": [
            {
              "role": "system",
              "content": "Bạn là trợ lý AI của sàn giao dịch bất động sản Ngọc Minh, chuyên tư vấn mua bán, đầu tư, định giá và pháp lý bất động sản tại Việt Nam. \
                Bạn có thể trả lời tự nhiên, ngắn gọn, thân thiện, nhưng mang tính chuyên môn. \
                Bạn có thể tư vấn về giá nhà đất, vị trí, quy hoạch, hợp đồng mua bán, xu hướng đầu tư, và giải thích khái niệm bất động sản cho khách hàng phổ thông."
            },
            {
              "role": "user",
              "content": "%s"
            }
          ],
          "temperature": 0.7,
          "max_tokens": 250
        }
        """.formatted(message);

        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonBody, mediaType);

        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("GPT request failed: " + response.code() + " " + response.message());
                return "Xin lỗi, tôi hiện không thể trả lời. Vui lòng thử lại sau.";
            }

            String resString = response.body().string();
            JSONObject resJson = new JSONObject(resString);
            return resJson
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "Xin lỗi, hiện hệ thống đang gặp sự cố. Vui lòng thử lại sau.";
        }
    }

}
