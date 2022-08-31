--CREATE EXTENSION pgcrypto;

insert into users ( email,full_name, username, password, address,profil_picture , phone_num, active) 
values ('tony@gmail.com', 'Tony Suparjo', 'toni','123456','Bantul, DIY','.jpg','081223123',true),
('filip@gmail.com', 'Filipe Fihillie', 'Filipe','123456','Gunung Kidul, DIY','.jpg','081223123',true),
('john@gmail.com', 'Brice John', 'John','123456','Jogja, DIY','.jpg','081223123',true),
('bofihillie0@addtoany.com', 'Brice Fihillie', 'bofihillie0','123456','Sleman, DIY','.jpg','081223123',true);

update public.users set password = crypt(password,gen_salt('bf'));

insert into roles(name)
values( 'ROLE_ADMIN'),
( 'ROLE_BUYER'),
( 'ROLE_SELLER');

insert into ekspedisi (name)
values( 'Pos Indonesia'),
( 'Si Cepat'),
( 'JNE');

insert into categories(name)
values( 'Sembako'),
( 'Makanan Ringan'),
( 'Keperluan Mandi'),
( 'Makanan Instan');

insert into product_statuses (name)
values( 'Avalilable'),
( 'Sold Out');

insert into products (sku, product_name , description, product_status_id, regular_price, quantity, user_id)
values ('AD3412341ASDA','Minyak Goreng','Minyak Goreng 2 Liter', 1 , '35000',10, 1),
('ASDA3243425','GULAKU','Gula Pasir 1 kg', 1 , '15000',10, 1),
('FGH346T3545','Taro','Snack Ringan', 1 , '2000',10, 1),
('JHYF5465343','Sabun Lifeboy','Sabun Mandi', 1 , '4000',10, 1);

insert into photos (photo_name)
values ('Minyak-Goreng.jpg'),
('Gula-Pasir.jpg'),
('Snack-Ringan.jpg'),
('Sabun-Lifeboy.jpg');

insert into user_roles (user_id, role_id)
values(1,2),
( 1,3);

insert into photo_product (product_id,photo_id)
values (1,1),
(2,2),
(3,3),
(4,4);


insert into orders (order_date, ekspedisi_id, total, user_id)
values ('2022-08-08 00:10:00','1','150000', 1 ),
('2022-07-10 00:10:00','2','50000', 4 ),
('2022-07-08 00:10:00','3','5000', 4 );


insert into payment (order_id, date_pay , amount, cc_num, cc_type,response)
values (1,'2022-08-08 00:10:00','150000','231241421423','BANK_BRI','PAYMENT_SUCCES');

insert into product_categories (categories_id, product_id)
values(1,1),
(2,1),
(3,2),
(4,3);

insert into order_products (quantity, subtotal, product_id)
values (2,70000,1);

insert into order_order_products (order_id, order_product_id) 
values(1,1);




insert into wishlist (user_id, product_id) 
values(1,1);
