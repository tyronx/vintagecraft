<?php

$blockclasses = array("log", "leaves", "leavesbranchy", "planks");

$blocktypes = array("birch", "oak", "maple", "mountaindogwood", "scotspine", "spruce", "acacia", "kapok", "coconutpalm");

foreach ($blockclasses as $blockclass) {
	foreach ($blocktypes as $blocktype) {
		$blockoutdir = "block/{$blockclass}/";
		$itemoutdir = "item/{$blockclass}/";
		
		$modeltype = "all";
		if ($blockclass == "log") $modeltype = "bark";
		
		file_put_contents($blockoutdir . $blocktype.".json", getBlockModel($modeltype, $blockclass, $blocktype));
		file_put_contents($itemoutdir . $blocktype.".json", getItemModel($blockclass, $blocktype));
	}
}


// modeltypes
//   cube_column
//   cube_all
//   

function getBlockModel($modeltype, $blockclass, $blocktype) {
	switch ($modeltype) {
		case "bark":
			return '{'
			. '	"parent": "block/cube_column",'
			. '	"textures": {'
			. '		"end": "vintagecraft:blocks/'.$blockclass.'/'.$blocktype.'",'
			. '		"side": "vintagecraft:blocks/'.$blockclass.'/'.$blocktype.'_bark"'
			. '	}'
			. '}';
		
		case "all":
			return '{'
			.'	"parent": "block/cube_all",'
			.'	"textures": {'
			.'		"all": "vintagecraft:blocks/'.$blockclass.'/'.$blocktype.'"'
			.'	}'
			.'}';
			
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