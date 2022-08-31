
CREATE OR REPLACE FUNCTION trigger_set_timestamp()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

create table users (
   user_id serial NOT NULL,
	address varchar(255) NULL,
	email varchar(255) not NULL,
	full_name varchar(255) not NULL,
	"password" varchar(255) not NULL,
	phone_num varchar(255) NULL,
	profil_picture varchar(255) NULL,
	username varchar(255) not NULL,
   active bool default true,
   inserted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   constraint pk_users primary key (user_id)
)   ;
CREATE TRIGGER set_timestamp
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

create table roles (
   role_id serial not null,
   name varchar(255)  not null,
   inserted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   constraint pk_roles primary key (role_id)
)   ;
CREATE TRIGGER set_timestamp
BEFORE UPDATE ON roles
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

create table ekspedisi(
	ekspedisi_id serial not null,
	name varchar(255) not null,
	inserted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_ekspedisi primary key (ekspedisi_id)
);
CREATE TRIGGER set_timestamp
BEFORE UPDATE ON ekspedisi
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

create table categories (
   categories_id serial not null,
   name  varchar(255) not null,
   inserted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   constraint pk_categories primary key (categories_id)
)   ;
CREATE TRIGGER set_timestamp
BEFORE UPDATE ON categories
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

create table product_statuses (
   product_status_id serial not null,
   name varchar(255) not null,
   inserted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   constraint pk_product_statuses primary key (product_status_id)
)   ;
CREATE TRIGGER set_timestamp
BEFORE UPDATE ON product_statuses
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

create table products (
   product_id serial not null,
   sku varchar(255) not null,
   product_name varchar(255) not null,
   description text,
   product_status_id integer not null ,
   regular_price numeric default 0,
   quantity  integer default 0,
   user_id integer not null ,
   inserted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
   constraint pk_products primary key (product_id),
   foreign key (product_status_id) references product_statuses (product_status_id),
   foreign key (user_id) references users (user_id)
)   ;
CREATE TRIGGER set_timestamp
BEFORE UPDATE ON products
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

create table photos (
   photo_id serial not null,
   photo_name  varchar(255) not null,
   inserted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   constraint pk_photos primary key (photo_id)
)   ;
CREATE TRIGGER set_timestamp
BEFORE UPDATE ON photos
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();


create table user_roles (
	user_roles_id serial not null,
   user_id integer not null,
   role_id integer not null,
   inserted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   constraint pk_user_roles primary key (user_roles_id),
    foreign key (user_id)references users (user_id),
    foreign key (role_id)references roles (role_id)
)   ;
CREATE TRIGGER set_timestamp
BEFORE UPDATE ON user_roles
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

create table orders (
   order_id serial not null,
   order_date date not null,
   ekspedisi_id integer not null,
   total numeric not null,
   user_id integer not null,
   inserted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   constraint pk_sales_orders primary key (order_id),
    foreign key (ekspedisi_id)references ekspedisi (ekspedisi_id),
    foreign key (user_id) references users (user_id)
)   ;
CREATE TRIGGER set_timestamp
BEFORE UPDATE on orders
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

create table photo_product (
   photo_id integer not null,
   product_id integer not null,
   inserted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   constraint pk_photo_product primary key (photo_id,product_id),
    foreign key (photo_id)references photos (photo_id),
    foreign key (product_id)references products (product_id)
)   ;
CREATE TRIGGER set_timestamp
BEFORE UPDATE ON photo_product
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

create table payment (
   payment_id serial not null,
   order_id integer not null,
   date_pay timestamp with time zone,
   amount numeric not null,
   cc_num varchar(255),
   cc_type varchar(255),
   response text,
   active bool default true,
    inserted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   constraint pk_payment primary key (payment_id),
   foreign key (order_id)references orders (order_id) 
)   ;
CREATE TRIGGER set_timestamp
BEFORE UPDATE ON payment
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

create table product_categories (
   categories_id integer not null,
   product_id  integer not null,
   inserted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   foreign key (product_id) references products (product_id),
    foreign key (categories_id)references categories (categories_id)
)   ;
CREATE TRIGGER set_timestamp
BEFORE UPDATE ON product_categories
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

create table order_products (
   order_product_id serial not null,
   product_id integer not null,
   quantity integer not null,
   subtotal numeric not null,
     inserted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   constraint pk_order_products primary key (order_product_id),
    foreign key (product_id) references  products (product_id)
     
)   ;
CREATE TRIGGER set_timestamp
BEFORE UPDATE ON order_products
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

create table wishlist(
	wishlist_id serial not null,
	user_id integer not null,
	product_id integer not null,
	 inserted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_wishlist primary key (wishlist_id),
    foreign key (user_id)references users (user_id),
    foreign key (product_id)references products (product_id) 
);
CREATE TRIGGER set_timestamp
BEFORE UPDATE ON wishlist
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

create table order_order_products (
   order_product_id serial not null,
   order_id integer not null,
   inserted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   constraint pk_order_order_products primary key (order_product_id, order_id),
    foreign key (order_product_id) references  order_products (order_product_id),
    foreign key (order_id) references  orders (order_id)
     
)   ;
CREATE TRIGGER set_timestamp
BEFORE UPDATE ON order_order_products
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

create table photos_profile (
   photo_id serial not null,
   photo_name  varchar(255) not null,
   inserted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   constraint pk_photos_profile primary key (photo_id)
)   ;
CREATE TRIGGER set_timestamp
BEFORE UPDATE ON photos_profile
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

alter table users drop column profil_picture;
alter table users add column profil_picture integer NULL;
alter table users add constraint fk_users_photos_profile
    foreign key (profil_picture)
    references photos_profile (photo_id);
