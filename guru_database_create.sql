create table categories (id bigint not null auto_increment, description varchar(255), primary key (id)) engine=InnoDB;
create table ingredients (id bigint not null auto_increment, amount decimal(19,2), description varchar(255), recipe_id bigint, uom_id bigint, primary key (id)) engine=InnoDB;
create table notes (id bigint not null auto_increment, recipe_notes longtext, primary key (id)) engine=InnoDB;
create table recipe_categories (recipe_id bigint not null, category_id bigint not null, primary key (recipe_id, category_id)) engine=InnoDB;
create table recipes (id bigint not null auto_increment, cook_time integer, description varchar(255), difficulty varchar(255), directions longtext, image longblob, prep_time integer, servings integer, source varchar(255), url varchar(255), notes_id bigint, primary key (id)) engine=InnoDB;
create table unit_of_measures (id bigint not null auto_increment, description varchar(255), primary key (id)) engine=InnoDB;
alter table ingredients add constraint FK7p08vcn6wf7fd6qp79yy2jrwg foreign key (recipe_id) references recipes (id);
alter table ingredients add constraint FKdltvxuwd79pkcbadawghu2q5m foreign key (uom_id) references unit_of_measures (id);
alter table recipe_categories add constraint FKl4gklbf4tpxuk41fp77pgd28l foreign key (category_id) references categories (id);
alter table recipe_categories add constraint FK3w4m6a9qnpwjgknvss7amxhjd foreign key (recipe_id) references recipes (id);
alter table recipes add constraint FK74kcww8qjnl4ursb95gfk9or6 foreign key (notes_id) references notes (id);
