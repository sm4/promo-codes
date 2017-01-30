package com.czequered.promocodes.service;

import com.czequered.promocodes.model.Code;
import com.czequered.promocodes.model.CodeCompositeId;
import com.czequered.promocodes.repository.CodeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Martin Varga
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
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
        code.setGameId("test");
        code.setCodeId("PUB1");
        when(codeRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(code)));
        Page<Code> codes = service.getCodes(0);
        assertThat(codes).containsExactly(code);
    }

    @Test
    public void getCode() {
        Code code = new Code();
        code.setGameId("test");
        code.setCodeId("PUB1");
        code.setPayload("Hello");
        when(codeRepository.findByGameIdAndCodeId(eq("test"), eq("PUB1"))).thenReturn(code);
        Code retrieved = service.getCode("test", "PUB1");
        assertThat(retrieved.getPayload()).isEqualTo("Hello");
    }

    @Test
    public void deleteCode() throws Exception {
        service.deleteCode("test", "PUB1");
        CodeCompositeId toDelete = new CodeCompositeId("test", "PUB1");
        verify(codeRepository).delete(eq(toDelete));
        verifyNoMoreInteractions(codeRepository);
    }

    @Test
    public void saveCode() throws Exception {
        Code code = new Code();
        code.setGameId("test");
        code.setCodeId("PUB1");
        service.saveCode(code);
        verify(codeRepository).save(eq(code));
        verifyNoMoreInteractions(codeRepository);
    }

}