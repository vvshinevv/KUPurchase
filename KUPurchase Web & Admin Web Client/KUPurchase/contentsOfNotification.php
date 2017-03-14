<?php
	require_once("./configDatabase.php");
?>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8"/>
	<link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/earlyaccess/hanna.css">
	<link rel="stylesheet" type="text/css" href="CSSPage.css" >
	<title>공지사항 세부 내역</title>
</head>
<body bgcolor="#a9a9a9">
	<p class="maintitle">공지사항 세부 내용</p>
	<div id="menu">
   		<a href="./MainPage.html" target="_self">메인 화면</a>
   		<a href="./KuProductList.php" target="_self">상품 목록</a>
    	<a href="./UserListPurchaseProductsBySuper.php">구매자 목록</a>
    	<a href="./TotalBills.php">계산서</a>
    	<a href="./AskAdministor.php">문의사항</a>
    	<a href="./Notification.php">공지사항</a>
  	</div>
  	<?php
  		$noticeManagementCode = mysqli_real_escape_string($mysqli, $_GET['id']);
  		echo $noticeManagementCode;

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
			$noticeArray['noticeOfUploadDate'] = $noticeOfUploadDate;
		}
  	?>
  	<table class="table2">
  		<tr>
  			<th class="table2">제목</th>
  			<td class="table2"><?php echo $noticeArray['noticeOfTitle']?></td>
  			<th class="table2">날짜</th>
  			<td class="table2"><?php echo $noticeArray['noticeOfUploadDate']?></td>
  		</tr>
  		<tr>
  			<th class="table2" >내용</th>
  			<td class="table2" colspan="3"><?php echo $noticeArray['noticeOfContents']?></td>
  		</tr>
  		<tr>
			<td colspan="4" align="right">
				<a href="./UpdateNotificationForm.php?id=<?=$noticeManagementCode?>">
					<input type="button" value="수정하기"/>

				</a>
				<a href="./DeleteNotificaion.php?id=<?=$noticeManagementCode?>">
					<input type="button" value="삭제하기"/>
				</a>
			</td>

		</tr>
  	</table>
</body>
</html>
<?php
	mysqli_stmt_close($statementNoticeCode);
	mysqli_close($mysqli);
	die();
?>
