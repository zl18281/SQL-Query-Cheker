#!/bin/sh

gs -sDEVICE=jpeg -r300 -sOutputFile=../../../../SQL/img/tree.jpg ./tree.ps
mogrify -trim -resize 800x600 ../../../../SQL/img/tree.jpg

