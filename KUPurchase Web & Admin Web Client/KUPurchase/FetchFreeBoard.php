<?php
	require_once("./configDatabase.php");

        $statement = mysqli_prepare($mysqli, "SELECT * FROM freeBoard");
        if(!$statement) {
                echo json_encode(array('resultCode'=>'-1'));
                die("statement query error: ".mysqli_error($mysqli));
        }

        if(!mysqli_stmt_execute($statement)) {
                echo json_encode(array('resultCode'=>'-1'));
                die("statement execute error: ".mysqli_stmt_error($statement));
        }

        mysqli_stmt_store_result($statement);
        mysqli_stmt_bind_result($statement, $freeBoardManagementCode ,$freeBoardTitle, $freeBoardContents, $freeBoardUploadDate, $userID, $freeBoardCount, $freeBoardImageURL);

        $notice = array();
        $count = 0 ;

        while(mysqli_stmt_fetch($statement)) {
                $notice['resultCode'] = $resultCode;
                $notice[$count] = array();
                $notice[$count]['freeBoardManagementCode'] = $freeBoardManagementCode;
                $notice[$count]['freeBoardTitle'] = $freeBoardTitle;
                $notice[$count]['freeBoardContents'] = $freeBoardContents;
                $notice[$count]['freeBoardUploadDate'] = $freeBoardUploadDate;
		$notice[$count]['userID'] = $userID;
                $notice[$count]['freeBoardCount'] = $freeBoardCount;
                $notice[$count]['freeBoardImageURL'] = $freeBoardImageURL;
                $count++;
        }
        echo json_encode($notice);
        mysqli_stmt_close($statement);
        mysqli_close($mysqli);
?>
