<?php

$metals = array("copper", "tin", "tinbronze", "iron", "steel", "palladium", "platinum", "titanium", "chromium", "osmium", "silver", "gold", "uranium", "zinc", "bismuth", "bismuthbronze", "lead");
$pieces = array("helmet", "chestplate", "leggings", "boots");

$outdir = "models/item/metalworking/armor/";

$variants = array();
$numstates = 0;

foreach ($metals as $metal) {
	foreach ($pieces as $piece) {
		file_put_contents($outdir . $metal . "_" . $piece . ".json", getItemModel("armor", $metal.'_'.$piece));
	}
	
	$variants[] = "\t\t" . '"cutout=false,metalandfacing='.$metal.'-d": { "model": "vintagecraft:metalworking/metalplate/'.$metal.'" }';
	$variants[] = "\t\t" . '"cutout=false,metalandfacing='.$metal.'-u": { "model": "vintagecraft:metalworking/metalplate/'.$metal.'", "x":180 }';
	$variants[] = "\t\t" . '"cutout=false,metalandfacing='.$metal.'-n": { "model": "vintagecraft:metalworking/metalplate/'.$metal.'", "x":90, "y": 180 }';
	$variants[] = "\t\t" . '"cutout=false,metalandfacing='.$metal.'-s": { "model": "vintagecraft:metalworking/metalplate/'.$metal.'", "x":90 }';
	$variants[] = "\t\t" . '"cutout=false,metalandfacing='.$metal.'-w": { "model": "vintagecraft:metalworking/metalplate/'.$metal.'", "x":90, "y":90 }';
	$variants[] = "\t\t" . '"cutout=false,metalandfacing='.$metal.'-e": { "model": "vintagecraft:metalworking/metalplate/'.$metal.'", "x": 90, "y":270 }';

	$variants[] = "\t\t" . '"cutout=true,metalandfacing='.$metal.'-d": { "model": "vintagecraft:metalworking/metalplate/'.$metal.'-cutout" }';
	$variants[] = "\t\t" . '"cutout=true,metalandfacing='.$metal.'-u": { "model": "vintagecraft:metalworking/metalplate/'.$metal.'-cutout", "x":180 }';
	$variants[] = "\t\t" . '"cutout=true,metalandfacing='.$metal.'-n": { "model": "vintagecraft:metalworking/metalplate/'.$metal.'-cutout", "x":90, "y": 180 }';
	$variants[] = "\t\t" . '"cutout=true,metalandfacing='.$metal.'-s": { "model": "vintagecraft:metalworking/metalplate/'.$metal.'-cutout", "x":90 }';
	$variants[] = "\t\t" . '"cutout=true,metalandfacing='.$metal.'-w": { "model": "vintagecraft:metalworking/metalplate/'.$metal.'-cutout", "x":90, "y":90 }';
	$variants[] = "\t\t" . '"cutout=true,metalandfacing='.$metal.'-e": { "model": "vintagecraft:metalworking/metalplate/'.$metal.'-cutout", "x": 90, "y":270 }';

	$numstates+=6;
	
	file_put_contents("models/block/metalworking/metalplate/{$metal}.json", getBlockModel("metalplate", $metal));
	file_put_contents("models/block/metalworking/metalplate/{$metal}-cutout.json", getBlockModel("metalplate-cutout", $metal));
	
	file_put_contents("models/item/metalworking/metalplate/{$metal}.json", getItemModel("metalplate", $metal));
}

for ($i = 0; $i < ceil($numstates / 16)+1; $i++) {
	file_put_contents("blockstates/metalplate" . (($i > 0) ? ($i+1) : "")  .".json", getBlockStates($variants));
}





function getBlockStates($variants) {
	return '{
	"variants": {
' . implode(",\r\n", $variants)
	. '
	}
}';
}

function getBlockModel($blockclass, $blocktype) {
	$base = 'base';
	if ($blockclass == 'metalplate-cutout') {
		$base = 'base-cutout';
	}
	
	return '{
    "parent": "vintagecraft:block/metalworking/metalplate/'.$base.'",
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
        },
        "gui": {
            "translation": [ 0, 4, 0 ],
            "scale": [ 1.1, 1.1, 1.1 ]
        }		
	}
}';
}