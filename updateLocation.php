<?php
	include_once('DB_connect.php');
	
	
	$latS = $_POST['latS'];
	$lngS = $_POST['lngS'];
	
	$latL = $_POST['latL'];
	$lngL = $_POST['lngL'];
	$email = $_POST['email'];
	
//		$latS = 60;
//		$lngS = 20;
//		
//		$latL = 90;
//		$lngL =30 ;
	
	echo checkLocation ($latS,$latL,$lngS,$lngL);

	function checkLocation ($latS,$latL,$lngS,$lngL){
		$query = "select * from bizmerchants where latitude between $latS and $latL and longitude between $lngS and $lngL";
		
		$result = mysql_query($query) or die("Query failed: " . mysql_error());	
		$overalResponse = "";
		if($result){
			if(mysql_num_rows($result)>0){
				
				$row=mysql_fetch_array($result);
				
				$name = $row['biz_name'];
				$category = $row['category'];
				$description = $row['description'];
				$vicinity = $row['retrieved_location_name_vicinity'];
				$email = $row['google_mail'];
				$promoMessage = fetchPromoMessage($email);
				
				$overalResponse .= $name . " sells " . $category . " (" . $description . ") and is locatedm near/along " . $vicinity . " and is running an offer; ". $promoMessage. ". You can use email " . $email; 
				
			}
		}	
		
		return $overalResponse;
	}
	
	
	
	function fetchPromoMessage($email){
		$type = "Offer or promotion";
		$query = "select * from business_added where google_email='$email' and type='$type'";
		
		$result = mysql_query($query) or die("Query failed: " . mysql_error());
		
		if($result){
			if(mysql_num_rows($result)>0){
				$row = mysql_fetch_array($result);
				$promoMsg = $row['promoMsg'];
				$exD = $row['offer_exp_date'];
				
				$promoMessage = $promoMsg . ". " . "It expires on " . $exD;
				
			}
		}
		
		return $promoMessage;
		
	}


?>