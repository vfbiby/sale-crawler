package com.muhuang.salecrawler;

import com.muhuang.salecrawler.cate.Cate;
import com.muhuang.salecrawler.cate.CateChildrenDTO;
import com.muhuang.salecrawler.cate.CateDTO;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CateListSaveControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Nested
    class HappyPath {

        @Test
        void postCateList_whenListIsValid_receiveOK() {
            CateChildrenDTO cateChildrenDTO = CateChildrenDTO.builder().id(1781762418L).name("7月新品").build();
            CateDTO cateDTO = CateDTO.builder().id(1782071372L).name("07/03 夏季新品")
                    .url("/nggshow.taobao.com/category-1782071372.htm?search=y&parentCatId=1781762418&parentCatName=7%D4%C2%D0%C2%C6%B7&catName=07%2F03+%CF%C4%BC%BE%D0%C2%C6%B7#bd\"").build();
            cateChildrenDTO.setChildren(List.of(cateDTO));
            ResponseEntity<Object> response = testRestTemplate.postForEntity("/api/1.0/cates", cateChildrenDTO, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

    }

}
