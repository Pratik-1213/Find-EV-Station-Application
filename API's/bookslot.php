<?php
    include("../connection.php");   


    if(isset($_POST['stationid']))
    {
    $stationid = $_POST["stationid"];
    $userid = $_POST["userid"];
    $b_date = $_POST["b_date"];
    $intime = $_POST["intime"];
    $outtime = $_POST["outtime"];
    $duration = $_POST["duration"];
    $slotid = $_POST["slotid"];
    $voltage = $_POST["voltage"];
    $b_status = "Booked";
    $amount = $_POST["amount"];
    $slotname = $_POST["slotname"];
    $vehicleid = $_POST["vehicleid"];
    $vehicle = $_POST["vehicle"];
    date_default_timezone_set('Asia/Kolkata');
    $now = date('Y-m-d H:i:s');
    $bookingon = $now;

    
    
        $sql = "INSERT INTO bookings(stationid, userid,  b_date, intime, outtime, duration,slotid, voltage, b_status, amount, bookingon, slotname, vehicleid, vehicle)";
        $sql .=" VALUES ($stationid , $userid, '$b_date','$intime', '$outtime', '$duration', $slotid, $voltage, '$b_status', $amount, '$now', '$slotname', $vehicleid,'$vehicle')";
        
       
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
        $tarray["status"] = 'gg failed';
            array_push($tarray);
    }
    
    
    header('Content-Type: application/json');
	echo json_encode($tarray);
    
?>