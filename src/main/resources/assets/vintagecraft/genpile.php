<?php

$blocktype = "orepile";
$oretypes = array("clay", "peat", "lignite", "bituminouscoal", "nativecopper", "limonite", "nativegold_quartz", "redstone", "diamond", "emerald", "lapislazuli", "cassiterite", "quartz", "rocksalt", "platinum", "ilmenite", "nativesilver_quartz", "sphalerite", "bismuthinite", "chromite", "sylvite_rocksalt", "olivine", "peridot_olivine", "galena", "sulfur", "saltpeter", "quartzcrystal", "coke");

$heights = array(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);

$variants = array();
$i = 0;
$states = 0;


foreach ($heights as $height) {
	$basemodel = getBaseBlockModel($height);
	file_put_contents("models/block/orepile/base_{$height}.json", getBaseBlockModel($height));
}


foreach (array("false", "true") as $burning) {
	foreach ($oretypes as $oretype) {
		foreach ($heights as $height) {
			$filename = "orepile/{$oretype}_{$height}";
			if ($burning == "true") $filename .= "_burning";
			
			$variants[] = "\t\t\"burning={$burning},height={$height},oretype={$oretype}\": { \"model\": \"vintagecraft:{$filename}\"}";
			file_put_contents("models/block/{$filename}.json", getBlockModel($oretype, $height, $burning));
		}
	}
}

file_put_contents("blockstates/orepile.json", getBlockStates($variants));


function getBlockStates($variants) {
	return '{
	"variants": {
' . implode(",\r\n", $variants)
	. '
	}
}';

}


function getBaseBlockModel($height) {
	$nheight = $height+1;
	
	return '{
    "__comment": "Model generated using MrCrayfishs Model Creator (http://mrcrayfish.com/modelcreator/)",
    "textures": {
        "texture": "#material",
        "particle": "#material"
    },
    "elements": [
        {
            "name": "Crumble1",
            "from": [ 1.0, '.$height.'.0, 1.0 ], 
            "to": [ 4.0, '.$nheight.'.0, 5.0 ], 
            "faces": {
                "north": { "texture": "#texture", "uv": [ 0.0, 0.0, 3.0, 1.0 ] },
                "east": { "texture": "#texture", "uv": [ 0.0, 0.0, 4.0, 1.0 ] },
                "south": { "texture": "#texture", "uv": [ 0.0, 0.0, 3.0, 1.0 ] },
                "west": { "texture": "#texture", "uv": [ 0.0, 0.0, 4.0, 1.0 ] },
                "up": { "texture": "#texture", "uv": [ 1.0, 1.0, 4.0, 5.0 ] },
                "down": { "texture": "#texture", "uv": [ 0.0, 0.0, 3.0, 4.0 ] }
            }
        },
        {
            "name": "Crumble2",
            "from": [ 5.0, '.$height.'.0, 9.0 ], 
            "to": [ 9.0, '.$nheight.'.0, 12.0 ], 
            "faces": {
                "north": { "texture": "#texture", "uv": [ 5.0, 0.0, 4.0, 1.0 ] },
                "east": { "texture": "#texture", "uv": [ 0.0, 0.0, 3.0, 1.0 ] },
                "south": { "texture": "#texture", "uv": [ 0.0, 0.0, 4.0, 1.0 ] },
                "west": { "texture": "#texture", "uv": [ 0.0, 0.0, 3.0, 1.0 ] },
                "up": { "texture": "#texture", "uv": [ 5.0, 9.0, 9.0, 12.0 ] },
                "down": { "texture": "#texture", "uv": [ 0.0, 0.0, 4.0, 3.0 ] }
            }
        },
        {
            "name": "Crumble3",
            "from": [ 1.0, '.$height.'.0, 12.0 ], 
            "to": [ 3.0, '.$nheight.'.0, 14.0 ], 
            "faces": {
                "north": { "texture": "#texture", "uv": [ 1.0, 12.0, 3.0, 13.0 ] },
                "east": { "texture": "#texture", "uv": [ 0.0, 0.0, 2.0, 1.0 ] },
                "south": { "texture": "#texture", "uv": [ 0.0, 0.0, 2.0, 1.0 ] },
                "west": { "texture": "#texture", "uv": [ 0.0, 0.0, 2.0, 1.0 ] },
                "up": { "texture": "#texture", "uv": [ 1.0, 12.0, 3.0, 14.0 ] },
                "down": { "texture": "#texture", "uv": [ 0.0, 0.0, 2.0, 2.0 ] }
            }
        },
        {
            "name": "Crumble4",
            "from": [ 10.0, '.$height.'.0, 3.0 ], 
            "to": [ 13.0, '.$nheight.'.0, 6.0 ], 
            "faces": {
                "north": { "texture": "#texture", "uv": [ 6.0, 0.0, 9.0, 1.0 ] },
                "east": { "texture": "#texture", "uv": [ 0.0, 0.0, 3.0, 1.0 ] },
                "south": { "texture": "#texture", "uv": [ 0.0, 0.0, 3.0, 1.0 ] },
                "west": { "texture": "#texture", "uv": [ 0.0, 0.0, 3.0, 1.0 ] },
                "up": { "texture": "#texture", "uv": [ 10.0, 3.0, 13.0, 6.0 ] },
                "down": { "texture": "#texture", "uv": [ 0.0, 0.0, 3.0, 3.0 ] }
            }
        },
        {
            "name": "Crumble5",
            "from": [ 13.0, '.$height.'.0, 11.0 ], 
            "to": [ 15.0, '.$nheight.'.0, 14.0 ], 
            "faces": {
                "north": { "texture": "#texture", "uv": [ 0.0, 0.0, 2.0, 1.0 ] },
                "east": { "texture": "#texture", "uv": [ 0.0, 0.0, 3.0, 1.0 ] },
                "south": { "texture": "#texture", "uv": [ 0.0, 0.0, 2.0, 1.0 ] },
                "west": { "texture": "#texture", "uv": [ 0.0, 0.0, 3.0, 1.0 ] },
                "up": { "texture": "#texture", "uv": [ 13.0, 11.0, 15.0, 14.0 ] },
                "down": { "texture": "#texture", "uv": [ 0.0, 0.0, 2.0, 3.0 ] }
            }
        },
        {
            "name": "Base",
            "from": [ 0.0, 0.0, 0.0 ], 
            "to": [ 16.0, '.$height.'.0, 16.0 ], 
            "faces": {
                "north": { "texture": "#texture", "uv": [ 0.0, 0.0, 16.0, '.$height.'.0 ] },
                "east": { "texture": "#texture", "uv": [ 0.0, 0.0, 16.0, '.$height.'.0 ] },
                "south": { "texture": "#texture", "uv": [ 0.0, 0.0, 16.0, '.$height.'.0 ] },
                "west": { "texture": "#texture", "uv": [ 0.0, 0.0, 16.0, '.$height.'.0 ] },
                "up": { "texture": "#texture", "uv": [ 0.0, 0.0, 16.0, 16.0 ] },
                "down": { "texture": "#texture", "uv": [ 0.0, 0.0, 16.0, 16.0 ] }
            }
        }
    ]
}';
}

function getBlockModel($oretype, $height, $burning) {
	if ($burning == "true") {
		return '{
		"parent": "vintagecraft:block/orepile/base_'.$height.'",
		"textures": {
			"material": "vintagecraft:blocks/ember"
		}
}';
	} else {
		return '{
		"parent": "vintagecraft:block/orepile/base_'.$height.'",
		"textures": {
			"material": "vintagecraft:blocks/orepile/'.$oretype.'"
		}
}';
	}

}