<?php
    include("../connection.php");   


    if(isset($_POST['name']))
    {
    $id = $_POST["id"];
    $name = $_POST["name"];
    $mobileno = $_POST["mobileno"];
    $email = $_POST["email"];
    $password = $_POST["password"];

    

    
    
        $sql = "UPDATE users SET name='$name', email='$email', mobileno='$mobileno', ";
        $sql .= " password='$password'  WHERE id = $id";
        
        
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