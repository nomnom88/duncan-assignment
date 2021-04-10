package nl.sourcelabs.interview.duncan.assignment.api.errorhandling;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Setter;

@RestController
@Setter
public class TestThrowableRestController {

    private Throwable throwable = new Throwable();

    @GetMapping("/")
    void throwThrowable() throws Throwable {
        throw throwable;
    }

}
