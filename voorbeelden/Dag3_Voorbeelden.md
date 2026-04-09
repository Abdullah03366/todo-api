# Dag 3 – Code Voorbeelden

## .NET Todo Controller (starter)

```csharp
[ApiController]
[Route("api/[controller]")]
public class TodosController : ControllerBase
{
    [HttpGet]
    public IActionResult GetTodos()
    {
        // TODO: haal todos op
        return Ok();
    }

    [HttpPost]
    public IActionResult CreateTodo([FromBody] CreateTodoDto dto)
    {
        // TODO: validatie + create + 201
        return StatusCode(501);
    }
}

public record CreateTodoDto(string Title);
```

---

## Java Spring Boot Controller (starter)

```java
@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @GetMapping
    public ResponseEntity<?> getTodos() {
        // TODO: haal todos op
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoRequest req) {
        // TODO: validatie + create + 201
        return ResponseEntity.status(501).build();
    }
}

record TodoRequest(String title) {}
```

---

## `.NET` Setup (Program.cs)

```csharp
var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();
builder.Services.AddSwaggerGen();
builder.Services.AddCors(opt => opt.AddPolicy("All", p => p.AllowAnyOrigin().AllowAnyMethod().AllowAnyHeader()));

var app = builder.Build();
app.UseSwagger();
app.UseSwaggerUI();
app.UseCors("All");
app.MapControllers();
app.Run();
```

---

## Java Spring Boot Setup (application.yml)

```yaml
spring:
  application:
    name: todo-api
  jpa:
    hibernate:
      ddl-auto: create-drop
server:
  port: 8080
```
