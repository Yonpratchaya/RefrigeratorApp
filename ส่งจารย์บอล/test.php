<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
     </head>
     <body>
     	
     	<?php

$food=array('หมู','ไก่','ไข่','ข้าว');
$cfood=count($food);

$aaa="%2C";

$test = "";

$page=array('1','2','3','4');
$cpage=count($page);


for ($k=0 ; $k <= ($cfood-1); $k++) {

	$test=$test.$aaa.$food[$k];
}
for ($i=0; $i <= ($cpage-1) ; $i++) { 
	$url = "http://cookingdiary.fanthai.com/?s=".$test."&paged=".$page[$i];
	

	
	$ch = curl_init($url);
	curl_setopt($ch,CURLOPT_RETURNTRANSFER,1);
	$data = curl_exec($ch);

//echo $url."<br>";

	$e = explode('<article class="item-list">', $data);
	$y = count($e);

	for ($j=1; $j < ($y); $j++) { 

		$title=explode("Permalink to ",$e[$j]);
		$title1=explode('rel="bookmark">',$title[1]);	

		$pict=explode('rel="bookmark">',$e[$j]);
		$picture=explode('</a>',$pict[2]);

		$url=explode('post-title"><a href="', $e[$j]);
		$urll=explode('" title="', $url[1]);
		
		echo $picture[0].$title1[0].$urll[0]."</br>";

	}
}
?>
     </body>
