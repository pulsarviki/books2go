package com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Message {

    public final String id;
    public final String bookId;
    public final String sender;
    public final String receiver;
    public final String bookStatus;
    public final int msgDate;


    public Message(
            @JsonProperty("id") String id,
            @JsonProperty("bookId") String bookId,
            @JsonProperty("sender") String sender,
            @JsonProperty("receiver") String receiver,
            @JsonProperty("bookStatus") String bookStatus,
            @JsonProperty("msgDate") int msgDate) {
        this.id = id;
        this.bookId = bookId;
        this.sender = sender;
        this.receiver = receiver;
        this.bookStatus = bookStatus;
        this.msgDate = msgDate;
    }

    @JsonAnySetter
    public void mongoId(String key, Object value) {
        if (!key.equals("_id"))
            throw new RuntimeException(String.format(
                    "unknown json: %s:%s", key, value));
    }
}
