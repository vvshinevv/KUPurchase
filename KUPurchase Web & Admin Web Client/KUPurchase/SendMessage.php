<?php
    require_once("./configDatabase.php");

    $headers = array(
            'Content-Type:application/json',
            'Authorization:key=AIzaSyAqSKg-xneQhPn-DGhvDQeieDGOk9G0RU4'
            );

    $toUserID = mysqli_real_escape_string($mysqli, $_POST["toUserID"]);
    $fromUserID = mysqli_real_escape_string($mysqli, $_POST["fromUserID"]);
    $sendMessage = mysqli_real_escape_string($mysqli, $_POST["sendMessage"]);
    $sendTime = date('Y-m-d H:i:s', time());

    // toUser 토큰정보 가져오기
    $fetchToken = mysqli_prepare($mysqli, "SELECT userToken FROM User WHERE userID=?");
    if(!$fetchToken) {
        echo json_encode(array('resultCode'=>'-1'));
        die('mysql fetchToken error: '.mysqli_error($mysqli));
    }
    mysqli_stmt_bind_param($fetchToken, "s", $toUserID);
    if(!mysqli_stmt_execute($fetchToken)) {
        echo json_encode(array('resultCode'=>'-1'));
        die('mysql fetchToken execute error: '.mysqli_error($fetchToken));
    }
    mysqli_stmt_store_result($fetchToken);
    mysqli_stmt_bind_result($fetchToken, $userToken);

    $toUserToken = "";
    while(mysqli_stmt_fetch($fetchToken)) {
        $toUserToken = $userToken;
    }
    echo $toUserToken;
    mysqli_stmt_close($fetchToken);
    
    //toUserID 의 테이블이 있는지 없는지
    $toUserIDState = mysqli_prepare($messageMysqli, "SELECT COUNT(*) AS count FROM information_schema.tables WHERE TABLE_SCHEMA='MessageRoom' AND TABLE_NAME = ?");
    if(!$toUserIDState) {
        echo json_encode(array('resultCode'=>'-1'));
        die('mysql toUserIDState error: '.mysqli_error($mysqli));
    }
    mysqli_stmt_bind_param($toUserIDState, "s", $toUserID);
    if(!mysqli_stmt_execute($toUserIDState)) {
        echo json_encode(array('resultCode'=>'-1'));
        die('mysql toUserIDState execute error: '.mysqli_stmt_error($toUserIDState));
    }

    mysqli_stmt_store_result($toUserIDState);
    mysqli_stmt_bind_result($toUserIDState, $count);

    $resultToUser = 0;
    while(mysqli_stmt_fetch($toUserIDState)) {
        $resultToUser = $count;
    }
    mysqli_stmt_close($toUserIDState);

    //fromUserID 의 테이블이 있는지 없는지
    $formUserIDState = mysqli_prepare($messageMysqli, "SELECT COUNT(*) AS count FROM information_schema.tables WHERE TABLE_SCHEMA='MessageRoom' AND TABLE_NAME = ?");
    if(!$formUserIDState) {
        echo json_encode(array('resultCode'=>'-1'));
        die('mysql formUserIDState error: '.mysqli_error($mysqli));
    }
    mysqli_stmt_bind_param($formUserIDState, "s", $fromUserID);
    if(!mysqli_stmt_execute($formUserIDState)) {
        echo json_encode(array('resultCode'=>'-1'));
        die('mysql formUserIDState execute error: '.mysqli_stmt_error($formUserIDState));
    }

    mysqli_stmt_store_result($formUserIDState);
    mysqli_stmt_bind_result($formUserIDState, $count);

    $resultFromUser = 0;
    while(mysqli_stmt_fetch($formUserIDState)) {
        $resultFromUser = $count;
    }
    mysqli_stmt_close($formUserIDState);

    // toUser의 테이블이 없다면...
    if($resultToUser == 0) {
        $createToUserMessageTable = mysqli_prepare($messageMysqli, "CREATE TABLE ".$toUserID." (
            messageManagementCode int(100) not null auto_increment primary key,
            toUserID varchar(30) not null,
            fromUserID varchar(30) not null,
            sendTime varchar(30) not null,
            message varchar(100) not null)");

        if(!$createToUserMessageTable) {
            echo json_encode(array('resultCode'=>'-1'));
            die('mysql createToUserMessageTable error: '.mysqli_error($messageMysqli));
        }
        //mysqli_stmt_bind_param($createToUserMessageTable, "s", $toUserID);
        if(!mysqli_stmt_execute($createToUserMessageTable)) {
            echo json_encode(array('resultCode'=>'-1'));
            die('mysql createToUserMessageTable execute error: '.mysqli_stmt_error($createToUserMessageTable));
        }
        mysqli_stmt_close($createToUserMessageTable);
    }

    // fromUser의 테이블이 없다면...
    if($resultFromUser == 0) {
        $createFromUserMessageTable = mysqli_prepare($messageMysqli, "CREATE TABLE ".$fromUserID." (
            messageManagementCode int(100) not null auto_increment primary key,
            toUserID varchar(30) not null,
            fromUserID varchar(30) not null,
            sendTime varchar(30) not null,
            message varchar(100) not null)");

        if(!$createFromUserMessageTable) {
            echo json_encode(array('resultCode'=>'-1'));
            die('mysql createFromUserMessageTable error: '.mysqli_error($messageMysqli));
        }
        //mysqli_stmt_bind_param($createFromUserMessageTable, "s", $fromUserID);
        if(!mysqli_stmt_execute($createFromUserMessageTable)) {
            echo json_encode(array('resultCode'=>'-1'));
            die('mysql createFromUserMessageTable execute error: '.mysqli_stmt_error($createFromUserMessageTable));
        }
        mysqli_stmt_close($createFromUserMessageTable);
    }

    //toUser에 메세지 삽입
    $insertToUserTable = mysqli_prepare($messageMysqli, "INSERT INTO ".$toUserID." (toUserID, fromUserID, sendTime, message) VALUES (?, ?, ?, ?)");
    if(!$insertToUserTable) {
        echo json_encode(array('resultCode'=>'-1'));
        die('mysql insertToUserTable error: '.mysqli_error($mysqli));
    }
    mysqli_stmt_bind_param($insertToUserTable, "ssss", $toUserID, $fromUserID, $sendTime, $sendMessage);

    if(!mysqli_stmt_execute($insertToUserTable)) {
        echo json_encode(array('resultCode'=>'-1'));
        die('mysql insertToUserTable execute error: '.mysqli_stmt_error($insertToUserTable));
    }
    mysqli_stmt_close($insertToUserTable);

    //fromUser에 메세지 삽입
    $insertFromUserTable = mysqli_prepare($messageMysqli, "INSERT INTO ".$fromUserID." (toUserID, fromUserID, sendTime, message) VALUES (?, ?, ?, ?)");
    if(!$insertFromUserTable) {
        echo json_encode(array('resultCode'=>'-1'));
        die('mysql insertFromUserTable error: '.mysqli_error($mysqli));
    }
    mysqli_stmt_bind_param($insertFromUserTable, "ssss", $toUserID, $fromUserID, $sendTime, $sendMessage);
    
    if(!mysqli_stmt_execute($insertFromUserTable)) {
        echo json_encode(array('resultCode'=>'-1'));
        die('mysql insertFromUserTable execute error: '.mysqli_stmt_error($insertFromUserTable));
    }
    mysqli_stmt_close($insertFromUserTable);

    mysqli_close($mysqli);
    mysqli_close($messageMysqli);
 
    // 푸시 메세지 보내기
    $arr = array();
    $arr['data'] = array();
    $arr['data']['fromUserID'] = $fromUserID;
    $arr['data']['message'] = $sendMessage;
    $arr['registration_ids'] = array();
    $arr['registration_ids'][0] = $toUserToken;
 
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, 'https://android.googleapis.com/gcm/send');
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_POSTFIELDS,json_encode($arr));
    $response = curl_exec($ch);
    curl_close($ch);
 
    $obj = json_decode($response);
    $cnt = $obj->{"success"};
    echo $cnt;
?>