/*Data Definition Language*/

/*Drop*/
drop table "users" cascade;
drop table "ledgers" cascade;
drop table "shipyards" cascade;
drop table "shipClasses" cascade;
drop type "condition" cascade;
drop table "ships" cascade;
drop type "componentClass" cascade;
drop table "componentTypes" cascade;
drop table "components" cascade;

/*Create*/
create table "users" (
	"username" varchar not null,
	"password" varchar not null,
	"isAdmin" boolean not null,
	
	constraint "pk_users"
		primary key ("username")
);
create table "ledgers" (
	"id" varchar not null,
	"username" varchar not null,
	"date" timestamp,
	"totalPrice" numeric,
	
	constraint "pk_ledgers"
		primary key ("id"),
	constraint "fk_ledgers_users"
		foreign key("username") 
	  		references "users"("username")
);
create table "shipyards" (
	"id" varchar not null,
	"name" varchar not null,
	"description" varchar not null,
	"locationFormula" varchar not null,
	
	constraint "pk_shipyards"
		primary key ("id")
);
create table "shipClasses" (
	"id" varchar not null,
	"name" varchar not null,
	"description" varchar not null,
	"engineMaxSize" integer not null,
	"engineMinSize" integer not null,
	"smallHardPoints" integer not null,
	"mediumHardPoints" integer not null,
	"largeHardPoints" integer not null,
	"smallAuxPoints" integer not null,
	"mediumAuxPoints" integer not null,
	"largeAuxPoints" integer not null,
	"cabins" integer not null,
	"bays" integer not null,
	
	constraint "pk_shipClasses"
		primary key ("id")
);
create type "condition" as enum (
	'NULL',
	'DAMAGED',
	'WORN',
	'FINE',
	'GOOD',
	'GREAT',
	'PRISTINE',
	'COUNT'
);
create table "ships" (
	"id" varchar not null,
	"name" varchar not null,
	"description" varchar not null,
	"location" varchar not null,
	"basePrice" numeric not null,
	"condition" "condition" not null,
	"class" varchar not null,
	"ledger" varchar,
	
	constraint "pk_ships"
		primary key ("id"),
	constraint "fk_ships_shipyards"
		foreign key ("location")
			references "shipyards"("id"),
	constraint "fk_ships_shipClasses"
		foreign key ("class")
			references "shipClasses"("id"),
	constraint "fk_ships_ledgers"
		foreign key ("ledger")
			references "ledgers"("id")
);
create type "componentClass" as enum (
	'NULL',
	'ENGINE',
	'HULL',
	'WEAPON',
	'AUXILIARY',
	'CABIN',
	'BAY',
	'MISC',
	'COUNT'
);
create table "componentTypes" (
	"id" varchar not null,
	"name" varchar not null,
	"description" varchar not null,
	"class" "componentClass" not null,
	"size" integer not null,
	"basePrice" numeric not null,
	
	constraint "pk_componentTypes"
		primary key ("id")
);
create table "components" (
	"ship" varchar not null,
	"type" varchar not null,
	"condition" "condition" not null,
	
	constraint "fk_components_ships"
		foreign key ("ship")
			references "ships"("id"),
	constraint "fk_components_componentTypes"
		foreign key ("type")
			references "componentTypes"("id")
);

/*Alter*/

/*Truncate*/

/*Comment*/

/*Rename*/
