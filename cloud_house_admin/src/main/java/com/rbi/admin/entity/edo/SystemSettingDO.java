package com.rbi.admin.entity.edo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "system_setting")
public class SystemSettingDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment'组织id'")
    private String organizationId;

    @Column(name = "setting_code",columnDefinition = "varchar(50) comment'设置编号'")
    private String settingCode;

    @Column(name = "setting_name",columnDefinition = "varchar(50) comment'设置名称'")
    private String settingName;

    @Column(name = "setting_type",columnDefinition = "varchar(50) comment'设置类型'")
    private String settingType;

    @Column(name = "status",columnDefinition = "tinyint(4) DEFAULT '0' comment '状态值'")
    private Integer status;

    @Column(name = "idt",columnDefinition = "datetime(0) comment'插入时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment '修改时间'")
    private String udt;

}
