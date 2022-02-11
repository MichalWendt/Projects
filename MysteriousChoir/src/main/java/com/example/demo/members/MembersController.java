package com.example.demo.members;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MembersController {

    private final MembersService membersService;

    public MembersController(MembersService membersService) {
        this.membersService = membersService;
    }

    @PostMapping
    public Long createMember(@Valid @RequestBody MemberRequestDto dto) {
        return membersService.createMember(dto).getId();
    }

    @GetMapping
    public List<MemberResponseDto> getMembers() {
        return membersService.findAll();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("{id}")
    public void updateMember(@PathVariable Long id, @Valid @RequestBody MemberRequestDto dto) {
        membersService.updateMember(id, dto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void deleteMember(@PathVariable Long id) {
        membersService.deleteMember(id);
    }
}
