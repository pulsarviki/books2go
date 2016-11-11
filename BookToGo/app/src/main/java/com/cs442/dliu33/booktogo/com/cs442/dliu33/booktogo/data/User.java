package com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    public final String username;
    public final String email;
    public final String password;
    public final String picture;

    public User(
            @JsonProperty("username") String username,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("picture") String picture) {

        this.username = username;
        this.email = email;
        this.password = password;
        this.picture = picture;
    }

    @JsonAnySetter
    public void mongoId(String key, Object value) {
        if (!key.equals("_id"))
            throw new RuntimeException(String.format(
                    "unknown json: %s:%s", key, value));
    }
}
