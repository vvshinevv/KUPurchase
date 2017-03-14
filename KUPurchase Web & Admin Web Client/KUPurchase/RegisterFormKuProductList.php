<!DOCTYPE html>
<?php
	require_once("./configDatabase.php");
?>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/earlyaccess/hanna.css">
	<link rel="stylesheet" type="text/css" href="CSSPage.css" >
	<script type="text/javascript">
		function CheckForm() {
			var check = document.myform;
			
			if(check.expireDate.value=="") {
				alert("상품 만료 날짜를 입력하세요.");
				check.expireDate.focus();
				return false;
			}
			if(check.startDepositDate.value=="") {
				alert("입금 시작 날짜를 입력하세요.");
				check.startDepositDate.focus();
				return false;
			}
			if(check.EndDepositDate.value=="") {
				alert("입금 종료 날짜를 입력하세요.");
				check.EndDepositDate.focus();
				return false;
			}
			if(check.productName.value=="") {
				alert("상품 이름을 입력하세요.");
				check.productName.focus();
				return false;
			}
			if(isNaN(check.productPrice.value)) {
				alert("숫자만 입력하세요.");
				check.productPrice.focus();
				return false;
			}
			if(!checkFileUpload()) {
				return false;
			}
			check.submit();

			//window.document.myform.submit();
		}
		function checkFileUpload() {
			var thumbext = document.getElementById('fileToUpload').value;
			thumbext = thumbext.slice(thumbext.indexOf(".")+1).toLowerCase();
			if(thumbext != "jpg" && thumbext != "png" && thumbext != "gif" && thumbext != "bmp") {
				alert("jpg, png, gif, bmp 확장자만 가능합니다.");
				document.getElementById('fileToUpload').focus();
				return false;
			} else {
				return true;
			}
		}
	</script>
	<title>KU공동구매 상품 등록</title>
</head>
<body bgcolor="#a9a9a9">
	
	<p class="maintitle">KU공동구매 상품 추가</p>
	<div id="menu">
   		<a href="./MainPage.html" target="_self">메인 화면</a>
   		<a href="./KuProductList.php" target="_self">상품 목록</a>
    	<a href="./UserListPurchaseProductsBySuper.php">구매자 목록</a>
    	<a href="./TotalBills.php">계산서</a>
    	<a href="./AskAdministor.php">문의사항</a>
    	<a href="./Notification.php">공지사항</a>
  	</div>

	<form action="./RegisterKuProductList.php" name="myform" method="post" enctype="multipart/form-data">
		<table class="table1">
			<tr>
				<th class="table1">상품 만료 날짜</th>
				<td class="table1"><input type="date" name="expireDate" id="expireDate" min="2001-01-01"></td>
			</tr>
			<tr>
				<th class="table1">입금 시작 날짜</th>
				<td class="table1"><input type="date" name="startDepositDate" id="startDepositDate" min="2001-01-01"></td>
			</tr>
			<tr>
				<th class="table1">입금 종료 날짜</th>
				<td class="table1"><input type="date" name="EndDepositDate" id="EndDepositDate" min="2001-01-01"></td>
			</tr>
			<tr>
				<th class="table1">상품 이름</th>
				<td class="table1"><input type="text" name="productName" id="productName" value="ex)휴지"></td> 
			</tr>
			<tr>
				<th class="table1">상품 가격</th>
				<td class="table1"><input type="text" name="productPrice" id="productPrice" value="ex)1000">원</td> 
			</tr>
			<tr>
				<th class="table1">이미지</th>
				<td class="table1">
					<input type="file" name="fileToUpload" id="fileToUpload">
				</td>
			</tr>
			<tr>
				<td></td>
				<td align="right">
					<input type="button" value="상품 등록" onClick="CheckForm()">
					<input type="reset" value="취 소">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
<?php
	mysqli_close($mysqli);
?>