package com.example.demo.members;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MembersServiceTest {

    private final MembersRepository membersRepository = Mockito.mock(MembersRepository.class);
    private final MembersService membersService = new MembersService(membersRepository);

    @Test
    @DisplayName("Error should be thrown when member can't be created")
    void createMember() {
        Mockito.when(membersRepository.save(Mockito.any())).thenAnswer(it -> {
            Member member = it.getArgument(0, Member.class);
            ReflectionTestUtils.setField(member,"id",1L);
            return member;
        });
        assertEquals(1L, membersService.createMember(new MemberRequestDto()).getId());
    }

    @Test
    @DisplayName("Error should be thrown when list is empty")
    void findAll() {
        Mockito.when(membersRepository.findByOrderByLastNameAscFirstNameAsc()).thenReturn(List.of(new Member()));
        assertNotNull(membersService.findAll());
    }

    @Test
    @DisplayName("Error should be thrown when member does not exist")
    void updateMember() {
        Mockito.when(membersRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> membersService.updateMember(1L, new MemberRequestDto()));
    }

    @Test
    @DisplayName("Error should be thrown when member does not exist")
    void deleteMember() {
        Mockito.when(membersRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> membersService.deleteMember(1L));
    }
}