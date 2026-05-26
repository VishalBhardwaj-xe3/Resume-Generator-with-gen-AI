package com.intervi.resume.repository;

import com.intervi.resume.models.BlackListToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListedTokenRepository extends MongoRepository<BlackListToken, String>{
    boolean existsByToken(String token);
    
}