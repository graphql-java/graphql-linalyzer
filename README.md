# GraphQL Linalyzer

GraphQL schema **li**nter and a**nalyzer** (linalyzer)

## Config file example

```yaml
rules:
  - name: camelCase
    severity: warning
  - name: noTabs
    severity: error
```

## Run it via docker

The latest build is available on docker hub via `andimarek/graphql-linalyzer`

```sh
docker run \
-v $(pwd)/<your-config.yml>:/linalyzer-config.yml \
-v $(pwd)/<your-schema-to-check>:/schema.graphql \
andimarek/graphql-linalyzer schema.graphql
```


