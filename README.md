# 校园活动综合管理系统
基于JavaWeb + MySQL实现的校园活动一站式管理平台，课程实训项目，项目已部署阿里云，公网浏览器可直接访问。

## 项目简介
系统面向管理员、活动负责人、普通学生三类用户，基于RBAC实现Admin/Leader/User三级权限管控；涵盖讲座、社团、竞赛、评优四大活动板块，实现活动发布管理、活动收藏、浏览记录留存、评优附件上传、站内消息提醒完整业务功能。
文件采用**数据库存路径+服务器存实体文件**方案，避免大文件存入数据库，优化存储与读写效率。

## 技术栈
### 后端
Java + Servlet + JDBC + Maven
### 前端
JSP + HTML + CSS + JavaScript
### 数据库
MySQL 8.0
### 部署
阿里云ECS + Tomcat8.5

## 数据库说明
1. 项目`/sql`目录内置`init_table.sql`脚本，包含全量8张数据表建表语句：users、lecture、club、competition、award、favorites、browse_record、messages。
2. 本地部署：新建MySQL数据库，执行SQL脚本自动生成数据表。
3. jdbc配置文件仅保留示例参数，运行前自行修改数据库账号、密码。

## 系统功能
### 1. 三级权限模块
- Admin：全平台管理权限，管理用户与所有活动数据
- Leader：自主发布、维护本人创建的各类活动
- User：浏览活动、收藏、查看浏览记录、接收站内消息

### 2. 四大活动模块
- 讲座/社团/竞赛：活动新增、编辑、删除、详情查看
- 评优模块：支持附件上传存档，文件落地服务器，库中仅保存路径

### 3. 用户交互
✅ 活动收藏：收藏数据存入favorites表
✅ 浏览历史：自动记录浏览轨迹存入browse_record
✅ 站内消息：消息已读/未读状态标记

## 本地部署步骤
1. 环境：JDK8 + Tomcat8.5 + MySQL8.0 + IDEA/Eclipse
2. 克隆源码
```bash
git clone https://github.com/ppptatoo/CampusActivitySystem.git
```
3. 执行 sql 目录初始化脚本，创建数据库表
4. 修改项目内 jdbc 连接配置（填写自己本地 MySQL 账号密码）
5. IDE 配置 Tomcat，启动项目，浏览器访问本地地址
## 线上访问
项目已部署阿里云：(http://120.77.178.130:8233/CampusActivitySystem/login)
## 后续优化展望
1. 前端页面样式优化升级
2. 基于浏览收藏数据实现活动个性化推荐
3. 新增后台数据可视化统计面板
