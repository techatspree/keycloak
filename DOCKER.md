# Build only the server

    mvn -pl quarkus/deployment,quarkus/dist -am -DskipTests clean install

# Run Docker build

    cp ./quarkus/dist/target/keycloak-127.0.0.1-SNAPSHOT.tar.gz ./quarkus/container/keycloak.tar.gz

    docker build --build-arg KEYCLOAK_VERSION=127.0.0.1-SNAPSHOT --build-arg KEYCLOAK_DIST=keycloak.tar.gz \
        -t spree.de/zetaguard/keycloak/keycloak:127.0.0.1-SNAPSHOT ./quarkus/container

# Test image

    docker run --rm --name keycloak -p 127.0.0.1:8080:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin spree.de/zetaguard/keycloak/keycloak:127.0.0.1-SNAPSHOT start-dev