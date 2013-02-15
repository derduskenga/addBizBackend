<?php
	include_once('DB_connect.php');
	//$email = $_POST['email'];
	$email = "addbizlbc@gmail.com";
	
	//call the function and pass it email as a parameter
	
	if($email!=""){
	
		echo distinctProducts($email);
	}
	
	
	
	function distinctProducts($email){
		$message = "";
		$query = "select distinct comodity from business_added where google_email='$email'";
		$result = mysql_query($query) or die("Query failed" . mysql_error());
		
		if(mysql_num_rows($result)>0){
			while($row = mysql_fetch_array($result)){
				$message .=$row['comodity']. ",";	
			}
		}else{
			$message .= "You do not have any products added";		
		}
		
		return $message;
	}
?>