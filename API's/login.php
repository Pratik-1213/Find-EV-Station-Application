<?php
    include("../connection.php");

    if(isset($_POST['email']))
    {
        $email = $_POST['email'];
        $password = $_POST['password'];


        $sql = "SELECT * FROM users WHERE  email = '$email' AND password = '$password'";
        $users = $conn->query($sql);
        if(mysqli_num_rows($users) > 0){
            $row = mysqli_fetch_assoc($users);
            $tarray = array();
            $tarray["status"] = 'success';            
		    $tarray["id"] = $row['id'];
            $tarray["name"] = $row['name'];
            $tarray["mobileno"] = $row['mobileno'];
            $tarray["email"] = $row['email'];
            $tarray["usertype"] = 'user';	
            array_push($tarray);
        }    
        else if($email == "admin@gmail.com" && $password=="123")
        {
            $tarray = array();
            $tarray["status"] = 'success';
            $tarray["id"] = "1";
            $tarray["usertype"] = "admin";
            $tarray["name"] = "Admin";
            $tarray["mobileno"] = "";
            $tarray["email"] = "admin@gmail.com";
            array_push($tarray);
        }
        
        else{
            $tarray = array();
            $tarray["status"] = 'failed';
            array_push($tarray);
        } 

    }
    
    header('Content-Type: application/json');
	echo json_encode($tarray);
    
?>