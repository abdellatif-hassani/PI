package com.example.speechtotextservice.controller;

import com.pi.speechtotextservice.service.SpeechToTextService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/speech-to-text")
public class SpeechToTextController {

    @Autowired
    private SpeechToTextService speechToTextService;

    @PostMapping("/transcribe")
    public String transcribeAudio(@RequestParam("audioUrl") String audioUrl) {
        return speechToTextService.transcribeAudio(audioUrl);
    }
}