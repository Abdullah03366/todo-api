# Dag 5 – Deployen naar Azure

## Doel

De TODO-app live zetten op Azure met:

- een Java 25 Spring Boot backend op **Azure App Service**
- een **Azure Database for PostgreSQL** database
- een aparte Vue frontend op **Azure Static Web Apps**

## Output

Een live backend-endpoint, een database en duidelijke deployment-instellingen voor frontend + backend.

## Opdracht

1. Maak de Azure-resources aan op basis van wat je op dag 1 hebt gekozen.

2. Configureer de backend voor Azure:
   - gebruik `main_postgres`
   - laat database- en JWT-instellingen uit environment variables komen
   - sta de Vue-origin toe via CORS

3. Deploy de backend naar Azure App Service.

4. Deploy de Vue-app naar Azure Static Web Apps.

5. Toon de live URL's in demo.

---

## Benodigde backend-aanpassingen in dit project

### Wat is al voorbereid

- `src/main/java/example/demo/todo/Application.java` heeft een publieke `main`-methode
- `src/main/resources/application.properties` gebruikt defaults via environment variables
- `src/main/java/example/demo/todo/config/CorsConfig.java` staat CORS toe voor lokale Vue-hosts en Azure-origin(s)

### Belangrijke environment variables voor Azure App Service

Stel deze in bij **App Service > Configuration > Application settings**:

- `SPRING_DATASOURCE_URL` → PostgreSQL JDBC-URL
- `SPRING_DATASOURCE_USERNAME` → databasegebruiker
- `SPRING_DATASOURCE_PASSWORD` → databasewachtwoord
- `APP_JWT_SECRET` → sterke geheime sleutel voor JWT-signatures
- `APP_JWT_EXPIRATION_MS` → optioneel, bijv. `3600000`
- `APP_CORS_ALLOWED_ORIGINS` → bijv. `https://<jouw-frontend>.azurestaticapps.net`
- `PORT` → laat Azure zelf beheren; je hoeft dit meestal niet te zetten

### Frontend/CORS aandachtspunt

De Vue-app draait op een aparte domain/origin. Daardoor moet de backend CORS toestaan voor de Static Web App URL. Voeg in productie de exacte frontend-origin toe, bijvoorbeeld:

- `https://<naam>.azurestaticapps.net`

Voor lokaal development blijven deze al geldig:

- `http://localhost:5173`
- `http://localhost:8080`

---

## Azure Portal stappen: backend + database

### 1. PostgreSQL aanmaken

1. Open de Azure Portal.
2. Maak een resource group aan of kies een bestaande.
3. Maak **Azure Database for PostgreSQL flexible server** aan.
4. Kies regio, naam, admin-gebruiker en wachtwoord.
5. Zet networking/firewall zo dat:
   - je Azure App Service de database kan bereiken
   - lokaal testen eventueel ook toegestaan is als je dat wilt
6. Maak een database aan, bijvoorbeeld `todo`.

### 2. App Service aanmaken

1. Maak een **App Service** aan.
2. Kies **Publish = Code**.
3. Kies runtime **Java 25**.
4. Kies Linux als dat beschikbaar is binnen jouw standaard.
5. Selecteer dezelfde resource group en regio als de database.
6. Maak of koppel een App Service Plan.

### 3. App settings configureren

Ga naar **App Service > Configuration** en voeg de environment variables toe uit de vorige sectie.

Gebruik voor `SPRING_DATASOURCE_URL` de PostgreSQL JDBC-URL van Azure, bijvoorbeeld:

```text
jdbc:postgresql://<server>.postgres.database.azure.com:5432/todo?sslmode=require
```

### 4. Deployment source koppelen

Je kunt de backend vanaf je repo deployen met de methode die je op de website gebruikt, bijvoorbeeld:

- **GitHub Actions** via de Deployment Center
- of een handmatige deployment van het JAR-bestand

Voor deze opdracht is het belangrijkste dat de App Service de JAR draait met Java 25 en de juiste app settings heeft.

### 5. Testen

Controleer na deployment:

- `/swagger-ui`
- een publieke GET-endpoint zoals `/api/users/me` met geldige auth
- database-connectiviteit

---

## Azure Portal stappen: Vue Static Web App

### 1. Static Web App aanmaken

1. Maak een **Static Web App** aan.
2. Koppel je Vue-repository.
3. Kies de branch die voor frontend deployment gebruikt wordt.
4. Vul build-instellingen in voor Vue/Vite.

### 2. API URL instellen in Vue

Zorg dat de Vue-app de backend-URL kent, bijvoorbeeld via:

- `VITE_API_BASE_URL=https://<jouw-app>.azurewebsites.net`

### 3. Backend-origin toevoegen aan CORS

Neem de frontend-URL op in `APP_CORS_ALLOWED_ORIGINS`.

---

## Leerdoelen

- Azure deployments
- Cloud resource configuratie
- End-to-end delivery
- CORS voor een aparte frontend-origin
- PostgreSQL in plaats van lokale opslag voor productie

## Eindpresentatie (Dag 5)

Demonstreer de live gedeployde TODO-app:

- **Live URL**: Toon de werkende backend endpoint in de browser (bijv. `https://your-app.azurewebsites.net/api/todos`)
- **Azure Resources**: Welke resources heb je aangemaakt? (App Service, PostgreSQL, Static Web App)
- **Configuration**: Hoe worden secrets en environment variables gemanaged?
- **CORS**: Laat zien dat de Vue-app de backend mag benaderen
- **Performance**: Hoe snel laadt de app? Zijn er performance issues?
- **Future Improvements**: Wat kan beter? (scaling, monitoring, database, frontend)
- **Full Journey**: Reflecteer op de week: van architectuur tot live deployment
