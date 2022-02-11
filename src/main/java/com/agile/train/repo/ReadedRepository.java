package com.agile.train.repo;

import com.agile.train.entity.Readed;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Mengting Lu
 * @date 2022/2/8 16:26
 */
@Repository
public interface ReadedRepository extends MongoRepository<Readed,String> {

    List<Readed> findByUserLoginNameAndQuestionId(String login, String questionId);

    List<Readed> findByUserLoginNameAndReader(String loginName, boolean b);

    void deleteByUserLoginNameAndQuestionId(String qqq, String id);
}
