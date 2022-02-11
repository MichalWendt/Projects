package com.example.demo.members;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MembersService {

    private final MembersRepository membersRepository;

    public MembersService(MembersRepository membersRepository) {
        this.membersRepository = membersRepository;
    }

    @Transactional
    public MemberResponseDto createMember(MemberRequestDto dto) {
        Member member = membersRepository.save(new Member(dto.getFirstName(), dto.getLastName(), dto.getPhoneNumber()));
        return new MemberResponseDto(member);
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDto> findAll() {
        return membersRepository.findByOrderByLastNameAscFirstNameAsc().stream()
                .map(MemberResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateMember(Long id, MemberRequestDto dto) {
        Member member = membersRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));
        member.setFirstName(dto.getFirstName())
                .setLastName(dto.getLastName())
                .setPhoneNumber(dto.getPhoneNumber());
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = membersRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));
        membersRepository.delete(member);
    }

}
