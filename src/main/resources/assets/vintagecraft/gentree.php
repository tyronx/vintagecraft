<?php

$blockclasses = array("log", "leaves", "leavesbranchy", "planks", "sapling");
$blocktypes = array("ash", "birch", "oak", "maple", "mountaindogwood", "scotspine", "spruce", "acacia", "kapok", "coconutpalm", "purpleheartwood", "crimsonkingmaple", "elephanttree", "myrtlebeech", "pear", "joshua");

/********** 1. Logs, Leaves, Branches, Planks *************/
foreach ($blockclasses as $blockclass) {
	$variants = array();
	
	foreach ($blocktypes as $blocktype) {
		$blockoutdir = "models/block/{$blockclass}/";
		$itemoutdir = "models/item/{$blockclass}/";
		
		$modeltype = "all";
		if ($blockclass == "log") $modeltype = "bark";
		
		file_put_contents($blockoutdir . $blocktype.".json", getBlockModel($modeltype, $blockclass, $blocktype));
		file_put_contents($itemoutdir . $blocktype.".json", getItemModel($blockclass, $blocktype));
		
		// blockstate variants
		$variants[] = "\t\t" . '"treetype='.$blocktype.'": { "model": "vintagecraft:'.$blockclass.'/'.$blocktype.'" }';
		
		// Make default textures for missing ones
		if (!file_exists("textures/blocks/{$blockclass}/{$blocktype}.png")) {
			copy("textures/blocks/{$blockclass}/ash.png", "textures/blocks/{$blockclass}/{$blocktype}.png");
		}
		if ($blockclass == "log") {
			if (!file_exists("textures/blocks/{$blockclass}/{$blocktype}_bark.png")) {
				copy("textures/blocks/{$blockclass}/ash_bark.png", "textures/blocks/{$blockclass}/{$blocktype}_bark.png");
			}
		}
	}
	
	file_put_contents("blockstates/" . $blockclass.".json", getBlockStates($variants));
	file_put_contents("blockstates/" . $blockclass."2.json", getBlockStates($variants));
	
	if ($blockclass == "planks") {
		file_put_contents("blockstates/doubleslab.json", getBlockStates($variants));
		file_put_contents("blockstates/doubleslab2.json", getBlockStates($variants));
	}
}


/********** 2. Fence *************/

$blockclass = "fence";
$subtypes = array("n", "ne", "ns", "nse", "nsew", "post", "inventory");

$variants = array();
foreach ($blocktypes as $blocktype) {
	$blockoutdir = "models/block/{$blockclass}/";
	$itemoutdir = "models/item/{$blockclass}/";
	
	foreach ($subtypes as $subtype) {
		file_put_contents($blockoutdir . $blocktype."_{$subtype}.json", getBlockModel($subtype, $blockclass, $blocktype));
	}
	
	file_put_contents($itemoutdir . $blocktype.".json", getItemModel($blockclass, $blocktype));
	
	$variants[] = 
'        "east=false,north=false,south=false,treetype='.$blocktype.',west=false": { "model": "vintagecraft:fence/'.$blocktype.'_post" },
        "east=false,north=true,south=false,treetype='.$blocktype.',west=false": { "model": "vintagecraft:fence/'.$blocktype.'_n", "uvlock": true },
        "east=true,north=false,south=false,treetype='.$blocktype.',west=false": { "model": "vintagecraft:fence/'.$blocktype.'_n", "y": 90, "uvlock": true },
        "east=false,north=false,south=true,treetype='.$blocktype.',west=false": { "model": "vintagecraft:fence/'.$blocktype.'_n", "y": 180, "uvlock": true },
        "east=false,north=false,south=false,treetype='.$blocktype.',west=true": { "model": "vintagecraft:fence/'.$blocktype.'_n", "y": 270, "uvlock": true },
        "east=true,north=true,south=false,treetype='.$blocktype.',west=false": { "model": "vintagecraft:fence/'.$blocktype.'_ne", "uvlock": true },
        "east=true,north=false,south=true,treetype='.$blocktype.',west=false": { "model": "vintagecraft:fence/'.$blocktype.'_ne", "y": 90, "uvlock": true },
        "east=false,north=false,south=true,treetype='.$blocktype.',west=true": { "model": "vintagecraft:fence/'.$blocktype.'_ne", "y": 180, "uvlock": true },
        "east=false,north=true,south=false,treetype='.$blocktype.',west=true": { "model": "vintagecraft:fence/'.$blocktype.'_ne", "y": 270, "uvlock": true },
        "east=false,north=true,south=true,treetype='.$blocktype.',west=false": { "model": "vintagecraft:fence/'.$blocktype.'_ns", "uvlock": true },
        "east=true,north=false,south=false,treetype='.$blocktype.',west=true": { "model": "vintagecraft:fence/'.$blocktype.'_ns", "y": 90, "uvlock": true },
        "east=true,north=true,south=true,treetype='.$blocktype.',west=false": { "model": "vintagecraft:fence/'.$blocktype.'_nse", "uvlock": true },
        "east=true,north=false,south=true,treetype='.$blocktype.',west=true": { "model": "vintagecraft:fence/'.$blocktype.'_nse", "y": 90, "uvlock": true },
        "east=false,north=true,south=true,treetype='.$blocktype.',west=true": { "model": "vintagecraft:fence/'.$blocktype.'_nse", "y": 180, "uvlock": true },
        "east=true,north=true,south=false,treetype='.$blocktype.',west=true": { "model": "vintagecraft:fence/'.$blocktype.'_nse", "y": 270, "uvlock": true },
        "east=true,north=true,south=true,treetype='.$blocktype.',west=true": { "model": "vintagecraft:fence/'.$blocktype.'_nsew", "uvlock": true }'
	;
}

for ($i = 0; $i < ceil(count($blocktypes) / 16); $i++) {
	file_put_contents("blockstates/" . $blockclass . (($i > 0) ? ($i+1) : "")  .".json", getBlockStates($variants));
}


/********** 2. Slabs *************/

$blockclass = "singleslab";
$subtypes = array("lower", "upper");

$variants = array();
foreach ($blocktypes as $blocktype) {
	$blockoutdir = "models/block/{$blockclass}/";
	$itemoutdir = "models/item/{$blockclass}/";
	
	foreach ($subtypes as $subtype) {
		file_put_contents($blockoutdir . $blocktype."_{$subtype}.json", getBlockModel($subtype, $blockclass, $blocktype));
	}
	
	file_put_contents($itemoutdir . $blocktype.".json", getItemModel($blockclass, $blocktype));
	
	$variants[] = 
'        "half=bottom,treetype='.$blocktype.'": { "model": "vintagecraft:singleslab/'.$blocktype.'_lower" },
        "half=top,treetype='.$blocktype.'":  { "model": "vintagecraft:singleslab/'.$blocktype.'_upper" }'
;
}

for ($i = 0; $i < ceil(count($blocktypes) / 8); $i++) {
	file_put_contents("blockstates/" . $blockclass . (($i > 0) ? ($i+1) : "")  .".json", getBlockStates($variants));
}


/********** 3. Fence gate *************/


$blockclass = "fencegate";
$subtypes = array("open", "closed");

$variants = array();
foreach ($blocktypes as $blocktype) {
	$blockoutdir = "models/block/{$blockclass}/";
	$itemoutdir = "models/item/{$blockclass}/";
	
	foreach ($subtypes as $subtype) {
		file_put_contents($blockoutdir . $blocktype."_{$subtype}.json", getBlockModel($subtype, $blockclass, $blocktype));
	}
	
	file_put_contents($itemoutdir . $blocktype.".json", getItemModel($blockclass, $blocktype));
	
	$variants[] = 
'        "facing=south,in_wall=false,open=false,treetype='.$blocktype.'": { "model": "vintagecraft:fencegate/'.$blocktype.'_closed" },
        "facing=west,in_wall=false,open=false,treetype='.$blocktype.'":  { "model": "vintagecraft:fencegate/'.$blocktype.'_closed", "y": 90, "uvlock": true },
        "facing=north,in_wall=false,open=false,treetype='.$blocktype.'": { "model": "vintagecraft:fencegate/'.$blocktype.'_closed", "y": 180, "uvlock": true },
        "facing=east,in_wall=false,open=false,treetype='.$blocktype.'":  { "model": "vintagecraft:fencegate/'.$blocktype.'_closed", "y": 270, "uvlock": true },
        "facing=south,in_wall=false,open=true,treetype='.$blocktype.'": { "model": "vintagecraft:fencegate/'.$blocktype.'_open" },
        "facing=west,in_wall=false,open=true,treetype='.$blocktype.'":  { "model": "vintagecraft:fencegate/'.$blocktype.'_open", "y": 90, "uvlock": true },
        "facing=north,in_wall=false,open=true,treetype='.$blocktype.'": { "model": "vintagecraft:fencegate/'.$blocktype.'_open", "y": 180, "uvlock": true },
        "facing=east,in_wall=false,open=true,treetype='.$blocktype.'":  { "model": "vintagecraft:fencegate/'.$blocktype.'_open", "y": 270, "uvlock": true },
        "facing=south,in_wall=true,open=false,treetype='.$blocktype.'": { "model": "vintagecraft:fencegate/'.$blocktype.'_closed" },
        "facing=west,in_wall=true,open=false,treetype='.$blocktype.'":  { "model": "vintagecraft:fencegate/'.$blocktype.'_closed", "y": 90, "uvlock": true },
        "facing=north,in_wall=true,open=false,treetype='.$blocktype.'": { "model": "vintagecraft:fencegate/'.$blocktype.'_closed", "y": 180, "uvlock": true },
        "facing=east,in_wall=true,open=false,treetype='.$blocktype.'":  { "model": "vintagecraft:fencegate/'.$blocktype.'_closed", "y": 270, "uvlock": true },
        "facing=south,in_wall=true,open=true,treetype='.$blocktype.'": { "model": "vintagecraft:fencegate/'.$blocktype.'_open" },
        "facing=west,in_wall=true,open=true,treetype='.$blocktype.'":  { "model": "vintagecraft:fencegate/'.$blocktype.'_open", "y": 90, "uvlock": true },
        "facing=north,in_wall=true,open=true,treetype='.$blocktype.'": { "model": "vintagecraft:fencegate/'.$blocktype.'_open", "y": 180, "uvlock": true },
        "facing=east,in_wall=true,open=true,treetype='.$blocktype.'":  { "model": "vintagecraft:fencegate/'.$blocktype.'_open", "y": 270, "uvlock": true }'
;
}

for ($i = 0; $i < ceil(count($blocktypes) / 1); $i++) {
	file_put_contents("blockstates/" . $blockclass . (($i > 0) ? ($i+1) : "")  .".json", getBlockStates($variants));
}



/********** 4. Stairs *************/


// modeltypes
//   cube_column
//   cube_all
//   


function getBlockStates($variants) {
	return '{
	"variants": {
' . implode(",\r\n", $variants)
	. '
	}
}';

}

function getBlockModel($modeltype, $blockclass, $blocktype) {
	if ($blockclass == "sapling") {
		return '{
    "parent": "block/cross",
    "textures": {
        "particle": "vintagecraft:blocks/sapling/'.$blocktype.'",
        "cross": "vintagecraft:blocks/sapling/'.$blocktype.'"
    }
}';
	}
	
	switch ($modeltype) {
		case "bark":
return '{
	"parent": "block/cube_column",
	"textures": {
		"end": "vintagecraft:blocks/'.$blockclass.'/'.$blocktype.'",
		"side": "vintagecraft:blocks/'.$blockclass.'/'.$blocktype.'_bark"
	}
}';
		
		case "all":
return '{
	"parent": "block/cube_all",
	"textures": {
		"all": "vintagecraft:blocks/'.$blockclass.'/'.$blocktype.'"
	}
}';

		case "n":
		case "ne":
		case "ns":
		case "nse":
		case "nsew":
		case "post":
		case "inventory":
return '{
	"parent": "block/fence_'.$modeltype.'",
	"textures": {
		"texture": "vintagecraft:blocks/planks/'.$blocktype.'"
	}
}';
		
		
		case "open":
		case "closed":
return '{
	"parent": "block/fence_gate_'.$modeltype.'",
	"textures": {
		"texture": "vintagecraft:blocks/planks/'.$blocktype.'"
	}
}';
		
		case 'lower':
return '{
    "parent": "block/half_slab",
    "textures": {
        "bottom": "vintagecraft:blocks/planks/'.$blocktype.'",
        "top": "vintagecraft:blocks/planks/'.$blocktype.'",
        "side": "vintagecraft:blocks/planks/'.$blocktype.'"
    }
}';

		case 'upper':
return '{
    "parent": "block/upper_slab",
    "textures": {
        "bottom": "vintagecraft:blocks/planks/'.$blocktype.'",
        "top": "vintagecraft:blocks/planks/'.$blocktype.'",
        "side": "vintagecraft:blocks/planks/'.$blocktype.'"
    }
}';

	}
}


function getItemModel($blockclass, $blocktype) {
	if ($blockclass == "sapling") {
		return '{
    "parent": "builtin/generated",
    "textures": {
        "layer0": "vintagecraft:blocks/sapling/'.$blocktype.'"
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
}';
	}
	if ($blockclass == "fencegate") {
	return '{
    "parent": "vintagecraft:block/'.$blockclass.'/'.$blocktype.'_closed",
    "display": {
        "thirdperson": {
            "rotation": [ 0, -90, 170 ],
            "translation": [ 0, 1.5, -2.75 ],
            "scale": [ 0.375, 0.375, 0.375 ]
        },
        "firstperson": {
            "rotation": [ 0, 90, 0 ],
            "translation": [ 0, 0, 0 ],
            "scale": [ 1, 1, 1 ]
        }
    }
}';
	}
	if ($blockclass == "fence") {
	return '{
	"parent": "vintagecraft:block/'.$blockclass.'/'.$blocktype.'_inventory",
	"display": {
		"thirdperson": {
			"rotation": [ 10, -45, 170 ],
			"translation": [ 0, 1.5, -2.75 ],
			"scale": [ 0.375, 0.375, 0.375 ]
		}
	}
}';
	}

	if ($blockclass == "singleslab") {
	return '{
	"parent": "vintagecraft:block/'.$blockclass.'/'.$blocktype.'_lower",
	"display": {
		"thirdperson": {
			"rotation": [ 10, -45, 170 ],
			"translation": [ 0, 1.5, -2.75 ],
			"scale": [ 0.375, 0.375, 0.375 ]
		}
	}
}';
	}

	
	return '{
	"parent": "vintagecraft:block/'.$blockclass.'/'.$blocktype.'",
	"display": {
		"thirdperson": {
			"rotation": [ 10, -45, 170 ],
			"translation": [ 0, 1.5, -2.75 ],
			"scale": [ 0.375, 0.375, 0.375 ]
		}
	}
}';
}