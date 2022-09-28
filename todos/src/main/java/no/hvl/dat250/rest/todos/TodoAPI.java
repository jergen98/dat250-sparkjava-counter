package no.hvl.dat250.rest.todos;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;

import static spark.Spark.*;

/**
 * Rest-Endpoint.
 */
public class TodoAPI {

    // DB
    static Gson gson = new Gson();
    static HashMap<Long, Todo> todoList = new HashMap<Long, Todo>();

    public static void main(String[] args) {
        if (args.length > 0) {
            port(Integer.parseInt(args[0]));
        } else {
            port(8080);
        }

        after((request, response) -> response.type("application/json"));


        post("/todos", (request, response) -> {
            Todo todo = gson.fromJson(request.body(), Todo.class);
            final Long id = Long.valueOf(todoList.size());
            String summary = todo.getSummary();
            String description = todo.getDescription();
            if (todo.getId() == null) {
                todo = new Todo(id, summary, description);
            }
            todoList.put(id, todo);
            String jsonInString = gson.toJson(todo);
            return jsonInString;
        });

        get("/todos", (request, response) -> new Gson().toJson(todoList.values()));

        get("/todos/:id", (request, response) -> {
            final Long id;
            try {
                id = Long.parseLong(request.params("id"));
            } catch (NumberFormatException e) {
                return String.format("The id \"%s\" is not a number!", request.params("id"));
            }
            Todo todo = todoList.get(id);
            if (todo == null) {
                return String.format("Todo with the id  \"%s\" not found!", id);
            }
            String jsonInString = gson.toJson(todo);
            return jsonInString;
        });


        put("/todos/:id", (request, response) -> {
            final Long id;
            try {
                id = Long.parseLong(request.params("id"));
            } catch(NumberFormatException e){
                return String.format("The id \"%s\" is not a number!", request.params("id"));
            }
            Todo todo = todoList.get(id);
            if (todo == null) {
                return String.format("Todo with the id  \"%s\" not found!", id);
            }
            Todo putTodo = new Gson().fromJson(request.body(), Todo.class);
            String summary = putTodo.getSummary();
            String description = putTodo.getDescription();
            todo = new Todo(id, summary, description);
            todoList.put(id, todo);
            return null;

        });

        delete("/todos/:id", (request, response) -> {
            final Long id;
            try {
                id = Long.parseLong(request.params("id"));
            } catch (NumberFormatException e){
                return String.format("The id \"%s\" is not a number!", request.params("id"));
            }
            Todo todo = todoList.get(id);
            if (todo == null){
                return String.format("Todo with the id  \"%s\" not found!", id);
            }
            todoList.remove(id);
            return null;

        });

    }
}
