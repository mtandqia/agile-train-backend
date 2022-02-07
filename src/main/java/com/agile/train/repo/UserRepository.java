package com.agile.train.repo;

import com.agile.train.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Mengting Lu
 * @date 2022/2/1 13:24
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<User> findOneByEmailIgnoreCase(String email);

    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<User> findOneByLogin(String login);

    //@Query(value="{$and:[{$or: [{'email': /?1/}, {'login': /?1/}]},{'authorities._id':/?0/}]})",sort= "{last_modified_time:-1}")
    @Query(value="{$and:[{'login': /?1/},{'authorities':/?0/}]}",sort= "{last_modified_time:-1}")
    Page<User> findByRoleAndKeyword(String role, String keyword, Pageable pageable);

}
