<?php

$metals = array("stone", "copper", "tinbronze", "bismuthbronze", "iron", "steel");
$tooltypes = array("axe", "shovel", "pickaxe", "shears", "sword", "saw", "hoe", "hammer", "carpenterstoolset");


$outdir = "item/tool/";

foreach ($metals as $metal) {
	foreach ($tooltypes as $tooltype) {
		if ($tooltype == "carpenterstoolset" && !in_array($metal, array("tinbronze", "bismuthbronze", "iron", "steel"))) continue;
		
		file_put_contents($outdir . $metal . "_" . $tooltype . ".json", '{
    "parent": "builtin/generated",
    "textures": {
        "layer0": "vintagecraft:items/tool/'.$metal.'_'.$tooltype.'"
    },
    "display": {
        "thirdperson": {
            "rotation": [ 0, 90, -35 ],
            "translation": [ 0, 1.25, -3.5 ],
            "scale": [ 0.85, 0.85, 0.85 ]
        },
        "firstperson": {
            "rotation": [ 0, -135, 25 ],
            "translation": [ 0, 4, 2 ],
            "scale": [ 1.7, 1.7, 1.7 ]
        }
    }
}');
	

		file_put_contents("item/toolhead/" . $metal . "_" . $tooltype . ".json", '{
    "parent": "builtin/generated",
    "textures": {
        "layer0": "vintagecraft:items/toolhead/'.$metal.'_'.$tooltype.'"
    },
    "display": {
        "thirdperson": {
            "rotation": [ 0, 90, -35 ],
            "translation": [ 0, 1.25, -3.5 ],
            "scale": [ 0.85, 0.85, 0.85 ]
        },
        "firstperson": {
            "rotation": [ 0, -135, 25 ],
            "translation": [ 0, 4, 2 ],
            "scale": [ 1.7, 1.7, 1.7 ]
        },
        "gui": {
			"translation": [ -1, -1, 0 ]
        }
    }
}');


	}
}

