<?php
    include("../connection.php");
    
    $sql = "SELECT B.*, S.name, S.location, S.city, S.latitude, S.longitude  FROM bookings AS B , stations AS S WHERE S.id = B.stationid ";
    if(isset($_POST['userid']))
    {
        $userid = $_POST['userid'];
        $sql .=" AND B.userid = ".$userid;
    }
   

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