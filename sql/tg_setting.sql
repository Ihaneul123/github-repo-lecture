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
(category1, category2, category3, value1, value2, display_order, delete_flag)
VALUES
(20, 1, 1, '株式会社ブライトスター', '会社A', 1, 0),
(20, 1, 2, '株式会社トップクラウド', '会社B', 2, 0),
(23, 1, 1, '役員', '役員', 1, 0),
(23, 1, 2, '総務', '総務', 2, 0),
(23, 1, 3, 'IT営業', 'IT営業', 3, 0),
(23, 1, 4, 'ITエンジニア', 'ITエンジニア', 4, 0),
(23, 1, 5, '不動産スタッフ', '不動産スタッフ', 5, 0);