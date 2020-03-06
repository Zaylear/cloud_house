package com.rbi.interactive.entity.dto;

import java.io.Serializable;
import java.util.List;

public class RoomInfoTreeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private String pid;
    private List<?> villageChoose2DTO;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public List<?> getVillageChoose2DTO() {
        return villageChoose2DTO;
    }

    public void setVillageChoose2DTO(List<?> villageChoose2DTO) {
        this.villageChoose2DTO = villageChoose2DTO;
    }

    @Override
    public String toString() {
        return "RoomInfoTreeDTO{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", pid='" + pid + '\'' +
                ", villageChoose2DTO=" + villageChoose2DTO +
                '}';
    }
}
