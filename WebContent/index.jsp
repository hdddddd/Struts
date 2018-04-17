<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv = "Content-Type" content = "text/html; charset=UTF-8">
<title>文件上传和下载</title>
</head>
<body>
	<form action = "FileUpload.action" method = "post" enctype = "multipart/form-data">
		<input class="fileName" type = "file" name = "file"/>
		<input class="submitButton" type = "submit" value = "上传" />
	</form>
	<!-- <form action="FileUpload.action" method = "post">
	<input type = "text" name = "name"/>
	<input " type = "submit" value = "上传11" />
	</form> -->
	<button id = "btn" onclick="downLoadFile()" style="position: absolute;top: 8px;left: 312px;">下载</button>
</body>
<script>
	/* $("#btn").click(function() {
		downLoadFile();
	}) */
	function downLoadFile() {
		var fileName = prompt("请输入文件名称", "");
		if (fileName != null && fileName != "") {
			window.location.href = "FileDownload.action?fileName=" + fileName;
		}
	}
</script>
</html>