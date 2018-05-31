  create table cosmeticCategory(
    _id integer primary key autoincrement,
    name not null,
    duration
  );

  insert into cosmeticCategory (name,duration) values ('스킨/토너/로션','1년');
  insert into cosmeticCategory (name,duration) values ('에센스/세럼/아이크림','6개월');
  insert into cosmeticCategory (name,duration) values ('크림','1년');
  insert into cosmeticCategory (name,duration) values ('메이크업베이스','6개월');
  insert into cosmeticCategory (name,duration) values ('파운데이션','1년');
  insert into cosmeticCategory (name,duration) values ('스틱커버/컨실러','1년');
  insert into cosmeticCategory (name,duration) values ('파우더/팩트','1년');
  insert into cosmeticCategory (name,duration) values ('아이섀도/블러셔','6개월');
  insert into cosmeticCategory (name,duration) values ('립스틱/립글로스/틴트','1년');
  insert into cosmeticCategory (name,duration) values ('아이라이너','1년');
  insert into cosmeticCategory (name,duration) values ('마스카라','6개월');
  insert into cosmeticCategory (name,duration) values ('클렌징','1년');
  insert into cosmeticCategory (name,duration) values ('자외선 차단제','6개월');
  insert into cosmeticCategory (name,duration) values ('네일/애나멜','2년');
  insert into cosmeticCategory (name,duration) values ('향수','3년');
  insert into cosmeticCategory (name,duration) values ('팩','1년');
  insert into cosmeticCategory (name,duration) values ('염색약','1년');


  create table cosmeticToolsCategory(
    _id integer primary key autoincrement,
     name not null,
     duration
  );

  insert into cosmeticToolsCategory (name,duration) values ('퍼프/스펀지','7일');
  insert into cosmeticToolsCategory (name,duration) values ('립 브러쉬(립용)','7일');
  insert into cosmeticToolsCategory (name,duration) values ('파운데이션 브러쉬','7일');
  insert into cosmeticToolsCategory (name,duration) values ('컨실러 브러쉬','7일');
  insert into cosmeticToolsCategory (name,duration) values ('파우더 브러쉬','21일');
  insert into cosmeticToolsCategory (name,duration) values ('뷰러','3개월');


   create table lensCategory(
      _id integer primary key autoincrement,
       name not null,
       duration
   );

   insert into lensCategory(name,duration) values ('1개월 착용 렌즈','1개월');
   insert into lensCategory(name,duration) values ('3개월 착용 렌즈','3개월');
   insert into lensCategory(name,duration) values ('6개월 착용 렌즈','6개월');
   insert into lensCategory(name,duration) values ('1년 착용 렌즈','1년');
   insert into lensCategory(name,duration) values ('렌즈 보존액','1개월');


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


