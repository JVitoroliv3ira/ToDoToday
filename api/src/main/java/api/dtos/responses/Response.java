package api.dtos.responses;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class Response<C> {
    private final C content;
    private final List<String> errors;
}
