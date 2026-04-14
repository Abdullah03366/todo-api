# YP todo-app opdracht van Abdullah Sanli YP Java

Deze repository bevat een 5-daags leertraject om een TODO-app te ontwerpen, bouwen, beveiligen en te deployen naar Azure.

## Overzicht per dag

- [Dag 1 – Architectuur & Design](./Dag1_Architectuur_Design.md)
- [Dag 2 – Pipelines bouwen](./Dag2_Pipelines_bouwen.md)
- [Dag 3 – Backend coderen](./Dag3_Backend_coderen.md)
- [Dag 4 – Backend uitbreiden + security](./Dag4_Backend_uitbreiden.md)
- [Dag 5 – Deployen naar Azure](./Dag5_Deployen_naar_Azure.md)

## Voorbeelden (snippets)

Alle korte code snippets staan in de map [voorbeelden](./voorbeelden/):

- [Dag 1 voorbeelden](./voorbeelden/Dag1_Voorbeelden.md)
- [Dag 2 voorbeelden](./voorbeelden/Dag2_Voorbeelden.md)
- [Dag 3 voorbeelden](./voorbeelden/Dag3_Voorbeelden.md)
- [Dag 4 voorbeelden](./voorbeelden/Dag4_Voorbeelden.md)
- [Dag 5 voorbeelden](./voorbeelden/Dag5_Voorbeelden.md)

## Doel van deze opdracht

- Architectuur en API-design kunnen uitleggen
- CI/CD pipelines opzetten met GitHub Actions
- REST backend bouwen in .NET of Java
- Security en tests toepassen
- End-to-end deployen naar Azure

## Benodigdheden

- Git (versiebeheer)
- Een IDE:
  - Visual Studio Code + extensies, of
  - JetBrains IDE (IntelliJ IDEA / Rider) via een persoonlijke licentie, of
  - Visual Studio (licentie of tijdelijke Community-versie)
- Voor .NET track: .NET SDK 10+
- Voor Java track: JDK 25+ en Maven
- GitHub account (voor repository en GitHub Actions)
- (Optioneel) GitHub Desktop via Company Portal → Pre Approved Catalogue
- Azure-account/subscription (voor dag 5)

Zie ook het volledige installatieplan: [Installatie stappenplan](./Installatie_Stappenplan.md)

## Deployment-overzicht

Voor Azure gebruiken we de `main_postgres`-branch. De `main`-branch blijft bedoeld voor lokale ontwikkeling met H2; voor Azure hoef je dus geen extra `application.properties`-bestand aan te maken.

### Backend op Azure App Service

De Spring Boot backend leest de volgende instellingen uit environment variables (met lokale defaults):

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `APP_JWT_SECRET`
- `APP_JWT_EXPIRATION_MS` (optioneel)
- `APP_CORS_ALLOWED_ORIGINS`
- `PORT` (door Azure App Service gezet)

### Vue frontend op Azure Static Web App

De Vue-app draait als aparte origin. Voeg daarom in Azure Static Web Apps de productie-frontend toe als toegestane CORS-origin op de backend. Voor lokaal is `http://localhost:5173` al toegestaan.

Tip voor de Vue-repo:

- stel een API base URL in, bijvoorbeeld via `VITE_API_BASE_URL`
- wijs die naar de backend-URL van Azure App Service
- zorg dat requests een `Authorization: Bearer ...` header kunnen meesturen

## Eindpresentatie

Aan het einde van elke dag presenteer je:

- Wat en hoe je gebouwd hebt
- Wat je geleerd hebt
- Wat morgen beter kan
- Hinderpunten en oplossingen
