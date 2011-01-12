#!/bin/bash

CLIENTFILES="Gim.jar smiles icon1.jpg online.png"

# Create the Client jar file
jar cvfm Gim.jar manifest.txt `find -name *.class`
chmod +x Gim.jar

# Create the zip file for the client
mkdir gim_client_svn`svnversion`
for file in $CLIENTFILES
do
	cp -R $file gim_client_svn`svnversion`
done

zip -r gim_client_svn`svnversion`.zip ./gim_client_svn`svnversion`/* -x *.svn*

rm -Rf ./gim_client_svn`svnversion`	
scp gim_client_svn`svnversion`.zip root@rooster.dyndns.info:/home/rooster/public_html/gim_client_current.zip
