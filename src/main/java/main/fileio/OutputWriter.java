package main.fileio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class OutputWriter {
    private final ObjectMapper objectMapper;

    public OutputWriter() {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Writes the given list of JSON output nodes to the specified file.
     * The provided list is serialized as JSON using the configured
     * {@link com.fasterxml.jackson.databind.ObjectMapper} and written
     * to the target file.
     * @param outputs the list of {@link ObjectNode} instances to be written
     *                to the output file
     * @param outputFile the file to which the JSON output will be written
     * @throws IOException if an I/O error occurs during writing
     */
    public void write(final List<ObjectNode> outputs,
                      final File outputFile) throws IOException {
        objectMapper.writeValue(outputFile, outputs);
    }
}
