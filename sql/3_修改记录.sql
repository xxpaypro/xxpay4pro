
-- ALTER TABLE t_mch_info ADD COLUMN miniRole CHAR(1) NULL COMMENT '小程序商户角色' AFTER SignStatus;
ALTER TABLE t_mch_info ADD COLUMN MiniRole bigint(6) 0 COMMENT '小程序商户角色' AFTER SignStatus;

ALTER TABLE t_mch_info ADD COLUMN ParentId BIGINT(20) NULL COMMENT '父节点, 即收银员对应的商户id、商户对应的卫健委角色ID' AFTER MiniRole;

ALTER TABLE t_mch_info ADD COLUMN HospitalId bigint(20) null COMMENT '医院Id' AFTER ParentId;