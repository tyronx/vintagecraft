<?php

$dir = opendir(".");

while($file = readdir($dir)) {
	if (preg_match("/\.png$/", $file)) {
		rename($file, strtolower(str_replace(" Raw", "", $file)));
	}
}