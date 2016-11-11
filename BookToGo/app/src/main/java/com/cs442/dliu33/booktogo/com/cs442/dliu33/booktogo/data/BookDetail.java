package com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Calendar;
import java.util.Date;

public class BookDetail {

    public final String id;
    public final String bookName;
    public final String version;
    public final String author;
    public final String bookImg;
    public final String condition;
    public final double price;
    public final double finalPrice;
    public final String seller;
    public final String buyer;
    public final String reserveBy;
    public final double currentBid;
    public final String currentBidder;
    public final int postDate;
    public final int purchasedDate;
    public final String status;

    @JsonCreator
    public BookDetail(
            @JsonProperty("id") String id,
            @JsonProperty("bookName") String bookName,
            @JsonProperty("version") String version,
            @JsonProperty("author") String author,
            @JsonProperty("bookImg") String bookImg,
            @JsonProperty("condition") String condition,
            @JsonProperty("price") double price,
            @JsonProperty("finalPrice") double finalPrice,
            @JsonProperty("seller") String seller,
            @JsonProperty("buyer") String buyer,
            @JsonProperty("reserveBy") String reserveBy,
            @JsonProperty("currentBid") double currentBid,
            @JsonProperty("currentBidder") String currentBidder,
            @JsonProperty("postDate") int postDate,
            @JsonProperty("purchasedDate") int purchasedDate,
            @JsonProperty("status") String status)
    {
        this.id = id;
        this.bookName = bookName;
        this.version = version;
        this.author = author;
        this.bookImg = bookImg;
        this.condition = condition;
        this.price = price;
        this.finalPrice = finalPrice;
        this.seller = seller;
        this.buyer = buyer;
        this.reserveBy = reserveBy;
        this.currentBid = currentBid;
        this.currentBidder = currentBidder;
        this.postDate = postDate;
        this.purchasedDate = purchasedDate;
        this.status =  status;
    }

    @JsonAnySetter
    public void mongoId(String key, Object value) {
        if (!key.equals("_id"))
            throw new RuntimeException(String.format(
                    "unknown json: %s:%s", key, value));
    }
}
