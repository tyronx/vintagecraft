<?php

$dir = opendir("rock/");

$outdir = "cobblestone/";
$overlayImg = imagecreatefrompng("cobblestone-overlay.png");

while ($file = readdir($dir)) {
	if (preg_match("/\.png$/", $file)) {
		$rockImg = imagecreatefrompng("rock/$file");
		list($wdt, $hgt) = getimagesize("rock/$file");
		
		imagecopy($rockImg, $overlayImg, 0, 0, 0, 0, $wdt, $hgt); 
		imagepng($rockImg, "cobblestone/".$file);
	}
}