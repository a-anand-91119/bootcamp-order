-- create orders table
create table if not exists orders
(
    id                     uuid primary key        default gen_random_uuid(),
    customer_id            uuid           not null,
    customer_email         varchar(255),
    customer_phone         varchar(20),
    status                 varchar(20)    not null,
    order_date             timestamp      not null,
    expected_delivery_date timestamp,
    actual_delivery_date   timestamp,
    subtotal               decimal(19, 2) not null,
    tax_amount             decimal(19, 2),
    shipping_amount        decimal(19, 2),
    discount_amount        decimal(19, 2),
    total_amount           decimal(19, 2) not null,
    currency               varchar(3)     not null,
    shipping_method        varchar(100),
    tracking_number        varchar(100),
    shipping_address       jsonb,
    billing_address        jsonb,
    payment_method         varchar(50),
    payment_status         varchar(20),
    payment_transaction_id varchar(100),
    notes                  text,
    source                 varchar(20)    not null,
    channel                varchar(20),
    promotions             jsonb,
    metadata               jsonb,
    created_date           timestamp      not null default current_timestamp,
    created_by             varchar(50)    not null default 'system',
    last_modified_date     timestamp,
    last_modified_by       varchar(50),
    is_deleted             boolean        not null default false,
    version                integer        not null default 0
);

-- create order_line_items table
create table if not exists order_line_items
(
    id                 uuid primary key        default gen_random_uuid(),
    order_id           uuid           not null,
    product_id         varchar(50)    not null,
    product_name       varchar(255)   not null,
    product_sku        varchar(100)   not null,
    quantity           integer        not null,
    unit_price         decimal(19, 2) not null,
    total_price        decimal(19, 2) not null,
    discount_amount    decimal(19, 2),
    tax_amount         decimal(19, 2),
    category           varchar(100),
    description        text,
    image_url          varchar(500),
    product_attributes jsonb,
    metadata           jsonb,
    created_date       timestamp      not null default current_timestamp,
    created_by         varchar(50)    not null default 'system',
    last_modified_date timestamp,
    last_modified_by   varchar(50),
    is_deleted         boolean        not null default false,
    version            integer        not null default 0,
    constraint fk_order_line_items_order foreign key (order_id) references orders (id) on delete cascade
);

create index idx_orders_customer_id on orders (customer_id);
create index idx_order_line_items_order_id on order_line_items (order_id);
