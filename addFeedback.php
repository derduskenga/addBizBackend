<?php 
include_once('DB_connect.php');

	//receive values
	$name = $_POST['name'];
	$email = $_POST['email'];
	$comment = $_POST['comment'];
	
	
	echo insertComment ($name,$email,$comment);
	
	function insertComment ($name,$email,$comment){
		$message = "";
		$query = "insert into comment (name,email,comment) values('$name','$email','$comment')";
		$result = mysql_query() or die("Query failed" . mysql_error());
		if($result){
			$message = "Thank you for submiting your comment";
		}else{
			$message .= "An error occured. Please try again";
		}
		
		return $message;
	
	}

?>