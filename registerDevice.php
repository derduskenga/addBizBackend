<?php
include_once('DB_connect.php');

	$email = $_POST['email'];
	$regID = $_POST['regID'];
	
	//$email = "lkdfjkgg";
	//$regID = "dfkdgdf";
	//$lat=3.9803773;
	
	if(CheckDevice($email)){
		echo "Device already registered";
	}else{
	
		echo registerDevice($email,$regID); 
	}
	function registerDevice($email,$regID){
		$message = "";
		$query = "insert into users (device_reg_ids, google_email) values('$regID','$email')";
		$result = mysql_query($query) or die("Query failes" . mysql_error());
		
		if($result){
			
			$message = "You registered successifully";
		
		}else{
		
			$message .= "An error occured. Please try again";
		}
		
		return $message;
	}
	
	
	function CheckDevice($email){
		$query = "select google_email,device_reg_ids from users where google_email='$email'";
		$result = mysql_query($query) or die("Query failed" . mysql_error());
		
		if($result){
			if(mysql_num_rows($result)>0){
				return true;
			}else{
				return false;
			}
		}
	
	}

?>