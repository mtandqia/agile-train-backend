package com.agile.train.repo;

import com.agile.train.entity.UserDownload;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Mengting Lu
 * @date 2022/2/6 12:57
 */
@Repository
public interface UserDownloadRepository extends MongoRepository<UserDownload,String> {

    long countByUserId(String id);

    UserDownload findOneByCoursewareIdAndUserId(String id, String userId);
}
