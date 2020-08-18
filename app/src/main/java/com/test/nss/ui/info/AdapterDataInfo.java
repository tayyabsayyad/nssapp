package com.test.nss.ui.info;

public class AdapterDataInfo {
    private String infoName;
    private String infoDesc;
    private String gitId;

    public AdapterDataInfo(String infoName, String infoDesc, String gitId) {
        this.infoName = infoName;
        this.infoDesc = infoDesc;
        this.gitId = gitId;
    }

    public String getGitId() {
        return gitId;
    }

    public String getInfoName() {
        return infoName;
    }

    public String getInfoDesc() {
        return infoDesc;
    }
}
