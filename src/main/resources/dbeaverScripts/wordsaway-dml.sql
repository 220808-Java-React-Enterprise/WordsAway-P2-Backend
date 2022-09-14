/*Data Manipulation LANGUAGE*/

/*Insert*/
insert into users ("username", "password", "isAdmin") values ('koukaakiva', 'fnord', 'True');

insert into shipyards ("id", "name", "description", "locationFormula") values ('d087c6dd-6be0-4bc8-b5d7-40c3910b0dff', 'Utopia Planitia Fleet Yards', 'The primary shipyard of Starfleet. Located in Mars Orbit.', '0ϴt25800lyR+0.6πθt1.66621AUr');
insert into shipyards ("id", "name", "description", "locationFormula") values ('2163ac6a-7294-421d-af7d-de07113a8354', 'Deep Space 9', 'Starfleet Shipyard and Deep Space Research station. Located in orbit of Bajor.', '1.78πϴt30550lyR+0.32πθt1.003AUr');
insert into shipyards ("id", "name", "description", "locationFormula") values ('533a7bcf-ed6e-4405-bbbb-1a7c2d518d2b', 'The Citadel', 'Capital of the Citadel Council. Located in around The Widow in the Serpent Nebula.', '2.1πϴt22796lyR+0.99πθt0.55AUr');
insert into shipyards ("id", "name", "description", "locationFormula") values ('da77f14f-25f0-4a67-918e-959428cbacb0', 'Babylon 5', 'The fifth and final space station in the Babylon Project, located in neutral space in orbit of Epsilon III near Epsilon Eridani.', '0.002πϴt298910lyR+2.41πθt9.2AUr');
insert into shipyards ("id", "name", "description", "locationFormula") values ('59867689-e621-4598-bd77-75e48fa05a49', 'Ceres Station', 'One of the first sites of human colonization in the Outer Planets. Located on Ceres.', '0ϴt25800lyR+0.9πθt2.8AUr');
insert into shipyards ("id", "name", "description", "locationFormula") values ('a13c5a83-b80d-48e9-915e-b19d2813f9e2', 'Tycho Station', 'The largest mobile construction platform in the Sol system, and the Belt headquarters of Tycho Manufacturing.', '0ϴt25800lyR+0.9πθt2.77AUr');

insert into "shipClasses" ("id", "name", "description" , "engineMaxSize", "engineMinSize", "smallHardPoints", "mediumHardPoints", "largeHardPoints", "smallAuxPoints", "mediumAuxPoints", "largeAuxPoints", "cabins", "bays") values ('a51bec52-5d9f-4339-8d7b-b2608597f254', 'Fighter', 'A small one-person ship used for dogfights.', 1, 1, 1, 0, 0, 1, 0, 0, 0, 0);
insert into "shipClasses" ("id", "name", "description" , "engineMaxSize", "engineMinSize", "smallHardPoints", "mediumHardPoints", "largeHardPoints", "smallAuxPoints", "mediumAuxPoints", "largeAuxPoints", "cabins", "bays") values ('03bde14a-de4a-4059-a06c-5f0a8df3bfc4', 'Corvette', 'The smallest class of starship that can operate without a host ship.', 1, 2, 4, 2, 0, 4, 4, 0, 6, 1);
insert into "shipClasses" ("id", "name", "description" , "engineMaxSize", "engineMinSize", "smallHardPoints", "mediumHardPoints", "largeHardPoints", "smallAuxPoints", "mediumAuxPoints", "largeAuxPoints", "cabins", "bays") values ('5a3cb301-5420-463a-8210-5d412bb1f327', 'Destroyer', 'A small but versitile military ship.', 2, 2, 8, 2, 1, 4, 4, 1, 12, 2);
insert into "shipClasses" ("id", "name", "description" , "engineMaxSize", "engineMinSize", "smallHardPoints", "mediumHardPoints", "largeHardPoints", "smallAuxPoints", "mediumAuxPoints", "largeAuxPoints", "cabins", "bays") values ('82ce3656-a3df-4298-9106-64dc50bae5f4', 'Battleship', 'A medium-sized military ship.', 2, 3, 12, 8, 2, 6, 4, 4, 24, 6);
insert into "shipClasses" ("id", "name", "description" , "engineMaxSize", "engineMinSize", "smallHardPoints", "mediumHardPoints", "largeHardPoints", "smallAuxPoints", "mediumAuxPoints", "largeAuxPoints", "cabins", "bays") values ('e472188f-19c3-4690-ab64-6f5a6643fac3', 'Titan', 'The largest military ship on the market.', 3, 3, 0, 20, 8, 12, 8, 8, 42, 10);

insert into "componentTypes" ("id", "name", "description", "class", "size", "basePrice") values ('771921b4-8ed3-4d01-bc8a-31db62b78800', 'Hawking Hyperdrive Engine', 'The goto engine for trveling FTL. Cheap and reliable.', 'ENGINE', 1, 100000);
insert into "componentTypes" ("id", "name", "description", "class", "size", "basePrice") values ('8ed816d7-d8bd-4d9f-ab70-78d1e6e17075', 'Tesla Hyperdrive Engine', 'Purely Electric FTL Engine.', 'ENGINE', 1, 156990);
insert into "componentTypes" ("id", "name", "description", "class", "size", "basePrice") values ('afdf8aa4-cc32-4a0a-845e-d5cd7428deea', 'Ipsum Ion-Drive Engine', 'Engine that requires reaction mass. Made by Ipsum Lorem Inc.', 'ENGINE', 2, 306050);
insert into "componentTypes" ("id", "name", "description", "class", "size", "basePrice") values ('1528dd58-eba0-4bb3-872e-0b883737a153', 'Slipsteam Engine Mk2', 'With the power of slipstream, go really fast.', 'ENGINE', 2, 549000);
insert into "componentTypes" ("id", "name", "description", "class", "size", "basePrice") values ('5d114bae-bf53-42e8-969b-64f07ce08c38', 'Starfleet Warp Engine', 'The standard engine of Starfleet.', 'ENGINE', 3, 100000000);
insert into "componentTypes" ("id", "name", "description", "class", "size", "basePrice") values ('09701089-4dbd-4d33-bf61-c4f4f713f5b2', 'The Einstein Drive', 'A engine that moves space instead of your ship. It''s all relative.', 'ENGINE', 2, 500000000);
insert into "componentTypes" ("id", "name", "description", "class", "size", "basePrice") values ('32b9a72e-1866-4034-8600-c2db609c5b84', 'Wormhole Generator 3000', 'The quickest path between two points is a hole punched in space.', 'ENGINE', 3, 9999999999);

insert into "componentTypes" ("id", "name", "description", "class", "size", "basePrice") values ('a5e69cbc-bc65-42cf-b337-b9f38dd4882e', 'Hyperion Flak Cannon', 'Defend yourself from missiles or tear a hole in an enmy ship.', 'WEAPON', 1, 4999.99);
insert into "componentTypes" ("id", "name", "description", "class", "size", "basePrice") values ('5035cc21-bd25-42d8-a99e-d45204e1dd12', 'Hyperion Chain Gun', 'Just tear a hole in an enmy ship.', 'WEAPON', 1, 6999.99);
insert into "componentTypes" ("id", "name", "description", "class", "size", "basePrice") values ('564ba38f-cac7-432d-809b-6b943e77681b', 'Hyperion Laucher', 'Load her full of junk and tear a hole in an enmy ship.', 'WEAPON', 1, 3999.99);
insert into "componentTypes" ("id", "name", "description", "class", "size", "basePrice") values ('ea6b8c77-f3bf-49dd-83f7-ff712fc27f70', 'Dahl Bolt Caster', 'Gun that fires plasma bolts at high speeds.', 'WEAPON', 1, 15499);
insert into "componentTypes" ("id", "name", "description", "class", "size", "basePrice") values ('9e1c8e3f-4a4f-4413-9660-fde2a477cd86', 'Standard Phaser', 'Starfleet focused energy weapon.', 'WEAPON', 1, 65000);
insert into "componentTypes" ("id", "name", "description", "class", "size", "basePrice") values ('08850cf6-bc47-42e9-b745-206a8422f110', 'Dahl Pulse Beam', 'Gun that fires intense energy pulses.', 'WEAPON', 2, 24999);
insert into "componentTypes" ("id", "name", "description", "class", "size", "basePrice") values ('eb3ec3c8-a840-4ca2-adf6-692df8cd8e17', 'TORGUE BAZOOKA!', 'MAKES THINGS EXPLODE!', 'WEAPON', 2, 10000);
insert into "componentTypes" ("id", "name", "description", "class", "size", "basePrice") values ('f1f1c5c3-90d1-4c5d-811a-1748a54ff2e4', 'TORGUE MISSILE THROWER!', 'MAKES THINGS EXPLODE!', 'WEAPON', 2, 10000);
insert into "componentTypes" ("id", "name", "description", "class", "size", "basePrice") values ('301d149c-1e66-46e6-b8bb-5cda9ebb7361', 'Maliwan Chain Gun', 'High end chain gun. 100K BPM.', 'WEAPON', 2, 65000);
insert into "componentTypes" ("id", "name", "description", "class", "size", "basePrice") values ('1ea84c9d-d79a-41ad-98c4-8723fe255458', 'Hyperion Missile Launcher', 'A missile makes the perfect gift.', 'WEAPON', 2, 65000);
insert into "componentTypes" ("id", "name", "description", "class", "size", "basePrice") values ('f1137b6e-1fee-420e-87c5-9ce6dd06a4e6', 'TORGUE EXPLODINATOR!', 'MAKES THINGS EXPLODE!', 'WEAPON', 3, 100000);
insert into "componentTypes" ("id", "name", "description", "class", "size", "basePrice") values ('26264488-f1ac-4573-91de-68f5403c60a3', 'Hawking Railgun', 'The goto railgun for reducing a target to dust.', 'WEAPON', 3, 5000000);
insert into "componentTypes" ("id", "name", "description", "class", "size", "basePrice") values ('6e582f89-104e-42f8-b8b0-459d6fd9a072', 'Hyperion Glassing Cannon', 'Glass a planet. No one can stop you with this bad boy.', 'WEAPON', 3, 6500000);
insert into "componentTypes" ("id", "name", "description", "class", "size", "basePrice") values ('71579287-9258-454d-9372-f412ece1119a', 'Photon Torpedo Launcher', 'Starfleet heavy weapon system.', 'WEAPON', 3, 5000000);

INSERT INTO "ships" (id,"name",description,"location","basePrice","condition","class",ledger) VALUES
	 ('6c3601ca-b90d-4dcf-90e9-8c6ab82d7676','USS Voyager','Starfleet vessle famous for its unscheduled seven-year journey across the Delta Quadrant.','d087c6dd-6be0-4bc8-b5d7-40c3910b0dff',1000000000,'WORN','82ce3656-a3df-4298-9106-64dc50bae5f4',NULL),
	 ('d1ad0c77-17af-4a57-aee8-f88dc00994b6','Millennium Falcon','An old hunk of junk.','59867689-e621-4598-bd77-75e48fa05a49',500000,'WORN','03bde14a-de4a-4059-a06c-5f0a8df3bfc4',NULL),
	 ('f7775955-cc56-4e2d-bf40-01b16da8ce88','USS Enterprise','Make it so.','59867689-e621-4598-bd77-75e48fa05a49',990826983.730182,'WORN','82ce3656-a3df-4298-9106-64dc50bae5f4',NULL),
	 ('0ea65b7d-e35d-4b9b-84f6-b751bb0d4d60','Battlestar Galactica', 'For when you need to outrun the Cylons.','59867689-e621-4598-bd77-75e48fa05a49',127514396.83476342,'GOOD','82ce3656-a3df-4298-9106-64dc50bae5f4',NULL),
	 ('31040d4b-61b5-4afa-9631-11644c2f04c6','Planet Express Ship','Our crew is expendable, you package is not.','59867689-e621-4598-bd77-75e48fa05a49',585749031.0217127,'WORN','82ce3656-a3df-4298-9106-64dc50bae5f4',NULL),
	 ('03ad299e-34f9-4731-a01b-7f40fdfaa102','The Heart of Gold','Oh no, not again.','59867689-e621-4598-bd77-75e48fa05a49',452581094.5233343,'GOOD','5a3cb301-5420-463a-8210-5d412bb1f327',NULL),
	 ('4302fcc9-9e03-431e-960a-f030d8843426','Bebop','Listen to her purr.','a13c5a83-b80d-48e9-915e-b19d2813f9e2',642046759.6709932,'GREAT','a51bec52-5d9f-4339-8d7b-b2608597f254',NULL),
	 ('5600d7a3-fcde-4c9c-95af-475c818640cb','Rocinante','Don''t ask why it looks like a Mars military ship.','533a7bcf-ed6e-4405-bbbb-1a7c2d518d2b',678984009.1562573,'FINE','5a3cb301-5420-463a-8210-5d412bb1f327',NULL),
	 ('e56ff2ee-0783-437f-bb9c-3ff8eed30461','Serenity','You can''t take the ski from me.','533a7bcf-ed6e-4405-bbbb-1a7c2d518d2b',739006711.1193316,'WORN','a51bec52-5d9f-4339-8d7b-b2608597f254',NULL),
	 ('c9238cde-d024-41af-adcc-77b9ea21f71d','Nauvoo','A Mormon-commissioned religious space exploration generational ship.','d087c6dd-6be0-4bc8-b5d7-40c3910b0dff',159478276.58334416,'GOOD','5a3cb301-5420-463a-8210-5d412bb1f327',NULL),
	 ('0d8c6a55-bc01-4155-9441-35aa5741a07d','Prometheus','Weyland Corporation scientific vessel.','533a7bcf-ed6e-4405-bbbb-1a7c2d518d2b',971034963.9988163,'WORN','5a3cb301-5420-463a-8210-5d412bb1f327',NULL),
	 ('3cd4f2cd-5674-480d-ad49-93485cab6919','USS Orville','You wanna open this jar of pickles for me?','a13c5a83-b80d-48e9-915e-b19d2813f9e2',589752782.2166338,'WORN','a51bec52-5d9f-4339-8d7b-b2608597f254',NULL);
	
INSERT INTO "components" (ship,"type","condition") VALUES
	 ('6c3601ca-b90d-4dcf-90e9-8c6ab82d7676','5d114bae-bf53-42e8-969b-64f07ce08c38','GREAT'),
	 ('6c3601ca-b90d-4dcf-90e9-8c6ab82d7676','9e1c8e3f-4a4f-4413-9660-fde2a477cd86','DAMAGED'),
	 ('6c3601ca-b90d-4dcf-90e9-8c6ab82d7676','9e1c8e3f-4a4f-4413-9660-fde2a477cd86','DAMAGED'),
	 ('6c3601ca-b90d-4dcf-90e9-8c6ab82d7676','9e1c8e3f-4a4f-4413-9660-fde2a477cd86','DAMAGED'),
	 ('6c3601ca-b90d-4dcf-90e9-8c6ab82d7676','9e1c8e3f-4a4f-4413-9660-fde2a477cd86','WORN'),
	 ('6c3601ca-b90d-4dcf-90e9-8c6ab82d7676','9e1c8e3f-4a4f-4413-9660-fde2a477cd86','WORN'),
	 ('6c3601ca-b90d-4dcf-90e9-8c6ab82d7676','9e1c8e3f-4a4f-4413-9660-fde2a477cd86','GOOD'),
	 ('6c3601ca-b90d-4dcf-90e9-8c6ab82d7676','71579287-9258-454d-9372-f412ece1119a','DAMAGED'),
	 ('6c3601ca-b90d-4dcf-90e9-8c6ab82d7676','71579287-9258-454d-9372-f412ece1119a','WORN'),
	 ('d1ad0c77-17af-4a57-aee8-f88dc00994b6','1528dd58-eba0-4bb3-872e-0b883737a153','GREAT');
INSERT INTO "components" (ship,"type","condition") VALUES
	 ('d1ad0c77-17af-4a57-aee8-f88dc00994b6','ea6b8c77-f3bf-49dd-83f7-ff712fc27f70','GREAT'),
	 ('d1ad0c77-17af-4a57-aee8-f88dc00994b6','ea6b8c77-f3bf-49dd-83f7-ff712fc27f70','PRISTINE'),
	 ('f7775955-cc56-4e2d-bf40-01b16da8ce88','32b9a72e-1866-4034-8600-c2db609c5b84','GOOD'),
	 ('f7775955-cc56-4e2d-bf40-01b16da8ce88','5035cc21-bd25-42d8-a99e-d45204e1dd12','PRISTINE'),
	 ('f7775955-cc56-4e2d-bf40-01b16da8ce88','08850cf6-bc47-42e9-b745-206a8422f110','FINE'),
	 ('0ea65b7d-e35d-4b9b-84f6-b751bb0d4d60','5035cc21-bd25-42d8-a99e-d45204e1dd12','WORN'),
	 ('0ea65b7d-e35d-4b9b-84f6-b751bb0d4d60','564ba38f-cac7-432d-809b-6b943e77681b','DAMAGED'),
	 ('0ea65b7d-e35d-4b9b-84f6-b751bb0d4d60','9e1c8e3f-4a4f-4413-9660-fde2a477cd86','FINE'),
	 ('03ad299e-34f9-4731-a01b-7f40fdfaa102','f1137b6e-1fee-420e-87c5-9ce6dd06a4e6','DAMAGED'),
	 ('4302fcc9-9e03-431e-960a-f030d8843426','1ea84c9d-d79a-41ad-98c4-8723fe255458','GREAT');
INSERT INTO "components" (ship,"type","condition") VALUES
	 ('5600d7a3-fcde-4c9c-95af-475c818640cb','08850cf6-bc47-42e9-b745-206a8422f110','GOOD'),
	 ('5600d7a3-fcde-4c9c-95af-475c818640cb','5d114bae-bf53-42e8-969b-64f07ce08c38','DAMAGED'),
	 ('0d8c6a55-bc01-4155-9441-35aa5741a07d','1528dd58-eba0-4bb3-872e-0b883737a153','DAMAGED'),
	 ('3cd4f2cd-5674-480d-ad49-93485cab6919','5d114bae-bf53-42e8-969b-64f07ce08c38','GOOD'),
	 ('31040d4b-61b5-4afa-9631-11644c2f04c6','32b9a72e-1866-4034-8600-c2db609c5b84','GOOD'),
	 ('e56ff2ee-0783-437f-bb9c-3ff8eed30461','771921b4-8ed3-4d01-bc8a-31db62b78800','GREAT'),
	 ('c9238cde-d024-41af-adcc-77b9ea21f71d','09701089-4dbd-4d33-bf61-c4f4f713f5b2','PRISTINE'),
	 ('3cd4f2cd-5674-480d-ad49-93485cab6919','301d149c-1e66-46e6-b8bb-5cda9ebb7361','PRISTINE');



/*Update*/

/*Delete*/

/*Lock*/

/*Call*/

/*Explain Plan*/
