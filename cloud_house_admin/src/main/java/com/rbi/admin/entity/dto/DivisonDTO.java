package com.rbi.admin.entity.dto;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
public class DivisonDTO {
    private Integer id;
    private String divisonCode;
    private String divisonName;
    private String pid;
    private String idt;
    private String udt;
    private List<?> divisonDTO;
}
