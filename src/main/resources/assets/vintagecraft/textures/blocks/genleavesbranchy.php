<?php

$indir = "leaves/";
$outdir = "leavesbranchy/";

$res = opendir($indir);

$overlayimg = imagecreatefrompng("leavesbranchy/branches.png");
imagealphablending($overlayimg, false);

while ($file = readdir($res)) {
	if (!preg_match("/\.png$/", $file)) continue;
	list($wdt, $hgt) = getimagesize("$indir/$file");
		
	$img = imagecreatefrompng("$indir/$file");
	imagealphablending($img, true);
	
	
		
	imagecopy($img, $overlayimg, 0, 0, 0, 0, $wdt, $hgt);
	
	imagesavealpha($img, true);
	
	imagepng($img, $outdir . $file);
}

echo "done!";