# Dag 1 – Code Voorbeelden (Architectuur & Design)

> Deze voorbeelden zijn **startpunten** en bewust niet volledig. Vul keuzes en details zelf aan.

## API Endpoints Documentatie

```txt
BASE_URL: https://api.example.com/api

GET /todos
  Description: Get all todos
  Response: [{ id, title, completed }, ...]
  Status: 200 OK

GET /todos/{id}
  Description: Get single todo
  Response: { id, title, completed }
  Status: 200 OK / 404 Not Found
```

---

## Technology Stack Decision Matrix

### Database

| Tech | Pros | Cons |
| ---- | ---- | ---- |
| **SQL Server** | MS ecosystem, powerful | Expensive |
| **PostgreSQL** | Open source, reliable | Less MS integration |
| **Cosmos DB** | Scalable, NoSQL option | Expensive |
| **SQLite** | Lightweight, zero setup, good for demos | Limited concurrency, not ideal for cloud scale |
| **In memory** | Very fast, perfect for tests/prototypes | No persistence, data lost on restart |

### Hosting

| Tech | Pros | Cons |
| ---- | ---- | ---- |
| **App Service** | Easy, integrated | Limited scaling |
| **Container Apps** | Modern, scalable | More complex |
| **Kubernetes (AKS)** | Full control, enterprise | Overkill for todo app |
