<?php

//$blocktype = "rock";
$blocktype = "doubleplant";

//$types = array("andesite", "basalt", "claystone", "conglomerate", "diorite", "gneiss", "granite", "limestone", "marble", "quartzite", "schist", "shale", "gabbro", "sandstone", "redsandstone", "chert");
//$types = array("goldenrod_bottom", "goldenrod2_bottom", "goldenrod3_bottom", "butterflymilkweed_bottom", "butterflymilkweed2_bottom", "orangebutterflybush_bottom");
$types = array("goldenrod_top", "goldenrod2_top", "goldenrod3_top", "butterflymilkweed_top", "butterflymilkweed2_top", "orangebutterflybush_top");

$outdir = "block/{$blocktype}/";

foreach ($types as $type) {

	$model = 
	'{
		"parent": "block/tallgrass",
		"textures": {
			"cross": "vintagecraft:blocks/'.$blocktype.'/'.$type.'"
		}
	}';
	
	file_put_contents($outdir . $type.".json", $model);
	
	
}