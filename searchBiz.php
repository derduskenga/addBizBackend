<?php
	
	include_once('DB_connect.php');
	$category = $_POST['category'];
	
	getBizList($category);
	
	function getBizList($category){
	
		// array for JSON response
		$response = array();
		
		$query = "select * from bizmerchants where category='$category'";
		$result = mysql_query($query) or die("Query failed" . mysql_error());
		$message = '{"success":"1","biz":[';
		//echo $message;
		if ($result) {
		
			$middle = '';
			if (mysql_num_rows($result) > 0) {
			
				//$biz = array();
				while($row = mysql_fetch_array($result)){
				
				$name= $row["biz_name"];
            	$vicinity = $row["retrieved_location_name_vicinity"];
            	$description= $row["description"];
            	$lat = $row["latitude"];
            	$lng = $row["longitude"];
				$email = $row["google_mail"];
				
					
					$middle .= '{"name":' . '"'.$name . '"'.',"vicinity":'. '"'. $vicinity. '"'. ',"description":'. '"'.$description . '"'. ',"lat":'. '"'.$lat. '"'.',"lng":'. '"'.$lng. '"'.	',"email":'. '"'.$email. '"'.'},';
					
					//echo $middle;
				}
				
 
           echo $message.mb_substr($middle,0,-1).']}';
			
			}else {
            // no product found
            $response["success"] = "0";
            $response["message"] = "No business found";
 
            // echo no users JSON
            echo json_encode($response);
		
		}
		}else{
        // no product found
        $response["success"] = "0";
        $response["message"] = "An error occured";
 
        // echo no users JSON
        echo json_encode($response);
    	}
	
	} 
	
?>