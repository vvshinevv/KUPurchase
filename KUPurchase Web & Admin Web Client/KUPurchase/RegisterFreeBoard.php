<?php
        require_once("./configDatabase.php");

        $userID = mysqli_real_escape_string($mysqli, $_POST['userID']);
        $freeBoardTitle = mysqli_real_escape_string($mysqli, $_POST['freeBoardTitle']);
        $freeBoardContents = mysqli_real_escape_string($mysqli, $_POST['freeBoardContents']);
        $isImageEmpty = mysqli_real_escape_string($mysqli, $_POST['isImageEmpty']);
        $image = $_POST["image"];
        $freeBoardCount = 0;

       
        if(!strcmp($isImageEmpty, "ImageEmpty")) {
                $ImageURL = "ImageEmpty";
        } else {
                $decodedImage = base64_decode("$image");
                $time = strtotime("now");
                file_put_contents("FreeBoardImages/".$userID.$time.".jpg", $decodedImage);
                $ImageURL = SERVER_ADDRESS."FreeBoardImages/".$userID.$time.".jpg";
        }
        
        $freeBoardUploadDate = date('Y-m-d H:i:s', time());

        $statement = mysqli_prepare($mysqli, "INSERT INTO freeBoard (freeBoardTitle, freeBoardContents, freeBoardUploadDate, userID, freeBoardCount, freeBoardImageURL) VALUES (?, ?, ?, ?, ?, ?)");
       
        if( !$statement ) {
                echo json_encode(array('resultCode'=>'-1'));
               die('mysqli error: '.mysqli_error($mysqli));
        }
        
        mysqli_stmt_bind_param($statement, "ssssis", $freeBoardTitle, $freeBoardContents, $freeBoardUploadDate, $userID,
                $freeBoardCount, $ImageURL);

        if( !mysqli_stmt_execute($statement) ) {
                echo json_encode(array('resultCode'=>'-1'));
                die('stmt err: '.mysqli_stmt_error($statement));
        }

        echo json_encode(array('resultCode'=>'1'));
        mysqli_stmt_close($statement);
        mysqli_close($mysqli);
        die();
?>
