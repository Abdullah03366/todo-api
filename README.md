# YP todo-app opdracht van Abdullah Sanli YP Java

Deze repository bevat een 5-daags leertraject om een TODO-app te ontwerpen, bouwen, beveiligen en te deployen naar Azure.

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

De Spring Boot backend leest de volgende instellingen uit environment variables:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `APP_JWT_SECRET`
- `APP_JWT_EXPIRATION_MS`
- `APP_CORS_ALLOWED_ORIGINS`
- `PORT` (door Azure App Service gezet)

### Vue frontend op Azure Static Web App

De Vue-app draait als aparte origin. In Azure Static Web Apps is de frontend toegevoegd als toegestane CORS-origin op de backend. Voor lokaal is `http://localhost:5173` al toegestaan.
De Vue frontend leest uit environment variables, de backend-URL via: `VITE_API_BASE_URL`

## Netwerkconfiguratie

- De backend draait op Azure App Service, bereikbaar via een publieke URL (bijv. `https://your-app-service.azurewebsites.net`).
- De frontend draait op Azure Static Web Apps, ook bereikbaar via een publieke URL (bijv. `https://your-static-web-app.azurestaticapps.net`).
- CORS is geconfigureerd op de backend om verzoeken van de frontend toe te staan, zowel lokaal als in Azure.
- De backend maakt verbinding met een Azure Database for PostgreSQL, waarvoor de connection string is ingesteld via environment variables in Azure App Service. De database is beveiligd en alleen toegankelijk vanuit de App Service.
- De backend en frontend communiceren via HTTPS, waarbij de backend API-endpoints worden aangeroepen vanuit de frontend met de juiste URL (in Azure).
- De backend is beveiligd met JWT-authenticatie, waarbij tokens worden gegenereerd en gevalideerd op de backend. De frontend slaat het JWT-token op in localStorage en voegt het toe aan de Authorization-header bij API-aanroepen naar de backend.
- De backend en frontend zijn beide toegankelijk via het internet, maar de database is alleen toegankelijk vanuit de backend, waardoor een extra beveiligingslaag wordt toegevoegd.
