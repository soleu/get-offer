TRUNCATE TABLE users;
TRUNCATE TABLE products;
TRUNCATE TABLE bids;
TRUNCATE TABLE auction_results;
TRUNCATE TABLE chat_rooms;
TRUNCATE TABLE chat_messages;

INSERT INTO users (ID, NICKNAME, IMAGE, CREATED_AT, UPDATED_AT)
VALUES (1, 'test', 'https://drive.google.com/file/d/1R9EIOoEWWgPUhY6e-t4VFuqMgknl7rm8/view?usp=sharing',
        '2024-01-01 00:00:00', '2024-01-01 00:00:00');
INSERT INTO users (ID, NICKNAME, IMAGE, CREATED_AT, UPDATED_AT)
VALUES (2, 'test2', 'https://drive.google.com/file/d/1R9EIOoEWWgPUhY6e-t4VFuqMgknl7rm8/view?usp=sharing',
        '2024-01-01 00:00:00', '2024-01-01 00:00:00');
INSERT INTO users (ID, NICKNAME, IMAGE, CREATED_AT, UPDATED_AT)
VALUES (3, 'test3', 'https://drive.google.com/file/d/1R9EIOoEWWgPUhY6e-t4VFuqMgknl7rm8/view?usp=sharing',
        '2024-01-01 00:00:00', '2024-01-01 00:00:00');

INSERT INTO products (ID, WRITER_ID, TITLE, DESCRIPTION, CATEGORY, CURRENT_PRICE, START_PRICE, STATUS, START_DATE,
                      END_DATE, CREATED_AT, UPDATED_AT, IMAGES)
VALUES (1, 1, 'nintendo', '닌텐도 새 제품', 'GAMES', 10000, 5000, 'IN_PROGRESS', '2024-01-02 00:00:00', '2024-01-04 00:00:00',
        '2024-01-02 00:00:00', '2024-01-02 00:00:00',
        '{"images":["https://picsum.photos/200/300","https://picsum.photos/200/300"]}');
INSERT INTO products (ID, WRITER_ID, TITLE, DESCRIPTION, CATEGORY, CURRENT_PRICE, START_PRICE, STATUS, START_DATE,
                      END_DATE, CREATED_AT, UPDATED_AT, IMAGES)
VALUES (2, 1, 'gucci belt', '어쩌구', 'CLOTHES', 230000, 50000, 'WAIT', '2024-01-03 00:00:00', '2024-01-04 00:00:00',
        '2024-01-02 00:00:00', '2024-01-02 00:00:00',
        '{"images":["https://picsum.photos/200/300","https://picsum.photos/200/300"]}');
INSERT INTO products (ID, WRITER_ID, TITLE, DESCRIPTION, CATEGORY, CURRENT_PRICE, START_PRICE, STATUS, START_DATE,
                      END_DATE, CREATED_AT, UPDATED_AT, IMAGES)
VALUES (3, 1, 'ikea chair', '저쩌', 'FURNITURE', 43000, 8000, 'COMPLETED', '2024-01-06 00:00:00', '2024-01-04 00:00:00',
        '2024-01-02 00:00:00', '2024-01-02 00:00:00',
        '{"images":["https://picsum.photos/200/300","https://picsum.photos/200/300"]}');
INSERT INTO products (ID, WRITER_ID, TITLE, DESCRIPTION, CATEGORY, CURRENT_PRICE, START_PRICE, STATUS, START_DATE,
                      END_DATE, CREATED_AT, UPDATED_AT, IMAGES)
VALUES (4, 2, 'nintendo', '닌텐도 새 제품', 'GAMES', 10000, 5000, 'IN_PROGRESS', '2024-01-02 00:00:00', '2024-01-04 00:00:00',
        '2024-01-02 00:00:00', '2024-01-02 00:00:00',
        '{"images":["https://picsum.photos/200/300","https://picsum.photos/200/300"]}');

ALTER TABLE products
    AUTO_INCREMENT = 5;

INSERT INTO bids (ID, PRODUCT_ID, BIDDER_ID, BID_PRICE, CREATED_AT, UPDATED_AT)
VALUES (1, 1, 2, 10000, '2024-01-06 00:00:00', '2024-01-04 00:00:00');
INSERT INTO bids (ID, PRODUCT_ID, BIDDER_ID, BID_PRICE, CREATED_AT, UPDATED_AT)
VALUES (2, 2, 2, 230000, '2024-01-06 00:00:00', '2024-01-04 00:00:00');
INSERT INTO bids (ID, PRODUCT_ID, BIDDER_ID, BID_PRICE, CREATED_AT, UPDATED_AT)
VALUES (3, 3, 2, 43000, '2024-01-06 00:00:00', '2024-01-04 00:00:00');
ALTER TABLE bids
    AUTO_INCREMENT = 4;

INSERT INTO auction_results (ID, PRODUCT_ID, BUYER_ID, FINAL_PRICE, AUCTION_STATUS, CREATED_AT, UPDATED_AT)
VALUES (1, 1, 2, 10000, 'COMPLETED', '2024-01-06 00:00:00', '2024-01-04 00:00:00');
ALTER TABLE auction_results
    AUTO_INCREMENT = 2;

INSERT INTO chat_rooms(ID, CREATED_AT, UPDATED_AT, PRODUCT_ID, REQUESTER_ID, SELLER_ID)
VALUES (1, '2024-11-27 21:34:28.123', '2024-11-27 21:34:28.567', 4, 1, 2);
INSERT INTO chat_messages(ID, CHAT_ROOM_ID, CONTENT, SENDER_ID, TIMESTAMP, TYPE)
VALUES (1, 1, '안녕하세요', 1, '2024-11-27 21:34:36', 'TEXT');
ALTER TABLE chat_rooms
    AUTO_INCREMENT = 2;