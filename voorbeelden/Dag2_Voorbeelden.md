# Dag 2 – Code Voorbeelden (CI/CD Pipelines)

## Van nul naar werkende pipeline – stap voor stap

### Stap 1: Maak een nieuwe GitHub repository aan

1. Ga naar [github.com](https://github.com) en log in
2. Klik op **New repository**
3. Geef je repo een naam, bijv. `todo-api`
4. Zet op **Public** of **Private**
5. Vink **Add a README file** aan
6. Klik op **Create repository**

---

### Stap 2: Maak een leeg project aan (lokaal)

> **Let op:** De onderstaande stappen gebruiken de **terminal**. Gebruik je **Visual Studio** of een **JetBrains IDE** (Rider / IntelliJ)? Dan kun je het project ook aanmaken via de ingebouwde GUI:
>
> - **Visual Studio**: File → New → Project → Web API
> - **Rider**: File → New Solution → ASP.NET Core Web Application
> - **IntelliJ IDEA**: File → New Project → Spring Boot (via Spring Initializr ingebouwd)

#### .NET Web API

```bash
# Maak een nieuwe map en navigeer erin
mkdir todo-api
cd todo-api

# Maak een Web API project aan
dotnet new webapi -n TodoApi

# Voeg beide samen in een solution
dotnet new sln -n TodoApi
dotnet sln add TodoApi/TodoApi.csproj
```

#### Java (Maven)

```bash
# Maak een nieuw Spring Boot project via Spring Initializr
# Ga naar: https://start.spring.io/
# Kies:
#   - Project: Maven
#   - Language: Java
#   - Spring Boot: 3.x
#   - Dependencies: Spring Web
# Klik op Generate en pak het zip-bestand uit
```

---

### Stap 3: Koppel je lokale project aan de GitHub repo

> **Let op:** De onderstaande stappen gebruiken de **terminal**. Gebruik je een **JetBrains IDE** of **Visual Studio**? Dan kun je Git ook beheren via de ingebouwde GUI:
>
> - **Visual Studio**: View → Git Changes (commit, push, pull via knoppen)
> - **Rider / IntelliJ**: Git-menu bovenin of de **Commit**-tab linksonder
> - Je kunt ook **GitHub Desktop** gebruiken als je dat hebt geïnstalleerd via Company Portal

```bash
# Navigeer naar je projectmap
cd todo-api

# Initialiseer git (als dat nog niet is gedaan)
git init

# Voeg de remote toe (kopieer de URL van jouw GitHub repo)
git remote add origin https://github.com/<jouw-naam>/todo-api.git

# Voeg een .gitignore toe
# .NET:
dotnet new gitignore
# Java: download via https://gitignore.io (kies: Java, Maven, IntelliJ)

# Eerste commit en push
git add .
git commit -m "Initial project setup"
git branch -M main
git push -u origin main
```

---

### Stap 4: Voeg de pipeline toe

```bash
# Maak de workflow map aan
mkdir -p .github/workflows

# Maak het pipeline bestand aan
# .NET:
touch .github/workflows/dotnet-ci.yml
# Java:
touch .github/workflows/java-ci.yml
```

Plak de pipeline YAML (zie hieronder) in het bestand en push:

```bash
git add .github/workflows/
git commit -m "Add CI pipeline"
git push
```

---

### Stap 5: Controleer de pipeline in GitHub

1. Ga naar je repository op GitHub
2. Klik op het tabblad **Actions**
3. Je ziet de pipeline automatisch starten na de push
4. Klik op de run om logs en resultaten te bekijken

> ✅ Groen = pipeline geslaagd | ❌ Rood = er is een fout, check de logs

---

## GitHub Actions - .NET CI Pipeline (template)

```yaml
name: .NET Build & Test
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

## GitHub Actions - Java CI Pipeline (template)

```yaml
name: Java Build & Test
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

## GitHub Actions Setup - Stap voor Stap

### 1. Repository structuur

```txt
MyProject/
├── .github/
│   └── workflows/
│       ├── dotnet-ci.yml of java-ci.yml
│       └── deploy-azure.yml
├── TodoApp.Api/
├── tests/
├── TodoApp.sln
├── README.md
└── .gitignore
```

### 2. Workflow Trigger Events

```yaml
# Push naar branches
on:
  push:
    branches: [main, develop]

# Pull requests
on:
  pull_request:
    branches: [main]

# Manual trigger
on: workflow_dispatch

# Scheduled (daily at 2am UTC)
on:
  schedule:
    - cron: '0 2 * * *'
```

### 3. Test Reports & Visibility

```yaml
# JUnit test results
- name: Publish Test Results
  uses: EnricoMi/publish-unit-test-result-action@v2
  if: always()
  with:
    files: '**/test-results/**/*.xml'
```

---

## Optional: Code Quality (SonarCloud)

### .NET

```yaml
- name: SonarCloud Scan
  uses: SonarSource/sonarcloud-github-action@master
  env:
    GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
    SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
```

### Java

```yaml
- name: SonarCloud Scan
  run: mvn clean verify sonar:sonar
  env:
    SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
```

---

## Common Issues & Solutions

### ❌ Pipeline hangs/times out

**Solution**: Check runner logs, increase timeout:

```yaml
timeout-minutes: 30
```

### ❌ Test failures in CI, but pass locally

**Solution**: Ensure same Java/dotnet version:

```yaml
- name: Check versions
  run: |
    dotnet --version    # or java -version
```

### ❌ Secrets not found

**Solution**: Add secrets in GitHub repo settings:

- Go: Settings → Secrets and variables → Actions
- Add: `SONAR_TOKEN`, `AZURE_PUBLISH_PROFILE`, etc.

### ❌ Artifact upload fails

**Solution**: Verify paths match build output:

```bash
# .NET
dotnet publish -o release-output

# Java
mvn package  # creates target/*.jar
```

---

## Performance Tips

1. **Use caching** voor dependencies:

    ```yaml
    cache: 'maven'  # Java
    # of
    cache: nuget    # .NET
    ```

2. **Parallel jobs** voor snellere builds:

    ```yaml
    strategy:
      matrix:
        dotnet-version: ['7.0.x', '8.0.x']
    ```

3. **Skip tests** in build-only jobs:

    ```bash
    dotnet build --no-restore
    mvn clean package -DskipTests
    ```
