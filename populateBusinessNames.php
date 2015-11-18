<?php

	include_once('DB_connect.php');
	$email = $_POST['email'];
	//$email = "addbizlbc@gmail.com";
	
	//call the function and pass it email as a parameter
	
	if($email!=""){
	
		echo businessList($email);
	}
	
	
	function businessList($email){
	
		$message = "";
		$query = "select biz_name from bizmerchants where google_mail='$email'";
		$result = mysql_query($query) or die("Query failed" . mysql_error());
		
		if(mysql_num_rows($result)>0){
		
			while($row = mysql_fetch_array($result)){
			
				$message .=$row['biz_name']. ",";	
			}
			
		}else{
			$message .= "No business,";		
		}
		
		return $message;
	}
?>