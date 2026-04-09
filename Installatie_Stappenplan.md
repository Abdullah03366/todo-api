# Installatie Stappenplan

Dit stappenplan helpt collega’s om snel op te starten voor het 5-daagse TODO-traject.

## 1. Installeer Git

1. Ga naar: <https://git-scm.com/downloads>
2. Download de installer voor jouw besturingssysteem
3. Installeer met de standaardopties
4. Controleer installatie:

```bash
git --version
```

## 2. Kies je IDE

Kies één van deze opties:

### Optie A: Visual Studio Code

1. Download: <https://code.visualstudio.com/>
2. Installeer VS Code
3. Installeer deze aanbevolen extensies:
   - .NET Install Tool/C# (Microsoft)
   - Extension Pack for Java (Microsoft)
   - GitHub Actions (GitHub)
   - YAML (Red Hat)
   - Azure Tools (Microsoft) (handig voor dag 5)

### Optie B: JetBrains IDE

1. Download IntelliJ via: <https://www.jetbrains.com/idea/download/>
2. Download Rider via: <https://www.jetbrains.com/rider/download/>
3. Installeer alleen de IDE die je nodig hebt en gebruik de persoonlijke licentie:
   - IntelliJ IDEA (voor Java), of
   - Rider (voor .NET)

### Optie C: Visual Studio

1. Als je al een Visual Studio licentie hebt, kun je die gewoon gebruiken
2. Je kunt ook tijdelijk de gratis Community-versie gebruiken
3. Download: <https://visualstudio.microsoft.com/downloads/>
4. Kies tijdens installatie de juiste workloads:
   - ASP.NET and web development (voor .NET)
   - (Optioneel) Azure development

## 3. Installeer SDK’s (afhankelijk van je track)

### Voor .NET

1. Download .NET SDK 10+: <https://dotnet.microsoft.com/download>
2. Controleer:

```bash
dotnet --version
```

### Voor Java

1. Download JDK 25+ (bijv. Temurin): <https://adoptium.net/>
2. Installeer Maven: <https://maven.apache.org/download.cgi>
3. Controleer:

```bash
java -version
mvn -version
```

## 4. GitHub-account en repo-toegang

1. Zorg dat je een GitHub account hebt: <https://github.com/>
2. Log in en controleer toegang tot de repository
3. Clone de repo lokaal:

```bash
git clone <repo-url>
```

### Optioneel: GitHub Desktop via Company Portal

1. Open **Company Portal**
2. Open of installeer de **Pre Approved Catalogue**
3. Zoek op **GitHub Desktop**
4. Klik op **Installeren**

Dit is handig als je liever met een grafische Git-client werkt in plaats van alleen terminal-commando’s.

## 5. (Voor dag 5) Azure account

1. Zorg voor een Azure account: <https://azure.microsoft.com/>
2. Activeer een subscription
3. (Optioneel) Installeer Azure CLI: <https://learn.microsoft.com/cli/azure/install-azure-cli>
4. Log in via de terminal:

```bash
az login
```

## 6. Quick check (alles werkt)

- Git werkt: `git --version`
- IDE opent de repository
- .NET track: `dotnet --version`
- Java track: `java -version` en `mvn -version`
- (Dag 5) Azure login werkt: `az account show`

## Klaar om te starten

Ga terug naar [README](./README.md) en volg de dagplanning.
