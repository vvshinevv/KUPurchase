<?php
	require_once("./configDatabase.php");
?>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8"/>
	<link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/earlyaccess/hanna.css">
	<link rel="stylesheet" type="text/css" href="CSSPage.css" >
	<title>공지사항 등록</title>
</head>
<body bgcolor="#a9a9a9">
	<p class="maintitle">공지사항 목록</p>
	<div id="menu">
   		<a href="./MainPage.html" target="_self">메인 화면</a>
   		<a href="./KuProductList.php" target="_self">상품 목록</a>
    	<a href="./UserListPurchaseProductsBySuper.php">구매자 목록</a>
    	<a href="./TotalBills.php">계산서</a>
      <a href="./AskAdministor.php">문의사항</a>
    	<a href="./Notification.php">공지사항</a>
  	</div>

  	<table class="table2">
  		<thead>
  			<tr>
  				<th class="table2">번호</th>
  				<th class="table2">제목</th>
  				<th class="table2">날짜</th>
  				<th class="table2">상세목록</th>
  			</tr>
  		</thead>
  		<tbody>
  			<?php
          $state = mysqli_prepare($mysqli, "SELECT * FROM notification");
          if(!$state) {
            die("state query error :".mysqli_error($mysqli));
          }
          if(!mysqli_stmt_execute($state)) {
            die("$state execute error :".mysqli_stmt_error($state));
          }

          mysqli_stmt_store_result($state);
          mysqli_stmt_bind_result($state, $noticeManagementCode,$noticeOfTitle, $noticeOfContents, $noticeOfUploadDate);
          $notice = array();
          while(mysqli_stmt_fetch($state)) {
            $notice['noticeManagementCode'] = $noticeManagementCode;
            $notice['noticeOfTitle'] = $noticeOfTitle;
            $notice['noticeOfContents'] = $noticeOfContents;
            $notice['noticeOfUploadDate'] = $noticeOfUploadDate;
  			?>
  			<tr>
  				<td class="table2"><?php echo $notice['noticeManagementCode']?></td>
  				<td class="table2"><?php echo $notice['noticeOfTitle']?></td>
  				<td class="table2"><?php echo $notice['noticeOfUploadDate']?></td>
  				<td class="table2"><a href="./contentsOfNotification.php?id=<?=$notice['noticeManagementCode']?>">상세 목록</a></td>
  			</tr>
  			<?php
  				}
  			?>
  			<tr>
				<td colspan="4" align="right">
					<a href="./RegisterNotificationForm.php">
						<input type="button" value="공지사항 추가"/>
					</a>
				</td>
			</tr>
  		</tbody> 
  	</table>
</body>
</html>