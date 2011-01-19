#!/bin/bash

CLIENTFILES="./bin/Gim.jar smiles icon1.jpg online.png"

rm Gim.jar gim_client.zip

cd bin

# Create the Client jar file
jar cvfm Gim.jar ../manifest.txt `find -name *.class`
chmod +x Gim.jar

cd ../

# Create the zip file for the client
mkdir gim_client
for file in $CLIENTFILES
do
	cp -R $file gim_client
done

zip -r gim_client.zip ./gim_client/* -x *.svn*

rm -Rf ./gim_client
rm ./bin/Gim.jar

scp gim_client.zip root@rooster.dyndns.info:/home/rooster/public_html/gim_client.zip

rm gim_client.zip
