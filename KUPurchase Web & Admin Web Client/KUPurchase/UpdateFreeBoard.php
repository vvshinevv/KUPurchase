<?php
        require_once("./configDatabase.php");

        $freeBoardTitle = mysqli_real_escape_string($mysqli, $_POST['freeBoardTitle']);
        $freeBoardContents = mysqli_real_escape_string($mysqli, $_POST['freeBoardContents']);
        $freeBoardManagementCode = mysqli_real_escape_string($mysqli, $_POST['freeBoardManagementCode']);
        $freeBoardImageURL = mysqli_real_escape_string($mysqli, $_POST['freeBoardImageURL']);
        $isImageEmpty = mysqli_real_escape_string($mysqli, $_POST['isImageEmpty']);
        $userID = mysqli_real_escape_string($mysqli, $_POST['userID']);
        $image = $_POST["image"];

        $decodedImage = base64_decode("$image");

        $temp = substr($productImagePath, 47);
        
        file_put_contents("FreeBoardImages/".$temp, $decodedImage);


        $imageURL = $freeBoardImageURL;

        //  if(!strcmp($isImageEmpty, "ImageEmpty")) { // 사진이 없는 경우
        //         $imageURL = "ImageEmpty";
        // } else {
        //         if(!strcmp($freeBoardImageURL, "ImageEmpty")) { // 사진이 없는 상태에서 사진을 추가함
        //                 $decodedImage = base64_decode("$image");
        //                 $time = strtotime("now");
        //                 file_put_contents("FreeBoardImages/".$userID.$time.".jpg", $decodedImage);
        //                 $imageURL = SERVER_ADDRESS."FreeBoardImages/".$userID.$time.".jpg";
        //         } else { // 사진은 있는 상태에서  
        //                 $decodedImage = base64_decode("$image");
        //                 $temp = substr($productImagePath, 47);
        //                 file_put_contents("FreeBoardImages/".$temp, $decodedImage);

        //                 $imageURL = $freeBoardImageURL;
        //         }                
        // }

        $statement = mysqli_prepare($mysqli, "UPDATE freeBoard SET freeBoardContents=?, freeBoardTitle=?, freeBoardImageURL=? WHERE freeBoardManagementCode=?");
        if(!$statement) {
                echo json_encode(array('resultCode'=>'-1'));
                die('mysql statement error: '.mysqli_error($mysqli));
        }
        mysqli_stmt_bind_param($statement, "sssi", $freeBoardContents, $freeBoardTitle, $imageURL, $freeBoardManagementCode);
        if(!mysqli_stmt_execute($statement)) {
                echo json_encode(array('resultCode'=>'-1'));
                die('stmt statement execute err: '.mysqli_stmt_error($statement));
        }

        echo json_encode(array('resultCode'=>'1'));
        mysqli_stmt_close($statement);
        mysqli_close($mysqli);

?>
