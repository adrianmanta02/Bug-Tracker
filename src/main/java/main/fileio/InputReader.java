package main.fileio;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.users.Developer;
import main.users.Manager;
import main.users.Reporter;
import main.users.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class InputReader {
    private final ObjectMapper objectMapper;

    public InputReader() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    /**
     * Reads a JSON file containing a collection of commands and returns them as a list
     * of JSON nodes.
     * The file located at the given path is parsed using Jackson into a root
     * {@link com.fasterxml.jackson.databind.JsonNode}. Each direct child node of the
     * root element is then collected into a list.
     * This method expects the root JSON element to be iterable (for example, a JSON
     * array). If the structure does not match this expectation, the returned list
     * may be empty or incomplete.
     * @param filePath the path to the JSON file containing the commands
     * @return a list of {@link JsonNode} representing the commands in the file
     * @throws IOException if the file cannot be read or the JSON content cannot be parsed
     */
    public List<JsonNode> readCommands(final String filePath) throws IOException {
        JsonNode root = objectMapper.readTree(new File(filePath));
        List<JsonNode> commands = new ArrayList<>();
        root.forEach(commands::add);
        return commands;
    }

    /**
     * Deserializes the content retrieved from a file path and builds
     * a list of User instances for each JsonNode read from the file.
     * @param filePath the path for the file containing user information
     * @return list of Users created
     * @throws IOException if parameter does not match User types.
     */
    public List<User> readUsers(final String filePath) throws IOException {
        JsonNode root = objectMapper.readTree(new File(filePath));
        List<User> users = new ArrayList<>();

        for (JsonNode userNode : root) {
            String role = userNode.get("role").asText();
            User user = switch (role.toLowerCase()) {
                case "reporter" -> objectMapper.treeToValue(userNode, Reporter.class);
                case "developer" -> objectMapper.treeToValue(userNode, Developer.class);
                case "manager" -> objectMapper.treeToValue(userNode, Manager.class);
                default -> throw new IllegalArgumentException("Unknown user role: " + role);
            };

            users.add(user);
        }

        return users;
    }
}
