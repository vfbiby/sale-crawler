package com.muhuang.salecrawler.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muhuang.salecrawler.sale.Sale;
import com.muhuang.salecrawler.sale.SaleRepository;
import com.muhuang.salecrawler.sale.SaleService;
import com.muhuang.salecrawler.shop.Shop;
import com.muhuang.salecrawler.shop.ShopRepository;
import com.muhuang.salecrawler.taobao.TaobaoHttpClient;
import com.muhuang.salecrawler.taobao.TaobaoSaleMonthlyResult;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {

    public static final String TOKEN = "rqPZ2yJQgNp1SQuR";
    @Resource
    private ItemRepository itemRepository;

    @Resource
    private ShopRepository shopRepository;

    @Resource
    private TaobaoHttpClient taobaoHttpClient;

    @Resource
    private SaleService saleService;

    public Item save(Item item) {
        Shop inDB = shopRepository.findByOutShopId(item.getShop().getOutShopId());
        item.setShop(inDB);
        setPublishedAt(item);
        return itemRepository.save(item);
    }

    private static void setPublishedAt(Item item) {
        if (item.getPublishedAt() == null)
            item.setPublishedAt(new Date());
    }

    public void saveAll(List<Item> collect) {
        collect.stream().peek(ItemService::setPublishedAt).collect(Collectors.toList());
        itemRepository.saveAll(collect);
    }

    public Page<Item> getUsers(Sort.Direction direction, String sortBy) {
        PageRequest pageRequest = PageRequest.of(0, 5, direction, sortBy);
        return itemRepository.findAll(pageRequest);
    }

    /**
     * 收藏
     *
     * @param itemId 商品id
     */
    public void favorite(Long itemId) {
        Item item = itemRepository.getReferenceById(itemId);
        TaobaoSaleMonthlyResult saleMonthlyResult = taobaoHttpClient.getMonthlySaleNum(item.getOutItemId(), TOKEN);
        if (saleMonthlyResult.saleMonthlyNum() > 0) {
            saleService.save(saleMonthlyResult.saleMonthlyNum(), item);
        }
    }

    @Resource
    OneBoundService oneBoundService;

    public Integer getTotalSellCountByOneBound(String toFetchItemId) {
        String json = oneBoundService.getTaobaoDetail(toFetchItemId);
        return parseSellCount(json);
    }

    public Integer parseSellCount(String json) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            JsonNode apiStacks = jsonNode.get("item").get("apiStack");
            return apiStacks.get(0).get("value").get("item").get("vagueSellCount").asInt();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    @Resource
    SaleRepository saleRepository;

    public void saveSellCount(Integer totalSellCount, String itemId) {
        Item item = itemRepository.findByOutItemId(itemId);
        Sale sale = Sale.builder()
                .saleDate(new Date())
                .number(totalSellCount)
                .item(item)
                .build();
        saleRepository.save(sale);
    }

    public Integer getTotalSellCountBySelfDB(String itemId) {
        return saleRepository.findAll().stream().findFirst().get().getNumber();
    }


    public Sale getSale(String toFetchItemId, Date date) {
//        Sale sale = saleRepository.findAll().stream().findFirst().get();
//        return sale;
        List<Sale> sale = saleRepository.findAll(new Specification<Sale>() {
            @Override
            public Predicate toPredicate(Root<Sale> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();

                LocalDateTime startOfTheDay = LocalDateTime.of(LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault()), LocalTime.MIN);
                LocalDateTime startOfTheEnd = LocalDateTime.of(LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault()), LocalTime.MAX);
                ps.add(cb.between(root.get("saleDate"), startOfTheDay, startOfTheEnd));

                //联表查询，利用root的join方法，根据关联关系表里面的字段进行查询。
                ps.add(cb.in(root.join("item").get("outItemId")).value(toFetchItemId));

                return query.where(ps.toArray(new Predicate[ps.size()])).getRestriction();
            }
        });
        return sale.stream().findFirst().get();
    }

}


