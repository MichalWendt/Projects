package com.example.demo.codes;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class CodesController {

    private final CodesService codesService;

    public CodesController(CodesService codesService) {
        this.codesService = codesService;
    }

    @GetMapping("/api/code/{uuid}") // return specific code by UUID in JSON format
    public CodeResponseNoIdDto returnJSONById(@PathVariable UUID uuid) {
        return new CodeResponseNoIdDto(codesService.findCodeByUuid(uuid));
    }

    @GetMapping("/code/{uuid}") // return specific code by UUID in HTML format
    public ModelAndView returnIdCode(@PathVariable UUID uuid, Model model) {
        CodeResponseDto codeResponseDto = codesService.findCodeByUuid(uuid);
        model.addAttribute("code", codeResponseDto);

        return new ModelAndView("code1", new HashMap<>());
    }

    @PostMapping("/api/code/new")   // creates new code
    public String createCode(@RequestBody CodeRequestDto newCode) {
        return "{ \"id\" : \"" + codesService.createCode(newCode).getUuid() + "\" }";
    }

    @GetMapping("/api/code/latest") // returns last 10 codes with no time and views restrictions in JSON format
    public List<CodeResponseNoIdDto> returnTenJSON() {
        List<CodeResponseNoIdDto> list = codesService.findLast10Codes().stream().map(CodeResponseNoIdDto::new).collect(Collectors.toList());
        Collections.reverse(list);
        return list;
    }

    @GetMapping("/code/latest") // returns last 10 codes with no time and views restrictions in HTML format
    public ModelAndView returnTenHTML(Model model) {

        List<CodeResponseDto> reversed = codesService.findLast10Codes();    // TODO: make better repository method
        Collections.reverse(reversed);
        model.addAttribute("codes", reversed);

        return new ModelAndView("latestCodes", new HashMap<>());
    }

    @GetMapping("/code/new")    // html form for creating codes (NOT IN USE)
    public ResponseEntity<String> returnCode() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type",
                "text/html");

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body("<!DOCTYPE>" +
                        "<html>\n" +
                        "<head>\n" +
                        "    <title>Create</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<textarea id=\"code_snippet\">" + "write your code here" + "</textarea> <br> Time restriction:" +
                        "<input id=\"time_restriction\" type=\"text\"/> <br>Views restriction:" +
                        "<input id=\"views_restriction\" type=\"text\"/><br>" +
                        "<button id=\"send_snippet\" type=\"submit\" onclick=\"send()\">Submit</button> <br>" +
                        "</pre>\n" +
                        "</body>\n" +
                        "</html>");
    }
}