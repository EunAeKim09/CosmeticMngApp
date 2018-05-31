  create table cosmeticCategory(
    _id integer primary key autoincrement,
    name not null,
    durationY default 0,
    durationM default 0,
    durationD default 0
  );

  insert into cosmeticCategory (name,durationY) values ('스킨/토너/로션',1);
  insert into cosmeticCategory (name,durationM) values ('에센스/세럼/아이크림',6);
  insert into cosmeticCategory (name,durationY) values ('크림',1);
  insert into cosmeticCategory (name,durationM) values ('메이크업베이스',6);
  insert into cosmeticCategory (name,durationY) values ('파운데이션',1);
  insert into cosmeticCategory (name,durationY) values ('스틱커버/컨실러',1);
  insert into cosmeticCategory (name,durationY) values ('파우더/팩트',1);
  insert into cosmeticCategory (name,durationM) values ('아이섀도/블러셔',6);
  insert into cosmeticCategory (name,durationY) values ('립스틱/립글로스/틴트',1);
  insert into cosmeticCategory (name,durationY) values ('아이라이너',1);
  insert into cosmeticCategory (name,durationM) values ('마스카라',6);
  insert into cosmeticCategory (name,durationY) values ('클렌징',1);
  insert into cosmeticCategory (name,durationM) values ('자외선 차단제',6);
  insert into cosmeticCategory (name,durationY) values ('네일/애나멜',2);
  insert into cosmeticCategory (name,durationY) values ('향수',3);
  insert into cosmeticCategory (name,durationY) values ('팩',1);
  insert into cosmeticCategory (name,durationY) values ('염색약',1);


  create table cosmeticToolsCategory(
    _id integer primary key autoincrement,
     name not null,
     durationY default 0,
     durationM default 0,
     durationD default 0
  );

  insert into cosmeticToolsCategory (name,durationD) values ('퍼프/스펀지',7');
  insert into cosmeticToolsCategory (name,durationD) values ('립 브러쉬(립용)',7);
  insert into cosmeticToolsCategory (name,durationD) values ('파운데이션 브러쉬',7);
  insert into cosmeticToolsCategory (name,durationD) values ('컨실러 브러쉬',7);
  insert into cosmeticToolsCategory (name,durationD) values ('파우더 브러쉬',21);
  insert into cosmeticToolsCategory (name,durationM) values ('뷰러',3);


   create table lensCategory(
      _id integer primary key autoincrement,
       name not null,
       durationY default 0,
       durationM default 0,
       durationD default 0
   );

   insert into lensCategory(name,durationM) values ('1개월 착용 렌즈',1);
   insert into lensCategory(name,durationM) values ('3개월 착용 렌즈',3);
   insert into lensCategory(name,durationM) values ('6개월 착용 렌즈',6);
   insert into lensCategory(name,durationY) values ('1년 착용 렌즈',1);
   insert into lensCategory(name,durationM) values ('렌즈 보존액',1);


   create table userCosmetic(
        _id integer primary key autoincrement,
        img,
        cate_id,
        name not null,
        openDate,
        endDate,
        memo,
        favorite default 0,
        FOREIGN KEY (cate_id) REFERENCES cosmeticCategory(_id)
   );






    create table userCosmeticTools (
        _id integer primary key autoincrement,
        img,
        cate_id,
        name not null,
        openDate,
        endDate,
        memo,
        favorite default 0,
        FOREIGN KEY (cate_id) REFERENCES cosmeticToolsCategory(_id)
    );

     create table userLens (
            _id integer primary key autoincrement,
            img,
            cate_id,
            name not null,
            openDate,
            endDate,
            memo,
            favorite default 0,
            FOREIGN KEY (cate_id) REFERENCES lensCategory(_id)
     );


