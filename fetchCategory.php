<?php
		
	include_once('DB_connect.php');
	
	echo "-Select category-," . fetchCategory ();
	
	function fetchCategory (){
		$categoryList = "";
		$query = "select * from productcategories";
		$result = mysql_query($query) or die("Query failed" .mysql_error());
		
		if($result){
			while ($row = mysql_fetch_array($result)){
				$categoryList .= $row['categoryName'] .",";
			}
		}
		
	return $categoryList;
	}
		
?>