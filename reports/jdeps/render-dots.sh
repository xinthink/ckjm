#!/bin/bash
for file in "$1"/*.dot
do
  echo "rendering '$file'"
  dot -Tpng "$file" -o "${file%.dot}.png"
done
