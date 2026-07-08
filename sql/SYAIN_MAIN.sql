drop table SYAIN_MAIN;
CREATE TABLE SYAIN_MAIN (
    SYAIN_ID SERIAL PRIMARY KEY,
    FIRST_NAME_KANJI VARCHAR(15) NOT NULL,
    LAST_NAME_KANJI VARCHAR(15) NOT NULL,
    FIRST_NAME_KANA VARCHAR(15) NOT NULL,
    LAST_NAME_KANA VARCHAR(15) NOT NULL,
    FIRST_NAME_EIGO VARCHAR(15) NOT NULL,
    LAST_NAME_EIGO VARCHAR(15) NOT NULL,
    SEIBETU INTEGER,
    TANJYOBI DATE,
    KOKUSEKI INTEGER,
    SYUSSINN VARCHAR(30),
    HAIGUSYA INTEGER,
    PASSPORT_NUM VARCHAR(20),
    PASSPORT_END_DATE DATE,
    VISA_KIKAN INTEGER,
    VISA_END_DATE DATE,
    ZAIRYU_SIKAKU INTEGER,
    KOJIN_NUM VARCHAR(20),
    ZAIRYU_NUM VARCHAR(20),
    SYOZOKU_KAISYA INTEGER NOT NULL,
    NYUUSYA_DATE DATE,
    TAISYA_DATE DATE,
    SYOKUGYO_KIND INTEGER NOT NULL,
    RAINITI_DATE DATE,
    BIKOU VARCHAR(255),
    YUUBIN CHAR(8),
    JYUSYO_1 VARCHAR(100),
    JYUSYO_2 VARCHAR(100),
    MOYORI_EKI VARCHAR(30),
    TEL VARCHAR(15),
    EMAIL VARCHAR(50),
    WECHAT VARCHAR(30),
    LINE VARCHAR(30),
    BOKOKU_JYUSYO VARCHAR(255),
    BOKOKU_KINNKYUU_RENNRAKU VARCHAR(255),
    SAISYUU_GAKUREKI INTEGER,
    GAKKOU_NAME VARCHAR(100),
    SENNMOM_NAME VARCHAR(100),
    SOTUGYO_DATE DATE,
    GYUMU_NENSU REAL,
    IT_OS VARCHAR(100),
    IT_GENGO VARCHAR(100),
    IT_DB VARCHAR(100),
    IT_WEB_SERVER VARCHAR(100),
    IT_FW VARCHAR(100),
    IT_OTHER VARCHAR(100),
    IT_BIKOU VARCHAR(1024),
    DELETE_FLAG INTEGER NOT NULL,
    TOUROKUBI TIMESTAMP,
    KOUSINNBI TIMESTAMP
);

INSERT INTO syain_main (
    first_name_kanji,
    last_name_kanji,
    first_name_kana,
    last_name_kana,
    first_name_eigo,
    last_name_eigo,
    seibetu,
    syozoku_kaisya,
    nyuusya_date,
    taisya_date,
    syokugyo_kind,
    delete_flag,
    tourokubi,
    kousinnbi
) VALUES
('一輝', '星川', 'イッキ', 'ホシカワ', 'Ikki', 'Hoshikawa', 1, 1, '2017-06-11', NULL, 4, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('艶', '呉', 'エン', 'ゴ', 'En', 'Go', 2, 1, '2018-04-01', NULL, 3, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('日那', '道', 'ヒナ', 'ドウ', 'Hina', 'Dou', 2, 1, '2019-04-01', NULL, 4, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('玲珠', 'ボウ', 'レイジュ', 'ボウ', 'Reiju', 'Bou', 2, 2, '2020-04-01', NULL, 4, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('エギ', '王', 'エギ', 'オウ', 'Egi', 'Ou', 1, 2, '2021-04-01', NULL, 2, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('安琦', '何', 'アンキ', 'カ', 'Anki', 'Ka', 2, 1, '2021-10-01', NULL, 4, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('暢', 'タク', 'チョウ', 'タク', 'Chou', 'Taku', 1, 2, '2022-04-01', '2024-03-31', 3, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


INSERT INTO syain_main (
    first_name_kanji,
    last_name_kanji,
    first_name_kana,
    last_name_kana,
    first_name_eigo,
    last_name_eigo,
    seibetu,
    syozoku_kaisya,
    nyuusya_date,
    taisya_date,
    syokugyo_kind,
    delete_flag,
    tourokubi,
    kousinnbi
) VALUES
('テスト','削除','テスト','サクジョ','Test','Delete',1,1,'2024-04-01',NULL,4,0,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

SELECT syain_id,
       last_name_kanji,
       first_name_kanji,
       delete_flag
FROM syain_main
WHERE last_name_kanji = '削除'
  AND first_name_kanji = 'テスト';
  
UPDATE syain_main
SET delete_flag = 0
WHERE syain_id = 9;

SELECT syain_id, last_name_kanji, first_name_kanji, it_os
FROM syain_main
ORDER BY syain_id DESC;

ALTER TABLE syain_main
ADD COLUMN kinyukikan_code VARCHAR(10),
ADD COLUMN kinyukikan_name VARCHAR(50),
ADD COLUMN siten_code VARCHAR(10),
ADD COLUMN siten_name VARCHAR(50),
ADD COLUMN kouza_num VARCHAR(10),
ADD COLUMN meigi_name VARCHAR(50);
