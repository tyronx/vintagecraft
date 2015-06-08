<?php

$blocktype = "crop";
$croptypes = array("peas", "wheat", "tomatoes");

$cropshapes = array("doubletallcross", "wheat", "doubletallcross");
$growthstages = array(9, 8, 13);

$variants = array();
$i = 0;
$states = 0;

foreach ($croptypes as $idx => $croptype) {

	for ($stage = 0; $stage < $growthstages[$idx]; $stage++) {
		$filename = "crops/{$croptype}-stage{$stage}";
		
		$variants[] = "\t\t\"croptypeandstage={$croptype}-stage{$stage}\": { \"model\": \"vintagecraft:{$filename}\"}";
		$states++;
	
		file_put_contents("models/block/{$filename}.json", getBlockModel($croptype, $cropshapes[$idx], $stage));
	}
	
}

$cnt = ceil($states / 16);
while ($cnt > 0) {
	if ($cnt == 1) $cnt = "";
	file_put_contents("blockstates/crops{$cnt}.json", getBlockStates($variants));
	$cnt--;
}





function getBlockStates($variants) {
	return '{
	"variants": {
' . implode(",\r\n", $variants)
	. '
	}
}';

}


function getBlockModel($croptype, $cropshape, $stage) {
	if ($cropshape == "wheat") {
return '{
	"parent": "vintagecraft:block/crops/'.$cropshape.'",
	"textures": {
		"crop": "vintagecraft:blocks/crops/'.$croptype.'-stage'.$stage.'"
	}
}';
	} 
	
	if ($cropshape == "doubletallcross" || $cropshape == "cross") {
return '{
	"parent": "vintagecraft:block/crops/'.$cropshape.'",
	"textures": {
		"uppercross": "vintagecraft:blocks/crops/upper/'.$croptype.'-stage'.$stage.'",
		"lowercross": "vintagecraft:blocks/crops/lower/'.$croptype.'-stage'.$stage.'"
	}
}';
		
	}
	
}


function getItemModel($croptype, $laststage) {
	return '{
	"parent": "vintagecraft:block/crops/'.$croptype.'-stage'.$laststage.'",
	"display": {
		"thirdperson": {
			"rotation": [ 10, -45, 170 ],
			"translation": [ 0, 1.5, -2.75 ],
			"scale": [ 0.375, 0.375, 0.375 ]
		}
	}
}';
}



