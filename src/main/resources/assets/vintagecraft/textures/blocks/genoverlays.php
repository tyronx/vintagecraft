<?php

$dir = opendir("rock/");

$outdir = "subsoil/";
$overlayImg = imagecreatefrompng("pebbles-overlay.png");

/*$outdir = "regolith/";
$overlayImg = imagecreatefrompng("stones-overlay.png");*/


while ($file = readdir($dir)) {
	if (preg_match("/\.png$/", $file)) {
		$rockImg = imagecreatefrompng("rock/$file");
		list($wdt, $hgt) = getimagesize("rock/$file");
		
		genStoneOverlay($rockImg, $overlayImg, $wdt, $hgt);
	
	
		$subSoilImg = imagecreatefrompng("subsoil-orig/{$file}");
		
		imagesavealpha($rockImg, false);
		imagealphablending($rockImg, true);
		
		imagecopy($subSoilImg, $rockImg, 0, 0, 0, 0, $wdt, $hgt);
		
		
		imagepng($subSoilImg, $outdir . $file);
		
		
	}
}


function genStoneOverlay($rockImg, $overlayImg, $wdt, $hgt) {
	imagealphablending($rockImg, false);
	imagesavealpha($rockImg, true);
		
	$alph = imagecolorallocatealpha($rockImg, 0, 0, 0, 255);
	imagefill($rockImg, 0, 0, $alph);
	
	
	$color = imagecolorallocatealpha($rockImg, 0, 0, 0, 127);
	imagefill($rockImg, 0, 0, $color);
	
	for ($x = 0; $x < $wdt; $x++) {
		for ($y = 0; $y < $hgt; $y++) {
			$alphaIndex = imagecolorat($overlayImg, $x, $y);
			$alphaColor = imagecolorsforindex($overlayImg, $alphaIndex);
			
			$colorIndex = imagecolorat($rockImg, $x, $y); 
			$color = imagecolorsforindex($rockImg, $colorIndex);
			
			$newcolor = imagecolorexactalpha($rockImg, $color['red'], $color['green'], $color['blue'], 255-$alphaColor['red']);
			if ($color == -1) {
			  $newcolor = imagecolorallocatealpha($rockImg, $color['red'], $color['green'], $color['blue'], 255-$alphaColor['red']);
			}
			
			
			imagesetpixel ($rockImg, $x, $y, $newcolor);
		}
	}	
}
