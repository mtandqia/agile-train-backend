package com.agile.train.dto;

import com.agile.train.entity.Courseware;

/**
 * @author Mengting Lu
 * @date 2022/3/30 15:01
 */
public class CoursewareWithDownloadInfo extends Courseware {
    private Boolean hasDownloaded;

    public CoursewareWithDownloadInfo(Courseware courseware,boolean hasd){
        super(courseware.getId(),courseware.getCoursewareUrl(), courseware.getCoursewareName(), courseware.getAddTime());
        hasDownloaded=hasd;
    }

    public Boolean getHasDownloaded() {
        return hasDownloaded;
    }

    public void setHasDownloaded(Boolean hasDownloaded) {
        this.hasDownloaded = hasDownloaded;
    }
}
