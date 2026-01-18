package main.commands.advanced.search.filters.utils;

import lombok.Getter;
import lombok.Setter;
import main.users.User;

import java.time.LocalDate;

/**
 * main.commands.advanced.search.filters.utils.SearchContext is a class
 * containing additional data needed in search contexts, such as user
 * privileges and time at which the request is being made. Made to
 * better encapsulate the additional properties.
 */
@Getter
@Setter
public final class SearchContext {
    final User user;
    final LocalDate timestamp;

    public SearchContext(final User user,
                         final LocalDate timestamp) {
        this.user = user;
        this.timestamp = timestamp;
    }
}
