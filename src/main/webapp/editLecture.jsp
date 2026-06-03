<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>编辑讲座 - 校园活动管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/manage.css">
</head>
<body>
<%@ include file="header.jsp" %>
<main class="main-content">
    <div class="form-shell">
        <div class="form-page">
            <h2 class="form-page-title">编辑讲座</h2>
            <form action="${pageContext.request.contextPath}/LectureServlet?action=edit" method="post">
                <input type="hidden" name="id" value="${lec.id}">
                <div class="form-group">
                    <label class="form-label">讲座标题</label>
                    <input class="form-input" type="text" name="title" value="${lec.title}" required>
                </div>
                <div class="form-group">
                    <label class="form-label">举办时间</label>
                    <input class="form-input" type="text" name="time" value="${lec.time}" required>
                </div>
                <div class="form-group">
                    <label class="form-label">活动地点</label>
                    <input class="form-input" type="text" name="address" value="${lec.address}" required>
                </div>
                <div class="form-group">
                    <label class="form-label">讲座内容</label>
                    <textarea class="form-textarea" name="content" rows="4" required>${lec.content}</textarea>
                </div>
                <button class="btn-submit" type="submit">保存修改</button>
            </form>
            <a class="back-link" href="${pageContext.request.contextPath}/LectureServlet?action=list">返回列表</a>
        </div>
    </div>
</main>
</body>
</html>
