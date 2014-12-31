<?php

//$itemtype = "doubleplant";
$itemtype = "topsoil";

//$types = array("goldenrod", "goldenrod2", "goldenrod3", "butterflymilkweed", "butterflymilkweed2", "orangebutterflybush");
$types = array("lowf_nograss", "lowf_verysparsegrass", "lowf_sparsegrass", "lowf_normalgrass", "medf_nograss", "medf_verysparsegrass", "medf_sparsegrass", "medf_normalgrass", "hif_nograss", "hif_verysparsegrass", "hif_sparsegrass", "hif_normalgrass");


//$types = array("andesite", "basalt", "claystone", "conglomerate", "diorite", "gneiss", "granite", "limestone", "marble", "quartzite", "schist", "shale", "gabbro", "sandstone", "redsandstone", "chert");

//$types = array("nativecopper","nativegold", "limonite", "lignite", "bituminouscoal");

//$types = array("copper", "bronze", "iron", "steel", "rhodium", "platinum", "iridium", "palladium", "osmium", "silver", "gold");

$outdir = "item/{$itemtype}/";

foreach ($types as $type) {

	$model = 
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
';
	
	file_put_contents($outdir . $type.".json", $model);
}