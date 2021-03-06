use mtdb
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL
GO

CREATE TABLE OBJ_INDEX_DBL
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL_INDEX1 ON OBJ_INDEX_DBL (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL_INDEX2 ON OBJ_INDEX_DBL (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__1
GO

CREATE TABLE OBJ_INDEX_DBL__1
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__1_INDEX1 ON OBJ_INDEX_DBL__1 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__1_INDEX2 ON OBJ_INDEX_DBL__1 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__2
GO

CREATE TABLE OBJ_INDEX_DBL__2
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__2_INDEX1 ON OBJ_INDEX_DBL__2 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__2_INDEX2 ON OBJ_INDEX_DBL__2 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__3
GO

CREATE TABLE OBJ_INDEX_DBL__3
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__3_INDEX1 ON OBJ_INDEX_DBL__3 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__3_INDEX2 ON OBJ_INDEX_DBL__3 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__4
GO

CREATE TABLE OBJ_INDEX_DBL__4
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__4_INDEX1 ON OBJ_INDEX_DBL__4 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__4_INDEX2 ON OBJ_INDEX_DBL__4 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__5
GO

CREATE TABLE OBJ_INDEX_DBL__5
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__5_INDEX1 ON OBJ_INDEX_DBL__5 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__5_INDEX2 ON OBJ_INDEX_DBL__5 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__6
GO

CREATE TABLE OBJ_INDEX_DBL__6
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__6_INDEX1 ON OBJ_INDEX_DBL__6 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__6_INDEX2 ON OBJ_INDEX_DBL__6 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__7
GO

CREATE TABLE OBJ_INDEX_DBL__7
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__7_INDEX1 ON OBJ_INDEX_DBL__7 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__7_INDEX2 ON OBJ_INDEX_DBL__7 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__8
GO

CREATE TABLE OBJ_INDEX_DBL__8
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__8_INDEX1 ON OBJ_INDEX_DBL__8 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__8_INDEX2 ON OBJ_INDEX_DBL__8 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__9
GO

CREATE TABLE OBJ_INDEX_DBL__9
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__9_INDEX1 ON OBJ_INDEX_DBL__9 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__9_INDEX2 ON OBJ_INDEX_DBL__9 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__10
GO

CREATE TABLE OBJ_INDEX_DBL__10
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__10_INDEX1 ON OBJ_INDEX_DBL__10 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__10_INDEX2 ON OBJ_INDEX_DBL__10 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__11
GO

CREATE TABLE OBJ_INDEX_DBL__11
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__11_INDEX1 ON OBJ_INDEX_DBL__11 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__11_INDEX2 ON OBJ_INDEX_DBL__11 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__12
GO

CREATE TABLE OBJ_INDEX_DBL__12
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__12_INDEX1 ON OBJ_INDEX_DBL__12 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__12_INDEX2 ON OBJ_INDEX_DBL__12 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__13
GO

CREATE TABLE OBJ_INDEX_DBL__13
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__13_INDEX1 ON OBJ_INDEX_DBL__13 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__13_INDEX2 ON OBJ_INDEX_DBL__13 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__14
GO

CREATE TABLE OBJ_INDEX_DBL__14
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__14_INDEX1 ON OBJ_INDEX_DBL__14 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__14_INDEX2 ON OBJ_INDEX_DBL__14 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__15
GO

CREATE TABLE OBJ_INDEX_DBL__15
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__15_INDEX1 ON OBJ_INDEX_DBL__15 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__15_INDEX2 ON OBJ_INDEX_DBL__15 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__16
GO

CREATE TABLE OBJ_INDEX_DBL__16
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__16_INDEX1 ON OBJ_INDEX_DBL__16 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__16_INDEX2 ON OBJ_INDEX_DBL__16 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__17
GO

CREATE TABLE OBJ_INDEX_DBL__17
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__17_INDEX1 ON OBJ_INDEX_DBL__17 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__17_INDEX2 ON OBJ_INDEX_DBL__17 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__18
GO

CREATE TABLE OBJ_INDEX_DBL__18
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__18_INDEX1 ON OBJ_INDEX_DBL__18 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__18_INDEX2 ON OBJ_INDEX_DBL__18 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__19
GO

CREATE TABLE OBJ_INDEX_DBL__19
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__19_INDEX1 ON OBJ_INDEX_DBL__19 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__19_INDEX2 ON OBJ_INDEX_DBL__19 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__20
GO

CREATE TABLE OBJ_INDEX_DBL__20
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__20_INDEX1 ON OBJ_INDEX_DBL__20 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__20_INDEX2 ON OBJ_INDEX_DBL__20 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__21
GO

CREATE TABLE OBJ_INDEX_DBL__21
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__21_INDEX1 ON OBJ_INDEX_DBL__21 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__21_INDEX2 ON OBJ_INDEX_DBL__21 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__22
GO

CREATE TABLE OBJ_INDEX_DBL__22
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__22_INDEX1 ON OBJ_INDEX_DBL__22 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__22_INDEX2 ON OBJ_INDEX_DBL__22 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__23
GO

CREATE TABLE OBJ_INDEX_DBL__23
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__23_INDEX1 ON OBJ_INDEX_DBL__23 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__23_INDEX2 ON OBJ_INDEX_DBL__23 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__24
GO

CREATE TABLE OBJ_INDEX_DBL__24
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__24_INDEX1 ON OBJ_INDEX_DBL__24 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__24_INDEX2 ON OBJ_INDEX_DBL__24 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__25
GO

CREATE TABLE OBJ_INDEX_DBL__25
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__25_INDEX1 ON OBJ_INDEX_DBL__25 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__25_INDEX2 ON OBJ_INDEX_DBL__25 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__26
GO

CREATE TABLE OBJ_INDEX_DBL__26
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__26_INDEX1 ON OBJ_INDEX_DBL__26 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__26_INDEX2 ON OBJ_INDEX_DBL__26 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__27
GO

CREATE TABLE OBJ_INDEX_DBL__27
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__27_INDEX1 ON OBJ_INDEX_DBL__27 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__27_INDEX2 ON OBJ_INDEX_DBL__27 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__28
GO

CREATE TABLE OBJ_INDEX_DBL__28
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__28_INDEX1 ON OBJ_INDEX_DBL__28 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__28_INDEX2 ON OBJ_INDEX_DBL__28 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__29
GO

CREATE TABLE OBJ_INDEX_DBL__29
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__29_INDEX1 ON OBJ_INDEX_DBL__29 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__29_INDEX2 ON OBJ_INDEX_DBL__29 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__30
GO

CREATE TABLE OBJ_INDEX_DBL__30
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__30_INDEX1 ON OBJ_INDEX_DBL__30 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__30_INDEX2 ON OBJ_INDEX_DBL__30 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO

/* drop/create OBJ_INDEX_DBL */
DROP TABLE OBJ_INDEX_DBL__31
GO

CREATE TABLE OBJ_INDEX_DBL__31
(
    TENANT_ID NUMERIC(7,0) NOT NULL,
    OBJ_DEF_ID VARCHAR(128) NOT NULL,
    COL_NAME VARCHAR(36) NOT NULL,
    OBJ_ID VARCHAR(64) NOT NULL,
    OBJ_VER NUMERIC(10,0) DEFAULT 0 NOT NULL,
    VAL FLOAT
)
GO

CREATE INDEX OBJ_INDEX_DBL__31_INDEX1 ON OBJ_INDEX_DBL__31 (TENANT_ID, OBJ_DEF_ID, COL_NAME, VAL)
GO
CREATE INDEX OBJ_INDEX_DBL__31_INDEX2 ON OBJ_INDEX_DBL__31 (TENANT_ID, OBJ_DEF_ID, OBJ_ID)
GO
