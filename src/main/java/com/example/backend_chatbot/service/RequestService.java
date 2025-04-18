package com.example.backend_chatbot.service;

import com.example.backend_chatbot.dto.response.MessageReply;
import com.example.backend_chatbot.dto.request.MessageRequest;
import com.example.backend_chatbot.entity.Message;
import com.example.backend_chatbot.repository.ConversationRepo;
import com.example.backend_chatbot.repository.MessageRepo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Service
public class RequestService {
    private final ConversationRepo conversationRepo;
    private final MessageRepo messageRepo;

    private final RestTemplate restTemplate = new RestTemplate();

    public RequestService(MessageRepo messageRepo, ConversationRepo conversationRepo){
        this.conversationRepo = conversationRepo;
        this.messageRepo = messageRepo;
    }

    public MessageReply getAnswerFromChatBot(MessageRequest dto){
        // Store conversation
        Message storeRequest = new Message();
        storeRequest.setRequest(dto.getMessage());
        storeRequest.setRequestTime(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));

        // Gọi model ở đây
        ResponseEntity<Map> response = makeRequest(dto);
        String reply = (String) response.getBody().get("reply");
        System.out.println(reply);

        storeRequest.setResponse(reply);
        storeRequest.setResponseTime(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        messageRepo.save(storeRequest);

        return new MessageReply(reply);
    }

    public MessageReply guessGetAnswer(MessageRequest dto){
        ResponseEntity<Map> response = makeRequest(dto);
        String reply = (String) response.getBody().get("reply");
        return new MessageReply(reply);
    }

    private ResponseEntity<Map> makeRequest(MessageRequest dto) {
        String url = "http://localhost:8000/chat";

        Map<String, String> body = new HashMap<>();

        body.put("question", dto.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        return restTemplate.postForEntity(url, request, Map.class);
    }

}
