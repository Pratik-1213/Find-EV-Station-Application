<?php
    include("../connection.php");   


    if(isset($_POST['name']))
    {
    $name = $_POST["name"];
    $location = $_POST["location"];
    $city = $_POST["city"];
    $latitude = $_POST["latitude"];
    $longitude = $_POST["longitude"];

    
    
        $sql = "INSERT INTO stations(name,  location, city, latitude, longitude)";
        $sql .=" VALUES ('$name' , '$location','$city', '$latitude', '$longitude')";
        
        
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