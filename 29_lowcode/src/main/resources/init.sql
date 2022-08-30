INSERT INTO sys_channel (id, createTime, lastmodifiedTime, channelCode, channelName, orderStr, parentId) VALUES (1, '2021-05-18 18:51:56', '2021-05-18 18:51:56', '1000', '总渠道', 1, 0);

INSERT INTO sys_role (id, createTime, lastmodifiedTime, roleCode, roleName) VALUES (1, '2019-05-14 16:26:12', '2019-05-14 16:26:12', 'user', '普通用户');
INSERT INTO sys_role (id, createTime, lastmodifiedTime, roleCode, roleName) VALUES (2, '2019-05-14 16:26:12', '2019-05-14 16:26:12', 'manager', '渠道负责人');
INSERT INTO sys_role (id, createTime, lastmodifiedTime, roleCode, roleName) VALUES (3, '2019-05-14 16:26:12', '2019-05-14 16:26:12', 'admin', '管理员');

INSERT INTO sys_menu (id, createTime, lastmodifiedTime, icon, importStr, parentId, path, title, sortIndex) VALUES (2, '2021-05-14 16:26:00', '2022-08-23 10:37:26', 'iconfont icon-xitongshezhi icon-menu', 'common/Through.vue', 0, '/index/systems', '系统管理', 1);
INSERT INTO sys_menu (id, createTime, lastmodifiedTime, icon, importStr, parentId, path, title, sortIndex) VALUES (3, '2021-05-14 16:26:00', '2021-09-10 12:01:28', 'iconfont icon-yonghuguanli', 'admin/systemManager/user/Index.vue', 2, '/index/user', '用户管理', 1);
INSERT INTO sys_menu (id, createTime, lastmodifiedTime, icon, importStr, parentId, path, title, sortIndex) VALUES (4, '2021-05-14 16:26:00', '2021-09-10 12:01:28', 'iconfont icon-yonghuguanli1', 'admin/systemManager/role/Index.vue', 2, '/index/role', '角色管理', 2);
INSERT INTO sys_menu (id, createTime, lastmodifiedTime, icon, importStr, parentId, path, title, sortIndex) VALUES (5, '2021-05-14 16:26:00', '2021-09-10 12:01:28', 'iconfont icon-qudaoguanli', 'admin/systemManager/channel/Index.vue', 2, '/index/channel', '机构管理', 3);
INSERT INTO sys_menu (id, createTime, lastmodifiedTime, icon, importStr, parentId, path, title, sortIndex) VALUES (6, '2021-05-14 16:26:00', '2022-08-23 10:37:26', 'iconfont icon-rizhiguanli icon-menu', 'common/Through.vue', 0, '/index/log', '日志管理', 2);
INSERT INTO sys_menu (id, createTime, lastmodifiedTime, icon, importStr, parentId, path, title, sortIndex) VALUES (7, '2021-05-13 17:20:00', '2021-09-10 12:01:28', 'iconfont icon-denglurizhi', 'admin/log/loginlog.vue', 6, '/index/loginLog', '登录日志', 1);
INSERT INTO sys_menu (id, createTime, lastmodifiedTime, icon, importStr, parentId, path, title, sortIndex) VALUES (8, '2021-05-13 17:20:00', '2021-09-10 12:01:28', 'iconfont icon-caozuorizhi', 'admin/log/operationlog.vue', 6, '/index/operationlog', '操作日志', 2);
INSERT INTO sys_menu (id, createTime, lastmodifiedTime, icon, importStr, parentId, path, title, sortIndex) VALUES (13, '2021-05-14 16:26:00', '2021-09-10 12:01:28', 'iconfont icon-xitongguanli-caidanguanli', 'admin/systemManager/menus/Index.vue', 2, '/index/menus', '菜单管理', 4);
INSERT INTO sys_menu (id, createTime, lastmodifiedTime, icon, importStr, parentId, path, title, sortIndex) VALUES (15, '2021-05-13 17:20:00', '2021-09-10 12:01:28', 'iconfont icon-caozuorizhi', 'admin/log/errorlog.vue', 6, '/index/errorlog', '错误日志', 3);
INSERT INTO sys_menu (id, createTime, lastmodifiedTime, icon, importStr, parentId, path, title, sortIndex) VALUES (17, '2022-05-24 21:29:45', '2022-08-23 10:37:26', 'iconfont icon-yonghuguanli1', 'metadata/Index.vue', 0, '/index/metadata', '元数据管理', 0);
INSERT INTO sys_menu (id, createTime, lastmodifiedTime, icon, importStr, parentId, path, title, sortIndex) VALUES (37, '2022-08-23 13:48:13', '2022-08-23 13:48:13', 'iconfont icon-rizhiguanli icon-menu', 'universal/Index.vue', 0, '/index/universal/1561953433691971584', '图书管理', 4);
INSERT INTO sys_menu (id, createTime, lastmodifiedTime, icon, importStr, parentId, path, title, sortIndex) VALUES (38, '2022-08-23 14:22:24', '2022-08-23 15:59:50', 'iconfont icon-rizhiguanli icon-menu', 'universal/Index.vue', 0, '/index/universal/1561962038331793408', '读者管理', 5);

INSERT INTO sys_role_menu (menuId, roleId, createTime) VALUES (2, 3, '2022-08-23 15:09:19');
INSERT INTO sys_role_menu (menuId, roleId, createTime) VALUES (3, 3, '2022-08-23 15:09:19');
INSERT INTO sys_role_menu (menuId, roleId, createTime) VALUES (4, 3, '2022-08-23 15:09:19');
INSERT INTO sys_role_menu (menuId, roleId, createTime) VALUES (5, 3, '2022-08-23 15:09:19');
INSERT INTO sys_role_menu (menuId, roleId, createTime) VALUES (6, 3, '2022-08-23 15:09:19');
INSERT INTO sys_role_menu (menuId, roleId, createTime) VALUES (7, 3, '2022-08-23 15:09:19');
INSERT INTO sys_role_menu (menuId, roleId, createTime) VALUES (8, 3, '2022-08-23 15:09:19');
INSERT INTO sys_role_menu (menuId, roleId, createTime) VALUES (13, 3, '2022-08-23 15:09:19');
INSERT INTO sys_role_menu (menuId, roleId, createTime) VALUES (15, 3, '2022-08-23 15:09:19');
INSERT INTO sys_role_menu (menuId, roleId, createTime) VALUES (17, 1, '2022-08-23 15:22:07');
INSERT INTO sys_role_menu (menuId, roleId, createTime) VALUES (17, 2, '2022-08-23 15:22:01');
INSERT INTO sys_role_menu (menuId, roleId, createTime) VALUES (17, 3, '2022-08-23 15:09:19');
INSERT INTO sys_role_menu (menuId, roleId, createTime) VALUES (37, 3, '2022-08-23 15:09:19');
INSERT INTO sys_role_menu (menuId, roleId, createTime) VALUES (38, 3, '2022-08-23 15:09:19');

INSERT INTO sys_user (id, createTime, lastmodifiedTime, channelId, deleted, enabled, name, password, roleId, salt, username) VALUES (1, null, null, 1, 0, 1, null, 'E81C5B99F11123006C1F58DA7488281D', 3, '962012d09b8170d912f0669f6d7d933', 'admin');

/*插入元数据模型相关信息*/
INSERT INTO meta_info (id, createTime, lastmodifiedTime, increment, metaDescription, metaName, tableCode, menuId) VALUES (1561953433691971584, '2022-08-23 13:48:13', '2022-08-23 13:49:21', true, '对图书数据进行管理', '图书管理', 'books', 37);
INSERT INTO meta_info (id, createTime, lastmodifiedTime, increment, metaDescription, metaName, tableCode, menuId) VALUES (1561962038331793408, '2022-08-23 14:22:24', '2022-08-23 15:59:50', false, '管理图书馆用户信息', '读者管理', 'readers', 38);

INSERT INTO meta_column (id, columnCode, columnName, dataType, metaId, sortIndex, viewShow, search) VALUES (29, 'bookName', '图书名称', 'varchar(255)', 1561953433691971584, 0, true, true);
INSERT INTO meta_column (id, columnCode, columnName, dataType, metaId, sortIndex, viewShow, search) VALUES (30, 'author', '作者', 'varchar(255)', 1561953433691971584, 1, true, true);
INSERT INTO meta_column (id, columnCode, columnName, dataType, metaId, sortIndex, viewShow, search) VALUES (31, 'publishTime', '发版时间', 'datetime', 1561953433691971584, 2, true, true);
INSERT INTO meta_column (id, columnCode, columnName, dataType, metaId, sortIndex, viewShow, search) VALUES (32, 'bookType', '图片类型', 'varchar(255)', 1561953433691971584, 3, true, true);
INSERT INTO meta_column (id, columnCode, columnName, dataType, metaId, sortIndex, viewShow, search) VALUES (33, 'name', '姓名', 'varchar(255)', 1561962038331793408, 0, true, true);
INSERT INTO meta_column (id, columnCode, columnName, dataType, metaId, sortIndex, viewShow, search) VALUES (34, 'age', '年龄', 'int', 1561962038331793408, 1, true, true);
INSERT INTO meta_column (id, columnCode, columnName, dataType, metaId, sortIndex, viewShow, search) VALUES (35, 'sex', '性别', 'text', 1561962038331793408, 2, true, false);
INSERT INTO meta_column (id, columnCode, columnName, dataType, metaId, sortIndex, viewShow, search) VALUES (36, 'address', '地址', 'varchar(255)', 1561962038331793408, 3, true, true);
INSERT INTO meta_column (id, columnCode, columnName, dataType, metaId, sortIndex, viewShow, search) VALUES (37, 'birthday', '生日', 'datetime', 1561962038331793408, 4, true, true);
INSERT INTO meta_column (id, columnCode, columnName, dataType, metaId, sortIndex, viewShow, search) VALUES (38, 'constellation', '星座', 'varchar(255)', 1561962038331793408, 5, true, true);

/*插入元数据模型测试数据*/
DROP TABLE IF EXISTS `readers`;
CREATE TABLE `readers`  (
  `id` bigint(0) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `age` int(0) NULL DEFAULT NULL,
  `sex` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `birthday` datetime(0) NULL DEFAULT NULL,
  `constellation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

INSERT INTO `readers` VALUES (1561962371242086400, '张三', 18, '男', '江西省南昌市', '2010-12-29 00:00:00', '摩羯座');
INSERT INTO `readers` VALUES (1561962371242086401, '王五', 19, '男', '江西省南昌市', '2010-12-29 00:00:00', '摩羯座');
INSERT INTO `readers` VALUES (1561962371242086402, '李四', 22, '男', '江西省南昌市', '2010-12-29 00:00:00', '摩羯座');
INSERT INTO `readers` VALUES (1561962371242086403, '隔壁老王', 23, '男', '江西省南昌市', '2010-12-29 00:00:00', '摩羯座');
INSERT INTO `readers` VALUES (1561962371242086404, '皓亮君', 18, '男', '江西省南昌市', '2010-12-29 00:00:00', '摩羯座');
INSERT INTO `readers` VALUES (1561962371242086405, '西毒', 25, '男', '江西省南昌市', '2010-12-29 00:00:00', '摩羯座');
INSERT INTO `readers` VALUES (1561962371242086406, '东邪', 18, '男', '江西省南昌市', '2010-12-29 00:00:00', '摩羯座');
INSERT INTO `readers` VALUES (1561980072387796992, '逆天而行', 22, '男', '江西', '2022-08-26 00:00:00', '射手座');

DROP TABLE IF EXISTS `books`;
CREATE TABLE `books`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `bookName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `publishTime` datetime(0) NULL DEFAULT NULL,
  `bookType` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

INSERT INTO `books` VALUES (1, '白夜行', '东野圭吾', '2022-08-01 00:00:00', '推理');
INSERT INTO `books` VALUES (2, '嫌疑人X的献身', '东野圭吾', '2022-08-11 00:00:00', '推理');
INSERT INTO `books` VALUES (9, '圣女的救赎', '东野圭吾', '2022-08-11 00:00:00', '推理');
INSERT INTO `books` VALUES (4, '放学后', '东野圭吾', '2022-08-11 00:00:00', '推理');
INSERT INTO `books` VALUES (5, '恶意', '东野圭吾', '2022-08-11 00:00:00', '推理');
INSERT INTO `books` VALUES (6, '假面山庄', '东野圭吾', '2022-08-11 00:00:00', '推理');
INSERT INTO `books` VALUES (7, '我杀了他', '东野圭吾', '2022-08-11 00:00:00', '推理');
INSERT INTO `books` VALUES (8, '谁杀了她', '东野圭吾', '2022-08-11 00:00:00', '推理');
INSERT INTO `books` VALUES (3, '无人生还', '阿加莎·克里斯蒂', '2022-08-10 00:00:00', '推理');