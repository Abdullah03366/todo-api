# Dag 5 – Code Voorbeelden (CI/CD Pipelines)

## GitHub Actions - .NET Pipeline (starter)

```yaml
name: .NET CI/CD
on:
  push:
    branches: [main, develop]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-dotnet@v4
        with:
          dotnet-version: '10.0.x'

      # TODO: restore
      # TODO: build
      # TODO: test
      # TODO: publish artifact
```

---

## GitHub Actions - Java Pipeline (starter)

```yaml
name: Java CI/CD
on:
  push:
    branches: [main, develop]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '25'
          distribution: 'temurin'
          cache: maven

      # TODO: build
      # TODO: test
      # TODO: upload artifact
```

---

## Deploy to Azure - .NET (starter)

```yaml
name: Deploy .NET to Azure
on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-dotnet@v4
        with:
          dotnet-version: '10.0.x'

      # TODO: publish stap
      # TODO: azure/webapps-deploy stap met eigen secrets
```

---

## Deploy to Azure - Java (starter)

```yaml
name: Deploy Java to Azure
on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '25'
          distribution: 'temurin'
          cache: maven

      # TODO: package stap
      # TODO: azure/webapps-deploy stap met eigen secrets
```

---

## Tips voor Deployment

1. **GitHub Secrets instellen**:
    - `AZURE_WEBAPP_NAME` - je App Service-naam
    - `AZURE_PUBLISH_PROFILE` - te downloaden uit de Azure Portal

2. **Test lokaal eerst**:

    ```bash
    # .NET
    dotnet run
    
    # Java
    mvn spring-boot:run
    ```

3. **Curl test naar live API**:

    ```bash
    # Zet je deployment URL hier
    curl https://my-todo-api-net.azurewebsites.net/api/todos
    ```

4. **Troubleshoot**:
    - Check Azure Portal logs
    - Kijk Pipeline logs in GitHub Actions
    - Zorg dat CORS correct is ingesteld
