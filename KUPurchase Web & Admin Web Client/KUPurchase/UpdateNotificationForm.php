<?php
	require_once("./configDatabase.php");

	$noticeManagementCode = mysqli_real_escape_string($mysqli, $_GET['id']);
  	$statementNoticeCode = mysqli_prepare($mysqli, "SELECT * FROM notification WHERE noticeManagementCode=?");
  	if( !$statementNoticeCode ) {
		echo json_encode(array('resultCode'=>'-1'));
		die('mysqli statementNoticeCode error: '.mysqli_error($mysqli));
	}
	mysqli_stmt_bind_param($statementNoticeCode, "i", $noticeManagementCode);
	if(!mysqli_stmt_execute($statementNoticeCode)) {
		echo json_encode(array('resultCode'=>'-1'));
		die('stmt statementNoticeCode execute err: '.mysqli_stmt_error($statementNoticeCode));
	}
	mysqli_stmt_store_result($statementNoticeCode);
	mysqli_stmt_bind_result($statementNoticeCode, $noticeCode, $noticeOfTitle, $noticeOfContents, $noticeOfUploadDate);

	$noticeArray = array();
	while(mysqli_stmt_fetch($statementNoticeCode)) {
		$noticeArray['noticeOfTitle'] = $noticeOfTitle;
		$noticeArray['noticeOfContents'] = $noticeOfContents;
	}
?>
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
	<title>공지사항 수정</title>
</head>
<body bgcolor="#a9a9a9">
	<p class="maintitle">KU공동구매 상품 목록</p>
	<div id="menu">
   		<a href="./MainPage.html" target="_self">메인 화면</a>
   		<a href="./KuProductList.php" target="_self">상품 목록</a>
    	<a href="./UserListPurchaseProductsBySuper.php">구매자 목록</a>
    	<a href="./TotalBills.php">계산서</a>
    	<a href="./AskAdministor.php">문의사항</a>
    	<a href="./Notification.php">공지사항</a>
  	</div>
	<form action="./UpdateNotification.php?id=<?=$noticeManagementCode?>" name="myform" method="post">
  		<table class="table1">
  			<tr>
  				<th class="table1">제목</th>
  				<td class="table1"><input type="text" name="noticeName" id="noticeName" value="<?=$noticeArray['noticeOfTitle']?>"></td>
  			</tr>
  			<tr>
  				<th class="table1" >내용</th>
  				<td class="table1" ><textarea name="noticeContent" id="noticeContent" rows="4" cols="50"><?php echo $noticeArray['noticeOfContents']?></textarea> </td>
  			</tr>
  			<tr>
  					<input type="hidden" name="noticeOfUploadDate" id="noticeOfUploadDate" value="<?php echo Date('Y-m-d')?>">
  				
  			</tr>
  			<tr>
				<td colspan="2" align="right">
					<input type="button" value="수정 완료" onclick="checkForm()">
					<input type="reset" value="취 소">
				</td>
			</tr>
		</table>
  	</form>
  	<?php
  		mysqli_stmt_close($statementNoticeCode);
  		mysqli_close($mysqli);
  		die();
  	?>
</body>
</html>