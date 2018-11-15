# GraphQL Linalyzer

## Building a Docker Image with the linalyzer Executable

```
docker build -t linalyzer .
```

## Executing linalyzer Inside a Docker Container

```
docker run --rm \
    --mount type=bind,source="$(pwd)/examples",target=/files \
    linalyzer \
    -c /files/example-config.yml /files/example-schema.graphql
```
