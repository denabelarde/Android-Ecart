--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `category` (
  `_id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `categoryname` text NOT NULL,
  `categoryid` INTEGER NOT NULL,
  `image` text,
  `categorycolor` text NOT NULL
);

CREATE TABLE IF NOT EXISTS `product` (
  `_id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `productid` INTEGER NOT NULL,
  `productname` text NOT NULL,
  `description` text,
  `price` text,
  `categoryid` INTEGER NOT NULL,
  `image` text
);

CREATE TABLE IF NOT EXISTS `shoppingcart` (
  `_id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `productid` INTEGER NOT NULL,
  `quantity` INTEGER NOT NULL,
  `price` text,
  `datecreated` text NOT NULL
);


INSERT INTO `category` (`categoryname`,`categoryid`,`image`,`categorycolor`)
SELECT 'Electronics' as `categoryname`, 1 as `categoryid`, 'electronics_icon' as `image`, '#CCA9D459' as `categorycolor`
UNION SELECT 'Clothing', 2, 'clothing_icon', '#CC50D9F9'
UNION SELECT 'Food', 3, 'food_icon', '#CCF98131'
UNION SELECT 'Cars', 4, 'car_icon', '#f16e4e';

INSERT INTO `product` (`productid`,`productname`,`description`,`price`,`categoryid`,`image`)
SELECT  1 as `productid`, 'Bench T-shirt (S/M/L/XL)' as `productname`, 'A very nice t-shirt packed with awesome designs. It has a soft cotton texture that can relax your body. We offer different sizes for your needs.' as `description`, 300 as `price`, 2 as `categoryid`, 'bench' as image
UNION SELECT 2, 'Penshoppe Maong Pants Jeans Blue (S/M/L/XL) straight cut','Color: Light Blue. Its pockets are deep, perfect condition. It adjustable waistline with soft cotton texture.', 400, 2, 'penshoppe'
UNION SELECT 3, 'Ultimate Bacon Cheeseburger','Our Ultimate Bacon Cheeseburger Sandwich features two 1/4 lb. savory fire-grilled beef patties, topped with thick-cut smoked bacon, melted American cheese, and featuring savory thick and Hearty sauce, all on a warm, toasted, Artisan bun.', 200, 3, 'baconburger'
UNION SELECT 4, 'Monster Meal Deal','2 Large Thin Crust Managers Choice and Pepperoni, 2 platters of Classic Spaghetti, and Carbonara Supreme, 12pc Party Pack Chicken Mojos and 3 Pitcher of ice cold Coke', 2475, 3, 'monstermeal'
UNION SELECT 5, '2014 Chevrolet Camaro ZL1','It is the most technologically advanced Camaro ever. With a 6.2L supercharged engine it delivers an impressive 580 horsepower and 556 lb.-ft. of torque. The kind of high-tech power that punches the 0–60 clock in just 3.9 seconds. Camaro ZL1 delivers more than under the hood. It is the total package', 2475, 4, 'camaro'
UNION SELECT 6, 'Bugatti Veyron EB 16.4','It is a mid-engined sports car, designed and developed by the Volkswagen Group and manufactured in Molsheim, France, by Bugatti Automobiles S.A.S. The Super Sport version of the Veyron is the fastest street-legal production car in the world, with a top speed of 431.072 km/h (267.856 mph). The original version has a top speed of 408.47 km/h (253.81 mph). It was named Car of the Decade (2000–2009) by the BBC television programme Top Gear. The standard Bugatti Veyron also won Top Gears Best Car Driven All Year award in 2005', 2000000, 4, 'bugati'
UNION SELECT 7, 'Smart TV','A smart TV device is either a television set with integrated Internet capabilities or a set-top box for television that offers more advanced computing ability and connectivity than a contemporary basic television set', 30000, 1, 'smarttv'
UNION SELECT 8, 'Smartphone','a cellular phone that is able to perform many of the functions of a computer, typically having a relatively large screen and an operating system capable of running general-purpose applications.', 20000, 1, 'ss5';
