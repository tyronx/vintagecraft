<?php

$blockclasses = array("rock", "subsoil", "regolith", "gravel", "sand", "cobblestone");
$blocktypes = array("andesite", "basalt", "claystone", "conglomerate", "diorite", "gneiss", "granite", "limestone", "marble", "quartzite", "schist", "shale", "gabbro", "sandstone", "redsandstone", "chert", "chalk", "kimberlite", "slate");
$availtypes = 16;

foreach ($blockclasses as $blockclass) {
	$variants = array();
	
	foreach ($blocktypes as $blocktype) {
		$blockoutdir = "models/block/{$blockclass}/";
		$itemoutdir = "models/item/{$blockclass}/";
		$modeltype = "all";

		file_put_contents($blockoutdir . $blocktype.".json", getBlockModel($modeltype, $blockclass, $blocktype));
		file_put_contents($itemoutdir . $blocktype.".json", getItemModel($blockclass, $blocktype));
		
		$variants[] = "\t\t" . '"rocktype='.$blocktype.'": { "model": "vintagecraft:'.$blockclass.'/'.$blocktype.'" }';
	}
	
	for ($i = 0; $i < ceil(count($blocktypes) / $availtypes); $i++) {
		file_put_contents("blockstates/" . $blockclass . (($i > 0) ? ($i+1) : "")  .".json", getBlockStates($variants));
	}
}


function getBlockStates($variants) {
	return '{
	"variants": {
' . implode(",\r\n", $variants)
	. '
	}
}';
}



function getBlockModel($modeltype, $blockclass, $blocktype) {
	return '{
		"parent": "block/cube_all",
		"textures": {
			"all": "vintagecraft:blocks/'.$blockclass.'/'.$blocktype.'"
		}
	}';
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
}
';
}