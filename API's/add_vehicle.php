<?php
    include("../connection.php");   


    if(isset($_POST['veh_number']))
    {
    $userid = $_POST['userid'];
    $veh_number = $_POST["veh_number"];
    $veh_name = $_POST["veh_name"];

    
    
        $sql = "INSERT INTO vehicles(userid, veh_number,  veh_name)";
        $sql .=" VALUES ('$userid', '$veh_number' , '$veh_name')";
        
        
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