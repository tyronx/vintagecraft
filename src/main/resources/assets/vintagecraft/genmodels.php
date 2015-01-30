<?php

//$blocktype = "rock";
//$blocktype = "doubleplant";

//$blocktype = "flower";
$blocktype = "leavesbranchy";

//$blocktype = "gravel";

//$types = array("andesite", "basalt", "claystone", "conglomerate", "diorite", "gneiss", "granite", "limestone", "marble", "quartzite", "schist", "shale", "gabbro", "sandstone", "redsandstone", "chert");
//$types = array("goldenrod_bottom", "goldenrod2_bottom", "goldenrod3_bottom", "butterflymilkweed_bottom", "butterflymilkweed2_bottom", "orangebutterflybush_bottom");
//$types = array("goldenrod_top", "goldenrod2_top", "goldenrod3_top", "butterflymilkweed_top", "butterflymilkweed2_top", "orangebutterflybush_top");

//$types = array('orangemilkweed', 'orangemilkweed2', 'purplemilkweed', 'purplemilkweed2', 'catmint', 'calendula', 'cornflower', 'cornflower2', 'lilyofthevalley', 'lilyofthevalley2', 'lilyofthevalley3', 'clover', 'goldenrod', 'goldenrod2', 'goldenrod3', 'forgetmenot', 'forgetmenot2', 'forgetmenot3', 'forgetmenot4', 'forgetmenot5', 'narcissus', 'narcissus2', 'narcissus3');

$types = array("ash", "birch", "douglasfir", "oak", "maple", "mountaindogwood", "pine", "spruce");

//$types = array("orangemilkweed", "orangemilkweed2", "purplemilkweed", "purplemilkweed2", "catmint", "calendula", "cornflower", "cornflower2", "lilyofthevalley", "lilyofthevalley2", "lilyofthevalley3", "clover");

$outdir = "block/{$blocktype}/";

foreach ($types as $type) {

/*	$model = 
	'{
		"parent": "block/cross",
		"textures": {
			"particle": "vintagecraft:blocks/'.$blocktype.'/'.$type.'",
			"cross": "vintagecraft:blocks/'.$blocktype.'/'.$type.'"
		}
	}';*/
	
	
	$model = 
	'{
		"parent": "block/cube_all",
		"textures": {
			"all": "vintagecraft:blocks/'.$blocktype.'/'.$type.'"
		}
	}';
	
	/*$model =
	'{
		"parent": "block/cube_column",
		"textures": {
			"end": "vintagecraft:blocks/'.$blocktype.'/'.$type.'",
			"side": "vintagecraft:blocks/'.$blocktype.'/'.$type.'_bark"
		}
	}';*/
	
	file_put_contents($outdir . $type.".json", $model);
	
	
}