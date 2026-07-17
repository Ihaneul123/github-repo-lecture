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


INSERT INTO tg_setting
(category1, category2, category3, value1, display_order, delete_flag)
VALUES
(3, 6, 1, 'DOS', 1, 0),
(3, 6, 2, 'Windows', 2, 0),
(3, 6, 3, 'Unix', 3, 0),
(3, 6, 4, 'Linux', 4, 0),
(3, 6, 5, 'android', 5, 0),
(3, 6, 6, 'IOS', 6, 0),
(3, 6, 7, 'AWS', 7, 0),
(3, 6, 8, 'SAP', 8, 0),
(3, 6, 9, 'Salesforce', 9, 0),
(3, 6, 10, 'Cosminexus', 10, 0),
(3, 6, 11, 'Docker', 11, 0),
(3, 6, 12, 'Kubernetes', 12, 0),
(3, 6, 13, 'Containerd', 13, 0);

--拡張性テスト
INSERT INTO tg_setting
(category1, category2, category3, value1, display_order, delete_flag)
VALUES
(1, 3, 1, '株式会社テスト会社', 3, 0);

INSERT INTO tg_setting
(category1, category2, category3, value1, display_order, delete_flag)
VALUES
(3, 4, 7, 'AIエンジニア', 7, 0);

SELECT *
FROM tg_setting
WHERE value1 = 'AIエンジニア';

DELETE FROM tg_setting
WHERE category1 = 1
  AND category2 = 3
  AND category3 = 1;

DELETE FROM tg_setting
WHERE category1 = 3
  AND category2 = 4
  AND category3 = 7;
  
 --OS拡張性テスト 
  INSERT INTO tg_setting
(category1, category2, category3, value1, display_order, delete_flag)
VALUES
(3, 6, 17, 'TestOS', 17, 0);

DELETE FROM tg_setting
WHERE category1 = 3
  AND category2 = 6
  AND category3 = 14;
