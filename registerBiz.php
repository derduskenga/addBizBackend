<?php
	include_once('DB_connect.php');
	$google_mail = $_POST['email'];
	$biz_name = $_POST['name'];
	$description = $_POST['type'];
	$vicinity = $_POST['vic'];
	$longitude = $_POST['lng'];
	$latitude = $_POST['lat'];
	$ategory = $_POST['category'];
	
	//if(CheckDevice($google_mail)){
		//echo "This email already has a business registered under it";
		
	//}else{
		
		echo registerBusiness($google_mail,$biz_name,$category,$description,$longitude,$latitude,$vicinity);
	//}
	
	
	function registerBusiness($google_mail,$biz_name,$category,$description,$longitude,$latitude,$vicinity){
		$message = "";
		$query = "insert into bizmerchants (biz_name,category,description,retrieved_location_name_vicinity,latitude,longitude,google_mail)
					values('$biz_name','$category','$description','$vicinity','$latitude','$longitude','$google_mail')";
					
		$result = mysql_query($query) or die("Query failed" . mysql_error());
		
		if($result){
		
			$message = "Your business was submitted successifully";
			
		}else{
		
			$message .= "An error occured. Please try again";
		}
		
		return $message;
	}
	
	
	//function CheckDevice($google_mail){
//		$query = "select * from bizmerchants where google_mail='$google_mail'";
//		$result = mysql_query($query) or die("Query failed" . mysql_error());
//		
//		if($result){
//			if(mysql_num_rows($result)>0){
//				return true;
//			}else{
//				return false;
//			}
//		}
//	
//	}
	
?>