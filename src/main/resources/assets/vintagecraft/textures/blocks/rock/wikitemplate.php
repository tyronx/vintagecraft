<?php

$x = opendir(".");

while ($file = readdir($x)) {
	$name = ucfirst(str_replace(".png", "", $file));
	$filename = ucfirst($file);
	echo
"|[[File:{$filename}]]
|{$name}
|
|
|-
";
	
}