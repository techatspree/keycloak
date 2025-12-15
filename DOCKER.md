# Build only the server

    mvn -pl quarkus/deployment,quarkus/dist -am -DskipTests clean install

# Run Docker build

    cd ./quarkus/container
    cp ../dist/target/keycloak-999.0.0-SNAPSHOT.tar.gz keycloak.tar.gz
    
    docker build --build-arg KEYCLOAK_VERSION=zetaguard_26.4.5 --build-arg KEYCLOAK_DIST=keycloak.tar.gz -t spree.de/zetaguard/keycloak/keycloak:26.4.5 .

# Test image

    docker run --name keycloak -p 127.0.0.1:8080:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin spree.de/zetaguard/keycloak/keycloak:26.4.5 start-dev