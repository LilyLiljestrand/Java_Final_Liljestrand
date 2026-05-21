package org.example.mccstudentrides.api;

import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, Integer>{
    boolean existsByToken(String token);
    Token getUserIdByToken(String token);
}
