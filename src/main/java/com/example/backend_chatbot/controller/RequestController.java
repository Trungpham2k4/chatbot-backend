package com.example.backend_chatbot.controller;

import com.example.backend_chatbot.dto.response.MessageReply;
import com.example.backend_chatbot.dto.request.MessageRequest;
import com.example.backend_chatbot.dto.response.ResponseDTO;
import com.example.backend_chatbot.service.RequestService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RequestController {
    private final RequestService requestService;

    public RequestController(RequestService requestService){
        this.requestService = requestService;
    }

    @PostMapping("/request")
    public ResponseDTO<MessageReply> request(@RequestBody MessageRequest request){
        return ResponseDTO.<MessageReply>builder()
                .data(requestService.getAnswerFromChatBot(request))
                .status(200)
                .message("Conversation successful")
                .build();
    }

    @PostMapping("/guest")
    public ResponseDTO<MessageReply> guest(@RequestBody MessageRequest request){
        return ResponseDTO.<MessageReply>builder()
                .data(requestService.guessGetAnswer(request))
                .status(200)
                .message("test success")
                .build();
    }
}
