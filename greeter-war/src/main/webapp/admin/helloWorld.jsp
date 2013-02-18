<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%--
final String contextPath = request.getContextPath();
Translation translation = (Translation)request.getAttribute("translation");
//final Translation translation = new Translation();//(Translation)request.getAttribute("translation");
if (translation == null) translation = new Translation();
final String trAddForm = translation.getTranslation("ADD_FORM");
final String trEditForm = translation.getTranslation("EDIT_FORM");
final String trDeleteForm = translation.getTranslation("DELETE_FORM");
final String trHeadingFormName = translation.getTranslation("FORM_NAMES");
final String servletPath = (String)request.getAttribute("servletPath");
--%>
<html>
    <head>
        <META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8" />
        <link rel="stylesheet" href="/omniplay/css/admin/data.css" type="text/css" />
        <link rel="stylesheet" href="/omniplay/css/admin/main.css" type="text/css" />        
        <!-- script src="/omniplay/js/admin/tableData.js"></script>
        <script src="/omniplay/js/admin/highligth.js"></script-->
        <title>HelloWorld Management</title>
    </head>
    <body class="bodyBasic">
        <div class="dataContent">
        
            <p class="beeformHeading">
			Hello World
            </p>
        </div>
    </body>
</html>