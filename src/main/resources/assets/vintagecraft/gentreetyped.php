<?php
// This script generates all json files tree type dependent blocks
// toolracks, logs, leaves, leaves, branchyleaves, planks, fence, fencegates, slabs, stairs, quartzglass

// Block+Item
$blockclasses = array("log", "leaves", "leavesbranchy", "planks", "sapling");
// Item only
$itemclasses = array("toolrack", "axle", "angledgearbox", "windmillrotor", "bellows");

$blockclassavailabletypes = array("log" => 16, "leaves" => 8, "leavesbranchy" => 8, "planks" => 16, "sapling" => 16);
$blocktypes = array("ash", "birch", "oak", "maple", "mountaindogwood", "scotspine", "spruce", "acacia", "kapok", "coconutpalm", "purpleheartwood", "crimsonkingmaple", "elephanttree", "myrtlebeech", "pear", "joshua", "poplar", "africanmahogany", "blackwalnut", "weepingwillow", "coyotewillow", "larch", "kauri");


/***************** Quartzglass *************/
$variants = array();
foreach ($blocktypes as $blocktype) {
	file_put_contents("models/block/quartzglass/{$blocktype}.json", quartzglasmodel($blocktype));
	
	$variants[] = "\t\t" . '"eastwest=false,treetype='.$blocktype.'": { "model": "vintagecraft:quartzglass/'.$blocktype.'" }';
	$variants[] = "\t\t" . '"eastwest=true,treetype='.$blocktype.'": { "model": "vintagecraft:quartzglass/'.$blocktype.'", "y":90 }';
	
	$itemjson = getItemModel("quartzglass", $blocktype);
	file_put_contents("models/item/quartzglass/{$blocktype}.json", $itemjson);
}

for ($i = 0; $i < ceil(count($blocktypes) / 8); $i++) {
		file_put_contents("blockstates/quartzglass" . (($i > 0) ? ($i+1) : "")  .".json", getBlockStates($variants));
}



/***************** Carpentertable *************/
$variants = array();
foreach ($blocktypes as $blocktype) {
	file_put_contents("models/block/carpentertable/{$blocktype}.json", carpentertableModel($blocktype));
	
	$variants[] = "\t\t" . '"facing=north,treetype='.$blocktype.'": { "model": "vintagecraft:carpentertable/'.$blocktype.'" }';
	$variants[] = "\t\t" . '"facing=east,treetype='.$blocktype.'": { "model": "vintagecraft:carpentertable/'.$blocktype.'", "y":90 }';
	$variants[] = "\t\t" . '"facing=south,treetype='.$blocktype.'": { "model": "vintagecraft:carpentertable/'.$blocktype.'", "y":180 }';
	$variants[] = "\t\t" . '"facing=west,treetype='.$blocktype.'": { "model": "vintagecraft:carpentertable/'.$blocktype.'", "y":270 }';
	
	$itemjson = getItemModel("carpentertable", $blocktype);
	file_put_contents("models/item/carpentertable/{$blocktype}.json", $itemjson);
}

file_put_contents("blockstates/carpentertable.json", getBlockStates($variants));



/******** Buckets *****/

$bucketcontents = array("empty", "water");
$blocktype = "bucket";
foreach ($bucketcontents as $bucketcontent) {
	foreach ($blocktypes as $blocktype) { 
		$parent = "empty";
		if ($bucketcontent != "empty") $parent="filled";
		
		$itemjson = '{
    "parent": "vintagecraft:block/woodbucket/'.$parent.'",
    "textures": {
        "wood": "vintagecraft:blocks/planks/'.$blocktype.'"
	},
    "display": {
        "thirdperson": {
            "rotation": [ 10, -45, 170 ],
            "translation": [ 0, 3, 1 ],
            "scale": [ 0.75, 0.75, 0.75 ]
        }
    }
}
';
		file_put_contents("models/item/woodbucket/{$blocktype}-{$bucketcontent}.json", $itemjson);
	}
}



/********** items only *************/
foreach ($itemclasses as $itemclass) {
	foreach ($blocktypes as $blocktype) {
		$texture = "blocks/planks";
		if ($itemclass == "toolrack") {
			$texture = "items/toolrack";
			file_put_contents("models/item/{$itemclass}/{$blocktype}.json", '{
			"parent": "builtin/generated",
			"textures": {
				"layer0": "vintagecraft:'.$texture.'/'.$blocktype.'"
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
		}');
		} else {
		
			$itemjson = getItemModel($itemclass, $blocktype);
			file_put_contents("models/item/{$itemclass}/{$blocktype}.json", $itemjson);
		}
	}
}

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
	
	$availtypes = $blockclassavailabletypes[$blockclass];
	
	for ($i = 0; $i < ceil(count($blocktypes) / $availtypes); $i++) {
		file_put_contents("blockstates/" . $blockclass . (($i > 0) ? ($i+1) : "")  .".json", getBlockStates($variants));
	}
	
	if ($blockclass == "planks") {
		for ($i = 0; $i < ceil(count($blocktypes) / 8); $i++) {
			file_put_contents("blockstates/doubleslab" . (($i > 0) ? ($i+1) : "")  .".json", getBlockStates($variants));
		}
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

$blockclass = "stairs";
$subtypes = array("normal", "outer", "inner");

$variants = array();
foreach ($blocktypes as $blocktype) {
	$blockoutdir = "models/block/{$blockclass}/";
	$itemoutdir = "models/item/{$blockclass}/";
	
	foreach ($subtypes as $subtype) {
		file_put_contents($blockoutdir . $blocktype."_{$subtype}.json", getBlockModel($subtype, $blockclass, $blocktype));
	}
	
	file_put_contents($itemoutdir . $blocktype.".json", getItemModel($blockclass, $blocktype."_normal"));
	
	$variants[] = 
'        "facing=east,half=bottom,shape=straight,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_normal" },
        "facing=west,half=bottom,shape=straight,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_normal", "y": 180, "uvlock": true },
        "facing=south,half=bottom,shape=straight,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_normal", "y": 90, "uvlock": true },
        "facing=north,half=bottom,shape=straight,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_normal", "y": 270, "uvlock": true },
        "facing=east,half=bottom,shape=outer_right,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_outer" },
        "facing=west,half=bottom,shape=outer_right,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_outer", "y": 180, "uvlock": true },
        "facing=south,half=bottom,shape=outer_right,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_outer", "y": 90, "uvlock": true },
        "facing=north,half=bottom,shape=outer_right,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_outer", "y": 270, "uvlock": true },
        "facing=east,half=bottom,shape=outer_left,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_outer", "y": 270, "uvlock": true },
        "facing=west,half=bottom,shape=outer_left,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_outer", "y": 90, "uvlock": true },
        "facing=south,half=bottom,shape=outer_left,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_outer" },
        "facing=north,half=bottom,shape=outer_left,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_outer", "y": 180, "uvlock": true },
        "facing=east,half=bottom,shape=inner_right,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_inner" },
        "facing=west,half=bottom,shape=inner_right,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_inner", "y": 180, "uvlock": true },
        "facing=south,half=bottom,shape=inner_right,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_inner", "y": 90, "uvlock": true },
        "facing=north,half=bottom,shape=inner_right,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_inner", "y": 270, "uvlock": true },
        "facing=east,half=bottom,shape=inner_left,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_inner", "y": 270, "uvlock": true },
        "facing=west,half=bottom,shape=inner_left,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_inner", "y": 90, "uvlock": true },
        "facing=south,half=bottom,shape=inner_left,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_inner" },
        "facing=north,half=bottom,shape=inner_left,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_inner", "y": 180, "uvlock": true },
        "facing=east,half=top,shape=straight,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_normal", "x": 180, "uvlock": true },
        "facing=west,half=top,shape=straight,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_normal", "x": 180, "y": 180, "uvlock": true },
        "facing=south,half=top,shape=straight,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_normal", "x": 180, "y": 90, "uvlock": true },
        "facing=north,half=top,shape=straight,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_normal", "x": 180, "y": 270, "uvlock": true },
        "facing=east,half=top,shape=outer_right,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_outer", "x": 180, "uvlock": true },
        "facing=west,half=top,shape=outer_right,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_outer", "x": 180, "y": 180, "uvlock": true },
        "facing=south,half=top,shape=outer_right,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_outer", "x": 180, "y": 90, "uvlock": true },
        "facing=north,half=top,shape=outer_right,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_outer", "x": 180, "y": 270, "uvlock": true },
        "facing=east,half=top,shape=outer_left,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_outer", "x": 180, "y": 90, "uvlock": true },
        "facing=west,half=top,shape=outer_left,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_outer", "x": 180, "y": 270, "uvlock": true },
        "facing=south,half=top,shape=outer_left,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_outer", "x": 180, "y": 180, "uvlock": true },
        "facing=north,half=top,shape=outer_left,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_outer", "x": 180, "uvlock": true },
        "facing=east,half=top,shape=inner_right,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_inner", "x": 180, "uvlock": true },
        "facing=west,half=top,shape=inner_right,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_inner", "x": 180, "y": 180, "uvlock": true },
        "facing=south,half=top,shape=inner_right,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_inner", "x": 180, "y": 90, "uvlock": true },
        "facing=north,half=top,shape=inner_right,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_inner", "x": 180, "y": 270, "uvlock": true },
        "facing=east,half=top,shape=inner_left,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_inner", "x": 180, "y": 90, "uvlock": true },
        "facing=west,half=top,shape=inner_left,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_inner", "x": 180, "y": 270, "uvlock": true },
        "facing=south,half=top,shape=inner_left,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_inner", "x": 180, "y": 180, "uvlock": true },
        "facing=north,half=top,shape=inner_left,treetype='.$blocktype.'":  { "model": "vintagecraft:stairs/'.$blocktype.'_inner", "x": 180, "uvlock": true }'
	;
}

for ($i = 0; $i < ceil(count($blocktypes) / 2); $i++) {
	file_put_contents("blockstates/" . $blockclass . (($i > 0) ? ($i+1) : "")  .".json", getBlockStates($variants));
}



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
	if ($blockclass == "stairs") {
		if ($modeltype == "normal") $modeltype = "";
		else $modeltype .= '_';
		
			return '{
    "parent": "block/'.$modeltype.'stairs",
    "textures": {
        "bottom": "vintagecraft:blocks/planks/'.$blocktype.'",
        "top": "vintagecraft:blocks/planks/'.$blocktype.'",
        "side": "vintagecraft:blocks/planks/'.$blocktype.'"
    }
}';
	}
	
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


	if ($blockclass == "axle" || $blockclass == "angledgearbox" || $blockclass ==  "windmillrotor") {
		return '{
	"parent": "vintagecraft:block/'.$blockclass.'",
    "textures": {
        "0": "vintagecraft:blocks/planks/'.$blocktype.'",
        "particle": "vintagecraft:blocks/planks/'.$blocktype.'"
    },
	"display": {
		"thirdperson": {
			"rotation": [ 10, -45, 170 ],
			"translation": [ 0, 1.5, -2.75 ],
			"scale": [ 0.75, 0.75, 0.75 ]
		}
	}
}';
		
	}
	
	if ($blockclass == "bellows") {
		return '{
	"parent": "vintagecraft:item/bellows/base",
    "textures": {
        "0": "vintagecraft:blocks/planks/'.$blocktype.'",
        "particle": "vintagecraft:blocks/planks/'.$blocktype.'"
    },
	"display": {
		"thirdperson": {
			"rotation": [ 10, -45, 170 ],
			"translation": [ 0, 1.5, -2.75 ],
			"scale": [ 0.5, 0.5, 0.5 ]
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

function carpentertableModel($blocktype) {
	return '{
    "parent": "vintagecraft:block/carpentertable/base",
    "textures": {
        "treetype": "vintagecraft:blocks/planks/'.$blocktype.'"
    }
}';
}

function quartzglasmodel($blocktype) {
	return '{
    "__comment": "Model generated using MrCrayfishs Model Creator (http://mrcrayfish.com/modelcreator/)",
    "textures": {
        "0": "vintagecraft:blocks/planks/'.$blocktype.'",
        "1": "vintagecraft:blocks/quartzglass",
		"particle": "vintagecraft:blocks/planks/'.$blocktype.'"
    },
    "elements": [
        {
            "name": "Glass",
            "from": [ 4.0, 4.0, 7.0 ], 
            "to": [ 12.0, 12.0, 9.0 ], 
            "faces": {
                "north": { "texture": "#1" },
                "east": { "texture": "#1" },
                "south": { "texture": "#1" },
                "west": { "texture": "#1" },
                "up": { "texture": "#1" },
                "down": { "texture": "#1" }
            }
        },
        {
		
            "name": "Down Plank",
            "from": [ 0.0, 0.0, 6.0 ], 
            "to": [ 12.0, 4.0, 10.0 ], 
				"faces": {
                "north": { "texture": "#0", "uv": [ 0.0, 8.0, 14.0, 12.0 ] },
                "east": { "texture": "#0", "uv": [ 0.0, 0.0, 4.0, 4.0 ] },
                "south": { "texture": "#0", "uv": [ 0.0, 8.0, 12.0, 12.0 ] },
                "west": { "texture": "#0", "uv": [ 0.0, 12.0, 4.0, 16.0 ] },
                "up": { "texture": "#0", "uv": [ 0.0, 0.0, 12.0, 4.0 ] },
                "down": { "texture": "#0", "uv": [ 0.0, 0.0, 12.0, 4.0 ] }
            }
        },
        {
            "name": "Left Plank",
            "from": [ 0.0, 4.0, 6.0 ], 
            "to": [ 4.0, 16.0, 10.0 ], 
            "faces": {
                "north": { "texture": "#0", "uv": [ 0.0, 0.0, 4.0, 12.0 ] },
                "east": { "texture": "#0", "uv": [ 0.0, 0.0, 4.0, 12.0 ] },
                "south": { "texture": "#0", "uv": [ 0.0, 0.0, 4.0, 12.0 ] },
                "west": { "texture": "#0", "uv": [ 0.0, 0.0, 4.0, 12.0 ] },
                "up": { "texture": "#0", "uv": [ 0.0, 0.0, 4.0, 4.0 ] },
                "down": { "texture": "#0", "uv": [ 0.0, 0.0, 4.0, 4.0 ] }
            }
        },
        {
            "name": "Right Plank",
            "from": [ 12.0, 0.0, 6.0 ], 
            "to": [ 16.0, 12.0, 10.0 ], 
            "faces": {
                "north": { "texture": "#0", "uv": [ 0.0, 0.0, 4.0, 12.0 ] },
                "east": { "texture": "#0", "uv": [ 0.0, 0.0, 4.0, 12.0 ] },
                "south": { "texture": "#0", "uv": [ 0.0, 0.0, 4.0, 12.0 ] },
                "west": { "texture": "#0", "uv": [ 0.0, 0.0, 4.0, 12.0 ] },
                "up": { "texture": "#0", "uv": [ 0.0, 0.0, 4.0, 4.0 ] },
                "down": { "texture": "#0", "uv": [ 0.0, 0.0, 4.0, 4.0 ] }
            }
        },
        {
            "name": "Top Plank",
            "from": [ 4.0, 12.0, 6.0 ], 
            "to": [ 16.0, 16.0, 10.0 ], 
            "faces": {
                "north": { "texture": "#0", "uv": [ 0.0, 0.0, 12.0, 4.0 ] },
                "east": { "texture": "#0", "uv": [ 0.0, 0.0, 4.0, 4.0 ] },
                "south": { "texture": "#0", "uv": [ 0.0, 0.0, 12.0, 4.0 ] },
                "west": { "texture": "#0", "uv": [ 0.0, 0.0, 4.0, 4.0 ] },
                "up": { "texture": "#0", "uv": [ 0.0, 0.0, 12.0, 4.0 ] },
                "down": { "texture": "#0", "uv": [ 0.0, 0.0, 12.0, 4.0 ] }
            }
        }

    ]
}';
}