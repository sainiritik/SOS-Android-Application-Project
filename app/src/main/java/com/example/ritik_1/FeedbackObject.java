package com.example.ritik_1;

public class FeedbackObject {
    private String id;
    private String feedback;
    private String emailAddress;

    // Default constructor required for Firebase
    public FeedbackObject() {
    }

    // Constructor with all fields
    public FeedbackObject(String id, String feedback, String emailAddress) {
        this.id = id;
        this.feedback = feedback;
        this.emailAddress = emailAddress;
    }

    // Getter for id
    public String getId() {
        return id;
    }

    // Setter for id
    public void setId(String id) {
        this.id = id;
    }

    // Getter for feedback
    public String getFeedback() {
        return feedback;
    }

    // Setter for feedback
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    // Getter for emailAddress
    public String getEmailAddress() {
        return emailAddress;
    }

    // Setter for emailAddress
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    // Override toString method to provide a readable representation of the object
    @Override
    public String toString() {
        return "FeedbackObject{" +
                "id='" + id + '\'' +
                ", feedback='" + feedback + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
