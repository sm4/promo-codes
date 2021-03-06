package com.czequered.promocodes.service;

import com.czequered.promocodes.model.Code;
import com.czequered.promocodes.repository.CodeRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Martin Varga
 */
public class CodeServiceImplTest {

    private CodeService service;

    private CodeRepository codeRepository;

    @Before
    public void before() {
        codeRepository = mock(CodeRepository.class);
        service = new CodeServiceImpl(codeRepository);
    }

    @Test
    public void getCodes() throws Exception {
        Code code = new Code();
        code.setGameId("auticko");
        code.setCodeId("PUB1");
        when(codeRepository.findByGameId(eq("auticko"))).thenReturn(Collections.singletonList(code));
        List<Code> codes = service.getCodes("auticko");
        assertThat(codes).containsExactly(code);
    }

    @Test
    public void getCode() {
        Code code = new Code();
        code.setGameId("auticko");
        code.setCodeId("PUB1");
        code.setPayload("Hello");
        when(codeRepository.findByGameIdAndCodeId(eq("auticko"), eq("PUB1"))).thenReturn(code);
        Code retrieved = service.getCode("auticko", "PUB1");
        assertThat(retrieved.getPayload()).isEqualTo("Hello");
    }

    @Test
    public void deleteCode() throws Exception {
        service.deleteCode("auticko", "PUB1");
        Code toDelete = new Code("auticko", "PUB1");
        verify(codeRepository).delete(eq(toDelete));
        verifyNoMoreInteractions(codeRepository);
    }

    @Test
    public void saveCode() throws Exception {
        Code code = new Code();
        code.setGameId("auticko");
        code.setCodeId("PUB1");
        service.saveCode(code);
        verify(codeRepository).save(eq(code));
        verifyNoMoreInteractions(codeRepository);
    }
}