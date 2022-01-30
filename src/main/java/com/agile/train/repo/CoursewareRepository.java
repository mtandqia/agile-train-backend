package com.agile.train.repo;

import com.agile.train.entity.Courseware;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Mengting Lu
 * @date 2022/1/30 14:12
 */
@Repository
public interface CoursewareRepository extends MongoRepository<Courseware,String> {

    Optional<Courseware> findOneById(String coursewareId);

    Optional<Courseware> findOneByCoursewareName(String fileName);
}
