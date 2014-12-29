<?php

$itemtype = "doubleplant";

$types = array("goldenrod", "goldenrod2", "goldenrod3", "butterflymilkweed", "butterflymilkweed2", "orangebutterflybush");


//$types = array("andesite", "basalt", "claystone", "conglomerate", "diorite", "gneiss", "granite", "limestone", "marble", "quartzite", "schist", "shale", "gabbro", "sandstone", "redsandstone", "chert");

//$types = array("nativecopper","nativegold", "limonite", "lignite", "bituminouscoal");

//$types = array("copper", "bronze", "iron", "steel", "rhodium", "platinum", "iridium", "palladium", "osmium", "silver", "gold");

$outdir = "item/{$itemtype}/";

foreach ($types as $type) {

	$model = 
'{
    "parent": "builtin/generated",
    "textures": {
        "layer0": "vintagecraft:blocks/'.$itemtype.'/'.$type.'_top"
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
}
';
	
	file_put_contents($outdir . $type.".json", $model);
}