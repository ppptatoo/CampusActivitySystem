<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="com.entity.User" %>

<%@ page import="com.servlet.LoginServlet" %>

<%@ page import="com.utils.RoleUtil" %>

<%

    User loginUser = (User) session.getAttribute(LoginServlet.SESSION_USER);

    boolean canPublish = RoleUtil.canPublish(loginUser);

    request.setAttribute("canPublish", canPublish);

%>

<!DOCTYPE html>

<html lang="zh-CN">

<head>

    <meta charset="UTF-8">

    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>竞赛管理 - 校园活动管理系统</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/manage.css">

</head>

<body>

<%@ include file="header.jsp" %>

<main class="main-content">

    <div class="page-header-card">

        <div>

            <h1 class="page-title">竞赛管理</h1>

            <p class="page-subtitle">浏览竞赛信息<c:if test="${canPublish}">，发布与维护竞赛</c:if>。</p>

        </div>

        <c:if test="${canPublish}">

            <a href="${pageContext.request.contextPath}/CompetitionServlet?action=toAdd" class="btn-add">+ 新增竞赛</a>

        </c:if>

    </div>



    <div class="table-card">

        <table class="data-table">

            <thead>

                <tr>

                    <th>序号</th>

                    <th>竞赛标题</th>

                    <th>竞赛时间</th>

                    <th>竞赛地点</th>

                    <th>竞赛详情</th>

                    <th>操作</th>

                </tr>

            </thead>

            <tbody>

                <c:choose>

                    <c:when test="${not empty competitionList}">

                        <c:forEach items="${competitionList}" var="comp" varStatus="status">

                            <c:set var="favKey" value="competition:${comp.id}" />

                            <c:set var="isFav" value="${favoriteKeys.contains(favKey)}" />

                            <c:set var="canEdit" value="${sessionScope.CURRENT_USER.role == 'admin' || (sessionScope.CURRENT_USER.role == 'leader' && sessionScope.CURRENT_USER.id == comp.userId)}" />

                            <tr>

                                <td>${status.count}</td>

                                <td>${comp.title}</td>

                                <td>${comp.time}</td>

                                <td>${comp.location}</td>

                                <td class="detail-cell" title="${comp.content}">
    								<div style="white-space:normal; word-break:break-all; overflow:visible;">
        								${comp.content}
    								</div>
    								<br>
    								<b>竞赛时间：</b>${comp.time}<br>
    								<b>竞赛地点：</b>${comp.location}
								</td>

                                <td class="action-cell">

                                    <c:if test="${not isFav}">
                                        <c:url var="favUrl" value="/UserCenterServlet">
                                            <c:param name="action" value="addFavorite"/>
                                            <c:param name="activityType" value="competition"/>
                                            <c:param name="activityId" value="${comp.id}"/>
                                            <c:param name="activityTitle" value="${comp.title}"/>
                                            <c:param name="redirect" value="${pageContext.request.contextPath}/CompetitionServlet"/>
                                        </c:url>
                                        <a class="action-link" href="${favUrl}">收藏</a>
                                    </c:if>

                                    <c:if test="${isFav}">

                                        <span class="text-muted">已收藏</span>

                                    </c:if>

                                    <c:if test="${canEdit}">

                                        <a class="action-link" href="${pageContext.request.contextPath}/CompetitionServlet?action=edit&id=${comp.id}">编辑</a>

                                        <a class="action-delete" href="${pageContext.request.contextPath}/CompetitionServlet?action=delete&id=${comp.id}" onclick="return confirm('确定删除该竞赛？')">删除</a>

                                    </c:if>

                                </td>

                            </tr>

                        </c:forEach>

                    </c:when>

                    <c:otherwise>

                        <tr class="empty-row">

                            <td colspan="6">暂无竞赛数据</td>

                        </tr>

                    </c:otherwise>

                </c:choose>

            </tbody>

        </table>

    </div>

</main>

</body>

</html>

