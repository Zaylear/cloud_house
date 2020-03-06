package com.rbi.admin.entity.edo;

import lombok.Data;

import javax.persistence.*;
@Data
@Entity
@Table(name = "default_search")
public class DefaultSearchDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织/机构ID'")
    private String organizationId;

    @Column(name = "surname",columnDefinition = "varchar(50) comment '业主名称'")
    private String surname;

    @Column(name = "idt",columnDefinition = "datetime(0) comment'插入时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment'修改时间'")
    private String udt;

}
