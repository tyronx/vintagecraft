<?php

$metals = array("stone", "copper", "tinbronze", "bismuthbronze", "iron");
$pieces = array("helmet", "chestplate", "leggings", "boots");


$outdir = "item/armor/";

foreach ($metals as $metal) {
	foreach ($pieces as $piece) {
		
		file_put_contents($outdir . $metal . "_" . $piece . ".json", '{
    "parent": "builtin/generated",
    "textures": {
        "layer0": "vintagecraft:items/armor/'.$metal.'_'.$piece.'"
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
}');
		
	}
}


