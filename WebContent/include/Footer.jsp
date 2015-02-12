<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page isELIgnored="false"%>
<jsp:useBean id="now" class="java.util.Date"></jsp:useBean>

<br />
<div class="Footer">
	<div>${pageContext.request.remoteAddr }</div>
	<div>
		Powered by <a href="http://zerojudge.tw/" target="_blank">ZeroJudge_Server
		</a> built ${applicationScope.built}
	</div>
</div>
