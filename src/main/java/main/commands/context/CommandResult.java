package main.commands.context;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
/*
  main.commands.context.CommandResult represents a template command followed by both errors and
  successful results obtained after running a command. It helps during the serialization process.
 */
public final class CommandResult {
    private final String command;
    private final String username;
    private final LocalDate timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String error;

    private final Map<String, Object> payload = new HashMap<>();

    public CommandResult(final CommandResultBuilder builder) {
        this.command = builder.command;
        this.username = builder.username;
        this.timestamp = builder.timestamp;
        this.error = builder.error;
        if (builder.payload != null) {
            this.payload.putAll(builder.payload);
        }
    }

    @JsonAnyGetter
    public Map<String, Object> getPayload() {
        return payload;
    }

    public static class CommandResultBuilder {

        private final String command;
        private final String username;
        private final LocalDate timestamp;
        private String error;
        private Map<String, Object> payload;

        public CommandResultBuilder(final String commandName,
                                    final String username,
                                    final LocalDate timestamp) {
            this.command = commandName;
            this.username = username;
            this.timestamp = timestamp;
        }

        /**
         * Adds optional field data to the {@code CommandResult} object
         * @param fieldName name of the field to be added for serialization
         * @param value the value corresponding to the {@code fieldName}
         * @return enhanced builder instance with field added.
         */
        public CommandResultBuilder data(final String fieldName, final Object value) {
            if (payload == null) {
                payload = new HashMap<>();
            }
            payload.put(fieldName, value);
            return this;
        }

        /**
         * Adds optional field error to the {@code CommandResult} object
         * @param errorName String field
         * @return enhanced builder instance with field added.
         */
        public CommandResultBuilder error(final String errorName) {
            this.error = errorName;
            return this;
        }

        /**
         * Generic method for Builder Pattern. Used after setting all optional parameters
         * and wanting to create the final object.
         * @return instance of object built
         */
        public CommandResult build() {
            return new CommandResult(this);
        }
    }
}
