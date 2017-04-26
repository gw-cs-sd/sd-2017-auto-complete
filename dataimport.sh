#!/bin/bash

for letter in a b c d e f g h i j k l m n o p q r s t u v w x y z
do
  collection=$1$letter
  collection+="1"
  fpath=$2$collection
  gzip -d -k $fpath.json.gz
  mongoimport --jsonArray --db oneGram --collection $collection --file $fpath.json
  rm $fpath.json

  collection=$1$letter
  collection+="2"
  fpath=$2$collection
  gzip -d -k $fpath.json.gz
  mongoimport --jsonArray --db twoGram --collection $collection --file $fpath.json
  rm $fpath.json

  collection=$1$letter
  collection+="3"
  fpath=$2$collection
  gzip -d -k $fpath.json.gz
  mongoimport --jsonArray --db threeGram --collection $collection --file $fpath.json
  rm $fpath.json

  collection=$1$letter
  collection+="4"
  fpath=$2$collection
  gzip -d -k $fpath.json.gz
  mongoimport --jsonArray --db fourGram --collection $collection --file $fpath.json
  rm $fpath.json
done
