<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8"/>
	<link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/earlyaccess/hanna.css">
	<link rel="stylesheet" type="text/css" href="CSSPage.css" >
	<script type="text/javascript">
		function checkForm() {
			var check = document.myform;

			if(check.noticeName.value=="") {
				alert("제목을 입력하세요.");
				check.noticeName.focus();
				return false;
			}
			if(check.noticeContent.value=="") {
				alert("내용을 입력하세요.");
				check.noticeContent.focus();
				return false;
			}
			check.submit();
		}
	</script>
	<title>공지사항 등록</title>
</head>
<body bgcolor="#a9a9a9">
	<p class="maintitle">공지사항 추가</p>
	<div id="menu">
   		<a href="./MainPage.html" target="_self">메인 화면</a>
   		<a href="./KuProductList.php" target="_self">상품 목록</a>
    	<a href="./UserListPurchaseProductsBySuper.php">구매자 목록</a>
    	<a href="./TotalBills.php">계산서</a>
    	<a href="./AskAdministor.php">문의사항</a>
    	<a href="./Notification.php">공지사항</a>
  	</div>

  	<form action="./RegisterNotification.php" name="myform" method="post">
  		<table class="table1">
  			<tr>
  				<th class="table1">제목</th>
  				<td class="table1"><input type="text" name="noticeName" id="noticeName"></td>
  			</tr>
  			<tr>
  				<th class="table1" >내용</th>
  				<td class="table1" ><textarea name="noticeContent" id="noticeContent" rows="4" cols="50"></textarea> </td>
  			</tr>
  			<tr>
  					<input type="hidden" name="noticeOfUploadDate" id="noticeOfUploadDate" value="<?php echo Date('Y-m-d')?>">
  				
  			</tr>
  			<tr>
				<td colspan="2" align="right">
					<input type="button" value="등록하기" onclick="checkForm()">
					<input type="reset" value="취 소">
				</td>
			</tr>
		</table>
  	</form>
</body>
</html>
