package com.intervi.resume.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDateTime;

@Data
@Document("BlackList_tokens")
public class BlackListToken {
    @Id
    private String Id;

    @Indexed(unique = true)
    private String token;

    private LocalDateTime blackListAt = LocalDateTime.now();
}
