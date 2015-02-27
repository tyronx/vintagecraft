<?php

$blockclasses = array("log", "leaves", "leavesbranchy", "planks");

$blocktypes = array("ash", "birch", "oak", "maple", "mountaindogwood", "scotspine", "spruce", "acacia", "kapok", "coconutpalm");

foreach ($blockclasses as $blockclass) {
	$variants = array();
	
	foreach ($blocktypes as $blocktype) {
		$blockoutdir = "models/block/{$blockclass}/";
		$itemoutdir = "models/item/{$blockclass}/";
		
		$modeltype = "all";
		if ($blockclass == "log") $modeltype = "bark";
		
		file_put_contents($blockoutdir . $blocktype.".json", getBlockModel($modeltype, $blockclass, $blocktype));
		file_put_contents($itemoutdir . $blocktype.".json", getItemModel($blockclass, $blocktype));
		
		$variants[] = "\t\t" . '"treetype='.$blocktype.'": { "model": "vintagecraft:'.$blockclass.'/'.$blocktype.'" }';
		
		if (!file_exists("textures/blocks/{$blockclass}/{$blocktype}.png")) {
			copy("textures/blocks/{$blockclass}/ash.png", "textures/blocks/{$blockclass}/{$blocktype}.png");
		}
		if ($blockclass == "log") {
			if (!file_exists("textures/blocks/{$blockclass}/{$blocktype}_bark.png")) {
				copy("textures/blocks/{$blockclass}/ash_bark.png", "textures/blocks/{$blockclass}/{$blocktype}_bark.png");
			}
		}
	}
	
	file_put_contents("blockstates/" . $blockclass.".json", getBlockStates($variants));
	file_put_contents("blockstates/" . $blockclass."2.json", getBlockStates($variants));
}


// modeltypes
//   cube_column
//   cube_all
//   


function getBlockStates($variants) {
	return '{
	"variants": {
' . implode(",\r\n", $variants)
	. '
	}
}';

}

function getBlockModel($modeltype, $blockclass, $blocktype) {
	switch ($modeltype) {
		case "bark":
return '{
	"parent": "block/cube_column",
	"textures": {
		"end": "vintagecraft:blocks/'.$blockclass.'/'.$blocktype.'",
		"side": "vintagecraft:blocks/'.$blockclass.'/'.$blocktype.'_bark"
	}
}';
		
		case "all":
return '{
	"parent": "block/cube_all",
	"textures": {
		"all": "vintagecraft:blocks/'.$blockclass.'/'.$blocktype.'"
	}
}';
	}
}


function getItemModel($blockclass, $blocktype) {
	return '{
	"parent": "vintagecraft:block/'.$blockclass.'/'.$blocktype.'",
	"display": {
		"thirdperson": {
			"rotation": [ 10, -45, 170 ],
			"translation": [ 0, 1.5, -2.75 ],
			"scale": [ 0.375, 0.375, 0.375 ]
		}
	}
}';
}