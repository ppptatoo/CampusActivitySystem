<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="com.entity.User" %>

<%@ page import="com.servlet.LoginServlet" %>

<%@ page import="com.utils.RoleUtil" %>

<%

    User loginUser = (User) session.getAttribute(LoginServlet.SESSION_USER);

    request.setAttribute("canPublish", RoleUtil.canPublish(loginUser));

%>

<!DOCTYPE html>

<html lang="zh-CN">

<head>

    <meta charset="UTF-8">

    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>评优管理 - 校园活动管理系统</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/manage.css">

</head>

<body>

<%@ include file="header.jsp" %>

<main class="main-content">

    <div class="page-header-card">

        <div>

            <h1 class="page-title">评优管理</h1>

            <p class="page-subtitle">浏览评优信息<c:if test="${canPublish}">，发布与维护评优</c:if>。</p>

        </div>

        <c:if test="${canPublish}">

            <a href="${pageContext.request.contextPath}/AwardServlet?action=toAdd" class="btn-add">+ 发布评优</a>

        </c:if>

    </div>



    <div class="table-card">

        <table class="data-table">

            <thead>

                <tr>

                    <th>序号</th>

                    <th>评优标题</th>

                    <th>发布时间</th>

                    <th>评优详情</th>

                    <th>附件名称</th>

                    <th>操作</th>

                </tr>

            </thead>

            <tbody>

                <c:choose>

                    <c:when test="${not empty awardList}">

                        <c:forEach items="${awardList}" var="award" varStatus="status">

                            <c:set var="loginUser" value="${sessionScope.CURRENT_USER}" />

                            <c:set var="favKey" value="award:${award.id}" />

                            <c:set var="isFav" value="${favoriteKeys.contains(favKey)}" />

                            <c:set var="canEdit" value="${loginUser.role eq 'admin' || (loginUser.role eq 'leader' && loginUser.id eq award.userId)}" />

                            <tr>

                                <td>${status.count}</td>

                                <td>${award.title}</td>

                                <td>${award.publishTime}</td>

                                <td class="detail-cell" title="${award.details}">
   									<div style="white-space:normal; word-break:break-all; overflow:visible;">
        								${award.details}
    								</div>
    								<br>
    								<b>发布时间：</b>${award.publishTime}
								</td>

                                <td>${award.fileName}</td>

                                <td class="action-cell">

                                    <c:if test="${not isFav}">
                                        <c:url var="favUrl" value="/UserCenterServlet">
                                            <c:param name="action" value="addFavorite"/>
                                            <c:param name="activityType" value="award"/>
                                            <c:param name="activityId" value="${award.id}"/>
                                            <c:param name="activityTitle" value="${award.title}"/>
                                            <c:param name="redirect" value="${pageContext.request.contextPath}/AwardServlet"/>
                                        </c:url>
                                        <a class="action-link" href="${favUrl}">收藏</a>
                                    </c:if>

                                    <c:if test="${isFav}">

                                        <span class="text-muted">已收藏</span>

                                    </c:if>

                                    <a class="action-link" href="${pageContext.request.contextPath}/DownloadServlet?filePath=${award.filePath}">下载</a>

                                    <c:if test="${canEdit}">

                                        <a class="action-link" href="${pageContext.request.contextPath}/editAward.jsp?id=${award.id}">编辑</a>

                                        <a class="action-delete" href="${pageContext.request.contextPath}/AwardServlet?action=delete&id=${award.id}" onclick="return confirm('确定删除该评优？')">删除</a>

                                    </c:if>

                                </td>

                            </tr>

                        </c:forEach>

                    </c:when>

                    <c:otherwise>

                        <tr class="empty-row">

                            <td colspan="6">暂无评优数据</td>

                        </tr>

                    </c:otherwise>

                </c:choose>

            </tbody>

        </table>

    </div>

</main>

</body>

</html>

