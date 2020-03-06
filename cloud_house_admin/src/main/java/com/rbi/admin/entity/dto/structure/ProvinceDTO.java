package com.rbi.admin.entity.dto.structure;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data

public class ProvinceDTO{
    private String provinceCode;
    private String provinceName;
}
