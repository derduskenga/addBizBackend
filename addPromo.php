<?php
	include_once('DB_connect.php');
	
	//receive post values fro app
	
	
	$email = $_POST['email'];
	$expD=$_POST['expD'];
	$promotionalMsg=$_POST['msg'];
	$typeOfAddition=$_POST['type'];
	$pName=$_POST['pName'];
	
	
	$expDArray = explode("-",$expD);
	
	$expDM = $expDArray[0];
	$expDD = $expDArray[1];
	$expDY = $expDArray[2];
	
	$expD = $expDD."-".$expDM."-".$expDY;
	

	if(checkOfferOrNewArrival($pName,$typeOfAddition)){
	
		echo insertPromo($email,$expD,$promotionalMsg,$typeOfAddition,$pName);
		
	}else{
	
		$databaseDate = strtotime(fetchEntryDate ($pName,$typeOfAddition));
		//echo $databaseDate;
		
		if(strtotime($expD)>$databaseDate){
			
			
			
			echo updatePromoOrNewArrival ($pName,$typeOfAddition,$expD);
			
			
		}else{
		
			echo $pName ." is running a/an ". $typeOfAddition. "  and has not expired";
		
		}
	}
	
	
	function insertPromo($email,$expD,$promotionalMsg,$typeOfAddition,$pName){
		$response = "";
		$query = "insert into business_added (google_email,comodity,promoMsg,type,offer_exp_date) values('$email','$pName','$promotionalMsg','$typeOfAddition','$expD')";
		$result = mysql_query($query) or die("Query failed" .mysql_query);
		if($result){
		
			$response .= "Your ". $typeOfAddition. " has been added";
			
		}else{
		
			$response .="An error occured. Please try again";
		}
		
		return $response;
		
	}
	
	function fetchEntryDate ($pName,$typeOfAddition){
	
		$query = "select * from business_added where comodity = '$pName' and type='$typeOfAddition'";
		$result = mysql_query($query) or die("Query failed" . mysql_error());
		
		if($result){
		
			while($row = mysql_fetch_array($result)){
			
				$date = $row['offer_exp_date'];
				//echo "date is " . $date;
			
			}
			
		}
		
		return $date;
	
	}
	
	function checkOfferOrNewArrival($pName,$typeOfAddition){//type could be new arrival or offer/promotion
	
		$query = "select * from business_added where comodity = '$pName' and type='$typeOfAddition'";
		
		$result = mysql_query($query) or die("Query failed" . mysql_error());
		
		if(mysql_num_rows($result)>0){
		
			//recors found
			//echo "similar record found";
			return false;
			
		}else{
		
			return true;
			
		}
	
	}
	
	function updatePromoOrNewArrival($pName,$typeOfAddition,$expD){
		$response="";
		$query = "update business_added set offer_exp_date='$expD' where comodity='$pName' and type='$typeOfAddition'";
		$result = mysql_query($query) or die("Query failed" . mysql_error());
		if($result){
		
			$response .= "Your ". $typeOfAddition. " has been updated";
			
		}else{
			
			$response .="An error occured. Please try again";
		}
		
		return $response;
	
	}
	

?>