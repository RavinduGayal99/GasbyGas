CREATE TABLE `user` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `type` VARCHAR(30) NOT NULL,
    `email` VARCHAR(30) NOT NULL UNIQUE,
    `password` VARCHAR(30) NOT NULL,
    `name` VARCHAR(50),
    `contact` VARCHAR(10) UNIQUE,
    `nic` VARCHAR(30) UNIQUE,
    `status` VARCHAR(30),
    `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `token` (
     `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     `pickupDate` DATE,
     `status` VARCHAR(30),
     `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updatedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `gas` (
       `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
       `item` VARCHAR(30) NOT NULL,
       `price` DECIMAL(10, 2) NOT NULL,
       `updatedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `customer` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `userId` INT NOT NULL,
    `type` VARCHAR(30) NOT NULL,
    `gasType` VARCHAR(30),
    `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`userId`) REFERENCES `user`(`id`)
);

CREATE TABLE `outlet` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `userId` INT NOT NULL,
    `type` VARCHAR(30) NOT NULL,
    `district` VARCHAR(30) NOT NULL,
    `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`userId`) REFERENCES `user`(`id`)
);

CREATE TABLE `stock` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `outletId` INT NOT NULL,
    `gasId`INT NOT NULL,
    `quantity` INT(10) NOT NULL,
    `updatedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`outletId`) REFERENCES `outlet`(`id`),
    FOREIGN KEY (`gasId`) REFERENCES `gas`(`id`)
);

CREATE TABLE `request` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `userId` INT NOT NULL,
    `gasId` INT NOT NULL,
    `outletId` INT NULL,
    `tokenId` INT NULL,
    `orderNo` BIGINT NULL,
    `unitPrice` DECIMAL(10, 2) NULL,
    `quantity` INT(10) NOT NULL,
    `totalPrice` DECIMAL(10, 2) NULL,
    `status` VARCHAR(30) NOT NULL,
    `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`userId`) REFERENCES `user`(`id`),
    FOREIGN KEY (`gasId`) REFERENCES `gas`(`id`),
    FOREIGN KEY (`outletId`) REFERENCES `outlet`(`id`),
    FOREIGN KEY (`tokenId`) REFERENCES `token`(`id`)
);


INSERT INTO `user` (`type`, `email`, `password`)
VALUES
    ('ADMIN', 'admin@gasbygas.com', 'Password1!'),
    ('DISPATCH_MANAGER', 'dm@gasbygas.com', 'Password1!');

INSERT INTO `gas` (`item`, `price`)
VALUES
    ('2.3KG', 831),
    ('5KG', 1665),
    ('12.5KG', 4000),
    ('25KG', 8000),
    ('BULK', 150);
