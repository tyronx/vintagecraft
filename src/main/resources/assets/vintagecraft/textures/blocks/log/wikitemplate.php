<?php

$x = opendir(".");
@mkdir ("out");

while ($file = readdir($x)) {
	if (strstr($file, "_bark") || $file == "." || $file==".." || !strstr($file, ".png")) continue;
	
	$name = ucfirst(str_replace(".png", "", $file));
	$filename = ucfirst($file);
	copy($file, "out/Log{$name}.png");
	
	echo 
"|[[File:Log{$name}.png]]
|[[File:Plank{$name}.png]]
|{$name}
|
|
|-
";
	
}