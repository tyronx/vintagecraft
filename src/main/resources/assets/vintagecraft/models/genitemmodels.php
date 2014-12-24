<?php

$itemtype = "stone";

$types = array("andesite", "basalt", "claystone", "conglomerate", "diorite", "gneiss", "granite", "limestone", "marble", "quartzite", "schist", "shale", "gabbro", "sandstone", "redsandstone", "chert");

//$types = array("nativecopper","nativegold", "limonite", "lignite", "bituminouscoal");

$outdir = "item/{$itemtype}/";

foreach ($types as $type) {

	$model = 
'{
    "parent": "builtin/generated",
    "textures": {
        "layer0": "vintagecraft:items/'.$itemtype.'/'.$type.'"
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
	
	file_put_contents($outdir . $type.".json", $model);
}