INSERT INTO USERS (ID, NICKNAME, IMAGE, CREATED_AT, UPDATED_AT)
VALUES (1, 'test', 'https://drive.google.com/file/d/1R9EIOoEWWgPUhY6e-t4VFuqMgknl7rm8/view?usp=sharing',
        '2024-01-01 00:00:00', '2024-01-01 00:00:00');
INSERT INTO USERS (ID, NICKNAME, IMAGE, CREATED_AT, UPDATED_AT)
VALUES (2, 'test2', 'https://drive.google.com/file/d/1R9EIOoEWWgPUhY6e-t4VFuqMgknl7rm8/view?usp=sharing',
        '2024-01-01 00:00:00', '2024-01-01 00:00:00');


INSERT INTO PRODUCTS (ID, WRITER_ID, NAME, DESCRIPTION, CATEGORY, CURRENT_PRICE, START_PRICE, STATUS, START_DATE,
                      END_DATE, CREATED_AT, UPDATED_AT, IMAGES)
VALUES (1, 1, 'nintendo', '닌텐도 새 제품', 'GAMES', 10000, 5000, 'IN_PROGRESS', '2024-01-02 00:00:00', '2024-01-04 00:00:00',
        '2024-01-02 00:00:00', '2024-01-02 00:00:00',
        '{"images":["https://picsum.photos/200/300","https://picsum.photos/200/300"]}');

