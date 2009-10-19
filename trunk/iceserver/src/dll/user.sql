    create table `ice`.`user`(
        `id` BIGINT UNSIGNED not null auto_increment,
       `name` CHAR(41) not null unique,
       `password` CHAR(255) not null,
       `email` VARCHAR(400),
        primary key (`id`)
    );

    create unique index `PRIMARY` on `ice`.`user`(`id`);
    create unique index `name` on `ice`.`user`(`name`);