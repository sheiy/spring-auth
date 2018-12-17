-- 用户admin密码admin
INSERT INTO `user` VALUES (1, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, '{scrypt}$e0801$YpYDy5BvxI6ISRONsjb/BEnlwrLxuDfjCYTOd6MQGZyFitJ/uHt+f+BuTI3RhkC4g/+WM3x7s0l/mADGNHBcOg==$SKKWshFmx1uZT+UFPUtuphDs6C7ZqNUs0aYhe1cF2Ts=', NULL, 'admin');
-- 两个角色
INSERT INTO `role` VALUES (1, NULL, NULL, 1, '2018-11-22 10:31:46.754000', '管理员', 'ROLE_ADMIN', '管理员');
INSERT INTO `role` VALUES (2, NULL, NULL, 1, '2018-11-15 08:34:57.866000', '普通用户', 'ROLE_USER', '普通用户');
-- 一个权限地址
INSERT INTO `permission_url` VALUES (1, NULL, NULL, NULL, NULL, NULL, b'1', 'auth', '[\"/**\"]');
-- 用户和角色关系
INSERT INTO `user_roles` VALUES (1, 1);
INSERT INTO `user_roles` VALUES (1, 2);
-- 角色和权限关系
INSERT INTO `role_permission_urls` VALUES (1, 1);