package org.waffles.models;

import java.util.Collections;
import java.util.List;

public class AiModels {

    /*
        OLLAMA
        JSON: {"model","llama3","prompt","hello","stream":false}
    */

    public static class OllamaRequest{

        String model;
        String prompt;
        boolean stream;

        public OllamaRequest(String model, String prompt){
            this.model = model;
            this.prompt = prompt;
            this.stream = false;// We want the whole text at once, not word-by-word
        }
    }

    public static class OllamaResponse{
        public String response;
    }

    /*
        GEMINI
        JSON: { "candidates": [ { "content": { "parts": [ { "text": "Hello!" } ] } } ] }
    */

    public static class Part{
        String text;

        public Part(String text){
            this.text = text;
        }
    }

    public static class Content{

        List<Part> parts;

        public Content(Part part) {
            this.parts = Collections.singletonList(part);
        }
    }


    public static class GeminiRequest{

        List<Content> contents;

        public GeminiRequest(String prompt){

            this.contents = Collections.singletonList(
                    new Content(new Part(prompt))
            );
        }
    }

    public static class GeminiResponse{

        public List<Candidate> candidates;

        public static class Candidate{
            public Content content;
        }

        public static class UsageMetaData{
            public int promptTokenCount;
            public int candidatesTokenCount;
            public int totalTokenCount;
        }

        public String getAnswer(){

            return candidates.get(0).content.parts.get(0).text;
        }
    }
}
