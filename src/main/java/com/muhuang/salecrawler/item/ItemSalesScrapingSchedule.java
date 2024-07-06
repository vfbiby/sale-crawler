package com.muhuang.salecrawler.item;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 商品销量抓取调度表
 *
 * @author Frank An
 */
@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ItemSalesScrapingSchedule {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "out_item_id", referencedColumnName = "outItemId", unique = true)
    private Item item;

    /**
     * 创建时间
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 最后一次抓取时间
     */
    @Column(name = "last_scraped_at")
    private LocalDateTime lastScrapedAt;

    /**
     * 抓取状态
     */
    @Enumerated(EnumType.STRING)
    @Convert(converter = ItemSalesScrapingStatusConvert.class)
    @Column(name = "status")
    private ItemSalesScrapingStatus status;


}
