-- xhs.cate_seq definition

CREATE TABLE `cate_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- xhs.item_seq definition

CREATE TABLE `item_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- xhs.long_term_common_note_vo definition

CREATE TABLE `long_term_common_note_vo` (
  `id` bigint NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `end_publish_time` date DEFAULT NULL,
  `long_term_find_page_percent` double NOT NULL,
  `long_term_follow_page_percent` double NOT NULL,
  `long_term_kol_home_page_percent` double NOT NULL,
  `long_term_other_page_percent` double NOT NULL,
  `long_term_read_beyond_rate` double NOT NULL,
  `long_term_read_num` int NOT NULL,
  `long_term_search_page_percent` double NOT NULL,
  `note_number` int NOT NULL,
  `recent_find_page_percent` double NOT NULL,
  `recent_follow_page_percent` double NOT NULL,
  `recent_kol_home_page_percent` double NOT NULL,
  `recent_other_page_percent` double NOT NULL,
  `recent_read_beyond_rate` double NOT NULL,
  `recent_read_num` int NOT NULL,
  `recent_search_page_percent` double NOT NULL,
  `start_publish_time` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- xhs.long_term_common_note_vo_seq definition

CREATE TABLE `long_term_common_note_vo_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- xhs.note_type_seq definition

CREATE TABLE `note_type_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- xhs.notes_rate_seq definition

CREATE TABLE `notes_rate_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- xhs.page_percent_vo definition

CREATE TABLE `page_percent_vo` (
  `id` bigint NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `imp_detail_percent` double NOT NULL,
  `imp_follow_percent` double NOT NULL,
  `imp_homefeed_percent` double NOT NULL,
  `imp_nearby_percent` double NOT NULL,
  `imp_other_percent` double NOT NULL,
  `imp_search_percent` double NOT NULL,
  `read_detail_percent` double NOT NULL,
  `read_follow_percent` double NOT NULL,
  `read_homefeed_percent` double NOT NULL,
  `read_nearby_percent` double NOT NULL,
  `read_other_percent` double NOT NULL,
  `read_search_percent` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- xhs.page_percent_vo_seq definition

CREATE TABLE `page_percent_vo_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- xhs.sale_seq definition

CREATE TABLE `sale_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- xhs.schedule definition

CREATE TABLE `schedule` (
  `id` bigint NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `out_item_id` varchar(255) DEFAULT NULL,
  `status` enum('RUNNING','PENDING','FAILED') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- xhs.schedule_seq definition

CREATE TABLE `schedule_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- xhs.shop definition

CREATE TABLE `shop` (
  `id` bigint NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `out_shop_id` varchar(255) DEFAULT NULL,
  `shop_name` varchar(255) DEFAULT NULL,
  `shop_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_dbuj4gxsnwk7olud01asv7om4` (`out_shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- xhs.shop_seq definition

CREATE TABLE `shop_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- xhs.users definition

CREATE TABLE `users` (
  `id` bigint NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- xhs.users_seq definition

CREATE TABLE `users_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- xhs.cate definition

CREATE TABLE `cate` (
  `id` bigint NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `cate_name` varchar(255) DEFAULT NULL,
  `out_cate_id` int DEFAULT NULL,
  `parent_id` bigint DEFAULT NULL,
  `out_shop_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe50c91603kxpa0t733agcwy18` (`parent_id`),
  KEY `FKcwd9leqiw897bgqbt7k2mkos2` (`out_shop_id`),
  CONSTRAINT `FKcwd9leqiw897bgqbt7k2mkos2` FOREIGN KEY (`out_shop_id`) REFERENCES `shop` (`out_shop_id`),
  CONSTRAINT `FKe50c91603kxpa0t733agcwy18` FOREIGN KEY (`parent_id`) REFERENCES `cate` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- xhs.item definition

CREATE TABLE `item` (
  `id` bigint NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `out_item_id` varchar(30) DEFAULT NULL,
  `pic` varchar(255) DEFAULT NULL,
  `published_at` datetime(6) DEFAULT NULL,
  `title` varchar(60) DEFAULT NULL,
  `out_cate_id` int DEFAULT NULL,
  `out_shop_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_iyw3ssatgf7veksdna5xw2thm` (`out_item_id`),
  KEY `FKrkbfne50lxh4v9j0df0kk87m` (`out_shop_id`),
  CONSTRAINT `FKrkbfne50lxh4v9j0df0kk87m` FOREIGN KEY (`out_shop_id`) REFERENCES `shop` (`out_shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- to fix: Error Code : 1822

CREATE INDEX idx_cate_out_cate_id ON xhs.cate (out_cate_id);

ALTER TABLE xhs.item
ADD CONSTRAINT `FK_item_to_cate`
FOREIGN KEY (`out_cate_id`)
REFERENCES xhs.cate (`out_cate_id`);

-- xhs.notes_rate definition

CREATE TABLE `notes_rate` (
  `id` bigint NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `capture_date` date DEFAULT NULL,
  `collect_median` int NOT NULL,
  `comment_median` int NOT NULL,
  `hundred_like_percent` double NOT NULL,
  `imp_median` int NOT NULL,
  `imp_median_beyond_rate` double NOT NULL,
  `interaction_beyond_rate` double NOT NULL,
  `interaction_median` int NOT NULL,
  `interaction_rate` double NOT NULL,
  `like_median` int NOT NULL,
  `m_engagement_num` int NOT NULL,
  `m_follow_cnt` int NOT NULL,
  `note_number` int NOT NULL,
  `picture3s_view_rate` double NOT NULL,
  `read_median` int NOT NULL,
  `read_median_beyond_rate` double NOT NULL,
  `share_median` int NOT NULL,
  `thousand_like_percent` double NOT NULL,
  `type` enum('L30','L90') DEFAULT NULL,
  `unique_notes_rate_id` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `video_full_view_beyond_rate` double NOT NULL,
  `video_full_view_rate` double NOT NULL,
  `video_note_number` int NOT NULL,
  `long_term_common_note_vo_id` bigint DEFAULT NULL,
  `page_percent_vo_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_hat6tpn09i4xqa9lrybhgggkp` (`long_term_common_note_vo_id`),
  UNIQUE KEY `UK_dttfbc8om0mwdy14gciw10xoi` (`page_percent_vo_id`),
  CONSTRAINT `FKl1ppdagj0tyn6ypmmaq6w4pl3` FOREIGN KEY (`page_percent_vo_id`) REFERENCES `page_percent_vo` (`id`),
  CONSTRAINT `FKsdc1pmobda0ss1vmjwj8to624` FOREIGN KEY (`long_term_common_note_vo_id`) REFERENCES `long_term_common_note_vo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- xhs.sale definition

CREATE TABLE `sale` (
  `id` bigint NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `incremental_sell_count` int NOT NULL,
  `sale_date` datetime(6) DEFAULT NULL,
  `sell_count` int NOT NULL,
  `out_item_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKj48yccann2gvjl8tgemmf9la1` (`out_item_id`),
  CONSTRAINT `FKj48yccann2gvjl8tgemmf9la1` FOREIGN KEY (`out_item_id`) REFERENCES `item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- xhs.note_type definition

CREATE TABLE `note_type` (
  `id` bigint NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `content_tag` varchar(255) DEFAULT NULL,
  `percent` double NOT NULL,
  `notes_rate_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqik31l63ludjfun94iv3i105u` (`notes_rate_id`),
  CONSTRAINT `FKqik31l63ludjfun94iv3i105u` FOREIGN KEY (`notes_rate_id`) REFERENCES `notes_rate` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;