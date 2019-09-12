package com.darwinbox.recruitment.objects;

import java.util.HashMap;
import java.util.Map;

public class ArchivePosition {

    private String archiveName;
    private String archiveDescription;
    private String ID;

    public String getArchiveName() {
        return archiveName;
    }

    public void setArchiveName(String archiveName) {
        this.archiveName = archiveName;
    }

    public String getArchiveDescription() {
        return archiveDescription;
    }

    public void setArchiveDescription(String archiveDescription) {
        this.archiveDescription = archiveDescription;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void toObject(Map<String, String> body) {

        setArchiveName("Archive reason1->done by automation");
        setArchiveDescription("created by automation");

    }

    public Map<String,String> toMap() {

        Map<String, String> body = new HashMap<>();
        body.put("yt0", "SAVE");
        body.put("RecruitmentArchieveReason[archive_name]",getArchiveName());
        body.put("RecruitmentArchieveReason[archive_desc]",getArchiveDescription());

        return body;
    }



}
