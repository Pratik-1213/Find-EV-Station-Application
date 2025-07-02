<?php
    include("../connection.php");
    $userid = $_POST['userid'];
    $sql = "SELECT * FROM vehicles WHERE userid = ".$userid;
    $result = mysqli_query($conn, $sql);
    $dataarray = array();
    while($row = mysqli_fetch_assoc($result))
    {
        $tarray = array();
		$tarray["status"] = 'success';
		$tarray = $row;		
		array_push($dataarray, $tarray);
    }
    header('Content-Type: application/json');
	echo json_encode(array("data"=>$dataarray));
    
?>