CREATE TABLE tg_setting (
    category1 INTEGER NOT NULL,
    category2 INTEGER NOT NULL,
    category3 INTEGER NOT NULL,
    value1 VARCHAR(255),
    value2 VARCHAR(255),
    value3 VARCHAR(255),
    display_order INTEGER,
    delete_flag INTEGER DEFAULT 0,
    PRIMARY KEY (category1, category2, category3)
);

INSERT INTO tg_setting
(category1, category2, category3, value1, display_order, delete_flag)
VALUES
(1, 1, 1, '株式会社ブライトスター', 1, 0),
(1, 2, 1, '株式会社トップクラウド', 2, 0);

INSERT INTO tg_setting
(category1, category2, category3, value1, display_order, delete_flag)
VALUES
(3, 1, 1, '男', 1, 0),
(3, 1, 2, '女', 2, 0);

INSERT INTO tg_setting
(category1, category2, category3, value1, display_order, delete_flag)
VALUES
(3, 4, 1, '役員', 1, 0),
(3, 4, 2, '総務', 2, 0),
(3, 4, 3, 'IT営業', 3, 0),
(3, 4, 4, 'ITエンジニア', 4, 0),
(3, 4, 5, '不動産スタッフ', 5, 0),
(3, 4, 6, '個人事業主', 6, 0);