<?php

// Block+Item
$blockclasses = array("rock", "subsoil", "regolith", "gravel", "sand", "cobblestone", "stoneanvil");
$blockclassgroups = array("terrafirma", "terrafirma", "terrafirma", "terrafirma", "terrafirma", "stoneworking", "metalworking");
// Item only
$itemclasses = array("stonepot", "grindstone", "grindstonebase");
$itemclassgroups = array("metalworking", "mechanics", "mechanics");

$blocktypes = array("andesite", "basalt", "claystone", "conglomerate", "diorite", "gneiss", "granite", "limestone", "marble", "quartzite", "schist", "shale", "gabbro", "sandstone", "redsandstone", "chert", "chalk", "kimberlite", "slate");
$organiclayers = array("nograss", "verysparsegrass", "sparsegrass", "normalgrass");
$anvilstages = array(0, 1, 2);

$availtypes = array("rock" => 16, "subsoil" => 4, "regolith" => 16, "gravel" => 16, "sand" => 16, "cobblestone" => 16, "stoneanvil" => 4);


foreach ($blockclasses as $index => $blockclass) {
	$variants = array();
	
	if (strlen($blockclassgroups[$index])) {
		$basedir = $blockclassgroups[$index] . "/";
	} else {
		$basedir = "";
	}
	
	foreach ($blocktypes as $blocktype) {
		$blockoutdir = "models/block/{$basedir}{$blockclass}/";
		$itemoutdir = "models/item/{$basedir}{$blockclass}/";
		$modeltype = "all";

		
		
		if ($blockclass == "subsoil") {
			foreach ($organiclayers as $organiclayer) {
				$variants[] = "\t\t" . '"rocktype='.$organiclayer.'-'.$blocktype.'": { "model": "vintagecraft:'.$basedir.$blockclass.'/'.$organiclayer.'-'.$blocktype.'" }';
				file_put_contents($blockoutdir . $organiclayer."-".$blocktype.".json", getBlockModel($modeltype, $blockclass, $blocktype, $organiclayer));
				file_put_contents($itemoutdir . $organiclayer."-".$blocktype.".json", getItemModel($blockclass, $blocktype, $organiclayer));
			}
		} elseif ($blockclass == "stoneanvil") {
			foreach ($anvilstages as $anvilstage) {
				$variants[] = "\t\t" . '"rocktype='.$blocktype.',stage='.$anvilstage.'": { "model": "vintagecraft:'.$basedir.$blockclass.'/'.$blocktype.'-'.$anvilstage.'" }';
				file_put_contents($blockoutdir . $blocktype."-".$anvilstage.".json", getBlockModel($modeltype, $blockclass, $blocktype, $anvilstage));
			}
			file_put_contents($itemoutdir . $blocktype . ".json", getItemModel($blockclass, $blocktype . "-2"));
		} else {
			file_put_contents($blockoutdir . $blocktype.".json", getBlockModel($modeltype, $blockclass, $blocktype));
			file_put_contents($itemoutdir . $blocktype.".json", getItemModel($blockclass, $blocktype));
			
			$variants[] = "\t\t" . '"rocktype='.$blocktype.'": { "model": "vintagecraft:'.$basedir.$blockclass.'/'.$blocktype.'" }';
		}
	}
	
	for ($i = 0; $i < ceil(count($blocktypes) / $availtypes[$blockclass]); $i++) {
		file_put_contents("blockstates/" . $blockclass . (($i > 0) ? ($i+1) : "")  .".json", getBlockStates($variants));
	}
}



foreach ($itemclasses as $index=>$itemclass) {
	if (strlen($itemclassgroups[$index])) {
		$basedir = $itemclassgroups[$index] . "/";
	} else {
		$basedir = "";
	}

	foreach ($blocktypes as $blocktype) {
		$itemoutdir = "models/item/{$basedir}{$itemclass}/";
		
		file_put_contents($itemoutdir . $blocktype.".json", getItemModel($itemclass, $blocktype, $basedir));
	}
}





function getBlockStates($variants) {
	return '{
	"variants": {
' . implode(",\r\n", $variants)
	. '
	}
}';
}



function getBlockModel($modeltype, $blockclass, $blocktype, $param = null) {
	if ($blockclass == "subsoil" && $param && $param != "nograss") {
		return '{
	"parent": "vintagecraft:block/topsoil/grass",
	"textures": {
		"particle": "vintagecraft:blocks/'.$blockclass.'/'.$blocktype.'",
		"bottom": "vintagecraft:blocks/'.$blockclass.'/'.$blocktype.'",
		"top": "vintagecraft:blocks/topsoil/grass_top_'.str_replace("grass", "", $param).'1",
		"side": "vintagecraft:blocks/'.$blockclass.'/'.$blocktype.'",
		"overlay": "vintagecraft:blocks/topsoil/grass_side_'.str_replace("grass", "", $param).'"
	}
}';
	}
	

	if ($blockclass == "stoneanvil") {
		return '{
	"parent": "vintagecraft:block/metalworking/stoneanvil/base_'.$param.'",
	"textures": {
		"stone": "vintagecraft:blocks/rock/'.$blocktype.'"
	}
}';

	}
	
	return '{
	"parent": "block/cube_all",
	"textures": {
		"all": "vintagecraft:blocks/'.$blockclass.'/'.$blocktype.'"
	}
}';
}


function getItemModel($blockclass, $blocktype, $organiclayer = null, $basedir = "") {
	if ($blockclass == "stonepot") {
		return '{
    "parent": "vintagecraft:item/'.$basedir.$blockclass.'/base",
    "textures": {
        "stone": "vintagecraft:blocks/cobblestone/'.$blocktype.'"
    },
    "display": {
        "thirdperson": {
            "rotation": [ 10, -45, 170 ],
            "translation": [ 0, 1.5, -2.75 ],
            "scale": [ 0.375, 0.375, 0.375 ]
        }
    }
}
';
	}

	if ($blockclass == "grindstone" || $blockclass == "grindstonebase") {
		return '{
    "parent": "vintagecraft:item/'.$basedir.$blockclass.'/base",
    "textures": {
        "stone": "vintagecraft:blocks/rock/'.$blocktype.'"
    },
    "display": {
        "thirdperson": {
            "rotation": [ 10, -45, 170 ],
            "translation": [ 0, 1.5, -2.75 ],
            "scale": [ 0.75, 0.75, 0.75 ]
        }
    }
}
';
	}
	
	if ($organiclayer) {
	return '{
"parent": "vintagecraft:block/'.$blockclass.'/'.$organiclayer.'-'.$blocktype.'",
    "display": {
        "thirdperson": {
            "rotation": [ 10, -45, 170 ],
            "translation": [ 0, 1.5, -2.75 ],
            "scale": [ 0.375, 0.375, 0.375 ]
        }
    }
}
';		
	}
	
	
	return '{
"parent": "vintagecraft:block/'.$basedir.$blockclass.'/'.$blocktype.'",
    "display": {
        "thirdperson": {
            "rotation": [ 10, -45, 170 ],
            "translation": [ 0, 1.5, -2.75 ],
            "scale": [ 0.375, 0.375, 0.375 ]
        }
    }
}
';
}