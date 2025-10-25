-- 권한 데이터 생성

INSERT INTO AUTHORITIES (
      CREATED_DATE
    , CREATED_ID
    , MODIFIED_DATE
    , MODIFIED_ID
    , AUTHORITY_CODE
    , AUTHORITY_NAME
    , DESCRIPTION
) VALUES (NOW(), 'System', NOW(), 'System', 'ROLE_ADMIN', '관리자', '관리자 권한')
       , (NOW(), 'System', NOW(), 'System', 'ROLE_MANAGER', '매니저', '관리자 대행 권한')
       , (NOW(), 'System', NOW(), 'System', 'ROLE_DB_ENGINEER', 'DB 엔지니어', 'DB 엔지니어 권한')
       , (NOW(), 'System', NOW(), 'System', 'ROLE_GUEST', '게스트', '초기 사용자 권한')
       ;
