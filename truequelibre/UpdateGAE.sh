cd truequelibre-war
mvn clean install
cd ..
cd truequelibre-ear
mvn clean install
mvn appengine:update
