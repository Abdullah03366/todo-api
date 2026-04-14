# Actuele Architectuur (Azure Deployment)

Hier is het actuele architectuurdiagram op basis van de gescheiden repositories (frontend / backend), de geïsoleerde PostgreSQL database en de vernieuwde abstractie van de environment variabelen.

![Taskmaster TODO-2026-04-14-214525.png](Taskmaster%20TODO-2026-04-14-214525.png)

### Mermaid Code voor het Architectuurdiagram:

```mermaid
flowchart LR
    %% Gebruiker
    User(("User/Client"))

    %% GitHub Repositories (Gescheiden)
    subgraph Repos ["GitHub (Source & CI/CD)"]
        direction TB
        
        subgraph Repo_FE ["Frontend Repository"]
            direction LR
            CodeFE["Vue Source Code"]
            PipelineFE["GitHub Actions (FE)\n(Build, Test, Deploy)"]
            CodeFE --> PipelineFE
        end
        
        subgraph Repo_BE ["Backend Repository"]
            direction LR
            CodeBE["Java Source Code"]
            SecretsBE["GitHub Secrets\n(Deployment Credentials)"]:::orangeBox
            PipelineBE["GitHub Actions (BE)\n(Build, Test, Deploy)"]
            CodeBE --> PipelineBE
            SecretsBE -.->|Injectie| PipelineBE
        end
    end

    %% Azure Cloud Environment
    subgraph Azure ["Azure Cloud Environment"]
        direction TB
        
        %% Frontend Zichtbaar voor Buitenwereld
        SWA["Azure Static Web Apps\n(Vue.js SPA)"]
        
        %% Backend & Database (Afgeschermd)
        subgraph Backend_Environment ["Backend Service (REST API)"]
            direction TB
            AppConfig["App Service Configuration\n(Env Vars: DB Config, JWT Secrets, CORS)"]:::orangeBox
            
            subgraph WebApp ["Azure App Service (Spring Boot)"]
                direction TB
                AuthFilter["JWT Auth Filter & CORS Check"]:::securityBox
                Controllers["REST API Controllers\n(Swagger/OpenAPI)"]
                Services["Business Logic & Services"]
                JpaRepo["Spring Data JPA\n(Hibernate)"]
                
                AuthFilter -->|Valideert Token / CORS| Controllers
                Controllers -->|Aanroep| Services
                Services -->|Gebruikt| JpaRepo
            end
            
            DB[("Azure Database for PostgreSQL\n(Isolated / Enkel Backend Toegang)")]
            LogStream["Azure App Service Log Stream\n(Live Backend Logs)"]
            
            AppConfig -.->|Injecteert Configuraties| WebApp
            JpaRepo -- "JDBC / Hibernate" --> DB
            WebApp -.->|Schrijft logs| LogStream
        end
    end

    %% Deployments
    PipelineFE -- "Deploy Frontend" --> SWA
    PipelineBE -- "Deploy Backend" --> WebApp

    %% Verkeerstromen & Autorisatie
    User -- "1. Bezoekt app (HTTPS)" --> SWA
    
    SWA -- "2. Authenticeert (POST /login)" --> AuthFilter
    AuthFilter -.->|3. Retouneert JWT-token| SWA
    SWA -- "4. API Requests (Authorization: Bearer)" --> AuthFilter

    %% Styling klassen
    classDef orangeBox fill:#f4a261,stroke:#333,stroke-width:1px,color:#000;
    classDef securityBox fill:#e07a5f,stroke:#333,stroke-width:1px,color:#fff;
    
    %% Algemene Subgraph styling
    style Repos fill:#f8f9fa,stroke:#adb5bd,stroke-width:2px,color:#000
    style Azure fill:#f0f8ff,stroke:#90caf9,stroke-width:2px,color:#000
    style Backend_Environment fill:#ffffff,stroke:#8ce,stroke-width:2px,stroke-dasharray: 5 5
````
