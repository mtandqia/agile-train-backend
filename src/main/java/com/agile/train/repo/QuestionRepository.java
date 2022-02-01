package com.agile.train.repo;

import com.agile.train.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Mengting Lu
 * @date 2022/2/1 13:22
 */
@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {

    Page<Question> findByQuestionTitleLikeOrQuestionContentLike(String keyword, String keyword1, Pageable pageable);
}
