<?php
    include("../connection.php");
    $id = $_POST["id"];

   
        $sql = "SELECT * FROM users WHERE id = $id";    
        $result = $conn->query($sql);        
        if(mysqli_num_rows($result) > 0){
            $row = mysqli_fetch_assoc($result);
            $tarray = array();
            
             $tarray = $row;
             $tarray["status"] = 'success';
            
            array_push($tarray);
        }
        else{
            $tarray = array();
            $tarray["status"] = 'failed';
            array_push($tarray);
        }
    
    header('Content-Type: application/json');
	echo json_encode($tarray);
    
?>