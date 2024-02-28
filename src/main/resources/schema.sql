create table client(
id serial primary key,
username varchar not null,
password varchar not null,
email varchar not null,
telephone_number varchar not null,
balance decimal not null,
date_of_birth date);

create table bank_accounts(
id serial primary key,
client_id int references client(id),
balance decimal not null,
creation_date date);

create table transactions(
id serial primary key,
sender_id int references client(id),
recipient_id int references client(id),
amount decimal not null,
date_time timestamp(0));