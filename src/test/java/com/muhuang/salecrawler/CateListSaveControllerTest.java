package com.muhuang.salecrawler;

import com.muhuang.salecrawler.cate.CateChildrenDTO;
import com.muhuang.salecrawler.cate.CateDTO;
import com.muhuang.salecrawler.cate.CateRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
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

    public static final String API_1_0_CATES = "/api/1.0/cates";
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Resource
    private CateRepository cateRepository;

    @BeforeEach
    void cleanup() {
        cateRepository.deleteAll();
    }

    @Nested
    class HappyPath {

        @Test
        void postCateList_whenListIsValid_receiveOK() {
            CateChildrenDTO cateChildrenDTO = CateChildrenDTO.builder().id(1781762418).name("7月新品").build();
            CateDTO cateDTO = CateDTO.builder().id(1782071372).name("07/03 夏季新品")
                    .url("/nggshow.taobao.com/category-1782071372.htm?search=y").build();
            cateChildrenDTO.setChildren(List.of(cateDTO));
            List<CateChildrenDTO> cateList = List.of(cateChildrenDTO);
            ResponseEntity<Object> response = testRestTemplate.postForEntity(API_1_0_CATES, cateList, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        void postCateList_whenListIsValid_parentCateSaveToDatabase() {
            int parentCateId = 1781762418;
            CateChildrenDTO cateChildrenDTO = CateChildrenDTO.builder().id(parentCateId).name("7月新品").build();
            CateDTO cateDTO = CateDTO.builder().id(1782071372).name("07/03 夏季新品")
                    .url("/nggshow.taobao.com/category-1782071372.htm?search=y").build();
            cateChildrenDTO.setChildren(List.of(cateDTO));
            List<CateChildrenDTO> cateList = List.of(cateChildrenDTO);
            testRestTemplate.postForEntity(API_1_0_CATES, cateList, Object.class);
            assertThat(cateRepository.findByOutCateId(parentCateId)).isNotNull();
        }

        @Test
        void postCateList_whenListIsValid_cateSaveToDataBaseWithParent() {
            int parentCateId = 1781762418;
            CateChildrenDTO cateChildrenDTO = CateChildrenDTO.builder().id(parentCateId).name("7月新品").build();
            int childCateId = 1782071372;
            CateDTO cateDTO = CateDTO.builder().id(childCateId).name("07/03 夏季新品")
                    .url("/nggshow.taobao.com/category-1782071372.htm?search=y").build();
            cateChildrenDTO.setChildren(List.of(cateDTO));
            List<CateChildrenDTO> cateList = List.of(cateChildrenDTO);
            testRestTemplate.postForEntity(API_1_0_CATES, cateList, Object.class);
            assertThat(cateRepository.findByOutCateId(childCateId).getParent().getOutCateId()).isEqualTo(parentCateId);
        }

        @Test
        void postCateList_whenListHas2ChildCate_2cateSaveToDataBaseWithParent() {
            int parentCateId = 1781762418;
            CateChildrenDTO cateChildrenDTO = CateChildrenDTO.builder().id(parentCateId).name("7月新品").build();
            int childCateId = 1782071372;
            CateDTO cateDTO = CateDTO.builder().id(childCateId).name("07/03 夏季新品")
                    .url("/nggshow.taobao.com/category-1782071372.htm?search=y").build();
            CateDTO cateDTO2 = CateDTO.builder().id(childCateId + 1).name("07/05 夏季新品")
                    .url("/nggshow.taobao.com/category-1782071372.htm?search=y").build();
            cateChildrenDTO.setChildren(List.of(cateDTO, cateDTO2));
            List<CateChildrenDTO> cateList = List.of(cateChildrenDTO);
            testRestTemplate.postForEntity(API_1_0_CATES, cateList, Object.class);
            assertThat(cateRepository.findAll().size()).isEqualTo(3);
        }

        @Test
        void postCateList_whenListHas2ParentCate_2cateSaveToDatabase() {
            int parentCateId = 1781762418;
            CateChildrenDTO cateChildrenDTO = CateChildrenDTO.builder().id(parentCateId).name("7月新品").build();
            CateChildrenDTO cateChildrenDTO2 = CateChildrenDTO.builder().id(parentCateId + 1).name("8月新品").build();
            List<CateChildrenDTO> cateList = List.of(cateChildrenDTO, cateChildrenDTO2);
            testRestTemplate.postForEntity(API_1_0_CATES, cateList, Object.class);
            assertThat(cateRepository.findAll().size()).isEqualTo(2);
        }

    }

    @Nested
    class SadPath {

    }

}
