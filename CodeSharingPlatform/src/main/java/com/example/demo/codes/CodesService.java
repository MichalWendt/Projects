package com.example.demo.codes;

import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CodesService {

    private static final String DATE_FORMATTER = "yyyy/MM/dd HH:mm:ss";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);

    private final CodesRepository codesRepository;

    public CodesService(CodesRepository codesRepository) {
        this.codesRepository = codesRepository;
    }

    @Transactional
    public CodeResponseDto createCode(CodeRequestDto dto) {
        dto.setDate(LocalDateTime.now().format(formatter));
        Code code = codesRepository.save(new Code(dto.getCode(),dto.getDate(), dto.getTime(), dto.getViews()));
        return new CodeResponseDto(code);
    }

    @Transactional
    public List<CodeResponseDto> findAll() {
        return codesRepository.findAll().stream().map(CodeResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public CodeResponseDto findCodeByUuid(UUID uuid){
        Code code = codesRepository.findCodeByUuid(uuid);

        if(code == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "");
        }
        if (code.getViews() < 0L || code.getLifeTime() < 0L) {
            codesRepository.delete(code);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "");
        }

        if (code.getViews() <= 0L && code.getLifeTime() <= 0L) {
            return new CodeResponseDto(code);
        }
        if (code.getLifeTime() <= 0L) {
            if(code.getViews() == 1L) {
                codesRepository.delete(code);
                code.setViews(0L);
            }
            code.setViews(code.getViews() - 1L);
            return new CodeResponseDto(code);
        }
        if (code.getViews() <= 0L) {
            Long newTime = Duration.between(LocalDateTime.now().minusSeconds(code.getLifeTime()), LocalDateTime.parse(code.getDate(),formatter)).getSeconds();
            if(newTime <= 0L) {
                codesRepository.delete(code);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "");
            }
            code.setTime(newTime);
            return new CodeResponseDto(code);
        }

        Long newTime = Duration.between(LocalDateTime.now().minusSeconds(code.getLifeTime()), LocalDateTime.parse(code.getDate(),formatter)).getSeconds();

        if(newTime <= 0L) {
            codesRepository.delete(code);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "");
        }
        if(code.getViews() == 1L) {
            codesRepository.delete(code);
            code.setViews(0L);
        }
        code.setTime(newTime);
        code.setViews(code.getViews() - 1L);
        return new CodeResponseDto(code);
    }

    @Transactional
    public List<CodeResponseDto> findLast10Codes() {
        List<Code> code = codesRepository.findAllByTimeEqualsAndViewsEquals(0L,0L);
        return code.subList(Math.max(code.size() - 10, 0), code.size())
                .stream().map(CodeResponseDto::new).collect(Collectors.toList());
    }

}
