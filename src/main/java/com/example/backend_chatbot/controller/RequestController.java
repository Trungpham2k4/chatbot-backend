package com.example.backend_chatbot.controller;

import com.example.backend_chatbot.dto.response.MessageReply;
import com.example.backend_chatbot.dto.request.MessageRequest;
import com.example.backend_chatbot.dto.response.MessageResponse;
import com.example.backend_chatbot.dto.response.ResponseDTO;
import com.example.backend_chatbot.service.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class RequestController {
    private final RequestService requestService;

    public RequestController(RequestService requestService){
        this.requestService = requestService;
    }

    @GetMapping("/getMessages/{id}")
    public ResponseDTO<List<MessageResponse>> getAllMessages(@PathVariable int id){
        return ResponseDTO.<List<MessageResponse>>builder()
                .data(requestService.getAllMessagesInConversation(id))
                .status(200)
                .message("Get data successful")
                .build();
    }

    @PostMapping("/request/{id}")
    public ResponseDTO<MessageReply> request(@RequestBody MessageRequest request, @PathVariable Integer id){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info("GrantedAuthority: {}", grantedAuthority));


        return ResponseDTO.<MessageReply>builder()
                .data(requestService.getAnswerFromChatBot(request, id))
                .status(200)
                .message("Conversation successful")
                .build();
    }

    @PostMapping("/guest")
    public ResponseDTO<MessageReply> guest(@RequestBody MessageRequest request){
        return ResponseDTO.<MessageReply>builder()
                .data(requestService.guestGetAnswer(request))
                .status(200)
                .message("Conversation successful")
                .build();
    }


    @PostMapping("/test")
    public ResponseDTO<MessageReply> test(){
        return ResponseDTO.<MessageReply>builder()
                .data(requestService.testSaveMessage())
                .status(200)
                .message("test success")
                .build();
    }
}
