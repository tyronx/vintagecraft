<?php

$blocktype = "rawore";
$rocktypes = array("andesite", "basalt", "claystone", "conglomerate", "diorite", "gneiss", "granite", "limestone", "marble", "quartzite", "schist", "shale", "gabbro", "sandstone", "redsandstone", "chert", "chalk", "kimberlite", "slate");
$oretypes = array("clay", "peat", "lignite", "bituminouscoal", "nativecopper", "limonite", "nativegold_quartz", "redstone", "diamond", "emerald", "lapislazuli", "cassiterite", "quartz", "rocksalt", "platinum", "ilmenite", "nativesilver_quartz", "sphalerite", "bismuthinite", "chromite", "sylvite_rocksalt", "olivine", "peridot_olivine", "galena", "sulfur", "magnetite");


$variants = array();
$i = 0;

foreach ($rocktypes as $rocktype) {
	foreach ($oretypes as $oretype) {
		$oreandrocktype = "{$oretype}-{$rocktype}";
		
		$variants [] = "\t\t\"oreandrocktype={$oreandrocktype}\": { \"model\": \"vintagecraft:rawore/{$oreandrocktype}\"}";
		
		file_put_contents("models/block/rawore/" . $oreandrocktype.".json", getBlockModel("rawore", $oreandrocktype));
		file_put_contents("models/item/rawore/"   . $oreandrocktype.".json", getItemModel("rawore", $oreandrocktype));
	}
}

$cnt = ceil(count($rocktypes) * count($oretypes) / 16);
while ($cnt-- > 0) {
	if ($cnt == 1) $cnt = "";
	file_put_contents("blockstates/rawore{$cnt}.json", getBlockStates($variants));
}


genOverlays();


function getBlockStates($variants) {
	return '{
	"variants": {
' . implode(",\r\n", $variants)
	. '
	}
}';

}


function getBlockModel($blockclass, $blocktype) {
return '{
	"parent": "block/cube_all",
	"textures": {
		"all": "vintagecraft:blocks/'.$blockclass.'/'.$blocktype.'"
	}
}';
}


function getItemModel($blockclass, $blocktype) {
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





function genOverlays() {
	chdir("textures/blocks");
	$rockdir = "rock/";
	$oredir = "ore_overlay/";
	$outdir = "rawore/";

	$rockdir_res = opendir($rockdir);
	$oredir_res = opendir($oredir);

	$rocks = array();

	while ($file = readdir($rockdir_res)) {
		if (preg_match("/\.png$/", $file)) {
			$rocks[] = $file;
		}
	}

	while ($orefile = readdir($oredir_res)) {
		if (!preg_match("/\.png$/", $orefile)) continue;
		foreach ($rocks as $rockfile) {
			list($wdt, $hgt) = getimagesize("$rockdir/$rockfile");
			
			$rockImg = imagecreatefrompng("$rockdir/$rockfile");
			$oreImg =  imagecreatefrompng("$oredir/$orefile");
			
			imagealphablending($oreImg, true);
			imagealphablending($rockImg, true);
			
			imagecopy($rockImg, $oreImg, 0, 0, 0, 0, $wdt, $hgt);
			
			imagepng($rockImg, $outdir . str_replace(".png", "", $orefile) . "-" . $rockfile);
			
			
		}
	}
}