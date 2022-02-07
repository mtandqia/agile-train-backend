package com.agile.train.repo;

import com.agile.train.entity.CommentAndUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
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

    int countByReplyUserLoginName(String login);

    @Query(value = "{'loginName':/?0/}",fields="{'questionId':1,'_id':0}")
    List<String> findQuestionIdList(String loginName);
}
