<?php
    include("../connection.php");   


    if(isset($_POST['id']))
    {
    $id = $_POST["id"];
    

    
    
        $sql = "DELETE FROM stations WHERE id = $id";
        
        
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