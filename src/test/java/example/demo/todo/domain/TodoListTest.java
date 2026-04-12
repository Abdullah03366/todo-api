package example.demo.todo.domain;

import example.demo.todo.domain.exceptions.InvalidDescriptionException;
import example.demo.todo.domain.exceptions.InvalidTitleException;
import example.demo.todo.domain.todolist.Description;
import example.demo.todo.domain.todolist.Title;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TodoListTest {

    @Test
    void constructorInitializesFieldsAndEmptyTodos() throws InvalidDescriptionException, InvalidTitleException {
        TodoList todoList = new TodoList("Work", "Work related todos");

        assertNotNull(todoList.getId());
        assertEquals("Work", todoList.getTitle().getTitle());
        assertEquals("Work related todos", todoList.getDescription().getDescription());
        assertNotNull(todoList.getTodos());
        assertTrue(todoList.getTodos().isEmpty());
    }

    @Test
    void addAndRemoveTodoWorks() throws Exception {
        TodoList todoList = new TodoList("Work", "Work related todos");
        Todo todo = new Todo("Task", "Description", Priority.HIGH);

        assertTrue(todoList.addTodo(todo));
        assertEquals(1, todoList.getTodos().size());
        assertTrue(todoList.removeTodo(todo));
        assertTrue(todoList.getTodos().isEmpty());
    }

    @Test
    void removeTodoReturnsFalseWhenTodoNotInList() throws Exception {
        TodoList todoList = new TodoList("Work", "Work related todos");
        Todo todo = new Todo("Task", "Description", Priority.HIGH);

        assertFalse(todoList.removeTodo(todo));
    }

    @Test
    void createAndAddTodoAddsTodoToCollection() throws Exception {
        TodoList todoList = new TodoList("Work", "Work related todos");

        Todo todo = todoList.createAndAddTodo("Task", "Description", Priority.MEDIUM);

        assertEquals(1, todoList.getTodos().size());
        assertSame(todo, todoList.getTodos().getFirst());
        assertEquals("Task", todo.getTitle().getTitle());
        assertEquals("MEDIUM", todo.getPriority());
    }

    @Test
    void setTitleAndDescriptionUpdatesFields() throws Exception {
        TodoList todoList = new TodoList("Work", "Work related todos");

        todoList.setTitle(new Title("Home"));
        todoList.setDescription(new Description("Home tasks"));

        assertEquals("Home", todoList.getTitle().getTitle());
        assertEquals("Home tasks", todoList.getDescription().getDescription());
    }

    @Test
    void toStringContainsKeyFields() throws Exception {
        TodoList todoList = new TodoList("Work", "Work related todos");

        String value = todoList.toString();

        assertTrue(value.contains("TodoList{"));
        assertTrue(value.contains("title="));
        assertTrue(value.contains("description="));
        assertTrue(value.contains("todos="));
    }
}

