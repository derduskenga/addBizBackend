<?php

	include_once('DB_connect.php');
	
	$type = "normal";
	$email = $_POST['email'];
	$name = $_POST['name'];
	$pDescription = $_POST['pDescription'];
	
	if(checkMail_type ($name,$type)){
	
		echo addProduct($type,$email,$name,$pDescription);
			
	}else{
		//similar product was found
		echo "Similar product already in register";
	}
	
	
	function addProduct($type,$email,$name,$pDescription){
		$message = "";
		$query = "insert into business_added (google_email,comodity,description,type) values('$email','$name','$pDescription','$type')";	
		$result = mysql_query($query) or die("Query failed" . mysql_error());
		if($result){
		
			$message = "Your product has been added succesifully";
			
		}else{
		
			$message .="Ann error occured. Please try again";
			
		}
		return $message;
	}
	
	
	
	function checkMail_type ($name,$type){
		$query = "select * from business_added where comodity='$name' and type='$type'";
		$result = mysql_query($query) or die("Query failed" . mysql_error());
		if(mysql_num_rows($result)>0) {
			return false;
		}else{
			return true;
		}
	}
	
?>
