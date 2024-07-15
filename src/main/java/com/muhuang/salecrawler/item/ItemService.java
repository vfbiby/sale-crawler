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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
        Sale sale = Sale.builder()
                .saleDate(new Date())
                .number(totalSellCount)
//                .item(Item.builder().outItemId(itemId).build())
                .build();
        saleRepository.save(sale);
    }

    public Integer getTotalSellCountBySelfDB(String itemId) {
        return saleRepository.findAll().stream().findFirst().get().getNumber();
    }


}
