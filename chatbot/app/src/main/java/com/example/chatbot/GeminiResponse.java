package com.example.chatbot;

import java.util.List;

public class GeminiResponse {

    public List<Candidate> candidates;

    public String getReplyText() {
        if (candidates != null && !candidates.isEmpty()) {
            Candidate first = candidates.get(0);
            if (first.content != null && first.content.parts != null && !first.content.parts.isEmpty()) {
                return first.content.parts.get(0).text;
            }
        }
        return "Sorry, I could not get a response.";
    }

    public static class Candidate {
        public Content content;
    }

    public static class Content {
        public List<Part> parts;
    }

    public static class Part {
        public String text;
    }
}