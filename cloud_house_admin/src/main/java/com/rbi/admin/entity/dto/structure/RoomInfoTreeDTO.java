package com.rbi.admin.entity.dto.structure;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RoomInfoTreeDTO implements Serializable {
    private String code;
    private String name;
    private String pid;
    private List<?> villageChoose2DTO;

}
