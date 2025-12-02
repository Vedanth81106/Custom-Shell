package org.waffles.models;

public class LeetcodeModels {

    public static class GraphRequest{

        public String query;
        public Object variables;

        public GraphRequest(String query){
            this.query = query;
            this.variables = new Object();
        }
    }

    public static class GraphResponse{

        public Data data;
    }

    public static class Data{

        public Question randomQuestion;
    }

    public static class Question{

        public String title;
        public String titleSlug; // The part of the URL (two-sum)
        public String difficulty;
    }

}
