<?php

$metals = array("stone", "copper", "tinbronze", "bismuthbronze", "iron");
$pieces = array("helmet", "chestplate", "leggings", "boots");


$outdir = "item/armor/";

foreach ($metals as $metal) {
	foreach ($pieces as $piece) {
		file_put_contents($outdir . $metal . "_" . $piece . ".json", getItemModel("armor", $metal.'_'.$piece));
	}
	
	file_put_contents("item/metalplate/" . $metal . ".json", getItemModel("metalplate", $metal));
	file_put_contents("block/metalplate/" . $metal . ".json", getBlockModel("metalplate", $metal));
}



function getBlockModel($blockclass, $blocktype) {
	return '{
    "parent": "vintagecraft:block/metalplate/base",
    "textures": {
        "metal": "vintagecraft:blocks/ingot/'.$blocktype.'"
    }
}';
}

function getItemModel($blockclass, $blocktype) {
	if ($blockclass == "armor") {
		return '{
    "parent": "builtin/generated",
    "textures": {
        "layer0": "vintagecraft:items/armor/'.$blocktype.'"
    },
    "display": {
        "thirdperson": {
            "rotation": [ -90, 0, 0 ],
            "translation": [ 0, 1, -3 ],
            "scale": [ 0.55, 0.55, 0.55 ]
        },
        "firstperson": {
            "rotation": [ 0, -135, 25 ],
            "translation": [ 0, 4, 2 ],
            "scale": [ 1.7, 1.7, 1.7 ]
        }
    }
}';
	}
	return '{
	"parent": "vintagecraft:block/metalplate/'.$blocktype.'",
	"display": {
		"thirdperson": {
			"rotation": [ 10, -45, 170 ],
			"translation": [ 0, 1.5, -2.75 ],
			"scale": [ 0.75, 0.75, 0.75 ]
		}
	}
}';
}