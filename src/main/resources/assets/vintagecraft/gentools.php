<?php

$metals = array("stone", "copper", "tinbronze", "bismuthbronze", "iron", "steel");
$tooltypes = array("axe", "shovel", "pickaxe", "shears", "sword", "saw", "hoe", "hammer", "carpenterstoolset");


$outdir = "models/item/tool/";

foreach ($metals as $metal) {
	foreach ($tooltypes as $tooltype) {
		if ($tooltype == "carpenterstoolset" && !in_array($metal, array("tinbronze", "bismuthbronze", "iron", "steel"))) continue;
		
		foreach (array("", "_dmd") as $variant) {
			$json = getItemJson("tool", $metal, $tooltype, $variant);
			file_put_contents($outdir . $metal . "_" . $tooltype . $variant. ".json", $json);
		
			$json = getItemJson("toolhead", $metal, $tooltype, $variant);
			file_put_contents("models/item/toolhead/" . $metal . "_" . $tooltype  . $variant. ".json", $json);
		}
	}
}



function getItemJson($itemtype, $metal, $tooltype, $dmd) {
	if ($dmd) {
		$dmd = ',
		"layer1": "vintagecraft:items/toolhead_diamondcrust/'.$tooltype.'"';
	}
	
	if ($itemtype == "toolhead") {
	return '{
    "parent": "builtin/generated",
    "textures": {
        "layer0": "vintagecraft:items/'.$itemtype.'/'.$metal.'_'.$tooltype.'"'.$dmd.'
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
}'
	;
	} else {
	return '{
    "parent": "builtin/generated",
    "textures": {
        "layer0": "vintagecraft:items/'.$itemtype.'/'.$metal.'_'.$tooltype.'"'.$dmd.'
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
}'
	;
	}
	
}



/********* Toolheads *************/


$tooltypes = array("axe", "hoe", "pickaxe", "saw", "shovel", "sword", "hammer");


foreach ($tooltypes as $tooltype) {
	$mask = imagecreatefrompng('textures/items/toolhead/layer_'.$tooltype.'.png');
	
	foreach ($metals as $metal) {
		if (!file_exists("textures/items/tool/{$metal}_{$tooltype}.png")) continue;
		
		$source = imagecreatefrompng("textures/items/tool/{$metal}_{$tooltype}.png");
		
		imagealphamask($source, $mask);
		imagepng($source, "textures/items/toolhead/{$metal}_{$tooltype}.png");
	}
}









function imagealphamask( &$picture, $mask ) {
    // Get sizes and set up new picture
    $xSize = imagesx( $picture );
    $ySize = imagesy( $picture );
    $newPicture = imagecreatetruecolor( $xSize, $ySize );
    imagesavealpha( $newPicture, true );
    imagefill( $newPicture, 0, 0, imagecolorallocatealpha( $newPicture, 0, 0, 0, 127 ) );

    // Resize mask if necessary
    if( $xSize != imagesx( $mask ) || $ySize != imagesy( $mask ) ) {
        $tempPic = imagecreatetruecolor( $xSize, $ySize );
        imagecopyresampled( $tempPic, $mask, 0, 0, 0, 0, $xSize, $ySize, imagesx( $mask ), imagesy( $mask ) );
        imagedestroy( $mask );
        $mask = $tempPic;
    }

    // Perform pixel-based alpha map application
    for( $x = 0; $x < $xSize; $x++ ) {
        for( $y = 0; $y < $ySize; $y++ ) {
            $alpha = imagecolorsforindex( $mask, imagecolorat( $mask, $x, $y ) );
            $alpha = 127 - floor( $alpha[ 'red' ] / 2 );
            $color = imagecolorsforindex( $picture, imagecolorat( $picture, $x, $y ) );
            imagesetpixel( $newPicture, $x, $y, imagecolorallocatealpha( $newPicture, $color[ 'red' ], $color[ 'green' ], $color[ 'blue' ], $alpha ) );
        }
    }

    // Copy back to original picture
    imagedestroy( $picture );
    $picture = $newPicture;
}