<?php

$variants = array(1, 2, 3);
$soils = array("lowf", "medf", "hif");
$grasstypes = array("verysparse", "sparse", "normal");

$outdir = "block/topsoil/";

foreach ($variants as $variant) {
	foreach ($soils as $soil) {
		foreach ($grasstypes as $grasstype) {
			$model = 

	'{
	"parent": "vintagecraft:block/topsoil/grass",
	"textures": {
		"particle": "vintagecraft:blocks/topsoil/topsoil_'.$soil.'",
		"bottom": "vintagecraft:blocks/topsoil/topsoil_'.$soil.'",
		"top": "vintagecraft:blocks/topsoil/grass_top_'.$grasstype.$variant.'",
		"side": "vintagecraft:blocks/topsoil/topsoil_'.$soil.'",
		"overlay": "vintagecraft:blocks/topsoil/grass_side_'.$grasstype.'"
	}
}';

		$name = $soil."_". $grasstype."grass".$variant;
		
		file_put_contents($outdir . $name.".json", $model);
		
		$name = $soil."_". $grasstype."grass";
		
		
			$item = '
{
"parent": "vintagecraft:block/topsoil/'.$name.'",
    "display": {
        "thirdperson": {
            "rotation": [ 10, -45, 170 ],
            "translation": [ 0, 1.5, -2.75 ],
            "scale": [ 0.375, 0.375, 0.375 ]
        }
    }
}';

			file_put_contents("item/topsoil/". $name.".json", $model);
		}
	}
}