package com.agile.train.repo;

import com.agile.train.entity.CommentAndUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wqy
 * @date 2022/2/1 22:11
 */
@Repository
public interface ForumRepository extends MongoRepository<CommentAndUser, String> {

    List<CommentAndUser> findAllByQuestionId(String questionId);

    int countByQuestionId(String id);
}
