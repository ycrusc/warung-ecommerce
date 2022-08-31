

select ur.role_id ,u.username ,p.product_name , p.description ,p.quantity ,p.regular_price ,op.subtotal ,e."name" ,o.order_date ,p2.cc_type ,p2.date_pay ,p2.response,r."name" 
from (((((((products p inner join users u on p.product_id = u.user_id)
inner join ekspedisi e on p.product_id = e.ekspedisi_id ) 
inner join order_products op on p.product_id = op.order_product_id ) 
inner join orders o on p.product_id = o.order_id  )
inner join payment p2 on p.product_id = p2.payment_id )
inner join user_roles ur on u.user_id = ur.user_id )
inner join roles r on ur.role_id  =r.role_id )
where r."name" = 'ROLE_BUYER'


select ur.role_id ,u.username ,p.product_name , p.description ,p.quantity ,p.regular_price ,op.subtotal ,e."name" ,o.order_date ,p2.cc_type ,p2.date_pay ,p2.response,r."name" 
from (((((((products p inner join users u on p.product_id = u.user_id)
inner join ekspedisi e on p.product_id = e.ekspedisi_id ) 
inner join order_products op on p.product_id = op.order_product_id ) 
inner join orders o on p.product_id = o.order_id  )
inner join payment p2 on p.product_id = p2.payment_id )
inner join user_roles ur on u.user_id = ur.user_id )
inner join roles r on ur.role_id  =r.role_id )
where r."name" = 'ROLE_SELLER' and u.username = 'toni'

select ur.role_id ,u.username ,p.product_name , p.description ,p.quantity ,p.regular_price ,e."name" ,o.order_date ,p2.cc_type ,p2.date_pay ,p2.response,r."name" 
from (((((((products p inner join users u on p.product_id = u.user_id)
inner join ekspedisi e on p.product_id = e.ekspedisi_id ) 
inner join order_products op on p.product_id = op.order_product_id ) 
inner join orders o on p.product_id = o.order_id  )
inner join payment p2 on p.product_id = p2.payment_id )
inner join user_roles ur on u.user_id = ur.user_id )
inner join roles r on ur.role_id  =r.role_id )
where r."name" = 'ROLE_BUYER'