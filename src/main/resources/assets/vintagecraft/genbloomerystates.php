<?php

$states = array("unlit_empty", "unlit_2", "unlit_4", "unlit_6", "unlit_8", "unlit_10", "unlit_12", "unlit_14", "unlit_16", "lit", "melted_4", "melted_6", "melted_8", "melted_10", "melted_12", "melted_14", "melted_16");
$angles = array("north" => "", "south" => "180", "west" => "270", "east" => "90");
$json = '{
    "variants": {
';

foreach ($states as $state) {
	foreach ($angles as $name => $angle) {
		if (!empty($angle)) {
			$json .= '        "facing='.$name.', fillheight='.$state.'": { "model": "vintagecraft:furnace/'. str_replace("_", "-", $state) .'", "y": '.$angle.' },' . "\r\n";
		} else {
			$json .= '        "facing='.$name.', fillheight='.$state.'": { "model": "vintagecraft:furnace/'. str_replace("_", "-", $state) .'" },' . "\r\n";
		}
	}
}

$json = substr($json, 0, strlen($json) - 3);
$json .= '
	}
}';

file_put_contents("blockstates/furnacesection.json", $json);