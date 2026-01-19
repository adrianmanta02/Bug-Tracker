package main.commands.commentcommands;

import main.commands.CommandValidationStrategy;
import main.users.User;

public final class AddCommentValidationFactory {
    private AddCommentValidationFactory() { }

    /**
     * Creates a validation strategy used for {@link AddCommentCommand}.
     * The command has different approaches when it comes to being validated,
     * that given to User's role difference.
     * @param user the user for whom the validation strategy is to be created
     * @return a {@link CommandValidationStrategy} suitable for the user's role,
     * or {@code null} if the role does not support the command itself.
     */
    public static CommandValidationStrategy<AddCommentCommand> create(final User user) {
        return switch (user.getRole()) {
            case DEVELOPER -> new DeveloperAddCommentValidation();
            case REPORTER -> new ReporterAddCommentValidation();
            case MANAGER -> null;
        };
    }
}
