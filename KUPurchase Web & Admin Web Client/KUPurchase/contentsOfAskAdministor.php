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
  		$managementCode = mysqli_real_escape_string($mysqli, $_GET['id']);
  		
  		$statementAskCode = mysqli_prepare($mysqli, "SELECT * FROM AskAdministor WHERE managementCode=?");
  		if( !$statementAskCode ) {
			echo json_encode(array('resultCode'=>'-1'));
			die('mysqli statementAskCode error: '.mysqli_error($mysqli));
		}
		mysqli_stmt_bind_param($statementAskCode, "i", $managementCode);
		if(!mysqli_stmt_execute($statementAskCode)) {
			echo json_encode(array('resultCode'=>'-1'));
			die('stmt statementAskCode execute err: '.mysqli_stmt_error($statementAskCode));
		}
		mysqli_stmt_store_result($statementAskCode);
		mysqli_stmt_bind_result($statementAskCode, $askDate, $askTitle, $askContents);

		$askArray = array();
		while(mysqli_stmt_fetch($statementAskCode)) {
			$askArray['askDate'] = $noticeOfTitle;
			$askArray['askTitle'] = $noticeOfContents;
			$askArray['askContents'] = $noticeOfUploadDate;
		}
  	?>
  	<table class="table2">
  		<tr>
  			<th class="table2">날짜</th>
  			<td class="table2"><?php echo $askArray['askDate']?></td>
  			<th class="table2">제목</th>
  			<td class="table2"><?php echo $askArray['askTitle']?></td>
  		</tr>
  		<tr>
  			<th class="table2" >내용</th>
  			<td class="table2" colspan="3"><?php echo $askArray['askContents']?></td>
  		</tr>
  	</table>
</body>
</html>
<?php
  mysqli_stmt_close($statementAskCode);
  mysqli_close($mysqli);
  die();
?>