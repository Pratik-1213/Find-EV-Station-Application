<?php
    include("../connection.php");

    if(isset($_POST['stationid']))
    {
        $stationid = $_POST['stationid'];

        $sql = "SELECT * FROM station_slots WHERE stationid = ".$stationid;
        $result = mysqli_query($conn, $sql);
        $dataarray = array();
        while($row = mysqli_fetch_assoc($result))
        {
            $tarray = array();
            $tarray["status1"] = 'success';
            $tarray = $row;		
            array_push($dataarray, $tarray);
        }
    }

    
    header('Content-Type: application/json');
	echo json_encode(array("data"=>$dataarray));
    
?>