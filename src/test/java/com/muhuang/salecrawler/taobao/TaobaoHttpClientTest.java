package com.muhuang.salecrawler.taobao;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author Frank An
 */
@SpringBootTest
public class TaobaoHttpClientTest {

    @Autowired
    private TaobaoHttpClient taobaoHttpClient;

    @Test
    @Disabled("调用第三方接口，没有逻辑，无需测试")
    void getTaobao_getMonthlySaleNum_isOk() {
        var result = taobaoHttpClient.getMonthlySaleNum("684393037672", "rqPZ2yJQgNp1SQuR");
        assertThat(result).isNotNull();
    }
}
