package nl.sourcelabs.interview.duncan.assignment.api.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {

    private String message;

    @Builder
    public ErrorResponse(final String message) {
        this.message = message;
    }
}
