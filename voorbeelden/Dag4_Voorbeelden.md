# Dag 4 – Code Voorbeelden (Tests & Security)

## .NET Unit Tests (starter)

```csharp
public class TodoControllerTests
{
    [Fact]
    public void CreateTodo_InvalidTitle_ReturnsBadRequest()
    {
        // TODO: arrange
        // TODO: act
        // TODO: assert
    }

    [Fact]
    public void DeleteTodo_UnknownId_ReturnsNotFound()
    {
        // TODO: arrange
        // TODO: act
        // TODO: assert
    }
}
```

---

## Java Unit Tests (starter)

```java
@WebMvcTest(TodoController.class)
class TodoControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void createTodo_invalidBody_returnsBadRequest() throws Exception {
        // TODO: perform request
        // TODO: assert status + foutmelding
    }

    @Test
    void deleteTodo_unknownId_returnsNotFound() throws Exception {
        // TODO: perform request
        // TODO: assert status
    }
}
```

---

## Security Checklist

### Input Validation

- [ ] Null/empty checks
- [ ] Max length validation
- [ ] Type validation

### Error Handling

- [ ] 400 Bad Request voor invalid input
- [ ] 404 Not Found voor missing resources
- [ ] 500 Server Error handling

### Additional Security

- [ ] CORS configured
- [ ] Logging van requests/responses
- [ ] SQL Injection prevention (parameterized)
- [ ] XSS prevention (proper serialization)

---

## Frontend-optie (HTML + JavaScript)

```html
<!DOCTYPE html>
<html>
<head>
    <title>Todo App (MVP)</title>
</head>
<body>
    <h1>Todo App (MVP)</h1>
    <input id="title" placeholder="Add new todo...">
    <button onclick="addTodo()">Add</button>
    <ul id="todos"></ul>

    <script>
        const API = "http://localhost:5000/api/todos"; // .NET example (Java often 8080)

        async function loadTodos() {
            const resp = await fetch(API);
            const todos = await resp.json();
            document.getElementById("todos").innerHTML = todos
                .map(t => `<li>${t.title}</li>`)
                .join("");
        }

        async function addTodo() {
            const title = document.getElementById("title").value;
            if (!title) return;
            await fetch(API, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ title })
            });
            document.getElementById("title").value = "";
            loadTodos();
        }

        loadTodos();
    </script>
</body>
</html>
```
