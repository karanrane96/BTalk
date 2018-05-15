package com.example.android.chatmini;

public class Conversation {
    public boolean seen;
    public long timeAgo;


    public Conversation() {
    }

    public Conversation(boolean seen, long timeAgo) {
        this.seen = seen;
        this.timeAgo = timeAgo;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(long timeAgo) {
        this.timeAgo = timeAgo;
    }
}
