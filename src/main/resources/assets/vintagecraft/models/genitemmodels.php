<?php

//$itemtype = "doubleplant";
//$itemtype = "topsoil";
//$itemtype = "gravel";
$itemtype = "leavesbranchy";

$types = array("ash", "birch", "douglasfir", "oak", "maple", "mountaindogwood", "pine", "spruce");


//$types = array('orangemilkweed', 'orangemilkweed2', 'purplemilkweed', 'purplemilkweed2', 'catmint', 'calendula', 'cornflower', 'cornflower2', 'lilyofthevalley', 'lilyofthevalley2', 'lilyofthevalley3', 'clover', 'goldenrod', 'goldenrod2', 'goldenrod3', 'forgetmenot', 'forgetmenot2', 'forgetmenot3', 'forgetmenot4', 'forgetmenot5', 'narcissus', 'narcissus2', 'narcissus3');

//$types = array("lowf_nograss", "lowf_verysparsegrass", "lowf_sparsegrass", "lowf_normalgrass", "medf_nograss", "medf_verysparsegrass", "medf_sparsegrass", "medf_normalgrass", "hif_nograss", "hif_verysparsegrass", "hif_sparsegrass", "hif_normalgrass");


//$types = array("andesite", "basalt", "claystone", "conglomerate", "diorite", "gneiss", "granite", "limestone", "marble", "quartzite", "schist", "shale", "gabbro", "sandstone", "redsandstone", "chert");

//$types = array("nativecopper","nativegold", "limonite", "lignite", "bituminouscoal");

//$types = array("copper", "bronze", "iron", "steel", "rhodium", "platinum", "iridium", "palladium", "osmium", "silver", "gold");

$outdir = "item/{$itemtype}/";

foreach ($types as $type) {

	/*$model = 
'{
"parent": "vintagecraft:block/'.$itemtype.'/'.$type.'",
    "display": {
        "thirdperson": {
            "rotation": [ 10, -45, 170 ],
            "translation": [ 0, 1.5, -2.75 ],
            "scale": [ 0.375, 0.375, 0.375 ]
        }
    }
}
';*/
	
	
/*	$model = 
'{
    "parent": "builtin/generated",
    "textures": {
        "layer0": "vintagecraft:blocks/'.$itemtype.'/'.$type.'"
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
}';*/

	$model = '{
"parent": "vintagecraft:block/'.$itemtype.'/'.$type.'",
    "display": {
        "thirdperson": {
            "rotation": [ 10, -45, 170 ],
            "translation": [ 0, 1.5, -2.75 ],
            "scale": [ 0.375, 0.375, 0.375 ]
        }
    }
}';
	
	file_put_contents($outdir . $type.".json", $model);
}