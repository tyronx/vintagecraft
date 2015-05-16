<?php

$metals = array("copper", "tinbronze", "iron", "steel", "bismuthbronze");
$angles = array("north" => "", "south" => "180", "west" => "270", "east" => "90");
$json = '{
    "variants": {
';

foreach ($metals as $metal) {
	foreach ($angles as $name => $angle) {
		if (!empty($angle)) {
			$json .= '        "facing='.$name.',metal='.$metal.'": { "model": "vintagecraft:anvil/'. str_replace("_", "-", $metal) .'", "y": '.$angle.' },' . "\r\n";
		} else {
			$json .= '        "facing='.$name.',metal='.$metal.'": { "model": "vintagecraft:anvil/'. str_replace("_", "-", $metal) .'" },' . "\r\n";
		}
	}
	
	file_put_contents("models/block/anvil/{$metal}.json", getModelJSON($metal));
	file_put_contents("models/item/anvilvc/{$metal}.json", getItemJSON($metal));
	
	file_put_contents("models/item/anvilbase/{$metal}.json", getItemJSON($metal, "anvilbase"));
	file_put_contents("models/item/anvilsurface/{$metal}.json", getItemJSON($metal, "anvilsurface"));
	
}

$json = substr($json, 0, strlen($json) - 3);
$json .= '
	}
}';

file_put_contents("blockstates/anvilvc.json", $json);


function getModelJSON($metal) {
	return '{
    "parent": "vintagecraft:block/anvil/base",
    "textures": {
        "metal": "vintagecraft:blocks/anvil/'.$metal.'"
    }
}';
}

function getItemJSON($metal, $parent = null) {
	if (!$parent) {
		$parent = 'block/anvil/'.$metal;
	return '{
	"parent": "vintagecraft:'.$parent.'",
	"display": {
		"thirdperson": {
			"rotation": [ 10, -45, 170 ],
			"translation": [ 0, 1.5, -2.75 ],
			"scale": [ 0.75, 0.75, 0.75 ]
		}
	}
}';

	} else {
		$parent = 'item/'.$parent.'/base';
	return '{
	"parent": "vintagecraft:'.$parent.'",
    "textures": {
        "metal": "vintagecraft:blocks/anvil/'.$metal.'"
    },
	"display": {
		"thirdperson": {
			"rotation": [ 10, -45, 170 ],
			"translation": [ 0, 1.5, -2.75 ],
			"scale": [ 0.75, 0.75, 0.75 ]
		}
	}
}';

	}
}