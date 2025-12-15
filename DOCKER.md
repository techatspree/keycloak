# Build only the server

    mvn -pl quarkus/deployment,quarkus/dist -am -DskipTests clean install

# Run Docker build

    docker build -t spree.de/zetaguard/keycloak/keycloak:999 .

