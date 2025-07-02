<?php
    include("../connection.php");   


    if(isset($_POST['stationid']))
    {
    $stationid = $_POST["stationid"];
    $voltage = $_POST["voltage"];
    $price = $_POST["price"];
    $status = $_POST["status"];
    

    
    
        $sql = "INSERT INTO station_slots(stationid,  voltage, price, status)";
        $sql .=" VALUES ('$stationid' , '$voltage','$price', '$status')";
        
        
        
        $result  = mysqli_query($conn, $sql); 
        $tarray = array();
        if(!$result)
        {
            $tarray["status"] = 'failed';
            array_push($tarray);
        }
        else
        {
            $tarray["status"] = 'success';
            array_push($tarray);
        }
   
    }
    else{
        $tarray["status"] = 'failed';
            array_push($tarray);
    }
    
    
    header('Content-Type: application/json');
	echo json_encode($tarray);
    
?>