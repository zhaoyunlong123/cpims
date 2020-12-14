CREATE TABLE cpims_prm_project_registration(
    id INT NOT NULL AUTO_INCREMENT  COMMENT '主键' ,
    project_name VARCHAR(128)    COMMENT '项目名称' ,
    owner VARCHAR(32)    COMMENT '业主' ,
    category INT    COMMENT '行业类别' ,
    property INT    COMMENT '建设性质' ,
    investment INT    COMMENT '投资分类' ,
    scope INT    COMMENT '投资范围' ,
    revision INT    COMMENT '乐观锁' ,
    created_by VARCHAR(32)    COMMENT '创建人' ,
    created_time DATETIME    COMMENT '创建时间' ,
    updated_by VARCHAR(32)    COMMENT '更新人' ,
    updated_time DATETIME    COMMENT '更新时间' ,
    PRIMARY KEY (id)
) COMMENT = '项目登记';

ALTER TABLE cpims_prm_project_registration COMMENT '项目登记';
CREATE TABLE cpims_prm_project_re(
    id INT NOT NULL AUTO_INCREMENT  COMMENT '主键' ,
    project_id INT NOT NULL   COMMENT '项目登记外键' ,
    project_name VARCHAR(128)    COMMENT '项目名称' ,
    owner VARCHAR(32)    COMMENT '业主' ,
    category INT    COMMENT '行业类别' ,
    property INT    COMMENT '建设性质' ,
    investment INT    COMMENT '投资分类' ,
    scope INT    COMMENT '投资范围' ,
    address VARCHAR(512)    COMMENT '建设地址' ,
    source VARCHAR(512)    COMMENT '土地来源' ,
    covers_area DECIMAL(32,10)    COMMENT '占地面积' ,
    construction_area DECIMAL(32,10)    COMMENT '建筑面积' ,
    planned_investment DECIMAL(32,8)    COMMENT '计划投资总额' ,
    sources_funding VARCHAR(512)    COMMENT '资金来源' ,
    schedule DATE    COMMENT '时间进度' ,
    basis VARCHAR(512)    COMMENT '建设依据' ,
    note VARCHAR(512)    COMMENT '备注' ,
    start_time DATE    COMMENT '计划开工时间' ,
    end_time DATE    COMMENT '计划竣工时间' ,
    main_content VARCHAR(3072)    COMMENT '项目主要内容' ,
    leadership VARCHAR(512)    COMMENT '领导批示' ,
    revision INT    COMMENT '乐观锁' ,
    created_by VARCHAR(32)    COMMENT '创建人' ,
    created_time DATETIME    COMMENT '创建时间' ,
    updated_by VARCHAR(32)    COMMENT '更新人' ,
    updated_time DATETIME    COMMENT '更新时间' ,
    PRIMARY KEY (id)
) COMMENT = '项目审核';

ALTER TABLE cpims_prm_project_re COMMENT '项目审核';
CREATE TABLE cpims_prm_history(
    id INT NOT NULL AUTO_INCREMENT  COMMENT '主键' ,
    project_id1 INT NOT NULL   COMMENT '项目登记外键' ,
    project_name VARCHAR(128)    COMMENT '项目名称' ,
    owner VARCHAR(32)    COMMENT '业主' ,
    category INT    COMMENT '行业类别' ,
    property INT    COMMENT '建设性质' ,
    investment INT    COMMENT '投资分类' ,
    scope INT    COMMENT '投资范围' ,
    project_id INT NOT NULL   COMMENT '项目登记外键' ,
    revision INT    COMMENT '乐观锁' ,
    created_by VARCHAR(32)    COMMENT '创建人' ,
    created_time DATETIME    COMMENT '创建时间' ,
    updated_by VARCHAR(32)    COMMENT '更新人' ,
    updated_time DATETIME    COMMENT '更新时间' ,
    PRIMARY KEY (id)
) COMMENT = '历史项目库';

ALTER TABLE cpims_prm_history COMMENT '历史项目库';
CREATE TABLE cpims_basic_data_dictionary(
    id INT NOT NULL AUTO_INCREMENT  COMMENT '主键' ,
    system_name VARCHAR(32)    COMMENT '子系统' ,
    group_name VARCHAR(32)    COMMENT '分组' ,
    real_value INT    COMMENT '真实值' ,
    show_value VARCHAR(32)    COMMENT '表现值' ,
    sort_value INT    COMMENT '排序值' ,
    remarks VARCHAR(128)    COMMENT '备注描述' ,
    revision INT    COMMENT '乐观锁' ,
    created_by VARCHAR(32)    COMMENT '创建人' ,
    created_time DATETIME    COMMENT '创建时间' ,
    updated_by VARCHAR(32)    COMMENT '更新人' ,
    updated_time DATETIME    COMMENT '更新时间' ,
    PRIMARY KEY (id)
) COMMENT = '数据字典 ';

ALTER TABLE cpims_basic_data_dictionary COMMENT '数据字典';
