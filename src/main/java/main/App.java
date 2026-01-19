package main;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.commands.Command;
import main.commands.CommandFactory;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.fileio.InputReader;
import main.fileio.OutputWriter;
import main.milestone.MilestoneContainer;
import main.notification.NotificationManager;
import main.ticket.utils.HistoryManager;
import main.ticket.utils.TicketContainer;
import main.users.User;
import main.users.UserContainer;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * main.App represents the main application logic that processes input commands,
 * generates outputs, and writes them to a file
 */
public final class App {
    private App() {
    }

    private static final String INPUT_USERS_FIELD = "input/database/users.json";

    /**
     * Runs the application: reads commands from an input file,
     * processes them, generates results, and writes them to an output file
     *
     * @param inputPath path to the input file containing commands
     * @param outputPath path to the file where results should be written
     */
    public static void run(final String inputPath, final String outputPath) throws IOException {
        initializeContainers();
        List<ObjectNode> outputs = processCommands(inputPath);
        writeOutput(outputs, outputPath);
    }

    private static List<ObjectNode> processCommands(final String inputPath) throws IOException {
        InputReader inputReader = new InputReader();
        List<JsonNode> commandNodes = inputReader.readCommands(inputPath);
        LocalDate firstCommandTimestamp = LocalDate.parse(commandNodes
                .getFirst()
                .get("timestamp")
                .asText()
        );

        CommandContext commandContext = new CommandContext(
                TicketContainer.getInstance(),
                MilestoneContainer.getInstance(),
                HistoryManager.getInstance(),
                firstCommandTimestamp
        );

        List<ObjectNode> outputs = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        TicketContainer ticketContainer = TicketContainer.getInstance();
        MilestoneContainer milestoneContainer = MilestoneContainer.getInstance();

        for (JsonNode commandNode : commandNodes) {
            if (!commandContext.isAppRunning()) {
                break;
            }

            LocalDate currentTimestamp = LocalDate.parse(commandNode.get("timestamp").asText());
            commandContext.updateAllMilestones(currentTimestamp);

            Command command = CommandFactory.getInstance().createCommand(commandNode);
            CommandResult result = command.executeCommand(commandContext);

            if (result != null) {
                ObjectNode output = objectMapper.valueToTree(result);
                outputs.add(output);
            }

            commandContext.sendAutomaticNotifications(currentTimestamp);
        }

        commandContext.reset();
        return outputs;
    }

    private static void initializeContainers() throws IOException {
        TicketContainer.getInstance().reset();
        MilestoneContainer.getInstance().reset();
        UserContainer.getInstance().reset();
        NotificationManager.getInstance().reset();
        HistoryManager.getInstance().reset();

        InputReader inputReader = new InputReader();
        List<User> users = inputReader.readUsers(INPUT_USERS_FIELD);
        for (User user : users) {
            UserContainer.getInstance().add(user);
        }
    }

    private static void writeOutput(final List<ObjectNode> outputs,
                                    final String outputPath) throws IOException {
        OutputWriter outputWriter = new OutputWriter();
        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();
        outputWriter.write(outputs, outputFile);
    }
}
