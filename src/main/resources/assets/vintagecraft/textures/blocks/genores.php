<?php

$rockdir = "rock/";
$oredir = "ore_overlay/";
$outdir = "ore/";

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