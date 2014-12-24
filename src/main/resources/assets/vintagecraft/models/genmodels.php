<?php

$blocktype = "rock";

$rocktypes = array("andesite", "basalt", "claystone", "conglomerate", "diorite", "gneiss", "granite", "limestone", "marble", "quartzite", "schist", "shale", "gabbro", "sandstone", "redsandstone", "chert");

$outdir = "block/{$blocktype}/";

foreach ($rocktypes as $rocktype) {

	$model = 
	'{
		"parent": "block/cube_all",
		"textures": {
			"all": "vintagecraft:blocks/'.$blocktype.'/'.$rocktype.'"
		}
	}';
	
	file_put_contents($outdir . $rocktype.".json", $model);
	
	
}