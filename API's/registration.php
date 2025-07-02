<?php
    include("../connection.php");   


    if(isset($_POST['name']))
    {
    $name = $_POST["name"];
    $email = $_POST["email"];
    $mobileno = $_POST["mobileno"];
    $password = $_POST["password"];

    
    
        $sql = "INSERT INTO users(name,  email, mobileno, password)";
        $sql .=" VALUES ('$name' , '$email','$mobileno', '$password')";
        
        
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