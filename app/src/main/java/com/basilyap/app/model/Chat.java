package com.basilyap.app.model;

public class Chat {

    private int id;
    private String email;
    private String title;
    private String question;
    private String answer;

    public Chat(int id, String email, String title, String question, String answer) {
        this.id = id;
        this.email = email;
        this.title = title;
        this.question = question;
        this.answer = answer;
    }

    public Chat() {
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getTitle() {
        return title;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
