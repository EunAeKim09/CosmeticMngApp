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
  insert into cosmeticCategory (name,duration) values ('향수','2017-06-28');
  insert into cosmeticCategory (name,duration) values ('팩','1년');
  insert into cosmeticCategory (name,duration) values ('염색약','1년');


  create table cosmeticTools(
    _id integer primary key autoincrement,
     name not null,
     duration
  );

  insert into cosmeticTools (name,duration) values ('퍼프/스펀지','7일');
  insert into cosmeticTools (name,duration) values ('립 브러쉬(립용)','7일');
  insert into cosmeticTools (name,duration) values ('파운데이션 브러쉬','7일');
  insert into cosmeticTools (name,duration) values ('컨실러 브러쉬','7일');
  insert into cosmeticTools (name,duration) values ('파우더 브러쉬','21일');
  insert into cosmeticTools (name,duration) values ('뷰러','3개월');


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
        favorite,
        FOREIGN KEY (cate_id) REFERENCES cosmeticCategory(_id))
   );

   insert into userCosmetic (cate_id,name,openDate,endDate,memo,favorite) values ('아이라이너','키스미 아이라이너'.'2018-05-29','2019-05-28','키스미13900원',0);
   insert into userCosmetic (cate_id,name,openDate,endDate,memo,favorite) values ('마스카라','키스미 마스카라'.'2018-05-29','2018-11-28','키스미13900원',0);
   insert into userCosmetic (cate_id,name,openDate,endDate,memo,favorite) values ('크림','이니스프리 크림'.'2018-05-29','2019-05-28','이니스프리 18900원',0);
   insert into userCosmetic (cate_id,name,openDate,endDate,memo,favorite) values ('스킨/토너/로션','이니스프리 그린티 토너'.'2018-05-29','2019-05-28','그린티토너 17300원',1);
   insert into userCosmetic (cate_id,name,openDate,endDate,memo,favorite) values ('스킨/토너/로션','이니스프리 그린티 로션'.'2018-05-29','2019-05-28','그린티 로션 18900원',1);
   insert into userCosmetic (cate_id,name,openDate,endDate,memo,favorite) values ('립스틱/립글로스/틴트','토니모리 틴트'.'2018-05-29','2019-05-28','틴트 5900원',0);
   insert into userCosmetic (cate_id,name,openDate,endDate,memo,favorite) values ('립스틱/립글로스/틴트','에뛰드 립스틱'.'2018-05-29','2019-05-28','에뛰드13900원',1);
   insert into userCosmetic (cate_id,name,openDate,endDate,memo,favorite) values ('클렌징','RMK클렌징'.'2018-05-29','2019-05-28','클렌징 오일13900원',0);
   insert into userCosmetic (cate_id,name,openDate,endDate,memo,favorite) values ('파우더/팩트','메이크업 포에버 팩트'.'2018-05-29','2019-05-28','43000원',0);
   insert into userCosmetic (cate_id,name,openDate,endDate,memo,favorite) values ('자외선 차단제','미샤 마일드 선크림'.'2018-05-29','2019-11-28','13900원',0);




    create table userCosmeticTools (
        _id integer primary key autoincrement,
        img,
        cate_id,
        name not null,
        openDate,
        endDate,
        memo,
        favorite,
        FOREIGN KEY (cate_id) REFERENCES cosmeticTools(_id)
    );

     create table userLens (
            _id integer primary key autoincrement,
            img,
            cate_id,
            name not null,
            openDate,
            endDate,
            memo,
            favorite,
            FOREIGN KEY (cate_id) REFERENCES cosmeticTools(_id)
     );


